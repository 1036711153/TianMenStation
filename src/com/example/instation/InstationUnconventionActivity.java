package com.example.instation;

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
import android.util.Log;
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
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.autocomplete.ArrayAdapter;

public class InstationUnconventionActivity extends Activity implements OnClickListener{
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
				CustomToast.UnComment_Custom_Toast(InstationUnconventionActivity.this,"连接开关门失败！！",0,Toast.LENGTH_SHORT);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instation_unconvention_activity);
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
					CustomToast.Custom_Toast(InstationUnconventionActivity.this,"请输入车牌号！",0,Toast.LENGTH_SHORT);
					return;
				} else {
							ControllerOpenCloseDoor closeDoor=new ControllerOpenCloseDoor(InstationUnconventionActivity.this, Configure.CCONTROLLER_DOOR_IP,mHandler);
						    closeDoor.OpenDoor();
						    
							open_door.setEnabled(false);
							open_door.setBackgroundResource(R.drawable.opendoorbuttong1);
							UnconventionInstation unconventionInstation=new UnconventionInstation();
							if (jsonuser.getWatchID()!=null) {
								unconventionInstation.setWatch_ID(jsonuser
										.getWatchID());
							}else {
								unconventionInstation.setWatch_ID("");
							}
							unconventionInstation.setLicencePlate(car_num.getText()
									.toString());
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");// 设置日期格式
							unconventionInstation.setTime(df.format(new Date()));
							unconventionInstation.setStationcode(Configure.Stationcode);
							unconventionInstation.setDriver_Name(drive_name.getText().toString());
							unconventionInstation.setTel(drive_tel.getText().toString());
							unconventionInstation.setRemark(remark.getText().toString());
							String vehicle_Property = car_type.getSelectedItem().toString();
							if (vehicle_Property.equals("旅游车")) {
								vehicle_Property="003";
							}else if (vehicle_Property.equals("过路车")) {
								vehicle_Property="004";
							}else if (vehicle_Property.equals("临时车")) {
								vehicle_Property="005";
							}
							unconventionInstation.setVehicle_Property(vehicle_Property);
							unconventionInstation.setNum(accountloadnum.getText().toString());
							unconventionInstation.save();
							//联网上传
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							Gson gson=new Gson();
							String content = gson.toJson(unconventionInstation);
							Log.e("content", content);
							params.put("content",content);
							client.post(Configure.IP+"/TianMen/PDAEntranceAction!unconventionEntrance_PDA.action", params,new AsyncHttpResponseHandler() {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									
									CustomToast.Custom_Toast(InstationUnconventionActivity.this,"上传数据成功!",0,Toast.LENGTH_SHORT);
									UnconventionInstation unconventionInstation = DataSupport
											.findLast(UnconventionInstation.class);
									DataSupport.delete(UnconventionInstation.class, unconventionInstation.getId());
								    ActivityCollector.finishAll();
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									CustomToast.UnComment_Custom_Toast(InstationUnconventionActivity.this,"链接服务器失败\n数据以及保存PDA数据库中!",0,Toast.LENGTH_SHORT);
								}
							});
				}
        	break;
		default:
			break;
		}
	}

}
