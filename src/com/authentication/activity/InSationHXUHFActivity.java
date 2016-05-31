package com.authentication.activity;

import java.lang.ref.WeakReference;

import org.litepal.crud.DataSupport;

import com.authentication.activity.OutSationHXUHFActivity.MyThread;
import com.authentication.utils.DataUtils;
import com.authentication.utils.ToastUtil;
import com.example.four_module.FourModuleAty;
import com.example.instation.InStationActivity;
import com.example.instation.InStationTakePhotoActivity;
import com.example.instation.InstationJsonUser;
import com.example.outstation.finishAty.ActivityCollector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android_serialport_api.SerialPortManager;
import android_serialport_api.UHFHXAPI;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InSationHXUHFActivity extends BaseUHFActivity {
	public boolean ISINTENT = false;
	public int mNum = 0;
	private String mRFID = "";
	public static final String RFID_KEY = "rfid_key";
	UHFHXAPI api;
	private MyThread t;

	class StartHander extends Handler {
		// WeakReference<Activity> mActivityRef;
		//
		// StartHander(Activity activity) {
		// mActivityRef = new WeakReference<Activity>(activity);
		// }

		@Override
		public void handleMessage(Message msg) {
			// Activity activity = mActivityRef.get();
			// if (activity == null) {
			// return;
			// }

			switch (msg.what) {
			case MSG_SHOW_EPC_INFO:
				ShowEPC((String) msg.obj);
				break;

			case MSG_DISMISS_CONNECT_WAIT_SHOW:
				prgDlg.dismiss();
				if ((Boolean) msg.obj) {
					Toast.makeText(getApplicationContext(),
							R.string.info_connect_success, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.info_connect_fail, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case INVENTORY_OVER:
				ToastUtil.showToast(InSationHXUHFActivity.this,
						R.string.inventory_over);
				break;

			}
		}
	};

	private Handler hMsg = new StartHander();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instation_scan_main);
		api = new UHFHXAPI();
		txtCount = (TextView) findViewById(R.id.txtCount);
		txtTimes = (TextView) findViewById(R.id.txtTimes);
		setting = (Button) findViewById(R.id.setting_params);
		buttonConnect = (ToggleButton) findViewById(R.id.togBtn_open);
		buttonInv = (ToggleButton) findViewById(R.id.togBtn_inv);
		final FragmentManager fragmentManager = getFragmentManager();
		objFragment = (TaglistFragment) fragmentManager
				.findFragmentById(R.id.fragment_taglist);

		findViewById(R.id.back_page).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InSationHXUHFActivity.this.finish();

			}
		});
		// 按下一个按钮直接就是没有RFID扫描动作
		findViewById(R.id.next_page).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InSationHXUHFActivity.this,
						InStationTakePhotoActivity.class);
				intent.putExtra(RFID_KEY, mRFID);
				startActivity(intent);
			}
		});

		findViewById(R.id.open_RFID).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isChecked = true;
				// TODO Auto-generated method stub
				if (isChecked) {
					if (prgDlg != null) {
						prgDlg.show();
					} else {
						prgDlg = ProgressDialog.show(
								InSationHXUHFActivity.this, "RFID", "链接成功",
								true, false);
					}
					new Thread() {
						@Override
						public void run() {
							Message closemsg = new Message();
							closemsg.obj = api.open();
							closemsg.what = MSG_DISMISS_CONNECT_WAIT_SHOW;
							hMsg.sendMessage(closemsg);
						}
					}.start();
				} else {
					if (!isOnPause) {
						api.close();
					}
				}

				if (isChecked) {
					isStop = false;
					Inv();
				} else {
					isStop = true;
				}

			}
		});

		t = new MyThread();
		t.start();
		ActivityCollector.addActivity(this);

	}

	/**
	 * ��ʾ�����õ��ı�ǩ��Ϣ
	 * 
	 * @param activity
	 * @param flagID
	 */
	public void ShowEPC(String flagID) {
		if (mediaPlayer == null) {
			return;
		}
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
		} else {
			mediaPlayer.start();
		}
		if (!tagInfoList.contains(flagID)) {
			number.put(flagID, 1);
			tagCount++;
			tagInfoList.add(flagID);

			// flagID就是RFID标签信息
			Log.e("flagID", flagID);

			// objFragment.addItem(flagID);

			try {
				txtCount.setText(String.format("%d", tagCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			int num = number.get(flagID);
			number.put(flagID, ++num);
			mNum++;
			// 如果扫描到4次就停止工作跳转页面
			if (mNum >= 4) {
				mRFID = flagID;
			}
			Log.i("whw", "flagID=" + flagID + "   num=" + num);
		}
		// objFragment.myadapter.notifyDataSetChanged();
		tagTimes++;
		try {
			// txtTimes.setText(String.format("%d", tagTimes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����̵����
	 */
	public void Inv() {
		pool.execute(task);
		tagInfoList.clear();
		tagCount = 0;
		tagTimes = 0;

		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isStop;
	private Runnable task = new Runnable() {

		@Override
		public void run() {
			api.startAutoRead(0x22, new byte[] { 0x00, 0x01 },
					new UHFHXAPI.AutoRead() {

						@Override
						public void timeout() {
							Log.i("whw", "timeout");
						}

						@Override
						public void start() {
							Log.i("whw", "start");
						}

						@Override
						public void processing(byte[] data) {
							String epc = DataUtils.toHexString(data).substring(
									4);
							hMsg.obtainMessage(MSG_SHOW_EPC_INFO, epc)
									.sendToTarget();
							Log.i("whw", "data=" + epc);
						}

						@Override
						public void end() {

							Log.i("whw", "end");
							Log.i("whw", "isStop=" + isStop);
							if (!isStop) {
								pool.execute(task);
							} else {
								hMsg.sendEmptyMessage(INVENTORY_OVER);
							}
						}
					});
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		ISINTENT = false;
		isOnPause = false;
	}

	private boolean isOnPause;

	@Override
	protected void onPause() {
		SerialPortManager.getInstance().closeSerialPort();
		ISINTENT = true;
		isOnPause = true;
		isStop = true;
		if (api != null) {
			api.close();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		t = null;
		prgDlg = null;
		objFragment = null;

		task = null;
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		SerialPortManager.getInstance().closeSerialPort();
		if (api != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					api.close();
					api = null;
				}
			}).start();
			Log.e("api", "api is null");
		}
		if (hMsg != null) {
			hMsg.removeCallbacksAndMessages(null);
			hMsg = null;
		}
		if (pool != null) {
			pool.shutdown();
		}
		System.gc();
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (!ISINTENT) {
				SystemClock.sleep(100);
				if (mNum >= 4) {
					mNum = 0;
					Intent intent = new Intent(getApplicationContext(),
							InStationTakePhotoActivity.class);
					intent.putExtra(RFID_KEY, mRFID);
					startActivity(intent);
					return;
				}
			}

		}
	}

}
