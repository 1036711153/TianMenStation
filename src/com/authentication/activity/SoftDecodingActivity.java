package com.authentication.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.provider.Settings;

/**
 * 
 * CFon640软解码
 * 
 * @author Administrator
 *
 */
public class SoftDecodingActivity extends Activity implements OnClickListener {

	public static final String SCANNER_POWER_ON = "SCANNER_POWER_ON";
	public static final String SCANNER_OUTPUT_MODE = "SCANNER_OUTPUT_MODE";
	public static final String SCANNER_TERMINAL_CHAR = "SCANNER_TERMINAL_CHAR";
	public static final String SCANNER_PREFIX = "SCANNER_PREFIX";
	public static final String SCANNER_SUFFIX = "SCANNER_SUFFIX";
	public static final String SCANNER_VOLUME = "SCANNER_VOLUME";
	public static final String SCANNER_TONE = "SCANNER_TONE";
	public static final String SCANNER_PLAYTONE_MODE = "SCANNER_PLAYTONE_MODE";
	private static WakeLock wakeLock = null; 
	private ToggleButton status;
	private ToggleButton scannerLoop;
	private Button start;
	private Button end;
	private Button clear;
	private EditText value;
	private Button settings;

	private TextView totalView;
	private TextView successView;

	private Spinner outputMode;
	private Spinner terminalChar;
	private EditText prefix;
	private EditText suffix;
	private Spinner volume;
	private Spinner playoneMode;

	private static final int ENABLE = 1;
	private static final int DISENABLE = 0;

	private Integer[] volumes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private String[] outputModes;
	private String[] terminalChars;
	private String[] playoneModes;

	int mPowerOnOff;
	int mOutputMode;
	int mTerminalChar;
	String mPrefix;
	String mSuffix;
	int mVolume;
	int mPlayoneMode;
	protected static ExecutorService pool;
	private boolean isLoop;
	//MyThread mt  = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_soft_decoding);
		initView();
		initData();
	}

	private void initView() {
		status = (ToggleButton) findViewById(R.id.scanner_status);
		status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Intent intent = new Intent(
							"com.android.server.scannerservice.onoff");
					intent.putExtra("scanneronoff", ENABLE);
					sendBroadcast(intent);
					Log.i("whw", "ENABLE");
				} else {
					Intent intent = new Intent(
							"com.android.server.scannerservice.onoff");
					intent.putExtra("scanneronoff", DISENABLE);
					sendBroadcast(intent);
					Log.i("whw", "DISENABLE");
				}

			}
		});
		scannerLoop = (ToggleButton) findViewById(R.id.scanner_loop);
		scannerLoop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isLoop = true;
				} else {
					isLoop = false;
				}
			}
		});
		start = (Button) findViewById(R.id.start_scan);
		end = (Button) findViewById(R.id.end_scan);
		clear = (Button) findViewById(R.id.clear_scanner_time);
		value = (EditText) findViewById(R.id.scan_value);
		settings = (Button) findViewById(R.id.scan_settings);

		totalView = (TextView) findViewById(R.id.total);
		successView = (TextView) findViewById(R.id.success_time);

		outputMode = (Spinner) findViewById(R.id.output_mode);
		terminalChar = (Spinner) findViewById(R.id.terminal_char);
		prefix = (EditText) findViewById(R.id.prefix);
		suffix = (EditText) findViewById(R.id.suffix);
		volume = (Spinner) findViewById(R.id.volume_spinner);
		playoneMode = (Spinner) findViewById(R.id.playone_mode_spinner);

		start.setOnClickListener(this);
		end.setOnClickListener(this);
		clear.setOnClickListener(this);
		settings.setOnClickListener(this);
	}

	private int total = 0;
	private int resultTime = 0;

	public class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			total++;
			totalView.setText("发起扫描次数: " + total);
			Intent startIntent = new Intent(
					"android.intent.action.SCANNER_BUTTON_DOWN", null);
			sendOrderedBroadcast(startIntent, null);
			
		}
	}
