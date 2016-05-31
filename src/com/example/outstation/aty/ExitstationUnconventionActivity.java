package com.example.outstation.aty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.authentication.activity.R;
import com.authentication.entity.AutoCarNum;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.Utils.T;
import com.example.configure.Configure;
import com.example.instation.InstationJsonUser;
import com.example.instation.LoginActivity;
import com.example.outstation.entity.UnconventionExitstation;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.autocomplete.ArrayAdapter;

public class ExitstationUnconventionActivity extends Activity implements OnClickListener{
	private ImageButton back_page;
	private AutoCompleteTextView car_num;
	private EditText drive_name;
	private EditText drive_tel;
	private EditText accountloadnum;
	private EditText remark;
	private Spinner  car_type;
	private Button open_door;
	private InstationJsonUser jsonuser;
	private List<String> mCarNums=new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private List<AutoCarNum> autoCarNums;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int a=(int) msg.obj;
			if (a==1) {
				CustomToast.UnComment_Custom_Toast(ExitstationUnconventionActivity.this,"连接开关门失败！！",0,Toast.LENGTH_SHORT);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exitstation_unconvention_activity);
		initID();
		initOnclick();
		ActivityCollector.addActivity(this);
	}

	private void initID() {
		// TODO Auto-generated method stub
		car_num=(AutoCompleteTextView) findViewById(R.id.car_num);
		back_page=(ImageButton) findViewById(R.id.back_page);
		drive_name=(EditText) findViewById(R.id.drive_name);
		drive_tel=(EditText) findViewById(R.id.drive_tel);
		accountloadnum=(EditText) findViewById(R.id.accountloadnum);
		remark=(EditText) findViewById(R.id.remark);
		car_type=(Spinner) findViewById(R.id.car_type);
		open_door=(Button) findViewById(R.id.open);
		jsonuser = DataSupport.findLast(InstationJsonUser.class);
		//查找车牌号操作
				DataBaseFindData dataBaseFindData=new DataBaseFindData();
				autoCarNums= dataBaseFindData.getAutoCarNum();
				if (autoCarNums.size()!=0) {
					for (int i = 0; i < autoCarNums.size(); i++) {
						mCarNums.add(autoCarNums.get(i).getCarNum());
					}
				}
				adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mCarNums);
				car_num.setThreshold(1);
				car_num.setAdapter(adapter);
	}

	private void initOnclick() {
		back_page.setOnClickListener(this);
		open_door.setOnClickListener(this);
		car_num.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputTools.HideKeyboard(view);
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.back_page:
        	this.finish();
        	break;
        case R.id.open:
				if (TextUtils.isEmpty(car_num.getText().toString())) {
					CustomToast.Custom_Toast(ExitstationUnconventionActivity.this,"请输入车牌号!",0,Toast.LENGTH_SHORT);
					return;
				} else {
//					CustomDialog.Builder builder = new CustomDialog.Builder(ExitstationUnconventionActivity.this);
//					builder.setMessage("是否确认开启栅栏");
//					builder.setTitle("确认开门");
//					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
							//设置你的操作事项

							
							ControllerOpenCloseDoor closeDoor=new ControllerOpenCloseDoor(ExitstationUnconventionActivity.this, Configure.CCONTROLLER_DOOR_IP2,mHandler);
						    closeDoor.OpenDoor();
						    
							open_door.setEnabled(false);
							open_door.setBackgroundResource(R.drawable.opendoorbuttong1);
							UnconventionExitstation unconventionExitstation=new UnconventionExitstation();
							if (jsonuser.getWatchID()!=null) {
								unconventionExitstation.setWatch_ID(jsonuser
										.getWatchID());
							}else {
								unconventionExitstation.setWatch_ID("");
							}
							unconventionExitstation.setLicencePlate(car_num.getText()
									.toString());
							unconventionExitstation.setStationcode(Configure.Stationcode);
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");// 设置日期格式
							unconventionExitstation.setTime(df.format(new Date()));
							unconventionExitstation.setDriver_Name(drive_name.getText().toString());
							unconventionExitstation.setTel(drive_tel.getText().toString());
							String vehicle_Property = car_type.getSelectedItem().toString();
							if (vehicle_Property.equals("旅游车")) {
								vehicle_Property="003";
							}else if (vehicle_Property.equals("过路车")) {
								vehicle_Property="004";
							}else if (vehicle_Property.equals("非营运车")) {
								vehicle_Property="005";
							}
							unconventionExitstation.setRemark(remark.getText().toString());
							unconventionExitstation.setVehicle_Property(vehicle_Property);
							unconventionExitstation.setNum(accountloadnum.getText().toString());
							unconventionExitstation.save();
							//联网上传
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							Gson gson=new Gson();
							String content = gson.toJson(unconventionExitstation);
							params.put("content",content);
							client.post(Configure.IP+"/TianMen/UnconventionExitAction!unExit_PDA.action", params,new AsyncHttpResponseHandler() {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									CustomToast.Custom_Toast(ExitstationUnconventionActivity.this,"上传数据成功!",0,Toast.LENGTH_SHORT);
									UnconventionExitstation unconventionExitstation = DataSupport
											.findLast(UnconventionExitstation.class);
									DataSupport.delete(UnconventionExitstation.class, unconventionExitstation.getId());
									ActivityCollector.finishAll();
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									CustomToast.UnComment_Custom_Toast(ExitstationUnconventionActivity.this,"链接服务器失败\n数据以及保存PDA数据库中!",0,Toast.LENGTH_SHORT);
								}
							});
						
//							dialog.dismiss();
//						}
//					});
//					builder.setNegativeButton("取消",
//							new android.content.DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									dialog.dismiss();
//								}
//							});
//					builder.create().show();
				}
        	break;
//        case R.id.close:
//        	CustomDialog.Builder builder2 = new CustomDialog.Builder(ExitstationUnconventionActivity.this);
//			builder2.setMessage("是否确认返回首页");
//			builder2.setTitle("确认返回");
//			builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					//设置你的操作事项
////					ControllerOpenCloseDoor closeDoor=new ControllerOpenCloseDoor(ExitstationUnconventionActivity.this, Configure.CCONTROLLER_DOOR_IP2,mHandler);
////				    closeDoor.CloseDoor();
//				    ActivityCollector.finishAll();
//					dialog.dismiss();
//				}
//			});
//			builder2.setNegativeButton("取消",
//					new android.content.DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					});
//			builder2.create().show();
//        	break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

}
