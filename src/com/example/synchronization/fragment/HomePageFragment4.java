package com.example.synchronization.fragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import com.authentication.activity.R;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseConvertText;
import com.example.configure.Configure;
import com.example.outstation.entity.ExitInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HomePageFragment4 extends Fragment{
	private MFragmentAdpter4 mAdapter;
	private List<ExitInfo> mExitInfos;
	private String content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mExitInfos=DataSupport.findAll(ExitInfo.class);
		Gson gson=new Gson();
		content=gson.toJson(mExitInfos);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.main_tab_outstation, container,false);
		ListView list_instation=(ListView) root.findViewById(R.id.list_instation);
		mAdapter=new MFragmentAdpter4(mExitInfos, getActivity());
		list_instation.setAdapter(mAdapter);
		Button btn_lianwang=(Button) root.findViewById(R.id.btn_lianwang);
		btn_lianwang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认上传出站所有信息?");
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
				
//				AlertDialog.Builder dialog = new AlertDialog.Builder(
//						getActivity());
//				dialog.setTitle("确认上传");
//				dialog.setMessage("是否确认上传出站所有信息");
//				dialog.setCancelable(false);
//				dialog.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								AsynHttpConnection();
//							}
//						});
//				dialog.setNegativeButton("取消",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						});
//				dialog.show();
			}
		});
		
		Button btn_wenben=(Button) root.findViewById(R.id.btn_wenben);
		btn_wenben.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认生成出站文本信息?");
				builder.setTitle("确认出站信息");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						try {
							DataBaseConvertText baseConvertText=new DataBaseConvertText(content, "OutStation","出站文本");
							baseConvertText.SaveText();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"生成文本失败！！",0,Toast.LENGTH_SHORT);
						}
						DataSupport.deleteAll(ExitInfo.class);
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
				
//				AlertDialog.Builder dialog = new AlertDialog.Builder(
//						getActivity());
//				dialog.setTitle("确认生成出站文本");
//				dialog.setMessage("是否确认生成出站文本信息");
//				dialog.setCancelable(false);
//				dialog.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								DataBaseConvertText baseConvertText=new DataBaseConvertText(content, "OutStation","出站文本");
//								baseConvertText.SaveText();
//								DataSupport.deleteAll(ExitInfo.class);
//								T.showShort(getActivity(), "生成文本成功！\n请去根目录下操作文件！");
//								getActivity().finish();
//							}
//						});
//				dialog.setNegativeButton("取消",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						});
//				dialog.show();
			}
		});
		
		return root;
	}
	
	

	private void AsynHttpConnection() {
		AsyncHttpClient client1=new AsyncHttpClient();
		RequestParams params1=new RequestParams();
		List<File> files=new ArrayList<File>();
		List<ExitInfo> filelInspectionCarDetails=DataSupport.findAll(ExitInfo.class);
		if (filelInspectionCarDetails.size()==0) {
			return;
		}
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	ExitInfo  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getInCarImaURL();
	    	String filename2=linshiInspectionCarDetail.getOutCarImaURL();
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
			client1.post(Configure.IP+"/TianMen/ExitUpFileAction.action", params1,new FileAsyncHttpResponseHandler(getActivity()) {
				
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
		params.put("gsonString", content);
		client.post(Configure.IP+"/TianMen/ExitCheckAction!saveExitInfo.action",params ,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CustomToast.Custom_Toast(getActivity(),"上传数据成功！",0,Toast.LENGTH_SHORT);
				DataSupport.deleteAll(ExitInfo.class);
				getActivity().finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(getActivity(),"上传数据成功失败\n请稍后再试！",0,Toast.LENGTH_SHORT);
			}
		});
	}

}
