package com.example.outstation.Servers;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.authentication.activity.R;
import com.authentication.activity.TestFingerprintActivity;
import com.authentication.entity.CarNum;
import com.example.Utils.CustomToast;
import com.example.Utils.ExitCheckInfoJson;
import com.example.configure.Configure;
import com.example.outstation.aty.ExitstationChooseActivity;
import com.example.outstation.entity.ExitCheckInfo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class OutofstationService extends Service{
	private static String lastlicenceplate="";
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
               ExitCheckInfoJson exitCheckInfoJson= (ExitCheckInfoJson) msg.obj;
                if (exitCheckInfoJson==null) {
					return;
				}
                String licenceplate= exitCheckInfoJson.getLicencePlate();
                //车牌号为空直接return
                if (TextUtils.isEmpty(licenceplate)) {
					return;
				}
                //这条记录刚刚已经出站并且接受到了
                if (lastlicenceplate.equals(licenceplate)) {
					return;
				}
                int count=DataSupport.count(ExitCheckInfo.class);
                if (count!=0) {
                	 ExitCheckInfo lastExitCheckInfo=DataSupport.findLast(ExitCheckInfo.class);
                	 if (licenceplate.equals(lastExitCheckInfo.getLicencePlate())) {
                		 //这条信息已经保存过了
						return;
					}
				}
 			    	ExitCheckInfo exitCheckInfo=new ExitCheckInfo();
 			    	exitCheckInfo.setAccurateLoadNum(exitCheckInfoJson.getAccurateLoadNum());
 			    	exitCheckInfo.setClassReportStatus(exitCheckInfoJson.getClassReportStatus());
 			    	exitCheckInfo.setVehicleId(exitCheckInfoJson.getVehicleId());
 			    	exitCheckInfo.setVehicleType(exitCheckInfoJson.getVehicleType());
 			    	exitCheckInfo.setInspectionStatus(exitCheckInfoJson.getInspectionStatus());
 			    	exitCheckInfo.setLicencePlate(exitCheckInfoJson.getLicencePlate());
 			    	
 			    	exitCheckInfo.setFirPhoto(exitCheckInfoJson.getFirPhoto());
 			    	exitCheckInfo.setFirAdultNum(exitCheckInfoJson.getFirAdultNum());
 			    	exitCheckInfo.setFirDriverId(exitCheckInfoJson.getFirDriverId());
 			    	exitCheckInfo.setFirFingerCode1(exitCheckInfoJson.getFirFingerCode1());
 			    	exitCheckInfo.setFirFingerCode2(exitCheckInfoJson.getFirFingerCode2());
 			    	exitCheckInfo.setFirFingerCode3(exitCheckInfoJson.getFirFingerCode3());
 			    	exitCheckInfo.setFirFingerCode4(exitCheckInfoJson.getFirFingerCode4());
 			    	
 			    	exitCheckInfo.setFirIdCard(exitCheckInfoJson.getFirIdCard());
 			    	exitCheckInfo.setFirName(exitCheckInfoJson.getFirName());
 			    	exitCheckInfo.setFirTel(exitCheckInfoJson.getFirTel());
 			    	
 			    	exitCheckInfo.setSecPhoto(exitCheckInfoJson.getSecPhoto());
 			    	exitCheckInfo.setSecAdultNum(exitCheckInfoJson.getSecAdultNum());
 			    	exitCheckInfo.setSecDriverId(exitCheckInfoJson.getSecDriverId());
 			    	exitCheckInfo.setSecFingerCode1(exitCheckInfoJson.getSecFingerCode1());
 			    	exitCheckInfo.setSecFingerCode2(exitCheckInfoJson.getSecFingerCode2());
 			    	exitCheckInfo.setSecFingerCode3(exitCheckInfoJson.getSecFingerCode3());
 			    	exitCheckInfo.setSecFingerCode4(exitCheckInfoJson.getSecFingerCode4());
 			    	
 			    	exitCheckInfo.setSecIdCard(exitCheckInfoJson.getSecIdCard());
 			    	exitCheckInfo.setSecName(exitCheckInfoJson.getSecName());
 			    	exitCheckInfo.setSecTel(exitCheckInfoJson.getSecTel());
 			    	exitCheckInfo.save();
 			    	//将保持的最后一个存在lastlicenceplate中对比
 			    	lastlicenceplate=exitCheckInfo.getLicencePlate();
 			    	Log.e("lastlicenceplate", lastlicenceplate);
 				
				NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification=new Notification(R.drawable.findfriend_icon_shake,"有车辆报班成功",System.currentTimeMillis());
				Intent intent=new Intent(OutofstationService.this,ExitstationChooseActivity.class);
				PendingIntent pi=PendingIntent.getActivity(OutofstationService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			    notification.setLatestEventInfo(OutofstationService.this, exitCheckInfoJson.getLicencePlate(), "报班成功！", pi);
			    manager.notify(1,notification);
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mTimer();
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	private void mTimer(){
		     ScheduledExecutorService executorService=Executors.newScheduledThreadPool(2);
		     mAsyHttpClient();
		     executorService.scheduleAtFixedRate(new EchoServer(), 10, 20, TimeUnit.SECONDS);
	}
	class  EchoServer implements Runnable{
		@Override
		public void run() {
			mAsyHttpClient();
		}
		
	}
	private void mAsyHttpClient() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
//					http://192.168.0.121:8090/tianmen/PushExitMessageAction!defaultMethod.action
					URL httpUrl=new URL(Configure.IP+"/TianMen/PushExitMessageAction!defaultMethod.action");
					HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					OutputStream out=conn.getOutputStream();
					String content="licenceplate="+lastlicenceplate;
					out.write(content.getBytes());
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					StringBuffer sb=new StringBuffer();
					String str;
					while ((str=reader.readLine())!=null) {
						sb.append(str);
					}
					JsonParser(sb.toString());
					System.out.println(sb.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void JsonParser(String jsonData) {
		try {
			String cname=null;
			try {
				JSONObject object=new JSONObject(jsonData);
				String flag=object.getString("flag");
				if (!flag.equals("true")) {
					return;
				}
				cname=object.getString("list");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Gson gson=new Gson();
			
			ExitCheckInfoJson	exitCheckInfoJson=gson.fromJson(cname, ExitCheckInfoJson.class);
			if (exitCheckInfoJson==null) {
				return;
			}
			Message msg=new Message();
			msg.obj=exitCheckInfoJson;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
   
}
