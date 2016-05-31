package com.example.outstation.aty;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.authentication.activity.R;
import com.authentication.activity.TestFingerprintActivity;
import com.example.Utils.CustomToast;
import com.example.outstation.adapter.Driver_InfoAdapter;
import com.example.outstation.entity.Driver;
import com.example.outstation.entity.ExitCheckInfo;
import com.example.outstation.finishAty.ActivityCollector;

public class OutStationAty2 extends Activity {
	private ImageButton next_page;
	private ImageButton back_page;
	private ExitCheckInfo myExitCheckInfo;
	private TextView CarNum;
	private List<Driver> drivers = new ArrayList<Driver>();
	private ListView listView;
	private Driver_InfoAdapter adapter;
	private String chepaihao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_outstation_aty2);
		initId();// 初始化ID操作
		HttpConnection();// 做网络事件处理，返回json数据解析
		setLisetner();// 点击之后可以实现跳转了
		ActivityCollector.addActivity(this);
	}

	private void Fill_Data() {
		String car_num = myExitCheckInfo.getLicencePlate();
		String car_type = myExitCheckInfo.getVehicleType();
		String mcar_type = null;
		if (car_type.equals("001")) {
			mcar_type = "本站";
		} else {
			mcar_type = "配载";
		}
		String car_num_type = car_num + "(" + mcar_type + ")";
		CarNum.setText(car_num_type);
		adapter = new Driver_InfoAdapter(drivers, OutStationAty2.this);
		listView.setAdapter(adapter);
	}

	private void setLisetner() {
		next_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myExitCheckInfo != null) {
					String inspec_reString = myExitCheckInfo
							.getInspectionStatus();
					if (inspec_reString == null) {
						inspec_reString = "";
					}

					String report_reString = myExitCheckInfo
							.getClassReportStatus();
					if (report_reString == null) {
						report_reString = "";
					}

					if (!inspec_reString.equals("202")
							|| !report_reString.equals("300")) {
						CustomToast.UnComment_Custom_Toast(OutStationAty2.this,"报班或者安检不合格！！",0,Toast.LENGTH_SHORT);
						return;
					}
				}

				if (chepaihao != null && myExitCheckInfo != null) {
					Log.e("chepaihao", chepaihao);
					Log.e("getLicencePlate", myExitCheckInfo.getLicencePlate());
					if (!chepaihao.equals(myExitCheckInfo.getLicencePlate())) {
						CustomToast.UnComment_Custom_Toast(OutStationAty2.this,"车牌号核对不成功",0,Toast.LENGTH_SHORT);
						return;
					}
				}

				Intent intent = new Intent(OutStationAty2.this,
						OutStationTakePhotoAty.class);
				intent.putExtra("myExitCheckInfo", myExitCheckInfo);
				startActivity(intent);
			}
		});

		back_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OutStationAty2.this.finish();
			}
		});

	}

	private void HttpConnection() {

			if (!TextUtils.isEmpty(myExitCheckInfo.getFirDriverId())) {
				String firImage = " ";
				if (myExitCheckInfo.getFirPhoto() != null) {
					firImage = myExitCheckInfo.getFirPhoto();
				} 
				
				String tel=" ";
				if (myExitCheckInfo.getFirTel()!=null) {
					tel=myExitCheckInfo.getFirTel();
				}
				
				
				String firname=" ";
				if (myExitCheckInfo.getFirName()!=null) {
					firname=myExitCheckInfo.getFirName();
				}
				
				
				String dirvelicid=" ";
				if (myExitCheckInfo.getFirAdultNum()!=null) {
					dirvelicid=myExitCheckInfo.getFirAdultNum();
				}
				
				String idcard=" ";
				if (myExitCheckInfo.getFirIdCard()!=null) {
					idcard=myExitCheckInfo.getFirIdCard();
				}
				
				String instpectionstatus=" ";
				if (myExitCheckInfo.getInspectionStatus()!=null) {
					instpectionstatus=myExitCheckInfo.getInspectionStatus();
				}
				
				String classreport=" ";
				if (myExitCheckInfo.getClassReportStatus()!=null) {
					classreport=myExitCheckInfo.getClassReportStatus();
				}
				
				
				Driver driver = new Driver(myExitCheckInfo.getFirDriverId(),
						firname,
						tel,
						dirvelicid,
						idcard, 
						firImage,
						instpectionstatus,
						classreport);
				
				drivers.add(driver);
			}

			if (!TextUtils.isEmpty(myExitCheckInfo.getSecDriverId())) {

				
				
				String secImage = " ";
				if (myExitCheckInfo.getSecPhoto() != null) {
					secImage = myExitCheckInfo.getSecPhoto();
				} 
				
				String sectel=" ";
				if (myExitCheckInfo.getSecTel()!=null) {
					sectel=myExitCheckInfo.getSecTel();
				}
				
				
				String secname=" ";
				if (myExitCheckInfo.getSecName()!=null) {
					secname=myExitCheckInfo.getSecName();
				}
				
				
				String secdirvelicid=" ";
				if (myExitCheckInfo.getSecAdultNum()!=null) {
					secdirvelicid=myExitCheckInfo.getSecAdultNum();
				}
				
				String secidcard=" ";
				if (myExitCheckInfo.getSecIdCard()!=null) {
					secidcard=myExitCheckInfo.getSecIdCard();
				}
				
				String instpectionstatus=" ";
				if (myExitCheckInfo.getInspectionStatus()!=null) {
					instpectionstatus=myExitCheckInfo.getInspectionStatus();
				}
				
				String classreport=" ";
				if (myExitCheckInfo.getClassReportStatus()!=null) {
					classreport=myExitCheckInfo.getClassReportStatus();
				}
				Driver driver2 = new Driver(myExitCheckInfo.getSecDriverId(),
						secname,
						sectel,
						secdirvelicid,
						secidcard,
						secImage,
						instpectionstatus,
						classreport);
				drivers.add(driver2);
			}
			Fill_Data();
	}

	private void initId() {
		listView = (ListView) findViewById(R.id.listView);
		CarNum = (TextView) findViewById(R.id.Car_Num);
		next_page = (ImageButton) findViewById(R.id.next_page);
		back_page = (ImageButton) findViewById(R.id.back_page);
		myExitCheckInfo = (ExitCheckInfo) getIntent()
				.getSerializableExtra("myExitCheckInfo");// 上一个活动传过来的指纹编码
		if (getIntent() != null) {
			chepaihao = getIntent().getStringExtra(
					TestFingerprintActivity.CHEPAIHAO_KEY);
		}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
