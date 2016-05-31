package com.example.four_module;

import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.authentication.activity.RegisterFingerprintActivity;
import com.authentication.entity.AutoCarNum;
import com.authentication.entity.CarNumLab;
import com.authentication.entity.Register_Finger_DataBase;
import com.authentication.entity.Register_Finger_Lab;
import com.custom.View.SlidingMenu;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.VerSionCode;
import com.example.anjian.AnjianLab;
import com.example.anjian.InspectionItem;
import com.example.anjian.aty.Inspection_Choose_Activity;
import com.example.configure.Configure;
import com.example.down_install_APK.APKInfo;
import com.example.down_install_APK.Down_Install_APK_aty;
import com.example.instation.InstationChooseActivity;
import com.example.instation.InstationJsonUser;
import com.example.instation.LoginActivity;
import com.example.instation.service.InStationService;
import com.example.outstation.aty.ExitstationChooseActivity;
import com.example.synchronization.aty.HomePageActivity;
import com.google.gson.Gson;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.bugly.crashreport.CrashReport;

public class FourModuleAty extends Activity implements OnClickListener {
	private SlidingMenu mSlidingMenu;
	private Button btn_instation;
	private Button btn_anjian;
	private Button btn_outstaion;
	private Button btn_tongbu;
	private Button btn_canshu;
	private Button close_menu;
	private Button btn_user_save_exit;
	private InstationJsonUser jsonuser;
	private int mFinishCount = 0;
	private  int code;
	private String right;
	//用于启动出站的intent服务
	private Intent startIntent;
	//用于启动进站服务的intent
	private Intent startIntent2;

