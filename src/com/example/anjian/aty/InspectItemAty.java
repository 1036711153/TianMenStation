package com.example.anjian.aty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import com.authentication.activity.R;
import com.example.Utils.CustomToast;
import com.example.Utils.SaveImage;
import com.example.anjian.Defect_description;
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionItem;
import com.example.anjian.InspectionSubitem;
import com.example.anjian.LinshiInspectionCarDetail;
import com.example.anjian.SumDefect_description;
import com.example.anjian.SumLinshiInspectionCarDetail;
import com.example.anjian.adpter.MyListViewAdapter;
import com.example.anjian.adpter.MyPopListViewAdapter;
import com.example.anjian.adpter.MyListViewAdapter.OnMyItemClickListener;
import com.example.anjian.adpter.MyPopListViewAdapter.OnMyPopItemClickListener;
import com.google.gson.Gson;
import com.picture.compress.CreatePhoto;
import com.picture.compress.PictureUtils;
import com.squareup.picasso.Picasso;

import android.R.interpolator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class InspectItemAty extends Activity {
	private static final int REQUEST_CODE = 1;
	private static final int CAMERA_REQUEST_CODE = 2;
	private ListView mListView;
	private MyListViewAdapter mAdapter;
	private List<InspectionSubitem> inspectionSubitems = new ArrayList<InspectionSubitem>();
	private Button saveButton;
	private Button resetButton;
	private ImageButton back_page;

	private View parentView;
	private PopupWindow popupWindow;
	private InspectionItem inspectionItem;
	private View conterview;
	private Button btn_take_photo;
	private String mMain_Project_Photo_Path="";
	private List<Defect_description> defect_descriptions;
	private List<Defect_description> seclct_defect_descriptions = new ArrayList<Defect_description>();
	private int pos;
	private ImageView inspec_image;
	private int[] mItemImgs_pass_aty = new int[] { 
			R.drawable.wgpic,
			R.drawable.zdxtpic, 
			R.drawable.zxxtpic, 
			R.drawable.cdxtpic,
			R.drawable.zmxtpic, 
			R.drawable.ltpic, 
			R.drawable.xgxt,
			R.drawable.aqsspic,
			R.drawable.sxtpic 
			};
	
	private int[] mTitle_image=new int[]{
			R.drawable.titlewg,
			R.drawable.titlezdxt, 
			R.drawable.titlezxxt, 
			R.drawable.titlecdxt,
			R.drawable.titlezmxt, 
			R.drawable.titlelt, 
			R.drawable.titlexgfs,
			R.drawable.titleaqss,
			R.drawable.titlesxt 
	};
	
	
	private String imagePath;
	private ImageView mtitle_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.anjian_inspectitem_main);
		initID();
		mAdapter = new MyListViewAdapter(InspectItemAty.this,
				inspectionSubitems);
		mListView.setAdapter(mAdapter);
		
		inspec_image.setImageResource(mItemImgs_pass_aty[pos]);
		//如果拍了就显示拍照的照片...
		
		mtitle_image.setImageResource(mTitle_image[pos]);
		setListener();
	}

	private void setListener() {
		//已经屏蔽掉这个功能了 
//		btn_take_photo.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//拍照
//				CreatePhoto createPhoto=new CreatePhoto("InspectionMianProject", "main_checks");
//				Uri uri = createPhoto.getFileUri();
//				mMain_Project_Photo_Path= createPhoto.getImagePath();
//				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//				startActivityForResult(intent,CAMERA_REQUEST_CODE);
//			}
//		});
		
		
		back_page.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InspectItemAty.this.finish();
			}
		});

		mAdapter.setMyOnItemClickListener(new OnMyItemClickListener() {

			@Override
			public void onhegeClick(View view, int position) {
				backgroundAlpha(0.2f);
				InspectionSubitem inspectionSubitem = inspectionSubitems
						.get(position);
				int id = inspectionSubitem.getId();
				Log.e("TAG", inspectionSubitem.getDefect_description());
				conterview = getLayoutInflater().inflate(
						R.layout.anjian_pop_main, null);
				popupWindow = new PopupWindow(conterview,
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setOnDismissListener(new poponDismissListener());
				Toast_PoPwindow(inspectionSubitem.getDefect_description(),
						position);
				ContentValues values = new ContentValues();
				values.put("conclusion", "1");
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values,
						"inspectionsubitem_name = ?",
						inspectionSubitems.get(position).getName());
			}

			@Override
			public void onbuhegeClick(View view, int position) {
				InspectionSubitem inspectionSubitem = inspectionSubitems
						.get(position);
				String descriptions = inspectionSubitem.getDefect_description();
				Intent intent = new Intent(InspectItemAty.this,
						InspectionSubAty.class);
				intent.putExtra("descriptions", descriptions);
				intent.putExtra("InspectionSubitem_name",
						inspectionSubitem.getName());
				startActivityForResult(intent, REQUEST_CODE);
			}

			@Override
			public void onTextItemClick(View view, int position) {
				CustomToast.Custom_Toast(InspectItemAty.this,inspectionSubitems.get(position).getDescription(),0,Toast.LENGTH_SHORT);
			}

			@Override
			public void onTextLongItemClick(View view, int position) {
				CustomToast.Custom_Toast(InspectItemAty.this,inspectionSubitems.get(position).getMethod(),0,Toast.LENGTH_SHORT);
			}
		});

		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListView.setAdapter(mAdapter);
				ContentValues values = new ContentValues();
				values.put("imageurl", "");
				values.put("conclusion", 1);
				values.put("defect_description", "");
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values,
						"zhuxiangmu_name = ?", inspectionItem.getCname());
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<LinshiInspectionCarDetail> linshiInspectionCarDetails = DataSupport
						.where("conclusion= ? and zhuxiangmu_name = ?", "0",
								inspectionItem.getCname()).find(
								LinshiInspectionCarDetail.class);
				int count = linshiInspectionCarDetails.size();
				int conclusion = 0;
				if (count == 0) {
					conclusion = 1;
				}
				Intent intent = getIntent();
				intent.putExtra("conclusion", conclusion);
				
				if (!TextUtils.isEmpty(imagePath)) {
					mMain_Project_Photo_Path=imagePath;
				}
				intent.putExtra("mMain_Project_Photo_Path", mMain_Project_Photo_Path);
				InspectItemAty.this.setResult(1, intent);
				InspectItemAty.this.finish();
			}
		});
	}

	private void initID() {
		mtitle_image=(ImageView) findViewById(R.id.mtitle_image);
		btn_take_photo=(Button) findViewById(R.id.btn_take_photo);
		back_page = (ImageButton) findViewById(R.id.back_page);
		inspec_image = (ImageView) findViewById(R.id.inspect_image);
		parentView = findViewById(R.id.main);
		mListView = (ListView) findViewById(R.id.listView1);
		resetButton = (Button) findViewById(R.id.reset);
		saveButton = (Button) findViewById(R.id.save);
		pos = getIntent().getIntExtra("pos", 0);
		imagePath=getIntent().getStringExtra("imagePath");
		Log.e("pos", pos + "");
		int inspectionItem_id = getIntent().getIntExtra("inspectionItem_id", 0);
		inspectionItem = DataSupport.find(InspectionItem.class,
				inspectionItem_id, true);
		inspectionSubitems = inspectionItem.getInspectionSubitems();
	}

	private void Toast_PoPwindow(String jsondata, final int position) {
		popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);

		final ListView popListView = (ListView) conterview
				.findViewById(R.id.pop_Listview);
		defect_descriptions = new ArrayList<Defect_description>();
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
		final MyPopListViewAdapter myPopListViewAdapter = new MyPopListViewAdapter(
				InspectItemAty.this, defect_descriptions);
		popListView.setAdapter(myPopListViewAdapter);
		seclct_defect_descriptions.clear();
		myPopListViewAdapter
				.setMyOnItemClickListener(new OnMyPopItemClickListener() {

					@Override
					public void onPopItemClick(View view, int position) {
						CheckBox checkBox = (CheckBox) view
								.findViewById(R.id.checkBox1);
						if (checkBox.isChecked()) {
							checkBox.setChecked(false);
							seclct_defect_descriptions
									.remove(defect_descriptions.get(position));
						} else {
							checkBox.setChecked(true);
							seclct_defect_descriptions.add(defect_descriptions
									.get(position));
						}
					}
				});
		conterview.findViewById(R.id.pop_save).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						backgroundAlpha(1f);
						// 保存数据到表中,并且关闭pop窗口
						SumDefect_description sumDefect_description = new SumDefect_description(
								seclct_defect_descriptions);
						Gson gson = new Gson();
						String mysumDefect_description = gson
								.toJson(sumDefect_description);
						ContentValues values = new ContentValues();
						values.put("defect_description",
								mysumDefect_description);
						values.put("conclusion", "1");
						DataSupport.updateAll(LinshiInspectionCarDetail.class,
								values, "inspectionsubitem_name = ?",
								inspectionSubitems.get(position).getName());
						popupWindow.dismiss();
					}
				});
		conterview.findViewById(R.id.pop_reset).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {// 重置數據列表
						popListView.setAdapter(myPopListViewAdapter);
						seclct_defect_descriptions.clear();
						ContentValues values = new ContentValues();
						values.put("defect_description", "");
						DataSupport.updateAll(LinshiInspectionCarDetail.class,
								values, "inspectionsubitem_name = ?",
								inspectionSubitems.get(position).getName());
					}
				});
	}

	/**
	 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * 
	 * @author cg
	 * 
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_CODE && resultCode == 1) {
			if (data == null) {
				return;
			}
			int conclusion = data.getIntExtra("conclusion", 0);
			if (conclusion == 1) {
				mAdapter.sethegeAndbuhegeButton();
			}
		}
		else if (requestCode==CAMERA_REQUEST_CODE) {
			//显示拍摄的照片
//			Uri uri2 = PictureUtils.compressImageFromFile(mMain_Project_Photo_Path);
//			inspec_image.setImageURI(uri2); 
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		popupWindow=null;
		inspectionSubitems=null;
		mListView=null;
		parentView=null;
		conterview=null;
		
		mItemImgs_pass_aty=null;
		seclct_defect_descriptions=null;
		mTitle_image=null;
		
		inspec_image.destroyDrawingCache();
		System.gc(); 
	}

}
