package com.example.synchronization.fragment;

import java.util.List;

import com.authentication.activity.R;
import com.example.instation.EntranceInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MFragmentAdpter extends BaseAdapter{
	private List<EntranceInfo> mEntranceInfos;
	private Context context;
	
	

	public MFragmentAdpter(List<EntranceInfo> mEntranceInfos, Context context) {
		this.mEntranceInfos = mEntranceInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEntranceInfos.size();
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
		EntranceInfo entranceInfo=mEntranceInfos.get(position);
		View mView=LayoutInflater.from(context).inflate(R.layout.main_tab_instation_item, null);
		TextView licence=(TextView) mView.findViewById(R.id.licence);
		licence.setText(entranceInfo.getLicnence());
		TextView date=(TextView) mView.findViewById(R.id.date);
		date.setText(entranceInfo.getTime());
		return mView;
	}

}
