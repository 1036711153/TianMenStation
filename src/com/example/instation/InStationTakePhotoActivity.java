package com.example.instation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.authentication.activity.InSationHXUHFActivity;
import com.authentication.activity.R;
import com.authentication.entity.AutoCarNum;
import com.example.Utils.ASNItoChart;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.outstation.finishAty.ActivityCollector;
import com.picture.compress.CreatePhoto;
import com.picture.compress.PictureUtils;
import com.squareup.picasso.Picasso;
import com.zhang.autocomplete.ArrayAdapter;

public class InStationTakePhotoActivity extends Activity{
	protected static final int CAMERA_REQUEST_CODE2 = 2;
	private ImageButton nextButton,backButton;
	private ImageButton camera_car_in;
	private ImageView imageCarIn;
	private String upload_photo1_path;
	private String upload_photo2_path;
	private String chepaiString=null;
	private AutoCompleteTextView input_carNum;
	private List<AutoCarNum> autoCarNums;
	private ArrayAdapter<String> adapter;
	private List<String> mCarNums=new ArrayList<String>();
	public static final String CHEPAIHAO_KEY="chepaihao_key";
	
	private CreatePhoto createPhoto;
	private PictureUtils pictureUtils;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.instationact2);
			createPhoto = new CreatePhoto("exitCarIn", "进站图片");
			pictureUtils=new PictureUtils();
			
			input_carNum=(AutoCompleteTextView) findViewById(R.id.input_carNum);
			if (getIntent()!=null) {
				String RFID= getIntent().getStringExtra(InSationHXUHFActivity.RFID_KEY);
				if (RFID!=null&&!TextUtils.isEmpty(RFID)) {
					ASNItoChart asnItoChart=new ASNItoChart(RFID);
					chepaiString=asnItoChart.getChepai();
					Log.e("InstationAty2", chepaiString);
					input_carNum.setEnabled(false);
					input_carNum.setText(chepaiString);
				}
			}
			
			//查找车牌号操作
			DataBaseFindData dataBaseFindData=new DataBaseFindData();
		    autoCarNums= dataBaseFindData.getAutoCarNum();
			if (autoCarNums.size()!=0) {
				for (int i = 0; i < autoCarNums.size(); i++) {
					mCarNums.add(autoCarNums.get(i).getCarNum());
				}
			}
			adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mCarNums);
			input_carNum.setThreshold(1);
			input_carNum.setAdapter(adapter);
			
			
			
			
			backButton=(ImageButton) findViewById(R.id.back_page);
			nextButton=(ImageButton) findViewById(R.id.next_page);
			camera_car_in=(ImageButton) findViewById(R.id.camera_car_in);
			imageCarIn=(ImageView) findViewById(R.id.image_car_in);
			
			setListener();
			ActivityCollector.addActivity(this);
		}
		
		


		private void setListener() {
            NEXTATY();//下一步
            CAMERA_CAR();//拍车身
            CAREMA_CAR_IN();//拍车内
            
            input_carNum.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					InputTools.HideKeyboard(view);
				}
			});
            
//            imageCar.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					//拍照
//					createPhoto.createFile();
//					Uri uri = createPhoto.getFileUri();
//					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//					startActivityForResult(intent,CAMERA_REQUEST_CODE1);
//				}
//			});
            imageCarIn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					createPhoto.createFile();
					Uri uri = createPhoto.getFileUri();
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent,CAMERA_REQUEST_CODE2);
				}
			});
            
            
	}
		private void CAREMA_CAR_IN() {
			camera_car_in.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//拍照
					createPhoto.createFile();
					Uri uri = createPhoto.getFileUri();
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent,CAMERA_REQUEST_CODE2);
				}
			});
		}
		private void NEXTATY() {
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InStationTakePhotoActivity.this.finish();
				
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(upload_photo1_path)||TextUtils.isEmpty(upload_photo2_path)) {
					CustomToast.Custom_Toast(InStationTakePhotoActivity.this,"请拍车身和车内照片",0,Toast.LENGTH_SHORT);
					return;
				}
				Intent intent=new Intent(InStationTakePhotoActivity.this,InStationUploadDataActivity.class);
				if (chepaiString==null) {
					chepaiString="";
				}
				intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText().toString());
				intent.putExtra("upload_photo1_path",upload_photo1_path);
				intent.putExtra("upload_photo2_path",upload_photo2_path);
				startActivity(intent);
			}
		});
		}
		private void CAMERA_CAR(){
//			camera_car.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//拍照
//					createPhoto.createFile();
//					Uri uri = createPhoto.getFileUri();
//					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//					startActivityForResult(intent,CAMERA_REQUEST_CODE1);
//				}
//			});
		}
	
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//			if (requestCode==CAMERA_REQUEST_CODE1&& resultCode==RESULT_OK) {
//				upload_photo1_path= createPhoto.getImagePath();
//				if (upload_photo1_path==null) {
//					return;
//				}
//				Uri uri2 = pictureUtils.compressImageFromFile(upload_photo1_path);
//				if (uri2!=null) {
//					imageCar.setImageURI(uri2);
//				}else {
//					imageCar.setImageResource(R.drawable.takeout_bg_progress);
//				}
//			  }
//			
//			else 
				if (requestCode==CAMERA_REQUEST_CODE2&& resultCode==RESULT_OK) {
				upload_photo1_path= createPhoto.getImagePath();
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
		protected void onDestroy() {
			super.onDestroy();
			ActivityCollector.removeActivity(this);
			createPhoto=null;
			pictureUtils=null;
			
			adapter=null;	
			autoCarNums=null;
			input_carNum.setAdapter(null);
			input_carNum=null;
			
//			imageCar.setImageURI(null);
//			imageCar.destroyDrawingCache();
			
			imageCarIn.setImageURI(null);
			imageCarIn.destroyDrawingCache();
		}
}
