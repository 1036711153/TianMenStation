package com.example.anjian.synchroscope;

import java.util.ArrayList;
import java.util.List;

import com.example.anjian.synchroscope.Anjian;
import com.example.anjian.synchroscope.Defect_description;
import com.example.anjian.synchroscope.InspectionItem;
import com.example.anjian.synchroscope.InspectionSubitem;

import com.google.gson.Gson;

public class Inspection_Synchroscope {
	
	
	public static String Synchroscope() {
		
		List<Defect_description>defect_descriptions1=new ArrayList<Defect_description>();
		defect_descriptions1.add(new Defect_description("有开裂明显锈蚀及变形"));
		defect_descriptions1.add(new Defect_description("车内不整洁"));
		
		List<Defect_description>defect_descriptions2=new ArrayList<Defect_description>();
		defect_descriptions2.add(new Defect_description("损毁"));
		
		List<Defect_description>defect_descriptions3=new ArrayList<Defect_description>();
		defect_descriptions3.add(new Defect_description("各档位不能正常工作"));
		
		List<Defect_description>defect_descriptions4=new ArrayList<Defect_description>();
		defect_descriptions4.add(new Defect_description("漏水"));
		defect_descriptions4.add(new Defect_description("漏油"));
		
		List<InspectionSubitem> inspectionSubitems1=new ArrayList<InspectionSubitem>();
		inspectionSubitems1.add(new InspectionSubitem("外观及车内环境", "整洁、周正、无开裂明显锈蚀及变形；车内整洁无杂物", "目视检查", defect_descriptions1));
		inspectionSubitems1.add(new InspectionSubitem("后视镜", "完好、无损毁", "目视检查", defect_descriptions2));
		inspectionSubitems1.add(new InspectionSubitem("雨刮器", "各档位能够正常工作", "目视检查", defect_descriptions3));
		inspectionSubitems1.add(new InspectionSubitem("发动机、水箱", "无漏水、漏油现象", "目视检查", defect_descriptions4));
		
		
		List<Defect_description>defect_descriptions5=new ArrayList<Defect_description>();
		defect_descriptions5.add(new Defect_description("不能正确指示"));
		
		List<Defect_description>defect_descriptions6=new ArrayList<Defect_description>();
		defect_descriptions6.add(new Defect_description("有漏气声"));
		
		List<Defect_description>defect_descriptions7=new ArrayList<Defect_description>();
		defect_descriptions7.add(new Defect_description("有报警"));
		
		List<Defect_description>defect_descriptions8=new ArrayList<Defect_description>();
		defect_descriptions8.add(new Defect_description("有龟裂、油污"));
		defect_descriptions8.add(new Defect_description("异常磨损，松紧不适度"));
		
		List<InspectionSubitem> inspectionSubitems2=new ArrayList<InspectionSubitem>();
		inspectionSubitems2.add(new InspectionSubitem("气压表", "能够正确指示", "目视检查", defect_descriptions5));
		inspectionSubitems2.add(new InspectionSubitem("制动管路密封性", "踩下制动踏板，各部位无漏气声", "目视检查", defect_descriptions6));
		inspectionSubitems2.add(new InspectionSubitem("制动系统自检", "打开发动机开关，查看故障指示灯，无报警", "目视检查", defect_descriptions7));
		inspectionSubitems2.add(new InspectionSubitem("空气压缩机传动带", "无龟裂、油污和异常磨损，松紧适度", "目视检查,指压检查", defect_descriptions8));
		
		
		
		List<Defect_description>defect_descriptions9=new ArrayList<Defect_description>();
		defect_descriptions9.add(new Defect_description("锁销缺损，连接不可靠"));
		
		List<Defect_description>defect_descriptions10=new ArrayList<Defect_description>();
		defect_descriptions10.add(new Defect_description("松旷、开裂"));
		
		List<Defect_description>defect_descriptions11=new ArrayList<Defect_description>();
		defect_descriptions11.add(new Defect_description("变形、裂纹"));
		
		
		List<InspectionSubitem> inspectionSubitems3=new ArrayList<InspectionSubitem>();
		inspectionSubitems3.add(new InspectionSubitem("左右转向盘", "锁销齐全紧固，连接可靠", "目视检查,敲击检查", defect_descriptions9));
		inspectionSubitems3.add(new InspectionSubitem("球销总成", "无松旷、开裂", "目视检查,敲击检查", defect_descriptions10));
		inspectionSubitems3.add(new InspectionSubitem("横直拉杆", "无变形无裂纹", "目视检查,敲击检查", defect_descriptions11));
		
		
		List<Defect_description>defect_descriptions12=new ArrayList<Defect_description>();
		defect_descriptions12.add(new Defect_description("支架破损、变形"));
		defect_descriptions12.add(new Defect_description("中间轴承松旷"));
		
		List<Defect_description>defect_descriptions13=new ArrayList<Defect_description>();
		defect_descriptions13.add(new Defect_description("泄漏"));
		
		List<Defect_description>defect_descriptions14=new ArrayList<Defect_description>();
		defect_descriptions14.add(new Defect_description("泄漏"));
		
		List<InspectionSubitem> inspectionSubitems4=new ArrayList<InspectionSubitem>();
		inspectionSubitems4.add(new InspectionSubitem("传动机构及连接", "支架无破损、变形，万向节、中间轴承无松旷", "目视检查,晃动检查", defect_descriptions12));
		inspectionSubitems4.add(new InspectionSubitem("自动变速密封性箱", "无泄漏", "目视检查", defect_descriptions13));
		inspectionSubitems4.add(new InspectionSubitem("液力缓速器", "无泄漏", "目视检查", defect_descriptions14));
		
		List<Defect_description>defect_descriptions15=new ArrayList<Defect_description>();
		defect_descriptions15.add(new Defect_description("缺损，不干净，有松脱"));
		defect_descriptions15.add(new Defect_description("远近光转换异常"));
		
		List<Defect_description>defect_descriptions16=new ArrayList<Defect_description>();
		defect_descriptions16.add(new Defect_description("缺损，不干净"));
		defect_descriptions16.add(new Defect_description("工作异常"));
		
		List<InspectionSubitem> inspectionSubitems5=new ArrayList<InspectionSubitem>();
		inspectionSubitems5.add(new InspectionSubitem("前照灯", "齐全、完好、表面清洁，无松脱；远近光转换正常。", "目视检查", defect_descriptions15));
		inspectionSubitems5.add(new InspectionSubitem("信号指示灯", "转向灯、雾灯、示廓灯、报警灯，齐全完好，表面清洁，各信号指示灯工作正常", "目视检查", defect_descriptions16));
		
		
		List<Defect_description>defect_descriptions17=new ArrayList<Defect_description>();
		defect_descriptions17.add(new Defect_description("深度不合适"));
		defect_descriptions17.add(new Defect_description("长度超过25mm"));
		defect_descriptions17.add(new Defect_description("轮胎间有异物嵌入"));
		
		List<Defect_description>defect_descriptions18=new ArrayList<Defect_description>();
		defect_descriptions18.add(new Defect_description("转向轮大于3.2mm"));
		defect_descriptions18.add(new Defect_description("其余轮胎大于1.6mm"));
		
		List<Defect_description>defect_descriptions19=new ArrayList<Defect_description>();
		defect_descriptions19.add(new Defect_description("不一致"));
		
		List<Defect_description>defect_descriptions20=new ArrayList<Defect_description>();
		defect_descriptions20.add(new Defect_description("不符合要求"));
		
		List<Defect_description>defect_descriptions21=new ArrayList<Defect_description>();
		defect_descriptions21.add(new Defect_description("缺损"));
		defect_descriptions21.add(new Defect_description("不紧固可靠"));
		
		List<InspectionSubitem> inspectionSubitems6=new ArrayList<InspectionSubitem>();
		inspectionSubitems6.add(new InspectionSubitem("外观", "不得有长度超过25mm或深度足以暴露布帘层 的破裂、割伤及突起、异物刺入；轮胎间无异物嵌入", "目视检查", defect_descriptions17));
		inspectionSubitems6.add(new InspectionSubitem("花纹深度", "转向轮不小于3.2mm，其余轮胎不小于1.6mm。", "目视检查必要时采用花纹深度尺。", defect_descriptions18));
		inspectionSubitems6.add(new InspectionSubitem("规格及其花纹", "同轴两侧规格及花纹一致。", "目视检查", defect_descriptions19));
		inspectionSubitems6.add(new InspectionSubitem("轮胎气压", "气压符合要求", "敲击检查必要时采用气压表", defect_descriptions20));
		inspectionSubitems6.add(new InspectionSubitem("轮胎及半轴螺丝螺母", "齐全、完好，紧固可靠。", "目视检查,敲击检查", defect_descriptions21));
		
		
		List<Defect_description>defect_descriptions22=new ArrayList<Defect_description>();
		defect_descriptions22.add(new Defect_description("裂纹、变形"));
		defect_descriptions22.add(new Defect_description("空气簧有泄漏"));
		
		List<Defect_description>defect_descriptions23=new ArrayList<Defect_description>();
		defect_descriptions23.add(new Defect_description("断裂、松旷"));
		
		List<InspectionSubitem> inspectionSubitems7=new ArrayList<InspectionSubitem>();
		inspectionSubitems7.add(new InspectionSubitem("弹性元件", "安装牢固，无裂纹、塑形变形，空气簧无泄漏。", "目视检查", defect_descriptions22));
		inspectionSubitems7.add(new InspectionSubitem("钢板弹簧", "u型螺栓螺母。吊耳销、锁销等齐全紧固，无断裂和松旷", "目视检查,敲击检查", defect_descriptions23));
		
		
		List<Defect_description>defect_descriptions24=new ArrayList<Defect_description>();
		defect_descriptions24.add(new Defect_description("缺损"));
		
		List<Defect_description>defect_descriptions25=new ArrayList<Defect_description>();
		defect_descriptions25.add(new Defect_description("缺损"));
		
		List<Defect_description>defect_descriptions26=new ArrayList<Defect_description>();
		defect_descriptions26.add(new Defect_description("缺损"));
		defect_descriptions26.add(new Defect_description("未在规定位置放置"));
		
		List<Defect_description>defect_descriptions27=new ArrayList<Defect_description>();
		defect_descriptions27.add(new Defect_description("无效"));
		defect_descriptions27.add(new Defect_description("未随车配备"));
		
		List<InspectionSubitem> inspectionSubitems8=new ArrayList<InspectionSubitem>();
		inspectionSubitems8.add(new InspectionSubitem("应急开关", "标示、机件完好，有疑问时进行启闭操作", "目视检查", defect_descriptions24));
		inspectionSubitems8.add(new InspectionSubitem("安全顶窗", "标示、机件完好，有疑问时进行启闭操作", "目视检查", defect_descriptions25));
		inspectionSubitems8.add(new InspectionSubitem("安全锤", "齐全并在规定位置放置", "目视检查", defect_descriptions26));
		inspectionSubitems8.add(new InspectionSubitem("灭火器", "有效并随车配备", "目视检查", defect_descriptions27));
		
		
		List<Defect_description>defect_descriptions28=new ArrayList<Defect_description>();
		defect_descriptions28.add(new Defect_description("方位不符规定"));
		defect_descriptions28.add(new Defect_description("有遮挡"));
		
		List<InspectionSubitem> inspectionSubitems9=new ArrayList<InspectionSubitem>();
		inspectionSubitems9.add(new InspectionSubitem("摄像头", "拍摄方位符合规定且无遮挡", "目视检查", defect_descriptions28));
		
		List<InspectionItem>inspectionItems=new ArrayList<InspectionItem>();
		inspectionItems.add(new InspectionItem("外观",inspectionSubitems1));
		inspectionItems.add(new InspectionItem("制动系统",inspectionSubitems2));
		inspectionItems.add(new InspectionItem("转向系统",inspectionSubitems3));
		inspectionItems.add(new InspectionItem("传动系统",inspectionSubitems4));
		inspectionItems.add(new InspectionItem("照明信号指示灯",inspectionSubitems5));
		inspectionItems.add(new InspectionItem("轮胎",inspectionSubitems6));
		inspectionItems.add(new InspectionItem("悬挂系统",inspectionSubitems7));
		inspectionItems.add(new InspectionItem("安全设施",inspectionSubitems8));
		inspectionItems.add(new InspectionItem("摄像头",inspectionSubitems9));
		
		
		Anjian anjian=new Anjian(inspectionItems);
		Gson gson=new Gson();
		
		return gson.toJson(anjian);
	}
	
	

}
