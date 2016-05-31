package com.authentication.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.utils.ToastUtil;

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

public class FingerprintActivity extends BaseActivity implements
		OnClickListener {

	
	private String[] m;

	private AsyncFingerprint asyncFingerprint;
	
	private Spinner spinner;
	
	private ArrayAdapter<String> adapter; 
	
	private Button register;

	private Button validate;
	
	private Button register2;

	private Button validate2;
	
	private Button clear;
	
	private Button calibration;

	private Button back;

	private ImageView fingerprintImage;

	private ProgressDialog progressDialog;

	private byte[] model;
	
	private TextView upfail;

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
				FingerprintActivity.this.model = (byte[]) msg.obj;
				if (FingerprintActivity.this.model != null) {
					Log.i("whw", "#################model.length="
							+ FingerprintActivity.this.model.length);
				}
				cancleProgressDialog();
//				ToastUtil.showToast(FingerprintActivity.this, "pageId="+msg.arg1+"  store="+msg.arg2);
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				cancleProgressDialog();
				if(msg.obj != null){
					Integer id = (Integer) msg.obj;
					ToastUtil.showToast(FingerprintActivity.this, getString(R.string.register_success)+"  pageId="+id);
				}else{
					ToastUtil.showToast(FingerprintActivity.this, R.string.register_success);
				}
				
				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(FingerprintActivity.this, R.string.register_fail);
				break;
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				showValidateResult((Boolean) msg.obj);
				break;
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				if(r != -1){
					ToastUtil.showToast(FingerprintActivity.this, getString(R.string.verifying_through)+"  pageId="+r);
				}else{
					showValidateResult(false);
				}
				break;
			case AsyncFingerprint.UP_IMAGE_RESULT:
				cancleProgressDialog();
				ToastUtil.showToast(FingerprintActivity.this, (Integer)msg.obj);
//				failTime++;
//				upfail.setText("�ϴ��ɹ���" + imageNum + "\n" + "�ϴ�ʧ�ܣ�" + failTime+ "\n" + "��������" + missPacket);
				break;
			default:
				break;
			}
		}

	};

	
//	int imageNum = 0;
//	int failTime = 0;
//	int missPacket = 0;
	private void showValidateResult(boolean matchResult) {
		if (matchResult) {
			ToastUtil.showToast(FingerprintActivity.this, R.string.verifying_through);
		} else {
			ToastUtil.showToast(FingerprintActivity.this, R.string.verifying_fail);
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
		setContentView(R.layout.fingeractivity);
		initView();
		initViewListener();
		initData();
	}

	private void initView() {
		spinner = (Spinner) findViewById(R.id.spinner);
		register = (Button) findViewById(R.id.register);
		validate = (Button) findViewById(R.id.validate);
		register2 = (Button) findViewById(R.id.register2);
		validate2 = (Button) findViewById(R.id.validate2);
		clear = (Button) findViewById(R.id.clear_flash);
		calibration = (Button) findViewById(R.id.calibration);
		back = (Button) findViewById(R.id.backRegister);
		upfail = (TextView) findViewById(R.id.upfail);
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
		
        //����ѡ������ArrayAdapter��������  
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);  
          
        //���������б�ķ��  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //��adapter ��ӵ�spinner��  
        spinner.setAdapter(adapter);  
          
        //����¼�Spinner�¼�����    
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 


	}
	
    //ʹ��������ʽ����  
    class SpinnerSelectedListener implements OnItemSelectedListener{  
  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position,  
                long arg3) {  
        	Log.i("whw", "position="+position);
        	switch (position) {
			case 0:
				asyncFingerprint.setFingerprintType(FingerprintAPI.SMALL_FINGERPRINT_SIZE);
				break;
			case 1:
				asyncFingerprint.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);
				break;
			default:
				break;
			}
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }  

	private void initData2() {
		asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),mHandler);
		
		
		asyncFingerprint.setOnEmptyListener(new OnEmptyListener() {
			
			@Override
			public void onEmptySuccess() {
				ToastUtil.showToast(FingerprintActivity.this, R.string.clear_flash_success);
				
			}
			
			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(FingerprintActivity.this, R.string.clear_flash_fail);
				
			}
		});
		
		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(FingerprintActivity.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(FingerprintActivity.this,
						R.string.calibration_fail);

			}
		});

	}
	

	private void initViewListener() {
		register.setOnClickListener(this);
		validate.setOnClickListener(this);
		register2.setOnClickListener(this);
		validate2.setOnClickListener(this);
		calibration.setOnClickListener(this);
		clear.setOnClickListener(this);
		back.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			asyncFingerprint.register();
			break;
		case R.id.validate:
//			if (model != null) {
				asyncFingerprint.validate(model);
//			} else {
//				ToastUtil.showToast(FingerprintActivity.this,
//						R.string.first_register);
//			}
			break;
		case R.id.register2:
			asyncFingerprint.register2();
			break;
		case R.id.validate2:
			asyncFingerprint.validate2();
			break;
		case R.id.calibration:
			Log.i("whw", "calibration start");
			asyncFingerprint.PS_Calibration();
			break;
		case R.id.clear_flash:
			asyncFingerprint.PS_Empty();
			break;
		case R.id.backRegister:
			startActivity(new Intent(FingerprintActivity.this, MyFingerprintActivity1.class));
		/*	finish();*/
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
