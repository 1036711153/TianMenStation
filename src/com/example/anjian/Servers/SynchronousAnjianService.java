package com.example.anjian.Servers;


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
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionCarSummary;
import com.example.configure.Configure;
import com.example.instation.EntranceInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronousAnjianService extends Service{
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
		     executorService.scheduleAtFixedRate(new EchoServer(), 25, 25, TimeUnit.SECONDS);
	}
	class  EchoServer implements Runnable{
		@Override
		public void run() {
			Log.d("AnjianService", "AnjianService");
			int count= DataSupport.count(InspectionCarSummary.class);
			if (count!=0) {
				Message msg=new Message();
				msg.obj=true;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	private void mAsyHttpClient() {
	    List<InspectionCarSummary> mInspectionCarSummaries=DataSupport.findAll(InspectionCarSummary.class);
		AsyncHttpClient client1=new AsyncHttpClient();
		RequestParams params1=new RequestParams();
		List<File> files=new ArrayList<File>();
		//将配载车安检图片添加到files文件集合中
		for (int i = 0; i < mInspectionCarSummaries.size(); i++) {
			String filepath=mInspectionCarSummaries.get(i).getCheckImg();
			if (!TextUtils.isEmpty(filepath)) {
				files.add(new File(filepath));
			}
			//主项目四张图片
	    	 String image1path=mInspectionCarSummaries.get(i).getImage1();
			 if (!TextUtils.isEmpty(image1path)) {
					files.add(new File(image1path));
				}
			 String image2path=mInspectionCarSummaries.get(i).getImage2();
			 if (!TextUtils.isEmpty(image2path)) {
					files.add(new File(image2path));
				}
			 String image3path=mInspectionCarSummaries.get(i).getImage3();
			 if (!TextUtils.isEmpty(image3path)) {
					files.add(new File(image3path));
				}
			 String image4path=mInspectionCarSummaries.get(i).getImage4();
			 if (!TextUtils.isEmpty(image4path)) {
					files.add(new File(image4path));
				}
		}
		int count=DataSupport.count(InspectionCarDetail.class);
		//如果安检明细表不为空则。。。上传安检明细信息
		if (count!=0) {
		List<InspectionCarDetail> filelInspectionCarDetails=DataSupport.findAll(InspectionCarDetail.class);
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	InspectionCarDetail  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getImageURL();
	    	if (!TextUtils.isEmpty(filename1)) {
	    		files.add(new File(filename1));
			}
		  }
		}
	  int filesize=files.size();
	  if (filesize!=0) {
			  //上传配载车安检结果
			   for (int i=0;i<files.size();i++) {
				   try {
						params1.put("image"+i, files.get(i));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
	        }
	//最后将所有的图片打包上传
     client1.post(Configure.IP+"/TianMen/UploadFile.servlet", params1,new FileAsyncHttpResponseHandler(SynchronousAnjianService.this) {
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// TODO Auto-generated method stub
			}
		});
		Gson gson=new Gson();
		String  content=gson.toJson(mInspectionCarSummaries);
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(Configure.IP+"/TianMen/PDAInfoAction!defaultMethod.action",params ,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				DataSupport.deleteAll(InspectionCarSummary.class);
				Log.e("aaa", "onSuccess");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
}
