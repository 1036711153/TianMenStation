package com.example.outstation.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.authentication.activity.R;
import com.example.outstation.View.CustomAnim;
import com.example.outstation.finishAty.ActivityCollector;

public class OutStationAty1 extends Activity {
	private ImageView myfiger_validate;
	private ImageView scale_finger;
	private ImageButton nextButton;
	private ImageButton backButton;
	private boolean config_finger = false;// 用于判断是否确认了指纹，没有确认为false，确认了之后设置为true
	private CustomAnim customAnim = new CustomAnim();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			scale_finger.startAnimation(customAnim);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_outstation_aty);
		customAnim.setDuration(6000);
		initId();
		find_finger_indb();
		setLisenter();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Message msg = Message.obtain();
					msg.obj=1;
					handler.sendMessage(msg);
					SystemClock.sleep(6000);
				}
			}
		}).start();
		ActivityCollector.addActivity(this);
	}

	private void find_finger_indb() {// 在pda端的数据库或者指纹接口查询指纹
		config_finger = true;
	}

	private void setLisenter() {
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (config_finger) {
					// 当确定了指纹之后会进行跳转去PC端查询到下一个模块；
					Intent intent = new Intent(OutStationAty1.this,
							OutStationAty2.class);
					intent.putExtra("finger", "123456");
					startActivity(intent);
				}

			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OutStationAty1.this.finish();
			}
		});
		
		myfiger_validate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	private void initId() {
		myfiger_validate=(ImageView) findViewById(R.id.myfiger_validate);
		scale_finger = (ImageView) findViewById(R.id.scale_finger);
		nextButton = (ImageButton) findViewById(R.id.next_page);
		backButton=(ImageButton) findViewById(R.id.back_page);
	}

}
