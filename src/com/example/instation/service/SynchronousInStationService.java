package com.example.instation.service;


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

import com.example.configure.Configure;
import com.example.instation.EntranceInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronousInStationService extends Service{
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
//		startTimer();
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
			int count= DataSupport.count(EntranceInfo.class);
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
		List<EntranceInfo> filelInspectionCarDetails=DataSupport.findAll(EntranceInfo.class);
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	EntranceInfo  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getUpload_photo1_path();
	    	String filename2=linshiInspectionCarDetail.getUpload_photo2_path();
	    	if (!TextUtils.isEmpty(filename1)&&!TextUtils.isEmpty(filename2)) {
	    		File file1 = new File(filename1);
	    		File file2 = new File(filename2);
	    		try {
					params1.put("image1"+i, file1);
					params1.put("image2"+i, file2);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	    
	    client1.post(Configure.IP+"/TianMen/EntranceUpFileAction.action", params1,new FileAsyncHttpResponseHandler(SynchronousInStationService.this) {
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	    List<EntranceInfo> mEntranceInfos=DataSupport.findAll(EntranceInfo.class);
		Gson gson=new Gson();
		String content=gson.toJson(mEntranceInfos);
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(Configure.IP+"/TianMen/PDAEntranceAction!defaultMethod.action",params ,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				DataSupport.deleteAll(EntranceInfo.class);
				Log.e("aaa", "onSuccess");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("aaa", "onFailure");
			}
		});
	}
}
