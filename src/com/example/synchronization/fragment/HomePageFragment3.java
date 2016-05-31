package com.example.synchronization.fragment;


import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.Toast;
import com.authentication.activity.R;
import com.authentication.entity.CarNumLab;
import com.authentication.entity.Register_Finger_Lab;
import com.dyr.custom.CustomDialog;
import com.example.Utils.CustomToast;
import com.example.Utils.ExitCheckInfoJson;
import com.example.Utils.ReadText;
import com.example.anjian.AnjianLab;
import com.example.configure.Configure;
import com.example.outstation.entity.ExitCheckInfo;
import com.google.gson.Gson;

public class HomePageFragment3 extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.main_tab_anjian, container,false);
		root.findViewById(R.id.xiazaibg).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认同步表格?");
				builder.setTitle("确认同步");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//设置你的操作事项
						try {
							AnjianLab anjianLab =new AnjianLab();
							anjianLab.tongbu_anjian(getActivity());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"同步安检表格数据失败！！",0,Toast.LENGTH_SHORT);
						}
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
		
		
      root.findViewById(R.id.car_datas).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认同步表格?");
				builder.setTitle("确认同步");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							CarNumLab carNumLab =new CarNumLab(null, Configure.IP+"/TianMen/ExitCheckAction!getAllLicencePlate.action", getActivity());
							carNumLab.AsyncHttp();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"同步车牌号数据失败！！",0,Toast.LENGTH_SHORT);
						}
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
      
      
      root.findViewById(R.id.person_dates).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认同步表格?");
				builder.setTitle("确认同步");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							Register_Finger_Lab register_Finger_Lab =new Register_Finger_Lab(null, Configure.IP+"/TianMen/ExitCheckAction!getSimpleDriverInfo.action", getActivity());
							register_Finger_Lab.AsyncHttp();
						} catch (Exception e) {
							e.printStackTrace();
							CustomToast.UnComment_Custom_Toast(getActivity(),"同步驾驶员数据失败！",0,Toast.LENGTH_SHORT);
						}
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
      
      root.findViewById(R.id.finger_tongbu).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
				builder.setMessage("是否确认导入报班数据?");
				builder.setTitle("确认导入");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//报班的文本导入信息
						String content=ReadText.SaveText("baoban", "test");
						JsonParser(content);
						
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
	
	
	private void JsonParser(String jsonData) {
		try {
			Log.e("baoban", jsonData);
			String cname=null;
			try {
				JSONObject object=new JSONObject(jsonData);
				String flag=object.getString("flag");
				if (!flag.equals("true")) {
					return;
				}
				cname=object.getString("list");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Gson gson=new Gson();
			ExitCheckInfoJson	exitCheckInfoJson=gson.fromJson(cname, ExitCheckInfoJson.class);
		    saveExitCheckInfo(exitCheckInfoJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void saveExitCheckInfo(ExitCheckInfoJson exitCheckInfoJson){
        if (exitCheckInfoJson==null) {
			return;
		}
        String licenceplate= exitCheckInfoJson.getLicencePlate();
        int count=DataSupport.count(ExitCheckInfo.class);
        if (count!=0) {
        	if (TextUtils.isEmpty(licenceplate)) {
				return;
			}
        	 ExitCheckInfo lastExitCheckInfo=DataSupport.findLast(ExitCheckInfo.class);
        	 if (licenceplate.equals(lastExitCheckInfo.getLicencePlate())) {
        		 //这条信息已经保存过了
				return;
		    	}
		      }
		    	ExitCheckInfo exitCheckInfo=new ExitCheckInfo();
		    	exitCheckInfo.setAccurateLoadNum(exitCheckInfoJson.getAccurateLoadNum());
		    	exitCheckInfo.setClassReportStatus(exitCheckInfoJson.getClassReportStatus());
		    	exitCheckInfo.setVehicleId(exitCheckInfoJson.getVehicleId());
		    	exitCheckInfo.setVehicleType(exitCheckInfoJson.getVehicleType());
		    	exitCheckInfo.setInspectionStatus(exitCheckInfoJson.getInspectionStatus());
		    	exitCheckInfo.setLicencePlate(exitCheckInfoJson.getLicencePlate());
		    	exitCheckInfo.setFirPhoto(exitCheckInfoJson.getFirPhoto());
		    	exitCheckInfo.setFirAdultNum(exitCheckInfoJson.getFirAdultNum());
		    	exitCheckInfo.setFirDriverId(exitCheckInfoJson.getFirDriverId());
		    	exitCheckInfo.setFirFingerCode1(exitCheckInfoJson.getFirFingerCode1());
		    	exitCheckInfo.setFirFingerCode2(exitCheckInfoJson.getFirFingerCode2());
		    	exitCheckInfo.setFirFingerCode3(exitCheckInfoJson.getFirFingerCode3());
		    	exitCheckInfo.setFirFingerCode4(exitCheckInfoJson.getFirFingerCode4());
		    	exitCheckInfo.setFirIdCard(exitCheckInfoJson.getFirIdCard());
		    	exitCheckInfo.setFirName(exitCheckInfoJson.getFirName());
		    	exitCheckInfo.setFirTel(exitCheckInfoJson.getFirTel());
		    	exitCheckInfo.setSecPhoto(exitCheckInfoJson.getSecPhoto());
		    	exitCheckInfo.setSecAdultNum(exitCheckInfoJson.getSecAdultNum());
		    	exitCheckInfo.setSecDriverId(exitCheckInfoJson.getSecDriverId());
		    	exitCheckInfo.setSecFingerCode1(exitCheckInfoJson.getSecFingerCode1());
		    	exitCheckInfo.setSecFingerCode2(exitCheckInfoJson.getSecFingerCode2());
		    	exitCheckInfo.setSecFingerCode3(exitCheckInfoJson.getSecFingerCode3());
		    	exitCheckInfo.setSecFingerCode4(exitCheckInfoJson.getSecFingerCode4());
		    	exitCheckInfo.setSecIdCard(exitCheckInfoJson.getSecIdCard());
		    	exitCheckInfo.setSecName(exitCheckInfoJson.getSecName());
		    	exitCheckInfo.setSecTel(exitCheckInfoJson.getSecTel());
		    	exitCheckInfo.save();
				CustomToast.UnComment_Custom_Toast(getActivity(),"导入报班信息成功！",0,Toast.LENGTH_SHORT);
		    	
	}
	

}
