package com.authentication.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.entity.AutoCarNum;
import com.authentication.utils.ToastUtil;
import com.example.Utils.ASNItoChart;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.T;
import com.example.configure.Configure;
import com.example.outstation.View.CustomAnim;
import com.example.outstation.aty.OutStationAty2;
import com.example.outstation.aty.OutStationAty_Finger;
import com.example.outstation.finishAty.ActivityCollector;
import com.example.parameter.WriteDataActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android_serialport_api.FingerprintAPI;

public class RegisterFingerprintActivity extends BaseActivity implements
		OnClickListener {
	
	private List<AutoCarNum> autoCarNums;
	private List<String> mCarNums=new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	private String chepaiString=null;
	private AutoCompleteTextView input_carNum;
    private int mPageID=-1;
	private ImageView myfiger_validate;
	private ImageView scale_finger;
	private boolean config_finger = false;// 用于判断是否确认了指纹，没有确认为false，确认了之后设置为true
	private CustomAnim customAnim = new CustomAnim();
	private String SecFingerprintCode;
	private String FirFingerprintCode;
	private static String mBaseFinger=null;
	
	public static final String CHEPAIHAO_KEY="chepaihao_key";

	
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
	
	
	private Button back_page;

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
//			scale_finger.startAnimation(customAnim);
			switch (msg.what) {
			case AsyncFingerprint.SHOW_PROGRESSDIALOG:
				cancleProgressDialog();
				showProgressDialog((Integer) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_IMAGE:
//				imageNum++;
//				upfail.setText("�ϴ��ɹ���" + imageNum + "\n" + "�ϴ�ʧ�ܣ�" + failTime+ "\n" + "��������" + missPacket);
//				showFingerImage(msg.arg1, (byte[]) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_MODEL:
				RegisterFingerprintActivity.this.model = (byte[]) msg.obj;
				if (RegisterFingerprintActivity.this.model != null) {
					Log.i("whw", "#################model.length="
							+ RegisterFingerprintActivity.this.model.length);
				}
				cancleProgressDialog();
//				ToastUtil.showToast(FingerprintActivity.this, "pageId="+msg.arg1+"  store="+msg.arg2);
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				cancleProgressDialog();
				if(msg.obj != null){
					Integer id = (Integer) msg.obj;
					ToastUtil.showToast(RegisterFingerprintActivity.this, getString(R.string.register_success)+"指纹编号:"+id);
					Intent intent=new Intent(RegisterFingerprintActivity.this,WriteDataActivity.class);
					intent.putExtra("ID", id);
					startActivity(intent);
				}else{
					if(mBaseFinger!=null){
					//注册指纹成功	
					ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.register_success);
					Intent intent=new Intent(RegisterFingerprintActivity.this,WriteDataActivity.class);
					intent.putExtra("ID", mBaseFinger);
					startActivity(intent);
					}else {
					  ToastUtil.showToast(RegisterFingerprintActivity.this, "注册指纹失败!");
					}
				}
				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.register_fail);
				break;
				
				
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				showValidateResult((Boolean) msg.obj);
				break;
				
				
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				if(r != -1){
					ToastUtil.showToast(RegisterFingerprintActivity.this, getString(R.string.verifying_through));
					mPageID=r;
					if (config_finger) {
						if (FirFingerprintCode.equals(mPageID+"")
								||SecFingerprintCode.equals(mPageID+"")) {
							Intent intent = new Intent(RegisterFingerprintActivity.this,
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
				ToastUtil.showToast(RegisterFingerprintActivity.this, (Integer)msg.obj);
				break;
			default:
				break;
			}
		}

	};

	
	private void showValidateResult(boolean matchResult) {
		if (matchResult) {
			ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.verifying_through);
		} else {
			ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.verifying_fail);
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
		setContentView(R.layout.register_finger);
		initView();
		initViewListener();
		initData();
		ActivityCollector.addActivity(this);
		
	}

	private void initView() {
		back_page=(Button) findViewById(R.id.back_page);
		myfiger_validate=(ImageView) findViewById(R.id.outstation_figer_validate);
		scale_finger = (ImageView) findViewById(R.id.scale_finger);
		input_carNum=(AutoCompleteTextView) findViewById(R.id.input_carNum);
		
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
				ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.clear_flash_success);
				
			}
			
			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(RegisterFingerprintActivity.this, R.string.clear_flash_fail);
				
			}
		});
		
		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(RegisterFingerprintActivity.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(RegisterFingerprintActivity.this,
						R.string.calibration_fail);

			}
		});

	}
	

	private void initViewListener() {
		myfiger_validate.setOnClickListener(this);
		register.setOnClickListener(this);
		validate.setOnClickListener(this);
		register2.setOnClickListener(this);
		validate2.setOnClickListener(this);
		calibration.setOnClickListener(this);
		clear.setOnClickListener(this);
		back.setOnClickListener(this);
		back_page.setOnClickListener(this);
	}
	
	
	public static void getFinger64Base(String Basefinger) {
		mBaseFinger=Basefinger;
	}


	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.outstation_figer_validate:
//			asyncFingerprint.register2();
//			break;
		case R.id.outstation_figer_validate:
			asyncFingerprint.register();
			break;
		case R.id.validate:
//			if (model != null) {
				asyncFingerprint.validate(model);
//			} else {
//				ToastUtil.showToast(RegisterFingerprintActivity.this,
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
			startActivity(new Intent(RegisterFingerprintActivity.this, MyFingerprintActivity1.class));
		/*	finish();*/
			break;
		case R.id.back_page:
			RegisterFingerprintActivity.this.finish();
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
									T.showShort(RegisterFingerprintActivity.this, "请二驾来按指纹");
									config_finger=true;
									return;
								}
							}
							if((TextUtils.isEmpty(FirDriver_ID)&&!TextUtils.isEmpty(SecDriver_ID))
									||(!TextUtils.isEmpty(FirDriver_ID)&&TextUtils.isEmpty(SecDriver_ID))) {
								Intent intent = new Intent(RegisterFingerprintActivity.this,
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
