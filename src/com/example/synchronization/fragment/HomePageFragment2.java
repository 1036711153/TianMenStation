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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.authentication.activity.R;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.DataBaseConvertText;
import com.example.anjian.InspectionCarDetail;
import com.example.anjian.InspectionCarSummary;
import com.example.configure.Configure;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomePageFragment2 extends Fragment{
	private MFragmentAdpter2 mAdapter;
	private List<InspectionCarSummary>mInspectionCarSummaries=new ArrayList<>();
	private String content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInspectionCarSummaries=DataSupport.findAll(InspectionCarSummary.class);
		Gson gson=new Gson();
		content=gson.toJson(mInspectionCarSummaries);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.main_tab_anjiantb, container,false);
		ListView list_instation=(ListView) root.findViewById(R.id.list_instation);
		mAdapter=new MFragmentAdpter2(mInspectionCarSummaries, getActivity());
		list_instation.setAdapter(mAdapter);
		Button btn_lianwang=(Button) root.findViewById(R.id.btn_lianwang);
		btn_lianwang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认上传安检信息?");
				builder.setTitle("确认上传");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								AsynHttpConnection();
							}
						}).start();
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
				builder.setMessage("是否确认生成安检文本信息?");
				builder.setTitle("确认安检信息");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						try {
							DataBaseConvertText baseConvertText=new DataBaseConvertText(content, "Security_check","安检文本");
							baseConvertText.SaveText();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"生成文本失败！！",0,Toast.LENGTH_SHORT);
						}
						DataSupport.deleteAll(InspectionCarSummary.class);
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
		if(mInspectionCarSummaries.size()==0){
            CustomToast.Custom_Toast(getActivity(),"无安检数据！",0,Toast.LENGTH_SHORT);
			return;
		}
		
		List<File> files=new ArrayList<File>();
		//将配载车安检图片添加到files文件集合中
		for (int i = 0; i < mInspectionCarSummaries.size(); i++) {
			String filepath=mInspectionCarSummaries.get(i).getCheckImg();
			if (!TextUtils.isEmpty(filepath)) {
				files.add(new File(filepath));
			}
			//主项目四张图片
	    	 String image1path=mInspectionCarSummaries.get(i).getImage1();
			 if (!TextUtils.isEmpty(image1path)) {
					files.add(new File(image1path));
				}
			 String image2path=mInspectionCarSummaries.get(i).getImage2();
			 if (!TextUtils.isEmpty(image2path)) {
					files.add(new File(image2path));
				}
			 String image3path=mInspectionCarSummaries.get(i).getImage3();
			 if (!TextUtils.isEmpty(image3path)) {
					files.add(new File(image3path));
				}
			 String image4path=mInspectionCarSummaries.get(i).getImage4();
			 if (!TextUtils.isEmpty(image4path)) {
					files.add(new File(image4path));
				}
		}
		
		
		int count=DataSupport.count(InspectionCarDetail.class);
		//如果安检明细表不为空则。。。上传安检明细信息
		if (count!=0) {
		List<InspectionCarDetail> filelInspectionCarDetails=DataSupport.findAll(InspectionCarDetail.class);
	    for (int i=0;i<filelInspectionCarDetails.size();i++) {
	    	InspectionCarDetail  linshiInspectionCarDetail=filelInspectionCarDetails.get(i);
	    	String filename1=linshiInspectionCarDetail.getImageURL();
	    	if (!TextUtils.isEmpty(filename1)) {
	    		files.add(new File(filename1));
			}
		  }
		}
		
	  int filesize=files.size();
	  if (filesize!=0) {
			  //上传配载车安检结果
			   for (int i=0;i<files.size();i++) {
				   try {
						params1.put("image"+i, files.get(i));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
	     }
		
	//最后将所有的图片打包上传
     client1.post(Configure.IP+"/TianMen/UploadFile.servlet", params1,new FileAsyncHttpResponseHandler(getActivity()) {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// TODO Auto-generated method stub
			}
		});
		
		
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("content", content);
		client.post(Configure.IP+"/TianMen/PDAInfoAction!defaultMethod.action",params ,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CustomToast.Custom_Toast(getActivity(),"上传数据成功！",0,Toast.LENGTH_SHORT);
//				DataSupport.deleteAll(InspectionCarSummary.class);
				getActivity().finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				CustomToast.UnComment_Custom_Toast(getActivity(),"上传数据失败\n请稍后再试！",0,Toast.LENGTH_SHORT);
			}
		});
	}

}
