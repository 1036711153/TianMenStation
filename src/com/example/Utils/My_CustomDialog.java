package com.example.Utils;

import android.content.Context;
import android.content.DialogInterface;

import com.dyr.custom.CustomDialog;

public class My_CustomDialog {
	public interface OnConfigItemClickListener
	{
		void itemPostive();
		void itemNegative();
	}
	private OnConfigItemClickListener onConfigItemClickListener;

	
	public  boolean showAlertDialog(Context context,String title,String messages) {
		final boolean isconfig=false;
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(messages);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				onConfigItemClickListener.itemPostive();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						onConfigItemClickListener.itemNegative();
					}
				});

		builder.create().show();
		return isconfig;

	}

}
