package com.example.outstation.aty;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.authentication.activity.OutSationHXUHFActivity;
import com.authentication.activity.R;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.Utils.CustomToast;
import com.example.configure.Configure;
import com.example.outstation.finishAty.ActivityCollector;

public class ExitstationChooseActivity extends Activity  implements OnClickListener{
	private ImageButton back_page;
	private Button instation_comment;
	private Button instation_exception;
	private Button close_door;
	private Button open_door;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int a=(int) msg.obj;
			if (a==1) {
				CustomToast.UnComment_Custom_Toast(ExitstationChooseActivity.this , "连接开关门失败！！" , 0 , Toast.LENGTH_SHORT);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exitstationchoose_activity);
		//取消通知栏信息
		NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(1);
		initID();
		initOnclick();
	}
	private void initID() {
		back_page=(ImageButton) findViewById(R.id.back_page);
		instation_comment=(Button) findViewById(R.id.instation_comment);
		instation_exception=(Button) findViewById(R.id.instation_exception);
		close_door=(Button) findViewById (R.id.close_door);
		open_door=(Button) findViewById (R.id.open_door);
	}
	private void initOnclick() {
		instation_comment.setOnClickListener(this);
		instation_exception.setOnClickListener(this);
		back_page.setOnClickListener(this);
		close_door.setOnClickListener(this);
		open_door.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.instation_comment:
			startActivity(new Intent(getApplicationContext(),OutSationHXUHFActivity.class));
			break;
        case R.id.instation_exception:
        	startActivity(new Intent(getApplicationContext(),ExitstationUnconventionActivity.class));
			break;
        case R.id.open_door:
        	CustomDialog.Builder builder = new CustomDialog.Builder(ExitstationChooseActivity.this);
			builder.setMessage("是否确认开启栅栏");
			builder.setTitle("确认开门");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//设置你的操作事项
					ControllerOpenCloseDoor openDoor=new ControllerOpenCloseDoor(ExitstationChooseActivity.this, Configure.CCONTROLLER_DOOR_IP2,mHandler);
		        	openDoor.OpenDoor();
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
        	break;
        case R.id.close_door:
        	CustomDialog.Builder builder2 = new CustomDialog.Builder(ExitstationChooseActivity.this);
			builder2.setMessage("是否确认关闭栅栏");
			builder2.setTitle("确认关门");
			builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//设置你的操作事项
					ControllerOpenCloseDoor closeDoor=new ControllerOpenCloseDoor(ExitstationChooseActivity.this, Configure.CCONTROLLER_DOOR_IP2,mHandler);
				    closeDoor.CloseDoor();
					dialog.dismiss();
				}
			});
			builder2.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder2.create().show();
        	break;
        case R.id.back_page:
        	this.finish();
        	break;
		default:
			break;
		}
	}
}
