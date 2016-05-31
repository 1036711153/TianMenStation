package com.example.anjian;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.content.Context;
import android.widget.Toast;

import com.example.Utils.CustomToast;
import com.example.anjian.synchroscope.Inspection_Synchroscope;
import com.example.configure.Configure;
import com.example.four_module.FourModuleAty;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

public class AnjianLab {
	public  void tongbu_anjian(final Context context) {
		String synchroscope = Inspection_Synchroscope.Synchroscope();
				//将三个表中数据全部删除:LinshiInspectionCarDetail,InspectionItem,InspectionSubitem这三个表数据.
				DataSupport.deleteAll(InspectionItem.class);
				DataSupport.deleteAll(InspectionSubitem.class);
				DataSupport.deleteAll(LinshiInspectionCarDetail.class);
				
				try {
					JSONObject object=new JSONObject(synchroscope);
					JSONArray myinspectionItems=object.getJSONArray("inspectionItems");
					List<InspectionItem>inspectionItems=new ArrayList<InspectionItem>();
					for (int i = 0; i < myinspectionItems.length(); i++) {
						JSONObject inspect=myinspectionItems.getJSONObject(i);
						String cname=inspect.getString("Cname");
						JSONArray myinspectionSubitems=inspect.getJSONArray("inspectionSubitems");
						List<InspectionSubitem>inspectionSubitems=new ArrayList<InspectionSubitem>();
						for (int j = 0; j < myinspectionSubitems.length(); j++) {
							JSONObject inspectSub=myinspectionSubitems.getJSONObject(j);
							String Name=inspectSub.getString("Name");
							String Sub_Description=inspectSub.getString("Description");
							String Method=inspectSub.getString("Method");
							String defect_description=inspectSub.getString("defect_descriptions");
							InspectionSubitem inspectionSubitem=new InspectionSubitem();
							inspectionSubitem.setName(Name);
							inspectionSubitem.setDescription(Sub_Description);
							inspectionSubitem.setMethod(Method);
							inspectionSubitem.setDefect_description(defect_description);
							inspectionSubitem.save();
							inspectionSubitems.add(inspectionSubitem);
						}
						InspectionItem inspectionItem=new InspectionItem();
						inspectionItem.setCname(cname);
						inspectionItem.setInspectionSubitems(inspectionSubitems);
						inspectionItem.save();
						inspectionItems.add(inspectionItem);
					}
					CustomToast.Custom_Toast(context,"同步安检表格数据成功！！",0,Toast.LENGTH_SHORT);
				} catch (Exception e) {
					e.printStackTrace();
					CustomToast.UnComment_Custom_Toast(context,"同步安检表格数据失败！！",0,Toast.LENGTH_SHORT);
				}
	}


}
