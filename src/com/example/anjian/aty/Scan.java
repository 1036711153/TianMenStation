package com.example.anjian.aty;

import com.authentication.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;


public class Scan extends Activity  {

	private ImageButton next;
	private ImageButton back;
	private Button open_RFID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anjian_scan_main);
		initId();
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Scan.this, CircleActivity.class));
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Scan.this.finish();
			}
		});
		
		//RFID读取
		open_RFID.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Scan.this, CircleActivity.class));
			}
		});
		
	}

	private void initId() {
		next = (ImageButton) findViewById(R.id.next_page);
		back = (ImageButton) findViewById(R.id.back_page);
        open_RFID=(Button) findViewById(R.id.open_RFID);
	}
}
