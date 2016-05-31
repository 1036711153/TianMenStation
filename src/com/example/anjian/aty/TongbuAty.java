package com.example.anjian.aty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.app.Fragment.SavedState;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.authentication.activity.R;
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionCarSummary;
import com.example.anjian.InspectionItem;
import com.example.anjian.InspectionSubitem;
import com.example.anjian.LinshiInspectionCarDetail;
import com.example.anjian.SumInspectionCarSummary;
import com.example.configure.Configure;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class TongbuAty extends Activity {
 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anjian_tongbu_main);
		SQLiteDatabase db=Connector.getDatabase();
		TongbuListener();
		ShangchuanListner();
	}

	private void ShangchuanListner() {
		findViewById(R.id.shangchuanbiao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Gson gson=new Gson();
				List<InspectionCarSummary>inspectionCarSummaries=DataSupport.findAll(InspectionCarSummary.class,true);
				SumInspectionCarSummary sumInspectionCarSummary=new SumInspectionCarSummary(inspectionCarSummaries);
				String content=gson.toJson(sumInspectionCarSummary);
				Save(content);
				Toast.makeText(getApplicationContext(), "生成文本成功！", 1).show();
				DataSupport.deleteAll(InspectionCarDetail.class);
				DataSupport.deleteAll(InspectionCarSummary.class);
				TongbuAty.this.finish();
				
				
//				AsyncHttpClient client=new AsyncHttpClient();
//				RequestParams params=new RequestParams();
//				params.put("content", content);
//				
//				client.post("http://10.0.3.2:8090/Upload_8-1/MyServelt5", params,new AsyncHttpResponseHandler() {
//					
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						Toast.makeText(getApplicationContext(), "上传数据成功！", 1).show();
//						DataSupport.deleteAll(InspectionCarDetail.class);
//						DataSupport.deleteAll(InspectionCarSummary.class);
//					}
//					
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//						Toast.makeText(getApplicationContext(), "链接服务器失败！", 1).show();
//					}
//				});
			}
		});
		
	}
	
	private void Save(String content) {
		File tmpDir=new File(Environment.getExternalStorageDirectory()+"/anjiantxt");
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
		String anjiantxt="anjian"+".txt";
	    String  anjian_TEXT = tmpDir.getAbsolutePath()+"/"+anjiantxt;
		File img=new File(anjian_TEXT);
		FileOutputStream outputStream=null;
		BufferedWriter writer=null;
		try {
			outputStream=new FileOutputStream(img);
			writer=new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	}

	public void TongbuListener() {
		findViewById(R.id.tongbubiao).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {}
		});
	}

}
