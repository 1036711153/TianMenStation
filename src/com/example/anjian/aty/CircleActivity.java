package com.example.anjian.aty;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.authentication.activity.AnjianSationHXUHFActivity;
import com.authentication.activity.R;
import com.authentication.entity.AutoCarNum;
import com.dyr.custom.CustomDialog;
import com.example.Utils.ASNItoChart;
import com.example.Utils.ControllerOpenCloseDoor;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseFindData;
import com.example.Utils.InputTools;
import com.example.Utils.T;
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionCarSummary;
import com.example.anjian.InspectionItem;
import com.example.anjian.LinshiInspectionCarDetail;
import com.example.configure.Configure;
import com.example.instation.InstationChooseActivity;
import com.example.instation.InstationJsonUser;
import com.example.instation.LoginActivity;
import com.example.outstation.finishAty.ActivityCollector;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.autocomplete.ArrayAdapter;
import com.zhy.view.CircleMenuLayout;
import com.zhy.view.CircleMenuLayout.OnMenuItemClickListener;

/**
 * <pre>
 * @author zhy 
 * http://blog.csdn.net/lmj623565791/article/details/43131133
 * </pre>
 */
public class CircleActivity extends Activity {
	private List<AutoCarNum> autoCarNums;
	private List<String> mCarNums = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private long currentTimeMillis1 = System.currentTimeMillis();
	private long currentTimeMillis2 = System.currentTimeMillis();
	private int PressCount = 0;
	private boolean isRepeat = true;
	private String chepaiString;
	private Button yijianhege;
	private AutoCompleteTextView input_carNum;
	private List<InspectionItem> inspectionItems;
	private ImageView myImageView;
	private ImageButton back;
	private int[] a = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };// 表示有多少长按或者单击后检查过项目，表示检查过项目的数目
	private int[] aa = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static CircleMenuLayout mCircleMenuLayout;
	private static String[] mItemTexts = new String[] { "外观 ", "制动系统", "转向系统",
			"传动系统", "照明信号指示灯", "轮胎", "悬挂系统", "安全设施", "摄像头" };
	private static int[] mItemImgs = new int[] {
			R.drawable.btn_waiguan_pressed, R.drawable.btn_zhidong_pressed,
			R.drawable.btn_zhuanxiang_pressed,
			R.drawable.btn_chuandong_pressed,
			R.drawable.btn_zhishideng_pressed, R.drawable.btn_luntai_pressed,
			R.drawable.btn_xuangua_pressed, R.drawable.btn_anquan_pressed,
			R.drawable.btn_shexiangtou_pressed };

	private static int[] mItemImgs_press = new int[] {
			R.drawable.btn_waiguan_b_pressed, R.drawable.btn_zhidong_b_pressed,
			R.drawable.btn_zhuanxiang_b_pressed,
			R.drawable.btn_chuandong_b_pressed,
			R.drawable.btn_zhishideng_b_pressed,
			R.drawable.btn_luntai_b_pressed, R.drawable.btn_xuangua_b_pressed,
			R.drawable.btn_anquan_b_pressed,
			R.drawable.btn_shexiangtou_b_pressed };

	private static int[] mItemImgs_Exps = new int[] {
			R.drawable.btn_waiguan_h_pressed, R.drawable.btn_zhidong_h_pressed,
			R.drawable.btn_zhuanxiang_h_pressed,
			R.drawable.btn_chuandong_h_pressed,
			R.drawable.btn_zhishideng_h_pressed,
			R.drawable.btn_luntai_h_pressed, R.drawable.btn_xuangua_h_pressed,
			R.drawable.btn_anquan_h_pressed,
			R.drawable.btn_shexiangtou_h_pressed };
	private List<String> mDatas = new ArrayList<String>();
	private ArrayAdapter<String> arrayAdapter;
	private InstationJsonUser jsonuser;
	private String[] mImagePath = new String[] { "", "", "", "", "", "", "",
			"", "" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 自已切换布局文件看效果
		setContentView(R.layout.anjian_activity_main02);
		initID();
		SQLiteDatabase db = Connector.getDatabase();
		setLisetener();
		input_carNum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputTools.HideKeyboard(view);
			}
		});
		ActivityCollector.addActivity(this);
	}

	private void setLisetener() {
		yijianhege.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<View> getmViews = mCircleMenuLayout.getmViews();
				for (int i = 0; i < getmViews.size(); i++) {
					((ImageView) getmViews.get(i))
							.setImageResource(mItemImgs_press[i]);
					a[i] = 1;
					aa[i] = 1;
				}
				// 一键合格后所有数据都变合格
				ContentValues values = new ContentValues();
				values.put("imageurl", "");
				values.put("conclusion", 1);
				values.put("defect_description", "");
				DataSupport.updateAll(LinshiInspectionCarDetail.class, values);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CircleActivity.this.finish();
			}
		});

		mCircleMenuLayout
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public void itemClick(View view, int position) {
						String imagePath = mImagePath[position];
						myImageView = (ImageView) view
								.findViewById(R.id.id_circle_menu_item_image);
						InspectionItem inspectionItem = inspectionItems
								.get(position);
						Intent intent = new Intent(CircleActivity.this,
								InspectItemAty.class);
						intent.putExtra("inspectionItem_id",
								inspectionItem.getId());
						intent.putExtra("pos", position);
						intent.putExtra("imagePath", imagePath);

						intent.putExtra("mItemImgs_pass_aty", position);
						a[position] = 1;
						startActivityForResult(intent, position);
					}

					// 提交按钮。。
					@Override
					public void itemCenterClick(final View view) {
						int result = 0;
						for (int i = 0; i < 9; i++) {
							result = a[i] + result;
						}

						if (result != 9) {
							CustomToast.Custom_Toast(CircleActivity.this,
									"请按程序检查完各个主项目之后再提交！！", 0,
									Toast.LENGTH_SHORT);
							return;
						}
						CustomDialog.Builder builder = new CustomDialog.Builder(
								CircleActivity.this);
						builder.setMessage("是否确定上传安检信息？");
						builder.setTitle("上传安检信息");
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										view.setEnabled(false);
										// 提交数据给服务器
										int conclusion = 1;// 1表示合格，0表示不合格
										List<LinshiInspectionCarDetail> linshiInspectionCarDetails = DataSupport
												.where("conclusion = ?", "0")
												.find(LinshiInspectionCarDetail.class);
										if (linshiInspectionCarDetails.size() != 0) {
											conclusion = 0;
										}
										InspectionCarSummary inspectionCarSummary = new InspectionCarSummary();
										List<LinshiInspectionCarDetail> mylinshiInspectionCarDetails = DataSupport
												.findAll(LinshiInspectionCarDetail.class);
										List<InspectionCarDetail> myinspectionCarDetails = new ArrayList<InspectionCarDetail>();
										for (int i = 0; i < mylinshiInspectionCarDetails
												.size(); i++) {
											LinshiInspectionCarDetail myLinshiInspectionCarDetail = mylinshiInspectionCarDetails
													.get(i);
											InspectionCarDetail myInspectionCarDetail = new InspectionCarDetail();
											myInspectionCarDetail
													.setConclusion(myLinshiInspectionCarDetail
															.getConclusion());
											myInspectionCarDetail
													.setDefect_Description(myLinshiInspectionCarDetail
															.getDefect_Description());
											myInspectionCarDetail
													.setZhuxiangmu_name(myLinshiInspectionCarDetail
															.getZhuxiangmu_name());
											myInspectionCarDetail
													.setImageURL(myLinshiInspectionCarDetail
															.getImageURL());
											myInspectionCarDetail
													.setInspectionSubitem_name(myLinshiInspectionCarDetail
															.getInspectionSubitem_name());
											myInspectionCarDetail.save();
											myinspectionCarDetails
													.add(myInspectionCarDetail);
										}
										Gson mGson = new Gson();
										String checkcontent = mGson
												.toJson(myinspectionCarDetails);
										inspectionCarSummary
												.setCheckcontent(checkcontent);
										inspectionCarSummary
												.setConclusion(conclusion);
										// 设置Vehicle车牌号
										if (TextUtils.isEmpty(chepaiString)) {
											chepaiString = input_carNum
													.getText().toString();
										}
										inspectionCarSummary
												.setVehicle_ID(chepaiString);
										inspectionCarSummary
												.setStationcode(Configure.Stationcode);
										SimpleDateFormat df = new SimpleDateFormat(
												"yyyy-MM-dd HH:mm:ss");// 设置日期格式
										inspectionCarSummary.setDateTime(df
												.format(new Date()));
										// 判断是不是第一次按下
										boolean condition4 = false;
										if (PressCount == 0) {
											condition4 = true;
											PressCount++;
										} else {
											condition4 = false;
										}

										// 用于处理上次按下和下次按下时间是不是在10秒之内，是就不保存
										if (isRepeat) {
											currentTimeMillis1 = System
													.currentTimeMillis();
											isRepeat = !isRepeat;
										} else {
											currentTimeMillis2 = System
													.currentTimeMillis();
											isRepeat = !isRepeat;
										}
										// 设置Inspector_ID
										String Watch_ID = jsonuser.getWatchID();
										inspectionCarSummary
												.setInspector_ID(Watch_ID);
										inspectionCarSummary.setCheckImg("");
										inspectionCarSummary
												.setInspectionCarDetails(myinspectionCarDetails);
										inspectionCarSummary
												.setImage1(mImagePath[0]);
										inspectionCarSummary
												.setImage2(mImagePath[1]);
										inspectionCarSummary
												.setImage3(mImagePath[2]);
										inspectionCarSummary
												.setImage4(mImagePath[3]);
										inspectionCarSummary
												.setImage5(mImagePath[4]);
										inspectionCarSummary
												.setImage6(mImagePath[5]);
										inspectionCarSummary
												.setImage7(mImagePath[6]);
										inspectionCarSummary
												.setImage8(mImagePath[7]);
										inspectionCarSummary
												.setImage9(mImagePath[8]);
										// 在保存之前判断是否多按了几次导致一条记录保存了多次
										// 仿抖操作，以免一次記錄存取多遍。。。若果是20秒内的操作就不保存。。
										int count = DataSupport
												.count(InspectionCarSummary.class);
										if (count != 0) {
											InspectionCarSummary Comparison_exitInfo = DataSupport
													.findLast(InspectionCarSummary.class);
											String compare_licenceplate = Comparison_exitInfo
													.getVehicle_ID();
											String compare_adultNum = Comparison_exitInfo
													.getConclusion() + "";
											boolean condition1 = !compare_licenceplate
													.equals(inspectionCarSummary
															.getVehicle_ID());
											boolean condition2 = !compare_adultNum
													.equals(inspectionCarSummary
															.getConclusion()
															+ "");
											boolean condition3 = Math
													.abs(currentTimeMillis1
															- currentTimeMillis2) > 10000;

											Log.e("condition1", condition1 + "");
											Log.e("condition2", condition2 + "");
											Log.e("condition3", condition3 + "");

											if (condition1 || condition2
													|| condition3 || condition4) {
												inspectionCarSummary.save();
											}
										} else {
											inspectionCarSummary.save();
										}
										Gson gson = new Gson();
										InspectionCarSummary lastCarSummary = DataSupport
												.findLast(InspectionCarSummary.class);
										List<InspectionCarSummary> mInspectionCarSummaries = new ArrayList<>();
										mInspectionCarSummaries
												.add(lastCarSummary);
										final String content = gson
												.toJson(mInspectionCarSummaries);

										AsyncHttpClient client1 = new AsyncHttpClient();
										RequestParams params1 = new RequestParams();

										List<LinshiInspectionCarDetail> filelInspectionCarDetails = DataSupport
												.findAll(LinshiInspectionCarDetail.class);

										// 上传安检子项目图片
										for (int i = 0; i < filelInspectionCarDetails
												.size(); i++) {
											LinshiInspectionCarDetail linshiInspectionCarDetail = filelInspectionCarDetails
													.get(i);
											String filename = linshiInspectionCarDetail
													.getImageURL();
											if (!TextUtils.isEmpty(filename)) {
												File file = new File(filename);
												try {
													params1.put("mimage" + i,
															file);
												} catch (FileNotFoundException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
										}
										// 将9个主项目图片封装好
										List<File> mFiles = new ArrayList<>();
										String image1 = lastCarSummary
												.getImage1();
										if (!TextUtils.isEmpty(image1)) {
											File file = new File(image1);
											mFiles.add(file);
										}
										String image2 = lastCarSummary
												.getImage2();
										if (!TextUtils.isEmpty(image2)) {
											File file = new File(image2);
											mFiles.add(file);
										}
										String image3 = lastCarSummary
												.getImage3();
										if (!TextUtils.isEmpty(image3)) {
											File file = new File(image3);
											mFiles.add(file);
										}
										String image4 = lastCarSummary
												.getImage4();
										if (!TextUtils.isEmpty(image4)) {
											File file = new File(image4);
											mFiles.add(file);
										}
										String image5 = lastCarSummary
												.getImage5();
										if (!TextUtils.isEmpty(image5)) {
											File file = new File(image5);
											mFiles.add(file);
										}
										String image6 = lastCarSummary
												.getImage6();
										if (!TextUtils.isEmpty(image6)) {
											File file = new File(image6);
											mFiles.add(file);
										}
										String image7 = lastCarSummary
												.getImage7();
										if (!TextUtils.isEmpty(image7)) {
											File file = new File(image7);
											mFiles.add(file);
										}
										String image8 = lastCarSummary
												.getImage8();
										if (!TextUtils.isEmpty(image8)) {
											File file = new File(image8);
											mFiles.add(file);
										}
										String image9 = lastCarSummary
												.getImage9();
										if (!TextUtils.isEmpty(image9)) {
											File file = new File(image9);
											mFiles.add(file);
										}

										if (mFiles.size() != 0) {
											for (int i = 0; i < mFiles.size(); i++) {
												try {
													params1.put("image" + i,
															mFiles.get(i));
												} catch (FileNotFoundException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
										}
										// 上传安检主项目图片
										client1.post(
												Configure.IP+"/TianMen/UploadFile.servlet"
												// "http://192.168.0.118:8080/TianMen1124/UploadFile.servlet"
												,
												params1,
												new FileAsyncHttpResponseHandler(
														CircleActivity.this) {

													@Override
													public void onSuccess(
															int arg0,
															Header[] arg1,
															File arg2) {
													}

													@Override
													public void onFailure(
															int arg0,
															Header[] arg1,
															Throwable arg2,
															File arg3) {
														CustomToast
																.UnComment_Custom_Toast(
																		CircleActivity.this,
																		"链接服务器失败!检查结果已保存在数据库中！！",
																		0,
																		Toast.LENGTH_SHORT);
													}
												});
										// 上传数据
										AsyncHttpClient client = new AsyncHttpClient();
										RequestParams params = new RequestParams();
										params.put("content", content);
										client.post(
												Configure.IP
														+ "/TianMen/PDAInfoAction!defaultMethod.action"
												// "http://192.168.0.118:8080/TianMen1124/PDAInfoAction!defaultMethod.action"
												, params,
												new AsyncHttpResponseHandler() {

													@Override
													public void onSuccess(
															int arg0,
															Header[] arg1,
															byte[] arg2) {
														CustomToast
																.Custom_Toast(
																		CircleActivity.this,
																		"上传数据成功!",
																		0,
																		Toast.LENGTH_SHORT);
//														InspectionCarSummary lastCarSummary = DataSupport
//																.findLast(InspectionCarSummary.class);
//														DataSupport
//																.delete(InspectionCarSummary.class,
//																		lastCarSummary
//																				.getId());
													}

													@Override
													public void onFailure(
															int arg0,
															Header[] arg1,
															byte[] arg2,
															Throwable arg3) {
														CustomToast
																.UnComment_Custom_Toast(
																		CircleActivity.this,
																		"链接服务器失败!检查结果已保存在数据库中！！",
																		0,
																		Toast.LENGTH_SHORT);
													}
												});

										ActivityCollector.finishAll();
										// 设置你的操作事项
										dialog.dismiss();
									 }
								});
						builder.setNegativeButton(
								"取消",
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										return;
									}
								});
						builder.create().show();

					}

					@Override
					public void itemLongClick(View view, int position) {
						InspectionItem inspectionItem = inspectionItems
								.get(position);
						if (aa[position] == 0) {
							aa[position] = 1;
							myImageView = (ImageView) view
									.findViewById(R.id.id_circle_menu_item_image);
							a[position] = 1;
							myImageView
									.setImageResource(mItemImgs_press[position]);
							// 将主项目中所有子项目设置为合格
							ContentValues values = new ContentValues();
							values.put("imageurl", "");
							values.put("conclusion", 1);
							values.put("defect_description", "");
							DataSupport.updateAll(
									LinshiInspectionCarDetail.class, values,
									"zhuxiangmu_name = ?",
									inspectionItem.getCname());
						} else {
							aa[position] = 0;
							myImageView = (ImageView) view
									.findViewById(R.id.id_circle_menu_item_image);
							a[position] = 0;
							myImageView.setImageResource(mItemImgs[position]);
							ContentValues values = new ContentValues();
							values.put("imageurl", "");
							values.put("conclusion", 1);
							values.put("defect_description", "");
							DataSupport.updateAll(
									LinshiInspectionCarDetail.class, values,
									"zhuxiangmu_name = ?",
									inspectionItem.getCname());
						}
					}
				});
	}

	private void initID() {
		// 登录的用户
		jsonuser = DataSupport.findLast(InstationJsonUser.class);
		// 一键合格
		yijianhege = (Button) findViewById(R.id.yijianhege);

		input_carNum = (AutoCompleteTextView) findViewById(R.id.input_carNum);
		if (getIntent() != null) {
			String chepai = getIntent().getStringExtra(
					Anjian_Take_Picture_Activity.CHEPAIHAO_KEY);
			if (chepai != null && !TextUtils.isEmpty(chepai)) {
				input_carNum.setEnabled(false);
				input_carNum.setText(chepai);
			}
			mImagePath[0] = getIntent().getStringExtra("image1_path");
			mImagePath[1] = getIntent().getStringExtra("image2_path");
			mImagePath[2] = getIntent().getStringExtra("image3_path");
			mImagePath[3] = getIntent().getStringExtra("image4_path");
		}

		back = (ImageButton) findViewById(R.id.back_page);
		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemImgs_press,
				mItemTexts);
		inspectionItems = DataSupport.findAll(InspectionItem.class, true);

		if (DataSupport.count(LinshiInspectionCarDetail.class) == 0) {
			for (int i = 0; i < inspectionItems.size(); i++) {
				InspectionItem inspectionItem = inspectionItems.get(i);
				for (int j = 0; j < inspectionItem.getInspectionSubitems()
						.size(); j++) {
					LinshiInspectionCarDetail inspectionCarDetail = new LinshiInspectionCarDetail();
					inspectionCarDetail
							.setInspectionSubitem_name(inspectionItem
									.getInspectionSubitems().get(j).getName());
					inspectionCarDetail.setZhuxiangmu_name(inspectionItems.get(
							i).getCname());
					inspectionCarDetail.setConclusion(1);
					inspectionCarDetail.setImageURL("");
					inspectionCarDetail.setDefect_Description("");
					inspectionCarDetail.save();
				}
			}
		} else {
			// 重置数据
			ContentValues values = new ContentValues();
			values.put("imageurl", "");
			values.put("conclusion", 1);
			values.put("defect_description", "");
			DataSupport.updateAll(LinshiInspectionCarDetail.class, values);
		}

		// 查找车牌号操作
		DataBaseFindData dataBaseFindData = new DataBaseFindData();
		autoCarNums = dataBaseFindData.getAutoCarNum();
		if (autoCarNums.size() != 0) {
			for (int i = 0; i < autoCarNums.size(); i++) {
				mCarNums.add(autoCarNums.get(i).getCarNum());
			}
		}

		// 模糊查询
		arrayAdapter = new ArrayAdapter<String>(CircleActivity.this,
				android.R.layout.simple_dropdown_item_1line, mCarNums);
		input_carNum.setThreshold(1);
		input_carNum.setAdapter(arrayAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		int conclusion = data.getIntExtra("conclusion", 0);
		if (conclusion == 1) {
			myImageView.setImageResource(mItemImgs_press[requestCode]);
		} else {
			myImageView.setImageResource(mItemImgs_Exps[requestCode]);
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		if (mCircleMenuLayout != null) {
			mCircleMenuLayout.destroyDrawingCache();
			mCircleMenuLayout.destroyLayout();
			List<View> getmViews = mCircleMenuLayout.getmViews();
			for (int i = 0; i < getmViews.size(); i++) {
				((ImageView) getmViews.get(i)).setImageResource(0);
				((ImageView) getmViews.get(i)).destroyDrawingCache();
				mCircleMenuLayout.getmViews().clear();
			}
			mCircleMenuLayout = null;
		}

		if (myImageView != null) {
			myImageView.setImageResource(0);
			myImageView.destroyDrawingCache();
			myImageView = null;
		}

		mImagePath = null;
		adapter = null;
		autoCarNums = null;
		input_carNum.setAdapter(null);
		input_carNum = null;
		jsonuser = null;
		System.gc();
	}

}
