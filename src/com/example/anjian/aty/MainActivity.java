package com.example.anjian.aty;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import com.authentication.activity.R;
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionCarSummary;
import com.example.anjian.InspectionItem;
import com.example.anjian.LinshiInspectionCarDetail;
import com.example.anjian.SumInspectionCarSummary;
import com.example.anjian.SumLinshiInspectionCarDetail;
import com.example.anjian.adpter.MyGridAdapter;
import com.example.anjian.adpter.MyGridAdapter.OnMyItemClickListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Button reset;
	private Button commit;
	private Button tongbu;
	private MyGridAdapter mAdapter;
	private GridView mGridView;
	private TextView qualify;
	private List<InspectionItem> inspectionItems ;
	private int[]a=new int[]{
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
	};//表示有多少长按或者单击后检查过项目，表示检查过项目的数目
    private Handler mHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if (msg.arg1==1) {
				qualify.setText("合格");
			}else {
				qualify.setText("不合格");
			}
    	};
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anjian_activity_main);
        SQLiteDatabase db=Connector.getDatabase();
        initID();
        setLisetener();
        mTimer();
    }

	private void mTimer() {
		 ScheduledExecutorService executorService=Executors.newScheduledThreadPool(2);
	     mAsyClient();
	     executorService.scheduleAtFixedRate(new EchoServer(), 1, 1, TimeUnit.SECONDS);
	}
    
	private void mAsyClient() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int conclusion=1;//1表示合格，0表示不合格
				List<LinshiInspectionCarDetail>linshiInspectionCarDetails=DataSupport.where("conclusion = ?","0").find(LinshiInspectionCarDetail.class);
				if (linshiInspectionCarDetails.size()!=0) {
					conclusion=0;
				}	
				if (conclusion==1) {
					Message msg=new Message();
					msg.arg1=1;
					mHandler.sendMessage(msg);
				}else {
					Message msg=new Message();
					msg.arg1=0;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
		
		
	}

	class  EchoServer implements Runnable{
		@Override
		public void run() {
			mAsyClient();
		}
		
	}
	
	private void setLisetener() {
		resetListener();
		tongbuListener();
		commitListener();
		gridViewListener();
	}

	private void gridViewListener() {
		mAdapter.setMyOnItemClickListener(new OnMyItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				InspectionItem inspectionItem=inspectionItems.get(position);
				Intent intent=new Intent(MainActivity.this,InspectItemAty.class);
				intent.putExtra("inspectionItem_id", inspectionItem.getId());
				view.setBackgroundResource(R.color.green);
				a[position]=1;
				startActivity(intent);
			}

			@Override
			public void onItemLongClick(View view, int position) {
				view.setBackgroundResource(R.color.green);
				a[position]=1;
			}
		});
	}
    //提交数据给服务器
	private void commitListener() {
		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int result=0;
				for (int i = 0; i < inspectionItems.size(); i++) {
					 result=a[i]+result;
				}
				if (result!=inspectionItems.size()) {
					Toast.makeText(getApplicationContext(), "请按程序检查完各个主项目之后再提交！", 1).show();
				    return;
				}
				//提交数据给服务器
				int conclusion=1;//1表示合格，0表示不合格
				List<LinshiInspectionCarDetail>linshiInspectionCarDetails=DataSupport.where("conclusion = ?","0").find(LinshiInspectionCarDetail.class);
				if (linshiInspectionCarDetails.size()!=0) {
					conclusion=0;
				}
				InspectionCarSummary inspectionCarSummary=new InspectionCarSummary();
				List<LinshiInspectionCarDetail> mylinshiInspectionCarDetails=DataSupport.findAll(LinshiInspectionCarDetail.class);
				List<InspectionCarDetail> myinspectionCarDetails=new ArrayList<InspectionCarDetail>();
				for (int i = 0; i < mylinshiInspectionCarDetails.size(); i++) {
					LinshiInspectionCarDetail myLinshiInspectionCarDetail=mylinshiInspectionCarDetails.get(i);
					InspectionCarDetail myInspectionCarDetail=new InspectionCarDetail();
					myInspectionCarDetail.setConclusion(myLinshiInspectionCarDetail.getConclusion());
					myInspectionCarDetail.setDefect_Description(myLinshiInspectionCarDetail.getDefect_Description());
					myInspectionCarDetail.setZhuxiangmu_name(myLinshiInspectionCarDetail.getZhuxiangmu_name());
					myInspectionCarDetail.setImageURL(myLinshiInspectionCarDetail.getImageURL());
					myInspectionCarDetail.setInspectionSubitem_name(myLinshiInspectionCarDetail.getInspectionSubitem_name());
					myInspectionCarDetail.save();
					myinspectionCarDetails.add(myInspectionCarDetail);
				}
				inspectionCarSummary.setConclusion(conclusion);
				inspectionCarSummary.setVehicle_ID("7189398");
				inspectionCarSummary.setDateTime("2015-8-20");
				inspectionCarSummary.setInspector_ID("183940");
				inspectionCarSummary.setInspectionCarDetails(myinspectionCarDetails);
				inspectionCarSummary.save();
//				Toast.makeText(getApplicationContext(), "保存数据成功！", 1);
				Gson gson=new Gson();
				InspectionCarSummary lastCarSummary=DataSupport.findLast(InspectionCarSummary.class,true);
				String content=gson.toJson(lastCarSummary);
				
			    MainActivity.this.finish();
				AsyncHttpClient client=new AsyncHttpClient();
				RequestParams params=new RequestParams();
				params.put("content", content);
				client.post("http://10.0.3.2:8090/Upload_8-1/MyServelt5", params,new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Toast.makeText(getApplicationContext(), "上传数据成功！", 1).show();
						InspectionCarSummary lastCarSummary=DataSupport.findLast(InspectionCarSummary.class);
						DataSupport.delete(InspectionCarSummary.class, lastCarSummary.getId());
					    MainActivity.this.finish();
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Toast.makeText(getApplicationContext(), "链接服务器失败!检查结果以保存在数据库中！", 1).show();
						MainActivity.this.finish();
					}
				});
			}
		});
	}

	private void tongbuListener() {
		tongbu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,CircleActivity.class));
				
			}
		});
	}
	private void resetListener() {
		reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mGridView.setAdapter(mAdapter);
				for (int i = 0; i < inspectionItems.size(); i++) {
					 a[i]=0;
				}
				//重置数据
				ContentValues values=new ContentValues();
				values.put("imageurl", "");
				values.put("conclusion", 1);
				values.put("defect_description", "");
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values);
			}
		});
	}


	private void initID() {
		qualify=(TextView) findViewById(R.id.qualify);
		reset=(Button) findViewById(R.id.button10);
		commit=(Button) findViewById(R.id.button11);
		tongbu=(Button) findViewById(R.id.tongbu);
		mGridView=(GridView) findViewById(R.id.gridView1);
        inspectionItems = DataSupport.findAll(InspectionItem.class,true);
		mAdapter=new MyGridAdapter(MainActivity.this, inspectionItems);
		mGridView.setAdapter(mAdapter);
		if (DataSupport.count(LinshiInspectionCarDetail.class)==0) {
			for (int i = 0; i < inspectionItems.size(); i++) {
				InspectionItem inspectionItem= inspectionItems.get(i);
				for (int j = 0; j < inspectionItem.getInspectionSubitems().size(); j++) {
					LinshiInspectionCarDetail inspectionCarDetail=new LinshiInspectionCarDetail();
					inspectionCarDetail.setInspectionSubitem_name(inspectionItem.getInspectionSubitems().get(j).getName());
					inspectionCarDetail.setZhuxiangmu_name(inspectionItems.get(i).getCname());
					inspectionCarDetail.setConclusion(1);
					inspectionCarDetail.setImageURL("");
					inspectionCarDetail.setDefect_Description("");
					inspectionCarDetail.save();
				}
		   }
		}else {
			//重置数据
			ContentValues values=new ContentValues();
			values.put("imageurl", "");
			values.put("conclusion", 1);
			values.put("defect_description", "");
			DataSupport.updateAll(LinshiInspectionCarDetail.class, values);
			
		}
	}
}
