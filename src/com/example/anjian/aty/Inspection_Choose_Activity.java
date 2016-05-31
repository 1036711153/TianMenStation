package com.example.anjian.aty;

import com.authentication.activity.AnjianSationHXUHFActivity;
import com.authentication.activity.R;
import com.authentication.activity.Uncomment_AnjianSationHXUHFActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Inspection_Choose_Activity extends Activity implements OnClickListener{
	private ImageButton back_page;
	private Button inspection_comment;
	private Button inspection_exception;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectionchoose_activity);
		initData();
		initOnclick();
		
	}
	private void initOnclick() {
		back_page.setOnClickListener(this);
		inspection_comment.setOnClickListener(this);
		inspection_exception.setOnClickListener(this);
		
	}
	private void initData() {
		back_page=(ImageButton) findViewById(R.id.back_page);
		inspection_comment=(Button) findViewById(R.id.inspection_comment);
		inspection_exception=(Button) findViewById(R.id.inspection_exception);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_page:
			this.finish();
			break;
		case R.id.inspection_comment:
			startActivity(new Intent(Inspection_Choose_Activity.this,AnjianSationHXUHFActivity.class));
			break;
		case R.id.inspection_exception:
			startActivity(new Intent(Inspection_Choose_Activity.this,Inspection_UnComment_Activity.class));
			break;

		default:
			break;
		}
	}

}
