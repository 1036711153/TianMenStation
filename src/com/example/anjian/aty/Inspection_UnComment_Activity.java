package com.example.anjian.aty;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.authentication.entity.AutoCarNum;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.anjian.InspectionCarSummary;
import com.example.configure.Configure;
import com.example.instation.InstationJsonUser;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.picture.compress.CreatePhoto;
import com.picture.compress.PictureUtils;
import com.zhang.autocomplete.ArrayAdapter;

public class Inspection_UnComment_Activity extends Activity implements OnClickListener{
	private List<String> mCarNums=new ArrayList<String>();
	private List<AutoCarNum> autoCarNums;
	private ImageButton back_page;
	private AutoCompleteTextView input_carNum;
	private ArrayAdapter<String> adapter;
	private String chepaiString;
	private Button saveButton;
	private Button resetButton;
	private ImageView  image;
	private Button take_photo;
	protected static final int CAMERA_REQUEST_CODE1 = 1;
	private String upload_photo1_path;
	private InstationJsonUser jsonuser;
	
    private CreatePhoto  createPhoto;
  	private PictureUtils pictureUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectionuncomment_activity);
		initData();
		initOnclick();
		ActivityCollector.addActivity(this);
	}
	private void initOnclick() {
		back_page.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		take_photo.setOnClickListener(this);
		image.setOnClickListener(this);
		input_carNum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputTools.HideKeyboard(view);
			}
		});
		
	}
	private void initData() {
		pictureUtils=new PictureUtils();
		createPhoto=new CreatePhoto("Uncomment_inspection", "配载车安检图片");
		
		jsonuser=DataSupport.findLast(InstationJsonUser.class);
		take_photo=(Button) findViewById(R.id.take_photo);
		back_page=(ImageButton) findViewById(R.id.back_page);
		resetButton = (Button) findViewById(R.id.reset);
		saveButton = (Button) findViewById(R.id.save);
		image=(ImageView) findViewById(R.id.image);
		input_carNum = (AutoCompleteTextView) findViewById(R.id.input_carNum);
		//查找车牌号操作
				DataBaseFindData dataBaseFindData=new DataBaseFindData();
			    autoCarNums= dataBaseFindData.getAutoCarNum();
				if (autoCarNums.size()!=0) {
					for (int i = 0; i < autoCarNums.size(); i++) {
						mCarNums.add(autoCarNums.get(i).getCarNum());
					}
				}
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, mCarNums);
				input_carNum.setThreshold(1);
				input_carNum.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image:
			createPhoto.createFile();
			Uri uri = createPhoto.getFileUri();
			Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent,CAMERA_REQUEST_CODE1);
			break;
		case R.id.back_page:
			this.finish();
			break;
			
		case R.id.save:
			//上传安检信息
			if (TextUtils.isEmpty(input_carNum.getText().toString())) {
				CustomToast.Custom_Toast(Inspection_UnComment_Activity.this,"请输入车牌号！！",0,Toast.LENGTH_SHORT);
				return;
			}
			
			if (TextUtils.isEmpty(upload_photo1_path)) {
				CustomToast.Custom_Toast(Inspection_UnComment_Activity.this,"请拍安检合格单！！",0,Toast.LENGTH_SHORT);
				return;
			}
			CustomDialog.Builder builder = new CustomDialog.Builder(Inspection_UnComment_Activity.this);
			builder.setMessage("上传配载车安检信息！！");
			builder.setTitle("是否上传信息");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//设置你的操作事项
					upload_uncommentInspection();
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
		case R.id.take_photo:
			//拍照
			createPhoto.createFile();
			Uri muri = createPhoto.getFileUri();
			Intent mintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mintent.putExtra(MediaStore.EXTRA_OUTPUT, muri);
			startActivityForResult(mintent,CAMERA_REQUEST_CODE1);
			break;
		case R.id.reset:
			input_carNum.setText("");
			image.setImageBitmap(null);
			break;
		default:
			break;
		}
		
	}
	
	//上传配载车安检信息
	private void upload_uncommentInspection() {
		
		InspectionCarSummary inspectionCarSummary=new InspectionCarSummary();
		//安检结果
		inspectionCarSummary.setConclusion(1);
		//车牌号
		inspectionCarSummary.setVehicle_ID(input_carNum.getText().toString());
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 设置日期格式
		inspectionCarSummary.setDateTime(df.format(new Date()));
		inspectionCarSummary.setCheckImg(upload_photo1_path);
		String Watch_ID=jsonuser.getWatchID();
		inspectionCarSummary.setInspector_ID(Watch_ID);
		inspectionCarSummary.setStationcode(Configure.Stationcode);
		inspectionCarSummary.save();
		AsyncHttpClient client1=new AsyncHttpClient();
		RequestParams params1=new RequestParams();
		Gson gson = new Gson();
		InspectionCarSummary lastCarSummary = DataSupport
				.findLast(InspectionCarSummary.class);
		List<InspectionCarSummary>mInspectionCarSummaries=new ArrayList<>();
		mInspectionCarSummaries.add(lastCarSummary);
		final String content = gson.toJson(mInspectionCarSummaries);
		params1.put("content", content);
		
		client1.post(
//				"http://192.168.0.118:8080/TianMen1124/PDAInfoAction!defaultMethod.action"
				Configure.IP+"/TianMen/PDAInfoAction!defaultMethod.action"
				, params1,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CustomToast.Custom_Toast(Inspection_UnComment_Activity.this,"上传数据成功!",0,Toast.LENGTH_SHORT);
				InspectionCarSummary lastCarSummary = DataSupport
						.findLast(InspectionCarSummary.class);
				DataSupport.delete(
						InspectionCarSummary.class,
						lastCarSummary.getId());
				AsyncHttpClient client=new AsyncHttpClient();
				RequestParams params=new RequestParams();
				File file = new File(upload_photo1_path);
				try {
					params.put("image1", file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("mmmm",Configure.IP);

				client.post(
						Configure.IP+"/TianMen/UploadFile.servlet", params,new FileAsyncHttpResponseHandler(Inspection_UnComment_Activity.this) {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, File arg2) {
						
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
						CustomToast.UnComment_Custom_Toast(Inspection_UnComment_Activity.this,"上传图片数据失败,数据已保存在PDA中!",0,Toast.LENGTH_SHORT);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(Inspection_UnComment_Activity.this,"上传数据失败,数据已保存在PDA中!",0,Toast.LENGTH_SHORT);
			}
		});
		ActivityCollector.finishAll();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==CAMERA_REQUEST_CODE1&& resultCode==RESULT_OK) {
			upload_photo1_path= createPhoto.getImagePath();
			if (upload_photo1_path==null) {
				return;
			}
			Uri uri2 = pictureUtils.compressBigImageFromFile(upload_photo1_path);
			if (uri2!=null) {
				image.setImageURI(uri2);
		    }else {
				image.setImageResource(R.drawable.takeout_bg_progress);
		  }
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		createPhoto=null;
		pictureUtils=null;
		image.setImageURI(null);
		ActivityCollector.removeActivity(this);
	}

}
