package cn.itcast.picture;

import java.io.File;

import com.authentication.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PictureActivity extends Activity implements OnClickListener{
	protected static final int CAMERA_REQUEST_CODE1 = 1;
	protected static final int CAMERA_REQUEST_CODE2 = 2;
	protected static final int CAMERA_REQUEST_CODE3 = 3;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_activity);
		image1=(ImageView) findViewById(R.id.image1);
		image2=(ImageView) findViewById(R.id.image2);
		image3=(ImageView) findViewById(R.id.image3);
		findViewById(R.id.image1).setOnClickListener(this);
		findViewById(R.id.image2).setOnClickListener(this);
		findViewById(R.id.image3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image1:
			Intent intent1 = new Intent(PictureActivity.this,TakePictureActivity.class);
			startActivityForResult(intent1, CAMERA_REQUEST_CODE1);
			break;
	    case R.id.image2:
	    	Intent intent2 = new Intent(PictureActivity.this,TakePictureActivity.class);
			startActivityForResult(intent2, CAMERA_REQUEST_CODE2);
			break;
	     case R.id.image3:
	    	 Intent intent3 = new Intent(PictureActivity.this,TakePictureActivity.class);
				startActivityForResult(intent3, CAMERA_REQUEST_CODE3);
		    break;
		default:
			break;
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE1 && resultCode == 1 ) {
			if (data == null) {
				return;
			}
			String file_pathname = data.getStringExtra("file_pathname");
			if (file_pathname==null) {
				return;
			}
			image1.setImageURI(Uri.fromFile(new File(file_pathname)));
		}else if (requestCode == CAMERA_REQUEST_CODE2 && resultCode == 1) {
			if (data == null) {
				return;
			}
			String file_pathname = data.getStringExtra("file_pathname");
			if (file_pathname==null) {
				return;
			}
			image2.setImageURI(Uri.fromFile(new File(file_pathname)));
		} else if (requestCode == CAMERA_REQUEST_CODE3 && resultCode == 1 ) {
			if (data == null) {
				return;
			}
			String file_pathname = data.getStringExtra("file_pathname");
			if (file_pathname==null) {
				return;
			}
			image3.setImageURI(Uri.fromFile(new File(file_pathname)));
		} 
	}

}
