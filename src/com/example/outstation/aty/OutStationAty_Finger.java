package com.example.outstation.aty;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.authentication.activity.BaseActivity;
import com.authentication.activity.InSationHXUHFActivity;
import com.authentication.activity.OutSationHXUHFActivity;
import com.authentication.activity.R;
import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.entity.AutoCarNum;
import com.authentication.utils.ToastUtil;
import com.example.Utils.ASNItoChart;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.T;
import com.example.anjian.aty.TongbuAty;
import com.example.configure.Configure;
import com.example.instation.LoginActivity;
import com.example.outstation.View.CustomAnim;
import com.example.outstation.entity.Driver;
import com.example.outstation.entity.MyExitCheckInfo;
import com.example.outstation.finishAty.ActivityCollector;
import com.example.parameter.WriteDataActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class OutStationAty_Finger extends BaseActivity implements
		OnClickListener {
	private List<AutoCarNum> autoCarNums;
	private List<String> mCarNums=new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	private String chepaiString=null;
	private AutoCompleteTextView input_carNum;
    private int mPageID=-1;
	private ImageView myfiger_validate;
	private ImageView scale_finger;
	private ImageButton nextButton;
	private ImageButton backButton;
	private boolean config_finger = false;// 用于判断是否确认了指纹，没有确认为false，确认了之后设置为true
	private CustomAnim customAnim = new CustomAnim();
	private String SecFingerprintCode;
	private String FirFingerprintCode;
	
	public static final String CHEPAIHAO_KEY="chepaihao_key";
	
	private String[] m;

	private AsyncFingerprint asyncFingerprint;

	private ProgressDialog progressDialog;

	private byte[] model;
	

	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + "fingerprint_image";

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			scale_finger.startAnimation(customAnim);
			switch (msg.what) {
			case AsyncFingerprint.SHOW_PROGRESSDIALOG:
				cancleProgressDialog();
				showProgressDialog((Integer) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_IMAGE:
//				imageNum++;
//				upfail.setText("�ϴ��ɹ���" + imageNum + "\n" + "�ϴ�ʧ�ܣ�" + failTime+ "\n" + "��������" + missPacket);
				showFingerImage(msg.arg1, (byte[]) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_MODEL:
				OutStationAty_Finger.this.model = (byte[]) msg.obj;
				if (OutStationAty_Finger.this.model != null) {
					Log.i("whw", "#################model.length="
							+ OutStationAty_Finger.this.model.length);
				}
				cancleProgressDialog();
//				ToastUtil.showToast(FingerprintActivity.this, "pageId="+msg.arg1+"  store="+msg.arg2);
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				cancleProgressDialog();
				if(msg.obj != null){
					//注册成功跳转页面填写个人信息
					Integer id = (Integer) msg.obj;
					ToastUtil.showToast(OutStationAty_Finger.this, getString(R.string.register_success)+"pageId="+id);
					Intent intent=new Intent(OutStationAty_Finger.this,WriteDataActivity.class);
					intent.putExtra("ID", id);
					startActivity(intent);
				}else{
					ToastUtil.showToast(OutStationAty_Finger.this, R.string.register_success);
				}
				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(OutStationAty_Finger.this, R.string.register_fail);
				break;
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				showValidateResult((Boolean) msg.obj);
				break;
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				if(r != -1){
					ToastUtil.showToast(OutStationAty_Finger.this, getString(R.string.verifying_through));
					mPageID=r;
					if (config_finger) {
						if (FirFingerprintCode.equals(mPageID+"")
								||SecFingerprintCode.equals(mPageID+"")) {
							Intent intent = new Intent(OutStationAty_Finger.this,
									OutStationAty2.class);
							intent.putExtra("finger", ""+mPageID);
							intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
							startActivity(intent);
						}
					}
					HttpConnection();
				}else{
					showValidateResult(false);
				}
				break;
			case AsyncFingerprint.UP_IMAGE_RESULT:
				cancleProgressDialog();
				ToastUtil.showToast(OutStationAty_Finger.this, (Integer)msg.obj);
//				failTime++;
				break;
			default:
				break;
			}
		}

	};

	
	private void showValidateResult(boolean matchResult) {
		if (matchResult) {
			ToastUtil.showToast(OutStationAty_Finger.this, R.string.verifying_through);
		} else {
			ToastUtil.showToast(OutStationAty_Finger.this, R.string.verifying_fail);
		}
	}

	private void showFingerImage(int fingerType, byte[] data) {
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		// saveImage(data);
		writeToFile(data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_outstation_aty);
		customAnim.setDuration(6000);
		initView();
		initViewListener();
		initData();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Message msg = Message.obtain();
					msg.obj=1;
					mHandler.sendMessage(msg);
					SystemClock.sleep(6000);
				}
			}
		}).start();
		ActivityCollector.addActivity(this);
	}



	private void initView() {
		myfiger_validate=(ImageView) findViewById(R.id.outstation_figer_validate);
		scale_finger = (ImageView) findViewById(R.id.scale_finger);
		nextButton = (ImageButton) findViewById(R.id.next_page);
		backButton=(ImageButton) findViewById(R.id.back_page);
		input_carNum=(AutoCompleteTextView) findViewById(R.id.input_carNum);
		if (getIntent()!=null) {
			String RFID= getIntent().getStringExtra(OutSationHXUHFActivity.RFID_KEY);
			if (RFID!=null) {
				ASNItoChart asnItoChart=new ASNItoChart(RFID);
				chepaiString=asnItoChart.getChepai();
				input_carNum.setEnabled(false);
			}
		}
		input_carNum.setText(chepaiString);
		//查找车牌号操作
		DataBaseFindData dataBaseFindData=new DataBaseFindData();
	    autoCarNums= dataBaseFindData.getAutoCarNum();
		if (autoCarNums.size()!=0) {
			for (int i = 0; i < autoCarNums.size(); i++) {
				mCarNums.add(autoCarNums.get(i).getCarNum());
			}
		}
		mAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mCarNums);
		input_carNum.setAdapter(mAdapter);
	}

	private String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	private void writeToFile(byte[] data) {
		String dir = rootPath + File.separator + "fingerprint_image";
		File dirPath = new File(dir);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}

		String filePath = dir + "/" + System.currentTimeMillis() + ".bmp";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initData() {
		m  =  this.getResources().getStringArray(R.array.fingerprint_size);
	}
	
	private void initData2() {
		asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),mHandler);
		
		
		asyncFingerprint.setOnEmptyListener(new OnEmptyListener() {
			
			@Override
			public void onEmptySuccess() {
				ToastUtil.showToast(OutStationAty_Finger.this, R.string.clear_flash_success);
			}
			
			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(OutStationAty_Finger.this, R.string.clear_flash_fail);
			}
		});
		
		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(OutStationAty_Finger.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(OutStationAty_Finger.this,
						R.string.calibration_fail);

			}
		});

	}
	

	private void initViewListener() {
		myfiger_validate.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.outstation_figer_validate:
			asyncFingerprint.validate2();
			break;
		case R.id.next_page:
			// 当确定了指纹之后会进行跳转去PC端查询到下一个模块；
			if (mPageID==-1) {
				ToastUtil.showToast(getApplicationContext(), "请先按指纹");
				return;
			}
			
			break;
		case R.id.back_page:
			OutStationAty_Finger.this.finish();
			break;
		default:
			break;
		}
	}

	private void saveImage(byte[] data) {
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdir();
		}

		File file = new File(path + File.separator + System.currentTimeMillis()
				+ ".bmp");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void showProgressDialog(String message) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void showProgressDialog(int resId) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getString(resId));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					asyncFingerprint.setStop(true);
				}
				return false;
			}
		});
		progressDialog.show();
	}

	private void cancleProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		cancleProgressDialog();
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("whw", "onRestart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("whw", "onStop");
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData2();
		Log.i("whw", "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		cancleProgressDialog();
		asyncFingerprint.setStop(true);
		Log.i("whw", "onPause");
	}
	
	private void HttpConnection() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("finger", mPageID);
		client.post(Configure.IP+"/Upload_8-1/MyServelt", params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						if (TextUtils.isEmpty(arg2)) {
							Toast.makeText(getApplicationContext(), "根据此指纹查不到相关驾驶员信息！", Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							JSONObject object = new JSONObject(arg2);
							String FirDriver_ID = object
									.getString("FirDriver_ID");
							String SecDriver_ID = object
									.getString("SecDriver_ID");
							FirFingerprintCode = object
									.getString("FirFingerprintCode");
							SecFingerprintCode = object
									.getString("SecFingerprintCode");
							if (!TextUtils.isEmpty(FirDriver_ID)&&!TextUtils.isEmpty(SecDriver_ID)) {
								if (FirFingerprintCode.equals(mPageID+"")
										 ||SecFingerprintCode.equals(mPageID+"")) {
									T.showShort(OutStationAty_Finger.this, "请二驾来按指纹");
									config_finger=true;
									return;
								}
							}
							if((TextUtils.isEmpty(FirDriver_ID)&&!TextUtils.isEmpty(SecDriver_ID))
									||(!TextUtils.isEmpty(FirDriver_ID)&&TextUtils.isEmpty(SecDriver_ID))) {
								Intent intent = new Intent(OutStationAty_Finger.this,
										OutStationAty2.class);
								intent.putExtra("finger", ""+mPageID);
								intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
								startActivity(intent);
								return;
							}
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"服务器异常，请稍后再试", 1).show();
							e.printStackTrace();
						} 
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Toast.makeText(getApplicationContext(), "网络异常，请稍后再试", 1)
								.show();
					}
				});
	         }
}