//	public class MyThread extends Thread {
//		private MyHandler lHandler;
//
//		@Override
//		public void run() {
//			while (isLoop) {
//				Looper curLooper = Looper.myLooper();
//				Looper mainLooper = Looper.getMainLooper();
//				String msg;
//				if (curLooper == null) {
//					lHandler = new MyHandler(mainLooper);
//					msg = "curLooper is null";
//				} else {
//					lHandler = new MyHandler(curLooper);
//					msg = "This is curLooper";
//				}
//				Log.i("whw","Thread.currentThread().getName() = " + Thread.currentThread().getName());
//				lHandler.removeMessages(1);
//				Message m = lHandler.obtainMessage(1, 1, 1, msg);
//				lHandler.sendMessage(m);
//				try {
//					Thread.sleep((int)(1 * 1000));
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	private void initData() {
		outputModes = getResources().getStringArray(R.array.output_mode_array);
		terminalChars = getResources().getStringArray(
				R.array.terminal_char_array);
		playoneModes = getResources().getStringArray(R.array.playone_mode);
		readSettingsData();
		if (mPowerOnOff == 1) {
			status.setChecked(true);
		} else {
			status.setChecked(false);
		}
		ArrayAdapter<String> outputAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, outputModes);
		outputMode.setAdapter(outputAdapter);
		outputMode.setSelection(mOutputMode);

		ArrayAdapter<String> terminalCharAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, terminalChars);
		terminalChar.setAdapter(terminalCharAdapter);
		terminalChar.setSelection(mTerminalChar);

		prefix.setText("" + mPrefix);
		suffix.setText("" + mSuffix);
		ArrayAdapter<Integer> volumeAdapter = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, volumes);
		volume.setAdapter(volumeAdapter);
		volume.setSelection(mVolume);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, playoneModes);
		playoneMode.setAdapter(adapter);
		playoneMode.setSelection(mPlayoneMode);
		
		IntentFilter filter = new IntentFilter("com.android.server.scannerservice.broadcast");
		registerReceiver(receiver, filter);
		pool = Executors.newSingleThreadExecutor();
	}

	private void readSettingsData() {
		mPowerOnOff = Settings.System.getInt(getContentResolver(),
				SCANNER_POWER_ON, 1);
		mOutputMode = Settings.System.getInt(getContentResolver(),
				SCANNER_OUTPUT_MODE, -1);
		mTerminalChar = Settings.System.getInt(getContentResolver(),
				SCANNER_TERMINAL_CHAR, -1);
		mPrefix = Settings.System.getString(getContentResolver(),
				SCANNER_PREFIX);
		mSuffix = Settings.System.getString(getContentResolver(),
				SCANNER_SUFFIX);
		mVolume = Settings.System.getInt(getContentResolver(), SCANNER_VOLUME,
				0);
		mPlayoneMode = Settings.System.getInt(getContentResolver(),
				SCANNER_PLAYTONE_MODE, 0);
	}

	private void writeSettingsData() {
		Settings.System.putInt(getContentResolver(), SCANNER_POWER_ON,
				status.isChecked() ? 1 : 0);
		Settings.System.putInt(getContentResolver(), SCANNER_OUTPUT_MODE,
				outputMode.getSelectedItemPosition());
		Settings.System.putInt(getContentResolver(), SCANNER_TERMINAL_CHAR,
				terminalChar.getSelectedItemPosition());
		Settings.System.putString(getContentResolver(), SCANNER_PREFIX, prefix
				.getText().toString());
		Settings.System.putString(getContentResolver(), SCANNER_SUFFIX, suffix
				.getText().toString());
		Settings.System.putInt(getContentResolver(), SCANNER_VOLUME,
				volumes[volume.getSelectedItemPosition()]);
		switch (playoneMode.getSelectedItemPosition()) {
		case 0:
			Settings.System.putInt(getContentResolver(), "SCANNER_SOUND", 1);
			Settings.System
					.putInt(getContentResolver(), "SCANNER_VIBRATION", 0);
			break;
		case 1:
			Settings.System.putInt(getContentResolver(), "SCANNER_SOUND", 0);
			Settings.System
					.putInt(getContentResolver(), "SCANNER_VIBRATION", 1);
			break;
		case 2:
			Settings.System.putInt(getContentResolver(), "SCANNER_SOUND", 1);
			Settings.System
					.putInt(getContentResolver(), "SCANNER_VIBRATION", 1);
			break;
		case 3:
			Settings.System.putInt(getContentResolver(), "SCANNER_SOUND", 0);
			Settings.System
					.putInt(getContentResolver(), "SCANNER_VIBRATION", 0);
			break;
		default:
			break;
		}
		Settings.System.putInt(getContentResolver(), SCANNER_PLAYTONE_MODE,
				playoneMode.getSelectedItemPosition());
	}

	@Override
	protected void onResume() {
		super.onResume();
		acquireWakeLock();
		//status.setChecked(true);
	}

	@Override
	protected void onPause() {
		//status.setChecked(false);
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		releaseWakeLock();
		Intent intent = new Intent(
				"com.android.server.scannerservice.onoff");
		intent.putExtra("scanneronoff", DISENABLE);
		sendBroadcast(intent);
		status.setChecked(false);
		scannerLoop.setChecked(false);
//		if(mt !=null)
//		{
//			mt.interrupt();
//			mt = null;
//		}
		Log.i("whw", "DISENABLE");
		if(null != receiver){
			try{
				unregisterReceiver(receiver);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	/** 
	 * 
	 * 函数说明: 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行 
	 * 
	 * */
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass().getCanonicalName());
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}
	/**
	 * 函数说明: 释放设备电源锁  
	 * 
	 * */
	public static void releaseWakeLock()
	{   
		if (null != wakeLock && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}  
	}
	private Runnable task = new Runnable() {
		private MyHandler lHandler;

		@Override
		public void run() {
			while (isLoop) {
				Looper curLooper = Looper.myLooper();
				Looper mainLooper = Looper.getMainLooper();
				String msg;
				if (curLooper == null) {
					lHandler = new MyHandler(mainLooper);
					msg = "curLooper is null";
				} else {
					lHandler = new MyHandler(curLooper);
					msg = "This is curLooper";
				}
				Log.i("whw",msg  + Thread.currentThread().getName());
				lHandler.removeMessages(1);
				Message m = lHandler.obtainMessage(1, 1, 1, msg);
				lHandler.sendMessage(m);
				try {
					Thread.sleep((int)(1 * 1500));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.start_scan:
			if(this.isLoop)
			{
				pool.execute(task);
//				mt = new MyThread();
//				mt.start();
				start.setEnabled(false);
			}else
			{
				Intent startIntent = new Intent(
						"android.intent.action.SCANNER_BUTTON_DOWN", null);
				sendOrderedBroadcast(startIntent, null);
			}
			break;
		case R.id.end_scan:
			status.setChecked(false);
			scannerLoop.setChecked(false);
			start.setEnabled(true);
			Intent endIntent = new Intent(
					"android.intent.action.SCANNER_BUTTON_UP", null);
			sendOrderedBroadcast(endIntent, null);
			break;
		case R.id.clear_scanner_time:
			value.setText("");
			totalView.setText("");
			successView.setText("");
			total = 0;
			resultTime = 0;
			break;
		case R.id.scan_settings:
			writeSettingsData();
			Intent intent = new Intent(
					"com.android.server.scannerservice.settingchange", null);
			sendOrderedBroadcast(intent, null);
			Log.i("whw", "settings");
			break;
		default:
			break;
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.i("whw", "getAction=" + intent.getAction().toString());
			if (intent.getAction().equals(
					"com.android.server.scannerservice.broadcast")) {
				String barcode = "";
				barcode = intent.getExtras().getString("scannerdata");
				Log.i("whw", "barcode=" + barcode);
				value.setText(barcode);
				resultTime++;
				successView.setText("" + resultTime);
			}
		}
	};

}
