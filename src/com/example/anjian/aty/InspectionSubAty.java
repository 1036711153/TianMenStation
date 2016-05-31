package com.example.anjian.aty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.litepal.crud.DataSupport;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.example.Utils.CustomToast;
import com.example.anjian.Defect_description;
import com.example.anjian.LinshiInspectionCarDetail;
import com.example.anjian.SumDefect_description;
import com.example.anjian.adpter.MyPopListViewAdapter;
import com.example.anjian.adpter.MyPopListViewAdapter.OnMyPopItemClickListener;
import com.google.gson.Gson;
import com.picture.compress.CreatePhoto;
import com.picture.compress.PictureUtils;
import com.squareup.picasso.Picasso;

public class InspectionSubAty extends Activity {
	protected static final int CAMERA_REQUEST_CODE1 = 1;
	private List<Defect_description> defect_descriptions = new ArrayList<Defect_description>();
	private List<Defect_description> seclct_defect_descriptions = new ArrayList<Defect_description>();
	private ImageButton imageButton;
	private ImageButton back_page;
	private ImageView imageView;
	private Button reserButton;
	private Button saveButton;
	private ListView listView;
	private String anjian_photo;
	MyPopListViewAdapter adapter;
	String InspectionSubitem_name;
	
	private CreatePhoto createPhoto ;
	private PictureUtils pictureUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.anjian_inspectsub_main);
		initId();
		adapter = new MyPopListViewAdapter(this, defect_descriptions);
		listView.setAdapter(adapter);
		back_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InspectionSubAty.this.finish();
			}
		});
		adapter.setMyOnItemClickListener(new OnMyPopItemClickListener() {

			@Override
			public void onPopItemClick(View view, int position) {
				CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.checkBox1);
				if (checkBox.isChecked()) {
					checkBox.setChecked(false);
					seclct_defect_descriptions.remove(defect_descriptions
							.get(position));
				} else {
					checkBox.setChecked(true);
					seclct_defect_descriptions.add(defect_descriptions
							.get(position));
				}
			}
		});
		reserButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				seclct_defect_descriptions.clear();
				listView.setAdapter(adapter);
				imageView.setImageBitmap(null);
				ContentValues values = new ContentValues();
				values.put("defect_description", "");
				values.put("imageurl", "");
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values,
						"inspectionsubitem_name = ?", InspectionSubitem_name);
			}
		});
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createPhoto.createFile();
				Uri uri = createPhoto.getFileUri();
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent,CAMERA_REQUEST_CODE1);
			}
		});
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (anjian_photo == null) {
					CustomToast.Custom_Toast(InspectionSubAty.this,"请拍照后再点保存!",0,Toast.LENGTH_SHORT);
					return;
				}
				if (seclct_defect_descriptions.size() == 0) {
					CustomToast.Custom_Toast(InspectionSubAty.this,"请选择缺陷描述!",0,Toast.LENGTH_SHORT);
					return;
				}
				// 保存數據--存數據到數據庫中
				SumDefect_description sumDefect_description = new SumDefect_description(
						seclct_defect_descriptions);
				Gson gson = new Gson();
				String mysumDefect_description = gson
						.toJson(sumDefect_description);
				ContentValues values = new ContentValues();
				values.put("defect_description", mysumDefect_description);
				values.put("imageurl", anjian_photo);
				values.put("conclusion", 0);
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values,
						"inspectionsubitem_name = ?", InspectionSubitem_name);
				Intent intent = getIntent();
				intent.putExtra("conclusion", 1);
				InspectionSubAty.this.setResult(1, intent);
				InspectionSubAty.this.finish();
			}
		});
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//拍照
				createPhoto.createFile();
				Uri uri = createPhoto.getFileUri();
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent,CAMERA_REQUEST_CODE1);
			}
		});
	}

	private void initId() {
		pictureUtils=new PictureUtils();
		createPhoto = new CreatePhoto("Security_Check_Photo", "安检图片");
		back_page = (ImageButton) findViewById(R.id.back_page);
		listView = (ListView) findViewById(R.id.Listview);
		imageButton = (ImageButton) findViewById(R.id.camera_button);
		imageView = (ImageView) findViewById(R.id.image);
		reserButton = (Button) findViewById(R.id.reset);
		saveButton = (Button) findViewById(R.id.save);
		if (getIntent()!=null) {
			String descriptions = getIntent().getStringExtra("descriptions");
			Toast_PoPwindow(descriptions);
			InspectionSubitem_name = getIntent().getStringExtra(
					"InspectionSubitem_name");
		}
	}

	private void Toast_PoPwindow(String jsondata) {
		try {
			JSONArray array = new JSONArray(jsondata);
			for (int i = 0; i < array.length(); i++) {
				String Description = array.getJSONObject(i).getString(
						"Description");
				defect_descriptions.add(new Defect_description(Description));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE1 && resultCode==RESULT_OK) {
			    anjian_photo= createPhoto.getImagePath();
			    if (anjian_photo==null) {
					return;
				}
				Uri uri2 = pictureUtils.compressImageFromFile(anjian_photo);
				if (uri2!=null) {
					imageView.setImageURI(uri2);
//					Picasso.with(InspectionSubAty.this).load(new File(anjian_photo)).into(imageView);
				}else {
					imageView.setImageResource(R.drawable.takeout_bg_progress);
				}
		      }
	       }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		pictureUtils=null;
		createPhoto=null;
		imageView.setImageURI(null);
		imageView.destroyDrawingCache();
	}
}
