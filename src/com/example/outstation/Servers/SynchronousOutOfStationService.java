package com.example.outstation.Servers;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.Utils.CustomToast;
import com.example.configure.Configure;
import com.example.instation.EntranceInfo;
import com.example.outstation.entity.ExitInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronousOutOfStationService extends Service{
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			 boolean isupload= (boolean) msg.obj;
               if (isupload) {
            	   mAsyHttpClient();
			   }
             }
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
	public void onDestroy() {
		super.onDestroy();
	}
	private void mTimer(){
		     ScheduledExecutorService executorService=Executors.newScheduledThreadPool(2);
		     executorService.scheduleAtFixedRate(new EchoServer(), 5, 25, TimeUnit.SECONDS);
	}
	class  EchoServer implements Runnable{
		@Override
		public void run() {
			int count= DataSupport.count(ExitInfo.class);
			//有数据就上传数据
			if (count!=0) {
				Message msg=new Message();
				msg.obj=true;
				mHandler.sendMessage(msg);
			}
		}
	}
	private void mAsyHttpClient() {
		AsyncHttpClient client1=new AsyncHttpClient();
		RequestParams params1=new RequestParams();
		List<File> files=new ArrayList<File>();
		List<ExitInfo> filelInspectionCarDetails=DataSupport.findAll(ExitInfo.class);
		if (filelInspectionCarDetails.size()==0) {
			return;
		}
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	ExitInfo  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getInCarImaURL();
	    	String filename2=linshiInspectionCarDetail.getOutCarImaURL();
	    	if (!TextUtils.isEmpty(filename1)&&!TextUtils.isEmpty(filename2)) {
	    		File file1 = new File(filename1);
	    		File file2 = new File(filename2);
	    		try {
					params1.put("image1", file1);
					params1.put("image2", file2);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    client1.post(Configure.IP+"/TianMen/ExitUpFileAction.action", params1,new FileAsyncHttpResponseHandler(SynchronousOutOfStationService.this) {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, File arg2) {
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				}
			});
		}
	   
	    List<ExitInfo> mExitInfos=DataSupport.findAll(ExitInfo.class);
		Gson gson=new Gson();
		String content=gson.toJson(mExitInfos);
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("gsonString", content);
		client.post(Configure.IP+"/TianMen/ExitCheckAction!saveExitInfo.action",params ,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				DataSupport.deleteAll(ExitInfo.class);
				Log.e("aaa", "onSuccess");
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
}
