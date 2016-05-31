package com.authentication.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android_serialport_api.FingerprintAPI;

import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.entity.AutoCarNum;
import com.authentication.utils.ToastUtil;
import com.example.Utils.ASNItoChart;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.Utils.Space_Character;
import com.example.configure.Configure;
import com.example.outstation.aty.OutStationAty2;
import com.example.outstation.aty.OutStationTakePhotoAty;
import com.example.outstation.entity.ExitCheckInfo;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.zhang.autocomplete.ArrayAdapter;

public class TestFingerprintActivity extends BaseActivity implements
		OnClickListener{
	
	private RelativeLayout  anjian_bg;
	private RelativeLayout  baoban_bg;
	private List<AutoCarNum> autoCarNums;
	private List<String> mCarNums=new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	private String chepaiString=null;
	private AutoCompleteTextView input_carNum;
	private ImageView myfiger_validate;
	private ImageButton nextButton;
	private ImageButton backButton;
	public static final String CHEPAIHAO_KEY="chepaihao_key";
	public static final String HTTPCONNECTION_KEY="httpconnection_key";
	//是否是双驾？？
	private static boolean isDoubleDriver=false;
	//用于判段是不是安过指纹，0代表没有，1代表有
	public static int FirconfigFinger=0;
	public static int SecconfigFinger=0;
	
	//构造函数传值。。。。
	public  static  void setIsDoubleDriver(boolean DoubleDriver){
		isDoubleDriver=DoubleDriver;
	}
	//联网查询得到的驾驶员对象
	private   ExitCheckInfo	myExitCheckInfo;
	
	
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
				break;
			case AsyncFingerprint.SHOW_FINGER_MODEL:
				TestFingerprintActivity.this.model = (byte[]) msg.obj;
				if (TestFingerprintActivity.this.model != null) {
					Log.i("whw", "#################model.length="
							+ TestFingerprintActivity.this.model.length);
				}
				cancleProgressDialog();
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				cancleProgressDialog();
				if(msg.obj != null){
					Integer id = (Integer) msg.obj;
					ToastUtil.showToast(getApplicationContext(), getString(R.string.register_success)+"  pageId="+id);
				}else{
					ToastUtil.showToast(getApplicationContext(), R.string.register_success);
				}
				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(getApplicationContext(), R.string.register_fail);
				break;
				
			//对比指纹成功失败的对比条件跳转。。。。。
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				boolean showValidateResult = showValidateResult((Boolean) msg.obj);
				//判断验证失败与否，没有验证通过直接退出
				if (!showValidateResult) {
					return;
				}
				if (myExitCheckInfo==null) {
					CustomToast.UnComment_Custom_Toast(getApplicationContext(),"驾驶员信息出错，指纹对比不成功！！",0,Toast.LENGTH_SHORT);
					return;
				}
				//验证处理代码
				if (isDoubleDriver) {
					if (FirconfigFinger==1&&SecconfigFinger==1) {
						//双驾驶员都按指纹之后直接跳转
						Intent intent = new Intent(getApplicationContext(),
								OutStationAty2.class);
						intent.putExtra("myExitCheckInfo", myExitCheckInfo);
						intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
						startActivity(intent);
					}else {
						//双驾驶员有一个没有按下指纹
						CustomToast.Custom_Toast(getApplicationContext(),"请让二驾前来按指纹",0,Toast.LENGTH_SHORT);
					}
				}else {
					//单驾直接跳转
					Intent intent = new Intent(getApplicationContext(),
							OutStationAty2.class);
					intent.putExtra("myExitCheckInfo", myExitCheckInfo);
					intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
					startActivity(intent);
				}
				break;
				
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				break;
			case AsyncFingerprint.UP_IMAGE_RESULT:
				cancleProgressDialog();
				ToastUtil.showToast(getApplicationContext(), (Integer)msg.obj);
				break;
			default:
				break;
			}
		}

	};

	
	private boolean showValidateResult(boolean matchResult) {
		if (matchResult) {
			CustomToast.Custom_Toast(getApplicationContext(),"验证通过!!", 0,Toast.LENGTH_SHORT);
			return true;
		} else {
			CustomToast.UnComment_Custom_Toast(getApplicationContext(),"验证失败,请重新按指纹!!", 0,Toast.LENGTH_SHORT);
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint);
		initView();
		initViewListener();
		initData();
		ActivityCollector.addActivity(this);
	}

	private void initView() {
		//静态变量初始化
		isDoubleDriver=false;
		FirconfigFinger=0;
		SecconfigFinger=0;
		
		anjian_bg=(RelativeLayout) findViewById(R.id.anjian_bg);
		baoban_bg=(RelativeLayout) findViewById(R.id.baoban_bg);
		
		myfiger_validate=(ImageView) findViewById(R.id.outstation_figer_validate);
//		scale_finger = (ImageView) findViewById(R.id.scale_finger);
		nextButton = (ImageButton) findViewById(R.id.next_page);
		backButton=(ImageButton) findViewById(R.id.back_page);
		input_carNum=(AutoCompleteTextView) findViewById(R.id.input_carNum);
		if (getIntent()!=null) {
			String RFID= getIntent().getStringExtra(OutSationHXUHFActivity.RFID_KEY);
			if (RFID!=null&&!TextUtils.isEmpty(RFID)) {
				ASNItoChart asnItoChart=new ASNItoChart(RFID);
				chepaiString=Space_Character.trimspace(asnItoChart.getChepai());
				input_carNum.setEnabled(false);
				input_carNum.setText(chepaiString);
			}
		}
		
		//查找车牌号操作
		DataBaseFindData dataBaseFindData=new DataBaseFindData();
	    autoCarNums= dataBaseFindData.getAutoCarNum();
		if (autoCarNums.size()!=0) {
			for (int i = 0; i < autoCarNums.size(); i++) {
				mCarNums.add(autoCarNums.get(i).getCarNum());
			}
		}
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mCarNums);
		input_carNum.setThreshold(1);
		input_carNum.setAdapter(mAdapter);
		
		
		
		spinner = (Spinner) findViewById(R.id.spinner);
		register = (Button) findViewById(R.id.register);
		validate = (Button) findViewById(R.id.validate);
		register2 = (Button) findViewById(R.id.register2);
		validate2 = (Button) findViewById(R.id.validate2);
		clear = (Button) findViewById(R.id.clear_flash);
		calibration = (Button) findViewById(R.id.calibration);
		back = (Button) findViewById(R.id.backRegister);
	}

	private String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	
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
        	position=0;
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
				ToastUtil.showToast(TestFingerprintActivity.this, R.string.clear_flash_success);
				
			}
			
			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(TestFingerprintActivity.this, R.string.clear_flash_fail);
				
			}
		});
		
		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(TestFingerprintActivity.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(TestFingerprintActivity.this,
						R.string.calibration_fail);

			}
		});
	}

	private void initViewListener() {
		myfiger_validate.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		register.setOnClickListener(this);
		validate.setOnClickListener(this);
		register2.setOnClickListener(this);
		validate2.setOnClickListener(this);
		calibration.setOnClickListener(this);
		clear.setOnClickListener(this);
		back.setOnClickListener(this);
		input_carNum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputTools.HideKeyboard(view);
			}
		});
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_page:
			// 当确定了指纹之后会进行跳转去PC端查询到下一个模块；
			if (myExitCheckInfo==null) {
				CustomToast.Custom_Toast(getApplicationContext(),"请先按指纹",0,Toast.LENGTH_SHORT);
				return;
			}else {
				Intent intent = new Intent(getApplicationContext(),
				OutStationTakePhotoAty.class);
		        intent.putExtra("myExitCheckInfo", myExitCheckInfo);
		        intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
		        startActivity(intent);
			}
			break;
		case R.id.back_page:
			TestFingerprintActivity.this.finish();
			break;
			
		case R.id.register:
			asyncFingerprint.register();
			break;
		//验证指纹的点击事件。。。
		case R.id.outstation_figer_validate:
			String carNum = input_carNum.getText().toString();
			HttpConnection2(carNum);
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
			startActivity(new Intent(getApplicationContext(), MyFingerprintActivity1.class));
		/*	finish();*/
			break;
			
		default:
			break;
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
	
	private void HttpConnection2(String carnum) {
		//先从表里面查出来这条信息，没有就联网去找。。。
		List<ExitCheckInfo> mCheckInfos = DataSupport.where(
		"licenceplate = ?", carnum + "").find(
		ExitCheckInfo.class);
		//不为0说明有这一条信息
		if (mCheckInfos.size()!=0) {
			ExitCheckInfo mCheckInfo = mCheckInfos.get(mCheckInfos.size()-1);
			String firfingerprintcode1=mCheckInfo.getFirFingerCode1();
			String firfingerprintcode2=mCheckInfo.getFirFingerCode2();
			String firfingerprintcode3=mCheckInfo.getFirFingerCode3();
			String firfingerprintcode4=mCheckInfo.getFirFingerCode4();
			if (firfingerprintcode1==null) {
				firfingerprintcode1="";
			}
			if (firfingerprintcode2==null) {
				firfingerprintcode2="";
			}
			if (firfingerprintcode3==null) {
				firfingerprintcode3="";
			}
			if (firfingerprintcode4==null) {
				firfingerprintcode4="";
			}
			
			List<String> firStrings=new ArrayList<>();
			
			firStrings.add(firfingerprintcode1);
			firStrings.add(firfingerprintcode2);
			firStrings.add(firfingerprintcode3);
			firStrings.add(firfingerprintcode4);
			
			String secfingerprintcode1=mCheckInfo.getSecFingerCode1();
			String secfingerprintcode2=mCheckInfo.getSecFingerCode2();
			String secfingerprintcode3=mCheckInfo.getSecFingerCode3();
			String secfingerprintcode4=mCheckInfo.getSecFingerCode4();
			
			if (secfingerprintcode1==null) {
				secfingerprintcode1="";
			}
			if (secfingerprintcode2==null) {
				secfingerprintcode2="";
			}
			if (secfingerprintcode3==null) {
				secfingerprintcode3="";
			}
			if (secfingerprintcode4==null) {
				secfingerprintcode4="";
			}
			
			List<String> secStrings=new ArrayList<>();
			secStrings.add(secfingerprintcode1);
			secStrings.add(secfingerprintcode2);
			secStrings.add(secfingerprintcode3);
			secStrings.add(secfingerprintcode4);
			
			//所有指纹都为空
//			if (firfingerprintcode1==null&&firfingerprintcode2==null&&firfingerprintcode3==null&&firfingerprintcode4==null
//					&&secfingerprintcode1==null&&secfingerprintcode2==null&&secfingerprintcode3==null&&secfingerprintcode4==null) {
//				CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"查不到驾驶员调度信息！",0,Toast.LENGTH_SHORT);
//				return;
//			}
			
			
//			//所有指纹长度都小于100，编码错误
//			if (firfingerprintcode1.length()<100&&firfingerprintcode2.length()<100&&firfingerprintcode3.length()<100&&firfingerprintcode4.length()<100
//					&&secfingerprintcode1.length()<100&&secfingerprintcode2.length()<100&&secfingerprintcode3.length()<100&&secfingerprintcode4.length()<100) {
//				CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"服务器指纹编码出错，请检查录入指纹情况",0,Toast.LENGTH_SHORT);
//				return;
//			}
			
			myExitCheckInfo=mCheckInfo;
			anjian_bg.setVisibility(View.VISIBLE);
			baoban_bg.setVisibility(View.VISIBLE);
			
//			Intent intent = new Intent(TestFingerprintActivity.this,
//					houjiaweiOutStationAty3.class);
//			intent.putExtra("myExitCheckInfo", myExitCheckInfo);
//			intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
//			startActivity(intent);
		}else {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("licencePlate", carnum);
		client.post(Configure.IP+"/TianMen/PushExitMessageAction!findexitmessage.action",params, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				//查询到数据然后指纹比对代码
//				Log.e("TestFingerPrintActivirty-arg2", arg2);
				//小于600指纹不存在
				if (arg2.length()<600) {
					CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"查不到相关驾驶车辆信息！",0,Toast.LENGTH_SHORT);
					return;
				}
				
				String cname=null;
				try {
					JSONObject object=new JSONObject(arg2);
					String flag=object.getString("flag");
					if (!flag.equals("true")) {
						CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"查不到相关驾驶车俩信息！",0,Toast.LENGTH_SHORT);
						return;
					}
					cname=object.getString("list");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gson gson=new Gson();
				myExitCheckInfo=gson.fromJson(cname, ExitCheckInfo.class);
				
				String firfingerprintcode1=myExitCheckInfo.getFirFingerCode1();
				String firfingerprintcode2=myExitCheckInfo.getFirFingerCode2();
				String firfingerprintcode3=myExitCheckInfo.getFirFingerCode3();
				String firfingerprintcode4=myExitCheckInfo.getFirFingerCode4();
				
				if (firfingerprintcode1==null) {
					firfingerprintcode1="";
				}
				if (firfingerprintcode2==null) {
					firfingerprintcode2="";
				}
				if (firfingerprintcode3==null) {
					firfingerprintcode3="";
				}
				if (firfingerprintcode4==null) {
					firfingerprintcode4="";
				}
				
				List<String> firStrings=new ArrayList<>();
				firStrings.add(firfingerprintcode1);
				firStrings.add(firfingerprintcode2);
				firStrings.add(firfingerprintcode3);
				firStrings.add(firfingerprintcode4);
				
				String secfingerprintcode1=myExitCheckInfo.getSecFingerCode1();
				String secfingerprintcode2=myExitCheckInfo.getSecFingerCode2();
				String secfingerprintcode3=myExitCheckInfo.getSecFingerCode3();
				String secfingerprintcode4=myExitCheckInfo.getSecFingerCode4();
				
				if (secfingerprintcode1==null) {
					secfingerprintcode1="";
				}
				if (secfingerprintcode2==null) {
					secfingerprintcode2="";
				}
				if (secfingerprintcode3==null) {
					secfingerprintcode3="";
				}
				if (secfingerprintcode4==null) {
					secfingerprintcode4="";
				}
				
				List<String> secStrings=new ArrayList<>();
				secStrings.add(secfingerprintcode1);
				secStrings.add(secfingerprintcode2);
				secStrings.add(secfingerprintcode3);
				secStrings.add(secfingerprintcode4);
				
				//所有指纹都为空
//				if (firfingerprintcode1==null&&firfingerprintcode2==null&&firfingerprintcode3==null&&firfingerprintcode4==null
//						&&secfingerprintcode1==null&&secfingerprintcode2==null&&secfingerprintcode3==null&&secfingerprintcode4==null) {
//					CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"查不到驾驶员调度信息！",0,Toast.LENGTH_SHORT);
//					return;
//				}
				
//				//所有指纹长度都小于100，编码错误
//				if (firfingerprintcode1.length()<100&&firfingerprintcode2.length()<100&&firfingerprintcode3.length()<100&&firfingerprintcode4.length()<100
//						&&secfingerprintcode1.length()<100&&secfingerprintcode2.length()<100&&secfingerprintcode3.length()<100&&secfingerprintcode4.length()<100) {
//					CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"服务器指纹编码出错，请检查录入指纹情况",0,Toast.LENGTH_SHORT);
//					return;
//				}
				
				anjian_bg.setVisibility(View.VISIBLE);
				baoban_bg.setVisibility(View.VISIBLE);
				
//				Intent intent = new Intent(TestFingerprintActivity.this,
//						houjiaweiOutStationAty3.class);
//				intent.putExtra("myExitCheckInfo", myExitCheckInfo);
//				intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
//				startActivity(intent);
				
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(TestFingerprintActivity.this,"网络异常请稍后！",0,Toast.LENGTH_SHORT);
				return;
			}
		});
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
		System.gc();
	}
	
	@Override
	protected void onDestroy() {
		cancleProgressDialog();
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		//销毁时候也初始化下
		asyncFingerprint.Recycle();
		asyncFingerprint=null;
		
		isDoubleDriver=false;
		FirconfigFinger=0;
		SecconfigFinger=0;
		
		mCarNums=null;
		myExitCheckInfo=null;
		
		mAdapter=null;
		adapter=null;	
		autoCarNums=null;
		input_carNum.setAdapter(null);
		input_carNum=null;
		System.gc();
	}



}
