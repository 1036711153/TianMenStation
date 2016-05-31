package com.example.outstation.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.authentication.activity.R;
import com.example.outstation.entity.Driver;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Driver_InfoAdapter extends BaseAdapter{
	
	private List<Driver> drivers;
	private Context context;
	
	public Driver_InfoAdapter(List<Driver> drivers, Context context) {
		super();
		this.drivers = drivers;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drivers.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Driver driver=drivers.get(position);
		ViewHoder hoder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.houjiawei_driver_main, null);
            hoder=new ViewHoder();
            hoder.drive_anjian_result=(TextView) convertView.findViewById(R.id.driver_anjian);
            hoder.drive_baoban=(TextView) convertView.findViewById(R.id.driver_baoban);
            hoder.drive_id=(TextView) convertView.findViewById(R.id.driver_id);
            hoder.drive_name=(TextView) convertView.findViewById(R.id.driver_name);
            hoder.drive_tel=(TextView) convertView.findViewById(R.id.driver_tel);
            hoder.drive_adultNum=(TextView) convertView.findViewById(R.id.driver_adultNum);
            hoder.drive_id_card=(TextView) convertView.findViewById(R.id.driver_ID_card);
            hoder.photo=(ImageView) convertView.findViewById(R.id.driver_photo);
            convertView.setTag(hoder);
		}else {
			hoder=(ViewHoder) convertView.getTag();
		}
		
		//安检
		String anjian_res="";
		if (driver.getAnjian_result()==null||TextUtils.isEmpty(driver.getAnjian_result())) {
			anjian_res="未安检";
			hoder.drive_anjian_result.setTextColor(context.getResources().getColor(R.color.red));
		}else if (driver.getAnjian_result().equals("202")) {
			anjian_res="已安检";
		}else {
			anjian_res="未安检";
			hoder.drive_anjian_result.setTextColor(context.getResources().getColor(R.color.red));	
		}
		hoder.drive_anjian_result.setText(anjian_res);
		
		//报班
		String baoban_res="";
		
		if (driver.getBaoban_result()==null||TextUtils.isEmpty(driver.getBaoban_result())) {
			baoban_res="未报班";
			hoder.drive_baoban.setTextColor(context.getResources().getColor(R.color.red));
		}else if (driver.getBaoban_result().equals("300")) {
			baoban_res="已报班";
		}else {
			baoban_res="未报班";
			hoder.drive_baoban.setTextColor(context.getResources().getColor(R.color.red));	
		}
		
		hoder.drive_baoban.setText(baoban_res);
		
		hoder.drive_id.setText(driver.getDriver_id());
		
		hoder.drive_name.setText(driver.getDriver_name());
		
		hoder.drive_tel.setText(driver.getTel());
		
		hoder.drive_adultNum.setText(driver.getAdultNum());
		
		hoder.drive_id_card.setText(driver.getID_Card());
		
		//加载图片
		String pathString;
		if (TextUtils.isEmpty(driver.getPhoto())) {
			pathString="http://192.168.1.101:8090/Upload_8-1/man_photo.png";
		}else {
			pathString=driver.getPhoto();
			Picasso.with(context).load(driver.getPhoto()).into(hoder.photo);
		}

		return convertView;
	}
	
	
	class ViewHoder{
		private TextView drive_anjian_result;
		private TextView drive_baoban;
		private TextView drive_id;
		private TextView drive_name;
		private TextView drive_tel;
		private TextView drive_adultNum;
		private TextView drive_id_card;
		private ImageView photo;
	}

}
