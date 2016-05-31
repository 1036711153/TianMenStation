package com.example.outstation.Servers;

import java.util.List;

import com.authentication.activity.R;
import com.example.outstation.entity.ExitCheckInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OutofStationLicenceAdapter extends BaseAdapter{
	private List<ExitCheckInfo> exitCheckInfos;
	private Context context;
	

	public OutofStationLicenceAdapter(List<ExitCheckInfo> exitCheckInfos,
			Context context) {
		this.exitCheckInfos = exitCheckInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return exitCheckInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rootvView=LayoutInflater.from(context).inflate(R.layout.show_licenceplate_item, null);
		TextView licenplate=(TextView) rootvView.findViewById(R.id.id_licenceplate);
		licenplate.setText(exitCheckInfos.get(position).getLicencePlate());
		return rootvView;
	}

}
