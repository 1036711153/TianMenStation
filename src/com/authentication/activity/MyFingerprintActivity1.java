package com.authentication.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.utils.ToastUtil;
import com.example.anjian.aty.Scan;
import com.example.anjian.aty.TongbuAty;
import com.example.instation.LoginActivity;
import com.example.outstation.aty.ScanAty;
import com.example.outstation.finishAty.ActivityCollector;
import com.example.parameter.WriteDataActivity;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android_serialport_api.FingerprintAPI;

public class MyFingerprintActivity1 extends BaseActivity implements
		OnClickListener {

	
	private String[] m;

	private AsyncFingerprint asyncFingerprint;

	
	private Button register;

	private Button validate;
	
	private Button clear;
	
	private Button enter_mysystem;
	
	private Button tongbu;
	

	private ImageView fingerprintImage;

	private ProgressDialog progressDialog;

	private byte[] model;
	

	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + "fingerprint_image";

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
				MyFingerprintActivity1.this.model = (byte[]) msg.obj;
				if (MyFingerprintActivity1.this.model != null) {
					Log.i("whw", "#################model.length="
							+ MyFingerprintActivity1.this.model.length);
				}
				cancleProgressDialog();
//				ToastUtil.showToast(FingerprintActivity.this, "pageId="+msg.arg1+"  store="+msg.arg2);
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				cancleProgressDialog();
				if(msg.obj != null){
					//注册成功跳转页面填写个人信息
					Integer id = (Integer) msg.obj;
					ToastUtil.showToast(MyFingerprintActivity1.this, getString(R.string.register_success)+"pageId="+id);
					Intent intent=new Intent(MyFingerprintActivity1.this,WriteDataActivity.class);
					intent.putExtra("ID", id);
					startActivity(intent);
					
				}else{
					ToastUtil.showToast(MyFingerprintActivity1.this, R.string.register_success);
				}
				
				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(MyFingerprintActivity1.this, R.string.register_fail);
				break;
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				showValidateResult((Boolean) msg.obj);
				break;
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				if(r != -1){
					ToastUtil.showToast(MyFingerprintActivity1.this, getString(R.string.verifying_through)+"  pageId="+r);
				}else{
					showValidateResult(false);
				}
				break;
			case AsyncFingerprint.UP_IMAGE_RESULT:
				cancleProgressDialog();
				ToastUtil.showToast(MyFingerprintActivity1.this, (Integer)msg.obj);
//				failTime++;
				break;
			default:
				break;
			}
		}

	};

	
	private void showValidateResult(boolean matchResult) {
		if (matchResult) {
			ToastUtil.showToast(MyFingerprintActivity1.this, R.string.verifying_through);
		} else {
			ToastUtil.showToast(MyFingerprintActivity1.this, R.string.verifying_fail);
		}
	}

	private void showFingerImage(int fingerType, byte[] data) {
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		// saveImage(data);
		fingerprintImage.setBackgroundDrawable(new BitmapDrawable(image));
		writeToFile(data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfingerprint1);
		initView();
		initViewListener();
		initData();
		
	}

	private void initView() {
		tongbu=(Button) findViewById(R.id.tongbu);
		enter_mysystem=(Button) findViewById(R.id.enter_mysystem);
		register = (Button) findViewById(R.id.myfinger_register);
		validate = (Button) findViewById(R.id.myfiger_validate);
		clear = (Button) findViewById(R.id.clear_flash);
		fingerprintImage = (ImageView) findViewById(R.id.fingerprintImage);
		
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
				ToastUtil.showToast(MyFingerprintActivity1.this, R.string.clear_flash_success);
			}
			
			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(MyFingerprintActivity1.this, R.string.clear_flash_fail);
			}
		});
		
		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(MyFingerprintActivity1.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(MyFingerprintActivity1.this,
						R.string.calibration_fail);

			}
		});

	}
	

	private void initViewListener() {
		tongbu.setOnClickListener(this);
		enter_mysystem.setOnClickListener(this);
		register.setOnClickListener(this);
		validate.setOnClickListener(this);
		clear.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myfinger_register:
			asyncFingerprint.register2();
			break;
		case R.id.myfiger_validate:
			asyncFingerprint.validate2();
			break;
		case R.id.clear_flash:
			asyncFingerprint.PS_Empty();
			break;
		case R.id.enter_mysystem:
			startActivity(new Intent(MyFingerprintActivity1.this,LoginActivity.class));
			break;
		case R.id.tongbu:
			startActivity(new Intent(MyFingerprintActivity1.this,InSationHXUHFActivity.class));
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



}
