package com.example.synchronization.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseConvertText;
import com.example.configure.Configure;
import com.example.instation.EntranceInfo;
import com.example.instation.UnconventionInstation;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class HomePageFragment1 extends Fragment{
	private MFragmentAdpter mAdapter;
	private List<EntranceInfo> mEntranceInfos;
//	private List<UnconventionInstation> mUnconventionInstations;
//	private List<DriveAndDates> mDriveAndDates=new ArrayList<>();
	private String content;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mEntranceInfos=DataSupport.findAll(EntranceInfo.class);
//		mUnconventionInstations=DataSupport.findAll(UnconventionInstation.class);
//		if (mEntranceInfos.size()!=0) {
//			for (int i = 0; i < mEntranceInfos.size(); i++) {
//				mDriveAndDates.add(new DriveAndDates(mEntranceInfos.get(i).getLicnence(), mEntranceInfos.get(i).getTime()));
//			}
//		}
//		if (mUnconventionInstations.size()!=0){
//			for (int i = 0; i < mEntranceInfos.size(); i++) {
//				mDriveAndDates.add(new DriveAndDates(mUnconventionInstations.get(i).getLicencePlate(), mUnconventionInstations.get(i).getTime()));
//			}
//		}
		Gson gson=new Gson();
		content=gson.toJson(mEntranceInfos);
	}
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.main_tab_instation, container,false);
		ListView list_instation=(ListView) root.findViewById(R.id.list_instation);
		mAdapter=new MFragmentAdpter(mEntranceInfos, getActivity());
		list_instation.setAdapter(mAdapter);
		Button btn_lianwang=(Button) root.findViewById(R.id.btn_lianwang);
		btn_lianwang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认上传进站信息?");
				builder.setTitle("确认上传");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						AsynHttpConnection();
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
			}
		});
		
		Button btn_wenben=(Button) root.findViewById(R.id.btn_wenben);
		btn_wenben.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认生成进站文本信息?");
				builder.setTitle("确认进站信息");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						try {
							DataBaseConvertText baseConvertText=new DataBaseConvertText(content, "Instation","进站文本");
							baseConvertText.SaveText();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"生成文本失败！！",0,Toast.LENGTH_SHORT);
						}
						DataSupport.deleteAll(EntranceInfo.class);
						CustomToast.Custom_Toast(getActivity(),"生成文本成功！\n请去根目录下操作文件！",0,Toast.LENGTH_SHORT);
						getActivity().finish();
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
			}
		});
		
		
		return root;
	}
	
	
	private void AsynHttpConnection() {
		AsyncHttpClient client1=new AsyncHttpClient();
		RequestParams params1=new RequestParams();
		
		
		List<File> files=new ArrayList<File>();
		List<EntranceInfo> filelInspectionCarDetails=DataSupport.findAll(EntranceInfo.class);
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	EntranceInfo  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getUpload_photo1_path();
	    	String filename2=linshiInspectionCarDetail.getUpload_photo2_path();
	    	if (!TextUtils.isEmpty(filename1)&&!TextUtils.isEmpty(filename2)) {
	    		Log.e("aaa", filename1);
	    		File file1 = new File(filename1);
	    		File file2 = new File(filename2);
	    		try {
					params1.put("image1", file1);
					params1.put("image2", file2);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			client1.post(Configure.IP+"/TianMen/EntranceUpFileAction.action", params1,new FileAsyncHttpResponseHandler(getActivity()) {
				@Override
				public void onSuccess(int arg0, Header[] arg1, File arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(Configure.IP+"/TianMen/PDAEntranceAction!defaultMethod.action",params ,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CustomToast.Custom_Toast(getActivity(),"上传数据成功！",0,Toast.LENGTH_SHORT);
				DataSupport.deleteAll(EntranceInfo.class);
				getActivity().finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(getActivity(),"上传数据失败\n请稍后再试！",0,Toast.LENGTH_SHORT);
			}
		});
	}
	

	
	
	
	

}
