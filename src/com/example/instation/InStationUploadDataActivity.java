package com.example.instation;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.authentication.entity.AutoCarNum;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.configure.Configure;
import com.example.outstation.aty.OutStationUploadDataAty;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.autocomplete.ArrayAdapter;

public class InStationUploadDataActivity extends Activity {
	private List<AutoCarNum> autoCarNums;
	private List<String> mCarNums = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private Button openButton;
	// private Button closeButton;
	private ImageButton backButton;
	private AutoCompleteTextView licence;
	private EditText adult_num;
	private EditText child_num;
	private String upload_photo1_path;
	private String upload_photo2_path;
	private InstationJsonUser jsonuser;
	private TextView user_name;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int a = (int) msg.obj;
			if (a == 1) {
				CustomToast.UnComment_Custom_Toast(InStationUploadDataActivity.this,
						"连接开关门失败！！", 0, Toast.LENGTH_SHORT);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instationact3);
		SQLiteDatabase db = Connector.getDatabase();
		backButton = (ImageButton) findViewById(R.id.back_page);
		openButton = (Button) findViewById(R.id.open);
		// closeButton = (Button) findViewById(R.id.close);
		licence = (AutoCompleteTextView) findViewById(R.id.editText1);
		adult_num = (EditText) findViewById(R.id.editText3);
		child_num = (EditText) findViewById(R.id.editText2);
		user_name = (TextView) findViewById(R.id.text_username);
		upload_photo1_path = (String) getIntent().getSerializableExtra(
				"upload_photo1_path");
		upload_photo2_path = (String) getIntent().getSerializableExtra(
				"upload_photo2_path");
		jsonuser = DataSupport.findLast(InstationJsonUser.class);
		user_name.setText(jsonuser.getName());
		if (getIntent() != null) {
			String chepaihao = getIntent().getStringExtra(
					InStationTakePhotoActivity.CHEPAIHAO_KEY);
			if (chepaihao != null || !TextUtils.isEmpty(chepaihao)) {
				licence.setText(chepaihao);
				licence.setEnabled(false);
			}
		}
		// 查找车牌号操作
		DataBaseFindData dataBaseFindData = new DataBaseFindData();
		autoCarNums = dataBaseFindData.getAutoCarNum();
		if (autoCarNums.size() != 0) {
			for (int i = 0; i < autoCarNums.size(); i++) {
				mCarNums.add(autoCarNums.get(i).getCarNum());
			}
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mCarNums);
		licence.setThreshold(1);
		licence.setAdapter(adapter);
		setListener();
		ActivityCollector.addActivity(this);
	}

	private void setListener() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InStationUploadDataActivity.this.finish();

			}
		});
		openButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(licence.getText().toString())) {
					CustomToast.Custom_Toast(InStationUploadDataActivity.this,
							"请填写车牌照号码", 0, Toast.LENGTH_SHORT);
					return;
				} else if (TextUtils.isEmpty(trimspace(adult_num.getText()
						.toString()))) {
					CustomToast.Custom_Toast(InStationUploadDataActivity.this,
							"请填写成人人数", 0, Toast.LENGTH_SHORT);
					return;
				} else if (TextUtils.isEmpty(trimspace(child_num.getText()
						.toString()))) {
					CustomToast.Custom_Toast(InStationUploadDataActivity.this,
							"请填写儿童人数", 0, Toast.LENGTH_SHORT);
					return;
				} else {
					// 设置你的操作事项
					ControllerOpenCloseDoor closeDoor = new ControllerOpenCloseDoor(
							InStationUploadDataActivity.this,
							Configure.CCONTROLLER_DOOR_IP, mHandler);
					closeDoor.OpenDoor();

					openButton.setEnabled(false);
					openButton
							.setBackgroundResource(R.drawable.opendoorbuttong1);

					EntranceInfo entranceInfo = new EntranceInfo();
					entranceInfo.setAdult_num(Integer
							.valueOf(trimspace(adult_num.getText().toString())));
					entranceInfo.setChild_num(Integer
							.valueOf(trimspace(child_num.getText().toString())));
					entranceInfo.setUpload_photo1_path(upload_photo1_path);
					entranceInfo.setUpload_photo2_path(upload_photo2_path);
					entranceInfo.setLicnence(licence.getText().toString());
					entranceInfo.setWatchID(jsonuser.getWatchID());
					entranceInfo.setStationcode(Configure.Stationcode);
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 设置日期格式
					entranceInfo.setTime(df.format(new Date()));
					entranceInfo.save();
					Gson gson = new Gson();
					List<EntranceInfo> mEntranceInfos = new ArrayList<>();
					mEntranceInfos.add(entranceInfo);
					final String gsonString = gson.toJson(mEntranceInfos);

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
					client1.post(Configure.IP
							+ "/TianMen/EntranceUpFileAction.action", params1,
							new FileAsyncHttpResponseHandler(
									InStationUploadDataActivity.this) {
								@Override
								public void onSuccess(int arg0, Header[] arg1,
										File arg2) {
									AsyncHttpClient client = new AsyncHttpClient();
									RequestParams params = new RequestParams();
									params.put("content", gsonString);
									client.post(
											Configure.IP
													+ "/TianMen/PDAEntranceAction!defaultMethod.action",
											params,
											new AsyncHttpResponseHandler() {
												@Override
												public void onSuccess(int arg0,
														Header[] arg1,
														byte[] arg2) {
													CustomToast
															.Custom_Toast(
																	InStationUploadDataActivity.this,
																	"上传数据成功！",
																	0,
																	Toast.LENGTH_SHORT);
													EntranceInfo entranceInfo = DataSupport
															.findLast(EntranceInfo.class);
													DataSupport.delete(
															EntranceInfo.class,
															entranceInfo
																	.getId());

												}

												@Override
												public void onFailure(int arg0,
														Header[] arg1,
														byte[] arg2,
														Throwable arg3) {
													CustomToast
															.UnComment_Custom_Toast(
																	InStationUploadDataActivity.this,
																	"链接服务器失败\n数据以及保存PDA数据库中！！",
																	0,
																	Toast.LENGTH_SHORT);
												}
											});
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										Throwable arg2, File arg3) {
									CustomToast.UnComment_Custom_Toast(
											InStationUploadDataActivity.this,
											"链接服务器失败\n数据以及保存PDA数据库中！！", 0,
											Toast.LENGTH_SHORT);
								}
							});
				}
				// 上传完直接退出
				ActivityCollector.finishAll();
			}
		});
	}

	private String trimspace(String s) {
		String str = s.replaceAll(" ", "");
		return str;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		adapter = null;
		autoCarNums = null;
		licence.setAdapter(null);
		licence = null;
	}
}
