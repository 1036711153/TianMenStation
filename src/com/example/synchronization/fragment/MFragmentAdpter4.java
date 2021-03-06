package com.example.synchronization.fragment;

import java.util.List;

import com.authentication.activity.R;
import com.example.outstation.entity.ExitInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MFragmentAdpter4 extends BaseAdapter{
	private List<ExitInfo> mExitInfos;
	private Context context;
	


	public MFragmentAdpter4(List<ExitInfo> mExitInfos, Context context) {
		super();
		this.mExitInfos = mExitInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mExitInfos.size();
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
		ExitInfo exitInfo=mExitInfos.get(position);
		View mView=LayoutInflater.from(context).inflate(R.layout.main_tab_instation_item, null);
		TextView licence=(TextView) mView.findViewById(R.id.licence);
		licence.setText(exitInfo.getLicencePlate());
		TextView date=(TextView) mView.findViewById(R.id.date);
		date.setText(exitInfo.getTime());
		return mView;
	}

}
