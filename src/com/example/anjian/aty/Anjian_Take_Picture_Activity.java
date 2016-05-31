package com.example.anjian.aty;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.itcast.picture.TakePictureActivity;

import com.authentication.activity.AnjianSationHXUHFActivity;
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

public class Anjian_Take_Picture_Activity extends Activity {
	protected static final int CAMERA_REQUEST_CODE1 = 1;
	protected static final int CAMERA_REQUEST_CODE2 = 2;
	protected static final int CAMERA_REQUEST_CODE3 = 3;
	protected static final int CAMERA_REQUEST_CODE4 = 4;
	private ImageButton nextButton, backButton;
	private int a[]=new int[]{
			0,0,0,0
	};
	
	private ImageView car_body, tire, safety_hammer, fire_extinguisher;
	private String image1_path="";
	private String image2_path="";
	private String image3_path="";
	private String image4_path="";

	private CreatePhoto createPhoto1;
	private CreatePhoto createPhoto2;
	private CreatePhoto createPhoto3;
	private CreatePhoto createPhoto4;

	private PictureUtils  pictureUtils;

	File createFile1;
	File createFile2;
	File createFile3;
	File createFile4;

	private String chepaiString = null;
	private AutoCompleteTextView input_carNum;
	private List<AutoCarNum> autoCarNums;
	private ArrayAdapter<String> adapter;
	private List<String> mCarNums = new ArrayList<String>();
	public static final String CHEPAIHAO_KEY = "chepaihao_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anjian_take_picture);
		input_carNum = (AutoCompleteTextView) findViewById(R.id.input_carNum);
		if (getIntent() != null) {
			String RFID = getIntent().getStringExtra(
					AnjianSationHXUHFActivity.RFID_KEY);
			if (RFID != null && !TextUtils.isEmpty(RFID)) {
				ASNItoChart asnItoChart = new ASNItoChart(RFID);
				chepaiString = asnItoChart.getChepai();
				input_carNum.setEnabled(false);
				input_carNum.setText(chepaiString);
			}
		}
		// 查找车牌号操作
		DataBaseFindData dataBaseFindData = new DataBaseFindData();
		autoCarNums = dataBaseFindData.getAutoCarNum();
		if (autoCarNums.size() != 0) {
			for (int i = 0; i < autoCarNums.size(); i++) {
				mCarNums.add(autoCarNums.get(i).getCarNum());
			}
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mCarNums);
		input_carNum.setThreshold(1);
		input_carNum.setAdapter(adapter);

