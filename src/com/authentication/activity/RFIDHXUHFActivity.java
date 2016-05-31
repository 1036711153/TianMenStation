package com.authentication.activity;

import java.lang.ref.WeakReference;
import com.authentication.utils.DataUtils;
import com.authentication.utils.ToastUtil;
import com.example.four_module.FourModuleAty;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import android_serialport_api.UHFHXAPI;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RFIDHXUHFActivity extends BaseUHFActivity {
	private static boolean ISINTENT = false;
	private static int mNum = 0;
	UHFHXAPI api;

	/**
	 * ���ڼ��д�����ʾ���¼���Ϣ�ľ�̬��
	 * 
	 * @author chenshanjing
	 * 
	 */
	class StartHander extends Handler {
		WeakReference<Activity> mActivityRef;

		StartHander(Activity activity) {
			mActivityRef = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Activity activity = mActivityRef.get();
			if (activity == null) {
				return;
			}

			switch (msg.what) {
			case MSG_SHOW_EPC_INFO:
				ShowEPC((String) msg.obj);
				break;

			case MSG_DISMISS_CONNECT_WAIT_SHOW:
				prgDlg.dismiss();
				if ((Boolean) msg.obj) {
					Toast.makeText(activity,
							activity.getText(R.string.info_connect_success),
							Toast.LENGTH_SHORT).show();
					setting.setEnabled(true);
					buttonInv.setClickable(true);
				} else {
					Toast.makeText(activity,
							activity.getText(R.string.info_connect_fail),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case INVENTORY_OVER:
				ToastUtil
						.showToast(RFIDHXUHFActivity.this, R.string.inventory_over);
				break;

			}
		}
	};

	private Handler hMsg = new StartHander(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_uhf);
		api = new UHFHXAPI();
		txtCount = (TextView) findViewById(R.id.txtCount);
		txtTimes = (TextView) findViewById(R.id.txtTimes);
		setting = (Button) findViewById(R.id.setting_params);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UHFDialogFragment dialog = new UHFDialogFragment();
				dialog.show(getFragmentManager(), "corewise");
			}
		});
		buttonConnect = (ToggleButton) findViewById(R.id.togBtn_open);
		buttonInv = (ToggleButton) findViewById(R.id.togBtn_inv);
		final FragmentManager fragmentManager = getFragmentManager();
		objFragment = (TaglistFragment) fragmentManager
				.findFragmentById(R.id.fragment_taglist);

		buttonConnect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						buttonConnect.setClickable(false);
						if (isChecked) {
							if (prgDlg != null) {
								prgDlg.show();
							} else {
								prgDlg = ProgressDialog.show(
										RFIDHXUHFActivity.this, "侯家伟", "链接成功",
										true, false);
							}
							buttonInv.setClickable(true);
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
								setting.setEnabled(false);
							}
							buttonInv.setClickable(false);
						}

						buttonConnect.setClickable(true);
					}
				});

		buttonInv.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				buttonInv.setClickable(false);
				if (isChecked) {
					isStop = false;
					Inv();
					setting.setEnabled(false);
				} else {
					isStop = true;
					setting.setEnabled(true);
				}

				buttonInv.setClickable(true);
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
                    while (!ISINTENT) {
                    	SystemClock.sleep(20);
                    	Log.e("AAA", "aaa");
                    	Log.e("mNum", mNum + "");
                    	if (mNum>=4) {
                    		startActivity(new Intent(RFIDHXUHFActivity.this,FourModuleAty.class));
						}
					}
			}
		}).start();
	}

	// /**
	// * ��ʾ�����õ��ı�ǩ��Ϣ
	// *
	// * @param activity
	// * @param flagID
	// */
	// public static void ShowEPC(String flagID) {
	// if (!tagInfoList.contains(flagID)) {
	// tagCount++;
	// tagInfoList.add(flagID);
	// objFragment.addItem(flagID);
	//
	// try {
	// txtCount.setText(String.format("%d", tagCount));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// tagTimes++;
	// try {
	// txtTimes.setText(String.format("%d", tagTimes));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * ��ʾ�����õ��ı�ǩ��Ϣ
	 * 
	 * @param activity
	 * @param flagID
	 */
	public static void ShowEPC(String flagID) {
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

			objFragment.addItem(flagID);

			try {
				txtCount.setText(String.format("%d", tagCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			int num = number.get(flagID);
			number.put(flagID, ++num);
			
			// 如果扫描到4次就停止工作跳转页面
			if (num >= 4) {
				ISINTENT=true;
				mNum=num;
			}
			Log.i("whw", "flagID=" + flagID + "   num=" + num);
		}
		objFragment.myadapter.notifyDataSetChanged();
		tagTimes++;
		try {
			txtTimes.setText(String.format("%d", tagTimes));
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
		objFragment.clearItem();

		try {
			txtCount.setText(String.format("%d", tagCount));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			txtTimes.setText(String.format("%d", tagTimes));
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
		isOnPause = false;
	}

	private boolean isOnPause;

	@Override
	protected void onPause() {
		isOnPause = true;
		isStop = true;
		if (buttonInv.isChecked()) {
			buttonInv.setChecked(false);
			buttonInv.setClickable(false);
			api.close();
		}
		if (buttonConnect.isChecked()) {
			buttonConnect.setChecked(false);
		}
		super.onPause();
	}

}
