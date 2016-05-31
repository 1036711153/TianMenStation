package com.example.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.authentication.activity.R;

public class CustomToast {
	public static void Custom_Toast(Context context,String text,int gravity,int time) {
		  Toast mToast = new Toast(context);
		  if (gravity!=0) {
			  mToast.setGravity(gravity, 0, 0);
		  }
		  View layout=LayoutInflater.from(context).inflate(R.layout.toast, null);
		  TextView textView=(TextView) layout.findViewById(R.id.mtext);
		  textView.setText(text);
		  mToast.setView(layout);
		  mToast.setDuration(time);
		  mToast.show();
	}
	
	public static void UnComment_Custom_Toast(Context context,String text,int gravity,int time) {
		  Toast mToast = new Toast(context);
		  if (gravity!=0) {
			  mToast.setGravity(gravity, 0, 0);
		  }
		  View layout=LayoutInflater.from(context).inflate(R.layout.toast, null);
		  TextView textView=(TextView) layout.findViewById(R.id.mtext);
		  textView.setText(text);
		  textView.setTextColor(context.getResources().getColor(R.color.pink));
		  mToast.setView(layout);
		  mToast.setDuration(time);
		  mToast.show();
	}
}
