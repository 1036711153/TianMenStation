package com.example.instation;

import org.litepal.crud.DataSupport;

import com.authentication.activity.R;
import com.example.outstation.finishAty.ActivityCollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class InStationActivity extends Activity{
	
	private Button open_rfidButton;
	private ImageButton backButton;
	private ImageButton nextButton;
	private boolean config_rfid=false;
	private InstationJsonUser jsonuser=new InstationJsonUser();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instation_scan_main);
		open_rfidButton=(Button) findViewById(R.id.open_RFID);
		backButton=(ImageButton) findViewById(R.id.back_page);
		nextButton=(ImageButton) findViewById(R.id.next_page);
		find_rfid_indb();
		setListener();
		ActivityCollector.addActivity(this);
	}
	private void find_rfid_indb() {
		config_rfid=true;
		
	}
	private void setListener() {
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InStationActivity.this.finish();
				
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContentValues values=new ContentValues();
				Intent intent=new Intent(InStationActivity.this,InStationTakePhotoActivity.class);
				startActivity(intent);
			}
		});
		open_rfidButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(config_rfid==true)
				{Intent intent=new Intent(InStationActivity.this,InStationTakePhotoActivity.class);
				 startActivity(intent);
				 }
				else {
				AlertDialog.Builder dialog=new AlertDialog.Builder(InStationActivity.this);
				dialog.setTitle("注   意：");
				dialog.setMessage("搜素不到该RFID");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {	
					}
				});
				dialog.show();
				}
			}
		});
	}
	
}
