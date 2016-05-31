package com.example.outstation.aty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.example.Utils.CustomToast;
import com.example.outstation.entity.ExitCheckInfo;
import com.example.outstation.finishAty.ActivityCollector;
import com.picture.compress.CreatePhoto;
import com.picture.compress.PictureUtils;

public class OutStationTakePhotoAty extends Activity implements OnClickListener{
	protected static final int CAMERA_REQUEST_CODE2 = 2;
	private ImageButton camera_car_in;
	private ImageButton next_page;
	private ImageButton back_page;
	private ImageView imageCarIn;
	private String upload_photo1_path;
	private String upload_photo2_path;
	private ExitCheckInfo myExitCheckInfo;
	
	private CreatePhoto createPhoto;
	private PictureUtils pictureUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.houjiawei_outstation_aty3);
		initId();
	    SetLinester();
	    ActivityCollector.addActivity(this);
	}
	private void SetLinester() {
		back_page.setOnClickListener(this);
		next_page.setOnClickListener(this);
		camera_car_in.setOnClickListener(this);
        imageCarIn.setOnClickListener(this);
	}
	private void initId() {
		pictureUtils=new PictureUtils();
		createPhoto = new CreatePhoto("exitCarIn", "出站图片");
		back_page=(ImageButton) findViewById(R.id.back_page);
		next_page=(ImageButton) findViewById(R.id.next_page);
		camera_car_in=(ImageButton) findViewById(R.id.camera_car_in);
		imageCarIn=(ImageView) findViewById(R.id.image_car_in);
		myExitCheckInfo=(ExitCheckInfo) getIntent().getSerializableExtra("myExitCheckInfo");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (requestCode==CAMERA_REQUEST_CODE2&& resultCode==RESULT_OK) {
			upload_photo2_path= createPhoto.getImagePath();
			if (upload_photo2_path==null) {
				return;
			}
			Uri uri2 = pictureUtils.compressImageFromFile(upload_photo2_path);
			if (uri2!=null) {
				imageCarIn.setImageURI(uri2);
			}else {
				imageCarIn.setImageResource(R.drawable.takeout_bg_progress);
			}
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_page:
			OutStationTakePhotoAty.this.finish();
			break;
		case R.id.next_page:
			if (TextUtils.isEmpty(upload_photo2_path)) {
				CustomToast.Custom_Toast(getApplicationContext(),"请拍车内照片！！",0,Toast.LENGTH_SHORT);
				return;
			}
			Intent intent=new Intent(getApplicationContext(),OutStationUploadDataAty.class);
			intent.putExtra("upload_photo1_path",upload_photo2_path);
			intent.putExtra("upload_photo2_path",upload_photo2_path);
			intent.putExtra("myExitCheckInfo", myExitCheckInfo);
			startActivity(intent);
			break;

		case R.id.camera_car_in:
			//拍照
			createPhoto.createFile();
			Uri uri = createPhoto.getFileUri();
			Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent2,CAMERA_REQUEST_CODE2);
			break;

		case R.id.image_car_in:
			//拍照
			createPhoto.createFile();
			Uri uri2 = createPhoto.getFileUri();
			Intent intent3=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent3.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
			startActivityForResult(intent3,CAMERA_REQUEST_CODE2);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.gc();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);

		createPhoto=null;
		pictureUtils=null;
		
		imageCarIn.setImageURI(null);
		imageCarIn.destroyDrawingCache();
		myExitCheckInfo=null;
		System.gc();
	}
}
