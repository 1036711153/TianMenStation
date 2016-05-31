package com.example.outstation.aty;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.Utils.CustomToast;
import com.example.Utils.InputTools;
import com.example.Utils.Space_Character;
import com.example.configure.Configure;
import com.example.instation.InstationJsonUser;
import com.example.outstation.entity.ExitCheckInfo;
import com.example.outstation.entity.ExitInfo;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OutStationUploadDataAty extends Activity implements OnClickListener {
//	private Button reset;
	private boolean is_safaty;
	private LinearLayout safaty_belt_layout;
	private Button open;
	private EditText adult_number;
	private EditText child_number;
	private TextView load_able_number;
	private CheckBox is_safaty_belt;
	private String upload_photo1_path;
	private String upload_photo2_path;
	private ExitCheckInfo myExitCheckInfo;
	private ImageButton back_page;
	private InstationJsonUser jsonuser;
	private TextView Inspection_name;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int a=(int) msg.obj;
			if (a==1) {
				CustomToast.UnComment_Custom_Toast(getApplicationContext(),"连接开关门失败！！",0,Toast.LENGTH_SHORT);
			}else if (a==2) {
				CustomToast.UnComment_Custom_Toast(getApplicationContext(),"链接服务器失败\n数据以及保存PDA数据库中",0,Toast.LENGTH_SHORT);
			}else if (a==3) {
				CustomToast.Custom_Toast(OutStationUploadDataAty.this,"上传数据成功！",0,Toast.LENGTH_SHORT);
				// 删除报班推送过来的信息
				String connnect_database_chepaigao=Space_Character.trimspace(myExitCheckInfo.getLicencePlate());
				int count=DataSupport.count(ExitCheckInfo.class);
				if (!TextUtils.isEmpty(connnect_database_chepaigao) && count!=0) {
				    DataSupport.deleteAll(ExitCheckInfo.class,"licenceplate = ?",connnect_database_chepaigao+"");
				}
				//删除刚刚生成的那一条记录
				int count1=DataSupport.count(ExitInfo.class);
				if (count1!=0) {
					ExitInfo exitInfo = DataSupport
							.findLast(ExitInfo.class);
					DataSupport.delete(ExitInfo.class,exitInfo.getId());
				}
			}
		};
	};
	
	private ControllerOpenCloseDoor closeDoor=new ControllerOpenCloseDoor(OutStationUploadDataAty.this, Configure.CCONTROLLER_DOOR_IP2,mHandler);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_outstation_aty4);
		initId();
		SetListener();
		adult_number.requestFocus();
		InputMethodManager imm = (InputMethodManager) adult_number.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
		ActivityCollector.addActivity(this);
	}

	private void SetListener() {
		open.setOnClickListener(this);
		back_page.setOnClickListener(this);
		safaty_belt_layout.setOnClickListener(this);
	}

	private void initId() {
		safaty_belt_layout=(LinearLayout) findViewById(R.id.safaty_belt_layout);
		Inspection_name=(TextView) findViewById(R.id.Inspection_name);
		jsonuser = DataSupport.findLast(InstationJsonUser.class);
		Inspection_name.setText(jsonuser.getName());
		back_page = (ImageButton) findViewById(R.id.back_page);
		open = (Button) findViewById(R.id.open);
		adult_number = (EditText) findViewById(R.id.myadult_number);
		child_number = (EditText) findViewById(R.id.mychild_number);
		load_able_number = (TextView) findViewById(R.id.load_able_number);
		is_safaty_belt = (CheckBox) findViewById(R.id.is_safaty_belt);

		upload_photo1_path = (String) getIntent().getSerializableExtra(
				"upload_photo1_path");
		upload_photo2_path = (String) getIntent().getSerializableExtra(
				"upload_photo2_path");
		myExitCheckInfo = (ExitCheckInfo) getIntent().getSerializableExtra(
				"myExitCheckInfo");
		load_able_number.setText(myExitCheckInfo.getAccurateLoadNum() + "");
	}

	private void Judge() {
		// 创建对象保存数据库，然后上传完之后删除这条记录
		ExitInfo exitInfo = new ExitInfo();
		exitInfo.setLicencePlate(myExitCheckInfo.getLicencePlate());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		exitInfo.setTime(df.format(new Date()));
		// 值班员的ID
		String Watch_ID = jsonuser.getWatchID();
		exitInfo.setWatchID(Watch_ID);
		exitInfo.setInCarImaURL(upload_photo2_path);
		exitInfo.setOutCarImaURL(upload_photo1_path);
		exitInfo.setIsAttached(1);
		exitInfo.setExitWay("手动");
		exitInfo.setStationcode(Configure.Stationcode);

		String adult_number1 = trimspace(adult_number.getText().toString());
		int AdultNum = Integer.parseInt(adult_number1);
		exitInfo.setAdultNum(AdultNum);

		String childNum = trimspace(child_number.getText().toString());
		int ChildrenNum = Integer.parseInt(childNum);
		exitInfo.setChildrenNum(ChildrenNum);
        //保存这一条记录
		exitInfo.save();
		
		List<ExitInfo> mExitInfos=new ArrayList<>();
		mExitInfos.add(exitInfo);
		Gson gson = new Gson();
		final String gsonString = gson.toJson(mExitInfos);

		AsyncHttpClient client1 = new AsyncHttpClient();
		RequestParams params1 = new RequestParams();
		File In_car_photo = new File(upload_photo1_path);
		File Out_car_photo = new File(upload_photo2_path);
		try {
			params1.put("image1", In_car_photo);
			params1.put("image2", Out_car_photo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		client1.post(Configure.IP+"/TianMen/ExitUpFileAction.action", params1,
				new FileAsyncHttpResponseHandler(getApplicationContext()) {
					@Override
					public void onSuccess(int arg0, Header[] arg1, File arg2) {
						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams params = new RequestParams();
						params.put("gsonString", gsonString);
						client.post(Configure.IP+"/TianMen/ExitCheckAction!saveExitInfo.action",
								params, new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										
										Message msg=new Message();
										msg.obj=3;
										mHandler.sendMessage(msg);
										
									}
									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										Message msg=new Message();
										msg.obj=2;
										mHandler.sendMessage(msg);
									  }
								   });
					             }
					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, File arg3) {
						Message msg=new Message();
						msg.obj=2;
						mHandler.sendMessage(msg);					}
				});
	           	ActivityCollector.finishAll();
	          }
	private String trimspace(String s) {
		String str = s.replaceAll(" ", "");  
		return str;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.open:
			if (!is_safaty_belt.isChecked()) {
				CustomToast.UnComment_Custom_Toast(getApplicationContext(),"请让驾驶员系上安全带",0,Toast.LENGTH_SHORT);
				return;
			} else if (TextUtils.isEmpty(trimspace(adult_number.getText().toString()))
					|| TextUtils.isEmpty(trimspace(child_number.getText().toString()))) {
				CustomToast.Custom_Toast(getApplicationContext(),"请填写成人或者儿童数量",0,Toast.LENGTH_SHORT);
				return;
			} else if (Integer.parseInt(trimspace(child_number.getText().toString()))
					+ Integer.parseInt(trimspace(adult_number.getText().toString())) > Integer
					.parseInt(myExitCheckInfo.getAccurateLoadNum()+"")) {
				CustomToast.UnComment_Custom_Toast(getApplicationContext(),"已经超载！！",0,Toast.LENGTH_SHORT);
				return;
			} else if (Integer.parseInt(trimspace(child_number.getText().toString())) > (Integer
					.parseInt(trimspace(myExitCheckInfo.getAccurateLoadNum()+""))*Configure.Out_OfSattionMaxChildCount)) {
				CustomToast.UnComment_Custom_Toast(getApplicationContext(),"儿童人数超数！！",0,Toast.LENGTH_SHORT);
				return;
			} else {
			    closeDoor.OpenDoor();
			    open.setEnabled(false);
			    open.setBackgroundResource(R.drawable.opendoorbuttong1);
			    Judge();
			}
			break;
		case R.id.back_page:
			OutStationUploadDataAty.this.finish();
			break;
		case R.id.safaty_belt_layout:
			is_safaty=!is_safaty;
			is_safaty_belt.setChecked(is_safaty);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		safaty_belt_layout=null;
		Inspection_name=null;
		is_safaty_belt=null;
		open=null;
		adult_number=null;
		child_number=null;
		load_able_number=null;
		back_page=null;
		closeDoor=null;
		jsonuser=null;
		System.gc();
	}
}
