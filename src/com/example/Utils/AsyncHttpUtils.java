package com.example.Utils;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.widget.Toast;

public class AsyncHttpUtils {
	private String content;
	private String ip;
	private Context context;
	
	public AsyncHttpUtils(String content, String ip, Context context) {
		this.content = content;
		this.ip = ip;
		this.context = context;
	}

	public void AsyncHttp() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(ip, params,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CustomToast.Custom_Toast(context,"数据传送成功！",0,Toast.LENGTH_SHORT);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(context,"数据传送失败\n请稍后再试！！",0,Toast.LENGTH_SHORT);
			}
		});
		
	}
}