		// 返回下一步按钮
		backButton = (ImageButton) findViewById(R.id.back_page);
		nextButton = (ImageButton) findViewById(R.id.next_page);
		// 4张照片
		car_body = (ImageView) findViewById(R.id.car_body);
		tire = (ImageView) findViewById(R.id.tire);
		safety_hammer = (ImageView) findViewById(R.id.safety_hammer);
		fire_extinguisher = (ImageView) findViewById(R.id.fire_extinguisher);
		initPicture();
		setListener();
		ActivityCollector.addActivity(this);
	}
	private void initPicture() {
		// TODO Auto-generated method stub
		 pictureUtils=new PictureUtils();
		 
		 createPhoto1= new CreatePhoto("car_body","InspectionMianProject");
		 createPhoto2= new CreatePhoto("tire","InspectionMianProject");
		 createPhoto3= new CreatePhoto("safety_hammer","InspectionMianProject");
		 createPhoto4= new CreatePhoto("fire_extinguisher","InspectionMianProject");
		
		 createFile1 = createPhoto1.createFile();
		 createFile2 = createPhoto2.createFile();
		 createFile3 = createPhoto3.createFile();
		 createFile4 = createPhoto4.createFile();
		 
		 image1_path=createPhoto1.getImagePath();
		 image2_path=createPhoto2.getImagePath();
		 image3_path=createPhoto3.getImagePath();
		 image4_path=createPhoto4.getImagePath();
	}

	private void setListener() {
		NEXTATY();// 下一步
		CAMERA_TAKE1();// 拍车身
		CAMERA_TAKE2();// 拍轮胎
		CAMERA_TAKE3();// 拍安全锤
		CAMERA_TAKE4();// 拍灭火器
		input_carNum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputTools.HideKeyboard(view);
			}
		});
	}

	private void NEXTATY() {

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Anjian_Take_Picture_Activity.this.finish();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (a[0]==0||a[1]==0||a[2]==0||a[3]==0) {
					CustomToast.Custom_Toast(Anjian_Take_Picture_Activity.this,
							"请拍4张安检要求照片", 0, Toast.LENGTH_SHORT);
					return;
				}else {
				Intent intent = new Intent(Anjian_Take_Picture_Activity.this,
						CircleActivity.class);
				intent.putExtra(CHEPAIHAO_KEY, input_carNum.getText()
						.toString());
				intent.putExtra("image1_path", image1_path);
				intent.putExtra("image2_path", image2_path);
				intent.putExtra("image3_path", image3_path);
				intent.putExtra("image4_path", image4_path);
				startActivity(intent);
				}
			}
		});
	}

	private void CAMERA_TAKE1() {
		car_body.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				Uri uri1 = Uri.fromFile(createFile1);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
				startActivityForResult(intent, CAMERA_REQUEST_CODE1);
			}
		});
	}

	private void CAMERA_TAKE2() {
		tire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				Uri uri2 = Uri.fromFile(createFile2);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
				startActivityForResult(intent, CAMERA_REQUEST_CODE2);
			}
		});
	}

	private void CAMERA_TAKE3() {
		safety_hammer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				Uri uri3 = Uri.fromFile(createFile3);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri3);
				startActivityForResult(intent, CAMERA_REQUEST_CODE3);
			}
		});
	}

	private void CAMERA_TAKE4() {
		fire_extinguisher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				Uri uri4 = Uri.fromFile(createFile4);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri4);
				startActivityForResult(intent, CAMERA_REQUEST_CODE4);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE1 && resultCode == RESULT_OK) {
			if (image1_path==null) {
				return;
			}
			Uri uri1 = pictureUtils.compressImageFromFile(image1_path);
			if (uri1 != null&&image1_path!=null) {
				//加载图片
				car_body.setImageURI(uri1);
				a[0]=1;
			} else {
				car_body.setImageResource(R.drawable.takeout_bg_progress);
			}
		} else if (requestCode == CAMERA_REQUEST_CODE2
				&& resultCode == RESULT_OK) {
			if (image2_path==null) {
				return;
			}
			Uri uri2 = pictureUtils.compressImageFromFile(image2_path);
			if (uri2 != null&&image2_path!=null) {
				tire.setImageURI(uri2);
				a[1]=1;
			} else {
				tire.setImageResource(R.drawable.takeout_bg_progress);
			}
		} else if (requestCode == CAMERA_REQUEST_CODE3
				&& resultCode == RESULT_OK) {
			if (image3_path==null) {
				return;
			}
			Uri uri3 = pictureUtils.compressImageFromFile(image3_path);
			if (uri3 != null&&image3_path!=null) {
				safety_hammer.setImageURI(uri3);
				a[2]=1;
			} else {
				safety_hammer.setImageResource(R.drawable.takeout_bg_progress);
			}
		} else if (requestCode == CAMERA_REQUEST_CODE4
				&& resultCode == RESULT_OK) {
			if (image4_path==null) {
				return;
			}
			Uri uri4 = pictureUtils.compressImageFromFile(image4_path);
			if (uri4 != null&&image4_path!=null) {
				fire_extinguisher.setImageURI(uri4);
				a[3]=1;
			} else {
				fire_extinguisher
						.setImageResource(R.drawable.takeout_bg_progress);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);

		createPhoto1=null;
		createPhoto2=null;
		createPhoto3=null;
		createPhoto4=null;
		createFile1=null;
		createFile2=null;
		createFile3=null;
		createFile4=null;
		
		
		
		adapter=null;	
		autoCarNums=null;
		input_carNum.setAdapter(null);
		input_carNum=null;
		
		car_body.setImageURI(null);
		car_body.destroyDrawingCache();
		tire.setImageURI(null);
		tire.destroyDrawingCache();
		safety_hammer.setImageURI(null);
		safety_hammer.destroyDrawingCache();
		fire_extinguisher.setImageURI(null);
		fire_extinguisher.destroyDrawingCache();
		
		System.gc(); 
	}
}
