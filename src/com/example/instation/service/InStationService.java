package com.example.instation.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

import com.authentication.activity.InSationHXUHFActivity;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.configure.Configure;
import com.example.instation.InstationChooseActivity;

public class InStationService extends Service{
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String chepaihaoString= (String) msg.obj;
			if (chepaihaoString==null||TextUtils.isEmpty(chepaihaoString)||chepaihaoString.equals("null")) {
				return;
			}
			CustomDialog.Builder builder = new CustomDialog.Builder(InStationService.this);
			builder.setMessage(chepaihaoString+"正在进站中...");
			builder.setTitle("配载车进站操作？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//设置你的操作事项
					dialog.dismiss();
					Intent intent=new Intent(getApplicationContext(),InSationHXUHFActivity.class);
                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                	startActivity(intent);
				}
			});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			CustomDialog ad = builder.create();  
	        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  
	        ad.setCanceledOnTouchOutside(false);                                   //点击外面区域不会让dialog消失  
	        ad.show(); 
             }
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
//		startTimer();
		mTimer();
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	private void mTimer(){
		     ScheduledExecutorService executorService=Executors.newScheduledThreadPool(2);
		     mAsyHttpClient();
		     executorService.scheduleAtFixedRate(new EchoServer(), 5, 5, TimeUnit.SECONDS);
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
					URL httpUrl=new URL(Configure.IP+"/TianMen/PDAEntranceAction!getLicencePlate.action");
					HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("POST");
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					StringBuffer sb=new StringBuffer();
					String str;
					while ((str=reader.readLine())!=null) {
						System.out.println(str);
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
				cname=object.getString("licencePlate");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (cname==null||TextUtils.isEmpty(cname)||cname.equals("null")) {
				return;
			}
			Message msg=new Message();
			msg.obj=cname;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
   
}