	private TextView user_name;
	private TextView user_id;
	private TextView user_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.four_module);
		CrashReport.initCrashReport(getApplicationContext(), "900017961", false);
		initID();
		initMenu();
		getVersionCode();
        //访问服务器最新版本号是多少？如果两个不相同则提示有新版本出现，如果相同则不提示
		HttpConnection();
		jsonuser=DataSupport.findLast(InstationJsonUser.class);
		if (jsonuser!=null) {
			right=jsonuser.getLicence();
			//判断权限
			
			if (right.contains("1")) {
				//进站
				btn_instation.setEnabled(true);
				btn_instation.setBackgroundResource(R.drawable.btn_jinzhanmodule_pressed);
			}
			if (right.contains("2")) {
				//安检
				btn_anjian.setEnabled(true);
				btn_anjian.setBackgroundResource(R.drawable.btn_anquanmodule_pressed);
			}
			if (right.contains("4")) {
				//出站
				btn_outstaion.setEnabled(true);
				btn_outstaion.setBackgroundResource(R.drawable.btn_chuzhanmodule_pressed);
			}
			if (right.contains("5")) {
				//后台
				btn_canshu.setEnabled(true);
				btn_canshu.setBackgroundResource(R.drawable.btn_canshumodule_pressed);
			}
			if (right.equals("6")||right.equals("0")) {
				//全部
				btn_canshu.setEnabled(true);
				btn_canshu.setBackgroundResource(R.drawable.btn_canshumodule_pressed);
				btn_instation.setEnabled(true);
				btn_instation.setBackgroundResource(R.drawable.btn_jinzhanmodule_pressed);
				btn_anjian.setEnabled(true);
				btn_anjian.setBackgroundResource(R.drawable.btn_anquanmodule_pressed);
				btn_outstaion.setEnabled(true);
				btn_outstaion.setBackgroundResource(R.drawable.btn_chuzhanmodule_pressed);
			}
		}
        btn_instation.setOnClickListener(this);
        btn_anjian.setOnClickListener(this);
        btn_outstaion.setOnClickListener(this);
        btn_tongbu.setOnClickListener(this);
        btn_canshu.setOnClickListener(this);
        close_menu.setOnClickListener(this);
		btn_user_save_exit.setOnClickListener(this);
	}


	private void initMenu() {
		List<InstationJsonUser> users=DataSupport.findAll(InstationJsonUser.class);
		if (users.size()!=0) {
			InstationJsonUser user=users.get(0);
			user_name.setText(user.getName());
			user_account.setText(user.getAccount());
			user_id.setText(user.getWatchID());
		}
	}


	private void HttpConnection() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
        client.post(Configure.IP+"/Upload_8-1/MyServelt9", params,new TextHttpResponseHandler() {
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			Gson gson=new Gson();
			final APKInfo info=gson.fromJson(arg2,APKInfo.class);
			//当版本不提时候提示更新版本号：
			if (code!=info.getVersioncode()) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(FourModuleAty.this);
				builder.setMessage("有新版本，请更新APK");
				builder.setTitle("提示");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						Intent intent=new Intent(FourModuleAty.this,Down_Install_APK_aty.class);
						intent.putExtra("APK_PATH", info.getPath());
						startActivity(intent);
						dialog.dismiss();
					}
				});

				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();
			}
	}
		
		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			
		}
	});
		
        
        int count=DataSupport.count(InspectionItem.class);
		//如果安检的主项目表中没有数据，说明需要进行安检的数据同步操作。
		if (count==0) {
			try {
			
				AnjianLab anjianLab =new AnjianLab();
				anjianLab.tongbu_anjian(FourModuleAty.this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CustomToast.UnComment_Custom_Toast(FourModuleAty.this,"同步安检数据表失败！！",0,Toast.LENGTH_SHORT);
			}
		}
	}
	private void getVersionCode() {
		VerSionCode verSionCode=new VerSionCode();
		code=verSionCode.getAPPVersionCodeFromAPP(FourModuleAty.this);
		Log.e("versioncode", code+"");
	}

	private void initID() {
		user_name= (TextView) findViewById(R.id.user_name);
		user_id= (TextView) findViewById(R.id.user_id);
		user_account= (TextView) findViewById(R.id.user_account);
		btn_user_save_exit= (Button) findViewById(R.id.user_save_exit);
		mSlidingMenu= (SlidingMenu) findViewById(R.id.id_menu);
		close_menu=(Button) findViewById(R.id.close_menu);
		btn_instation=(Button) findViewById(R.id.btn_instation);
		btn_anjian=(Button) findViewById(R.id.btn_anjian);
		btn_outstaion=(Button) findViewById(R.id.btn_outstaion);
		btn_tongbu=(Button) findViewById(R.id.btn_tongbu);
		btn_canshu=(Button) findViewById(R.id.btn_canshu);
		//dosomething httpconnection
		int count=DataSupport.count(AutoCarNum.class);
		//如果安检的主项目表中没有数据，说明需要进行安检的数据同步操作。
		if (count==0) {
			try {
				CarNumLab carNumLab =new CarNumLab(null, Configure.IP+"/TianMen/ExitCheckAction!getAllLicencePlate.action", FourModuleAty.this);
				carNumLab.AsyncHttp();
			} catch (Exception e) {
				e.printStackTrace();
				CustomToast.UnComment_Custom_Toast(FourModuleAty.this,"同步车牌号数据失败！！",0,Toast.LENGTH_SHORT);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_instation:
			startActivity(new Intent(FourModuleAty.this,InstationChooseActivity.class));
			break;
		case R.id.btn_anjian:
			startActivity(new Intent(FourModuleAty.this,Inspection_Choose_Activity.class));
			break;
		case R.id.btn_outstaion:
			startActivity(new Intent(FourModuleAty.this,ExitstationChooseActivity.class));
			break;
		case R.id.btn_tongbu:
			startActivity(new Intent(FourModuleAty.this,HomePageActivity.class));
			break;
		case R.id.btn_canshu:
			//参数设置页面
			//dosomething httpconnection
			int num=DataSupport.count(Register_Finger_DataBase.class);
			//如果安检的主项目表中没有数据，说明需要进行安检的数据同步操作。
			if (num==0) {
				try {
					Register_Finger_Lab register_Finger_Lab =new Register_Finger_Lab(null, Configure.IP+"/TianMen/ExitCheckAction!getSimpleDriverInfo.action", FourModuleAty.this);
					register_Finger_Lab.AsyncHttp();
				} catch (Exception e) {
					e.printStackTrace();
					CustomToast.UnComment_Custom_Toast(FourModuleAty.this,"同步驾驶员数据失败！！",0,Toast.LENGTH_SHORT);
				}
			}
			startActivity(new Intent(FourModuleAty.this,RegisterFingerprintActivity.class));
			break;
			case R.id.close_menu:
				mSlidingMenu.toggle();
				break;
			case R.id.user_save_exit:
				mFinishCount=1;
				LoginActivity.Save_Exit();
				FourModuleAty.this.finish();
				break;
		default:
			break;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mFinishCount = 0;
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void finish() {
		mFinishCount++;
		if (mFinishCount == 1) {
			CustomToast.Custom_Toast(FourModuleAty.this,"再按一次退出程序！！",0,Toast.LENGTH_SHORT);
		} else if (mFinishCount == 2) {
			super.finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//出站//pc报班机为3
		if (right.contains("4")||right.contains("6")) {
//			startIntent=new Intent(this,OutofstationService.class);
//			startService(startIntent);
//			startService(new Intent(this,SynchronousOutOfStationService.class));
		}
		
		//进站
		if (right.contains("1")||right.contains("6")) {
			startIntent2=new Intent(this,InStationService.class);
			startService(startIntent2);
			//进站自动同步
//			startService(new Intent(this,SynchronousInStationService.class));
		}
		
//		if (right.contains("2")||right.contains("6")) {
//			//安检自动同步
//			startService(new Intent(this,SynchronousAnjianService.class));
//		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (startIntent!=null) {
			stopService(startIntent);
		}
		if (startIntent2!=null) {
			stopService(startIntent2);
		}
		java.lang.System.exit(0);
	}
}
