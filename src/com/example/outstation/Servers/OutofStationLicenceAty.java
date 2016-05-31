package com.example.outstation.Servers;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.authentication.activity.R;
import com.example.outstation.entity.ExitCheckInfo;

public class OutofStationLicenceAty extends Activity{
	private List<ExitCheckInfo>exitCheckInfos=new ArrayList<ExitCheckInfo>();
    private ListView mListView;
    private OutofStationLicenceAdapter mAdapter;
    private Button back_page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_licenceplate);
		
		NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(1);
		
		mListView=(ListView) findViewById(R.id.listview);
		exitCheckInfos=DataSupport.findAll(ExitCheckInfo.class);
		mAdapter=new OutofStationLicenceAdapter(exitCheckInfos, OutofStationLicenceAty.this);
		mListView.setAdapter(mAdapter);
		findViewById(R.id.back_page).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OutofStationLicenceAty.this.finish();
			}
		});
	}

}
