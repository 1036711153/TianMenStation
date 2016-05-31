package com.example.parameter;


import it.sauronsoftware.base64.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.authentication.entity.Register_Finger_DataBase;
import com.example.Utils.AsyncHttpUtils;
import com.example.configure.Configure;
import com.google.gson.Gson;

public class WriteDataActivity extends Activity{
	private AutoCompleteTextView nameAtc;
	private TextView ID_CARD;
	private TextView drive_id;
	private Button submit;
	private Button reset;
    private  List<Register_Finger_DataBase> register_Finger_DataBases=new ArrayList<Register_Finger_DataBase>();
	private List<String> Driver_names=new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private Register_Finger_DataBase mfingerDriver;
	private String base64String;
	private WriteDateAdpter mAdpter;
	private ImageView drive_image;
	private Button back_page;
	
	private List<Register_Finger_DataBase> mListView_register_Finger_DataBases;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_data);
		SQLiteDatabase db = Connector.getDatabase();
		initData();
//		try {
////			CreatePhoto createPhoto=new CreatePhoto("ImageView","ConvertByteToImageView");
////			File createFile = createPhoto.createFile();
////			Log.e("base64String", base64String);
////			byte[]  byteIcon=base64String.getBytes("UTF-8");
////			byte[] decode = Base64.decode(byteIcon);
////			Log.e("decode.length", decode.length+"");
////			Bitmap image = BitmapFactory.decodeByteArray(decode, 0, decode.length);
////			drive_image.setImageBitmap(image);
////			saveImage(decode);
////			writeToFile(decode);
////			Bitmap mbimap = ByteConvertImage.Bytes2Bimap(decode);
////			PictureUtils.SaveByteBitmap(mbimap, createPhoto.getImagePath());
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
		
		nameAtc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//有BUG
				String name=nameAtc.getText().toString();
				mListView_register_Finger_DataBases=
						DataSupport.where("name = ?",name).find(Register_Finger_DataBase.class);
				mAdpter=new WriteDateAdpter(mListView_register_Finger_DataBases, WriteDataActivity.this);
//				mListView.setAdapter(mAdpter);
			}
		});
		
		
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				mfingerDriver = mListView_register_Finger_DataBases.get(position);
//				ID_CARD.setText(mfingerDriver.getId_card());
//			    drive_id.setText(mfingerDriver.getDriver_id());
//			   }
//		});
		
		
		
		
	
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (ID_CARD.getText().toString().equals("")||drive_id.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "请填写个人信息后提交注册", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!nameAtc.getText().toString().equals(mfingerDriver.getName())) {
					Toast.makeText(getApplicationContext(), "驾驶员姓名出错！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				AlertDialog.Builder dialog2 = new AlertDialog.Builder(
						WriteDataActivity.this);
				dialog2.setTitle("确认上传指纹");
				dialog2.setMessage("是否确认上传指纹信息？");
				dialog2.setCancelable(false);
				dialog2.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								ContentValues values=new ContentValues();
								values.put("pageid", base64String);
								DataSupport.update(Register_Finger_DataBase.class, values, mfingerDriver.getId());
								Register_Finger_DataBase register_Finger_DataBase=DataSupport.find(Register_Finger_DataBase.class, mfingerDriver.getId());
								List<Register_Finger_DataBase> register_Finger_DataBases =new ArrayList<>();
								register_Finger_DataBases.add(register_Finger_DataBase);
								Gson gson=new Gson();
								String content = gson.toJson(register_Finger_DataBases);
								try {
									AsyncHttpUtils asyncHttpUtils=new AsyncHttpUtils(content, Configure.IP+"/TianMen/SaveCodeAction!registerFingerCode.action", WriteDataActivity.this);
									asyncHttpUtils.AsyncHttp();
									WriteDataActivity.this.finish();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				dialog2.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				dialog2.show();
			}
		});
		
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog2 = new AlertDialog.Builder(
						WriteDataActivity.this);
				dialog2.setTitle("确认重置");
				dialog2.setMessage("是否确认重置信息");
				dialog2.setCancelable(false);
				dialog2.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								nameAtc.setText("");
								ID_CARD.setText("");
								drive_id.setText("");
							}
						});
				dialog2.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				dialog2.show();
			}
		});
		
		back_page.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WriteDataActivity.this.finish();
			}
		});
	}
	
	
	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + "fingerprint_image";
	
	private void saveImage(byte[] data) {
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdir();
		}

		File file = new File(path + File.separator + System.currentTimeMillis()
				+ ".bmp");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	private void writeToFile(byte[] data) {
		String dir = rootPath + File.separator + "fingerprint_image";
		File dirPath = new File(dir);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}

		String filePath = dir + "/" + System.currentTimeMillis() + ".bmp";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initData() {
		drive_image=(ImageView) findViewById(R.id.drive_list);
		back_page=(Button) findViewById(R.id.back_page);
		reset=(Button) findViewById(R.id.reset);
		submit=(Button) findViewById(R.id.submit);
		ID_CARD=(TextView) findViewById(R.id.ID_Card);
		drive_id=(TextView) findViewById(R.id.Drive_Vehicle);
		nameAtc=(AutoCompleteTextView) findViewById(R.id.name);
		base64String=getIntent().getStringExtra("ID");
		compeletDriverData();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Driver_names);
		nameAtc.setAdapter(adapter);
	}

	private void compeletDriverData() {
	    register_Finger_DataBases=DataSupport.findAll(Register_Finger_DataBase.class);
		for (Register_Finger_DataBase register_Finger_DataBase : register_Finger_DataBases) {
			Driver_names.add(register_Finger_DataBase.getName());
		}
	}
}
