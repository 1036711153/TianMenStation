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
import com.example.instation.LoginActivity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class CarNumLab {
	private String content;
	private String ip;
	private Context context;
	
	public CarNumLab(String content, String ip, Context context) {
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
				DataSupport.deleteAll(AutoCarNum.class);
				String cname=null;
				try {
					JSONObject object=new JSONObject(arg2);
					cname=object.getString("licencePlate");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Gson gson=new Gson();
				List<CarNum> carNums=gson.fromJson(cname, new TypeToken<List<CarNum>>(){}.getType());
				if (carNums==null||carNums.size()==0) {
					return;
				}
				
				for (CarNum carNum : carNums) {
					Log.e("carNum", carNum.getCarnum());
					AutoCarNum autoCarNum=new AutoCarNum();
					autoCarNum.setCarNum(carNum.getCarnum());
					autoCarNum.save();
				}
				CustomToast.Custom_Toast(context,"车牌号数据同步成功！！",0,Toast.LENGTH_SHORT);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				CustomToast.UnComment_Custom_Toast(context,"车牌号数据同步失败，服务器连接出错！！",0,Toast.LENGTH_SHORT);
			}
		});
		
	}

}
