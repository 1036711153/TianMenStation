package com.example.down_install_APK;

import com.authentication.activity.R;
import com.example.Utils.APK_Down_Install;
import com.example.configure.Configure;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.SystemClock;

public class Down_Install_APK_aty extends Activity implements Runnable{
	private Thread thread;
	APK_Down_Install apk_Down_Install;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.down_install_apk);
		if (getIntent()!=null) {
			String APK_PATH=getIntent().getStringExtra("APK_PATH");
			apk_Down_Install=new APK_Down_Install(Down_Install_APK_aty.this, APK_PATH);
			thread=new Thread(this);
			thread.start();
		}
	}
	@Override
	public void run() {
		  apk_Down_Install.Down_Install();
	}
}
