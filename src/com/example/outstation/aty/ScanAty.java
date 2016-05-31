package com.example.outstation.aty;


import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.authentication.activity.R;
import com.example.outstation.finishAty.ActivityCollector;


public class ScanAty extends Activity  {

	private ImageButton next;
	private ImageButton back;
	private Button open_RFID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_scan_main);
		SQLiteDatabase db = Connector.getDatabase();
		initId();
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ScanAty.this, OutStationAty_Finger.class));
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ScanAty.this.finish();
			}
		});
		
		open_RFID.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ScanAty.this, OutStationAty_Finger.class));
			}
		});
		ActivityCollector.addActivity(this);
	}

	private void initId() {
		next = (ImageButton) findViewById(R.id.next_page);
		back = (ImageButton) findViewById(R.id.back_page);
        open_RFID=(Button) findViewById(R.id.open_RFID);
	}
}
