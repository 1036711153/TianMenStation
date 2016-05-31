package com.authentication.entity;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.Utils.CustomToast;
import com.example.Utils.T;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class Register_Finger_Lab {
	private String content;
	private String ip;
	private Context context;
	
	public Register_Finger_Lab(String content, String ip, Context context) {
		this.content = content;
		this.ip = ip;
		this.context = context;
	}

	public void AsyncHttp() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(ip, params,new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				DataSupport.deleteAll(Register_Finger_DataBase.class);
				
				String cname=null;
				try {
					JSONObject object=new JSONObject(arg2);
					cname=object.getString("driverInfo");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Gson gson=new Gson();
				List<Register_Finger> register_Fingers=gson.fromJson(cname, new TypeToken<List<Register_Finger>>(){}.getType());
				for (Register_Finger register_Finger : register_Fingers) {
					Log.e("carNum", register_Finger.getName());
					Register_Finger_DataBase register_Finger_DataBase=new Register_Finger_DataBase();
					register_Finger_DataBase.setName(register_Finger.getName());
					register_Finger_DataBase.setDriver_id(register_Finger.getDriver_id());
					register_Finger_DataBase.setId_card(register_Finger.getId_card());
					register_Finger_DataBase.save();
				}
				CustomToast.Custom_Toast(context,"驾驶员数据同步成功！！",0,Toast.LENGTH_SHORT);

			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				CustomToast.UnComment_Custom_Toast(context,"驾驶员数据同步失败，服务器连接出错！！",0,Toast.LENGTH_SHORT);
			}
		});
		
	}

}
