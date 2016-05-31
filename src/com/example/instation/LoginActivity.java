package com.example.instation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.authentication.activity.R;
import com.example.Utils.CustomToast;
import com.example.Utils.InputTools;
import com.example.Utils.Space_Character;
import com.example.configure.Config_DataDase;
import com.example.configure.Configure;
import com.example.four_module.FourModuleAty;
import com.example.outstation.entity.ExitCheckInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.bugly.proguard.ad;
import com.zhang.autocomplete.ArrayAdapter;

public class LoginActivity extends Activity {
//	private ArrayAdapter<String> adapter;
//	private List<mAdmin> madmins=new ArrayList<mAdmin>();
//	private List<String> musernames=new ArrayList<>();
	private static EditText euser;
	
	private static EditText epassword;
	private  String username;
	private  String password;
	private Button loginbutton;
	private Button controller;
	private InstationJsonUser jsonuser=new InstationJsonUser();
	private View parentView;
	private PopupWindow popupWindow;
	private View conterview;
	private EditText ip;
	private Button reset;
	private Button save;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		SQLiteDatabase db=Connector.getDatabase();
		initID();
		initIp();
		DataSupport.deleteAll(ExitCheckInfo.class);
		setonClickListener();
	}
	
	private void initID() {
		controller=(Button) findViewById(R.id.controller);
		parentView=findViewById(R.id.linearLayout1);
		conterview = getLayoutInflater().inflate(
				R.layout.login_pop_main, null);
		popupWindow = new PopupWindow(conterview,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		euser=(EditText) findViewById(R.id.editText_User);
		epassword=(EditText) findViewById(R.id.editText_password);
		loginbutton=(Button) findViewById(R.id.login);
		
//		madmins=DataSupport.findAll(mAdmin.class);
//		if (madmins.size()!=0) {
//			for (mAdmin mAdmin : madmins) {
//				musernames.add(mAdmin.getMadmin());
//			}
//		}
//		adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, musernames);
//		euser.setThreshold(1);
//		euser.setAdapter(adapter);
		
	}
	
	private void initIp() {
		int count=DataSupport.count(Config_DataDase.class);
		if (count==0) {
			Config_DataDase config_DataDase=new Config_DataDase();
			config_DataDase.setIP("http://192.168.1.220:8090");
			config_DataDase.save();
		}else {
		    Config_DataDase findFirst = DataSupport.findFirst(Config_DataDase.class);
	        String mIP=findFirst.getIP();
	        Configure.IP= Space_Character.trimspace(mIP);
		}
	}
	private void setonClickListener() {
//		euser.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				InputTools.HideKeyboard(view);
//			}
//		});
		
		
		
		findViewById(R.id.login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		controller.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        	popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	        	
	        	ip=(EditText) conterview.findViewById(R.id.id_ip);
	    	    reset=(Button) conterview.findViewById(R.id.reset);
	    		save=(Button) conterview.findViewById(R.id.save);
	    		
				int count=DataSupport.count(Config_DataDase.class);
				if (count!=0) {
					Config_DataDase findFirst = DataSupport.findFirst(Config_DataDase.class);
			        String mIP=findFirst.getIP();
			        Log.e("mIP", mIP);
			        if (mIP==null||TextUtils.isEmpty(mIP)) {
						ip.setText("http://192.168.0.220:8090");
					}else {
					    ip.setText(mIP);
					}
				}
				
				 reset.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							    ip.setText("");
						}
					});
			        
					save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int count=DataSupport.count(Config_DataDase.class);
							if (count!=0) {
								String IP=ip.getText().toString();
								Config_DataDase findFirst = DataSupport.findFirst(Config_DataDase.class);
								int id=findFirst.getId();
						        ContentValues values=new ContentValues();
						        values.put("ip", IP);
						        DataSupport.update(Config_DataDase.class, values, id);
						        Configure.IP= Space_Character.trimspace(IP);
							}
							popupWindow.dismiss();
						}
					});
			}
		});
	}
	
	public void login(){
		username=Space_Character.trimspace(euser.getText().toString());
		password=Space_Character.trimspace(epassword.getText().toString());
		if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
			CustomToast.UnComment_Custom_Toast(LoginActivity.this,"用户名或密码为空！！",0,Toast.LENGTH_SHORT);
		    return;
		}
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("username", username);
		params.put("password", password);
		client.post(Configure.IP+"/TianMen/LoginAction!defaultMethod.action",params,new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				
				try {
					Log.e("login", arg2);
					JSONObject object=new JSONObject(arg2);
					boolean flag=object.getBoolean("flag");
					if (!flag) {
						CustomToast.UnComment_Custom_Toast(LoginActivity.this,"用户名与密码不匹配！！",0,Toast.LENGTH_SHORT);
						return;
					}
					String contentUser=object.getString("user");
					Gson gson=new Gson();
					PDAUser pdaUser=gson.fromJson(contentUser, PDAUser.class);
					String account=pdaUser.getAccount();
					String user_name=pdaUser.getUsername();
					String user_ID=pdaUser.getID();
					//設置權限0-4代表不同權限
					String licence=pdaUser.getRightCode();
					jsonuser.setAccount(account);
					jsonuser.setName(user_name);
					jsonuser.setWatchID(user_ID);
					jsonuser.setLicence(licence);
//					Log.e("TAG12", jsonuser.toString());
					int countNum=DataSupport.count(InstationJsonUser.class);
					if (countNum==0) {
						jsonuser.save();
					}
					else {
						ContentValues values=new ContentValues();
						values.put("account", account);
						values.put("watchid", user_ID);
						values.put("name", user_name);
						values.put("licence", licence);
						DataSupport.updateAll(InstationJsonUser.class, values);
					}
				if (TextUtils.isEmpty(user_name)) {
					CustomToast.UnComment_Custom_Toast(LoginActivity.this,"用户名与密码不匹配！！",0,Toast.LENGTH_SHORT);
					return;
				}
				else{
					//保存登录的的用户名,看存不存在，存在就不保存，不存在就保存
//					int count =DataSupport.count(mAdmin.class);
//					if (count==0) {
//						mAdmin admin=new mAdmin();
//						admin.setMadmin(username);
//						admin.save();
//					}else {
////						if(!musernames.contains(username)){
////							mAdmin admin=new mAdmin();
////							admin.setMadmin(username);
////							admin.save();
////						}
//					}
					Intent intent=new Intent(LoginActivity.this,FourModuleAty.class);
					startActivity(intent);
					} 
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			    CustomToast.UnComment_Custom_Toast(LoginActivity.this,"连接服务器失败！！",0,Toast.LENGTH_SHORT);
			}
		});
		
	}
	
	public static void Save_Exit() {
		euser.setText("");
		epassword.setText("");
	}
	
	
	
	/**
	 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * 
	 * @author cg
	 * 
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		java.lang.System.exit(0);
	}
}
