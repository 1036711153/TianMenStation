package com.example.synchronization.fragment;

import java.util.List;

import com.authentication.activity.R;
import com.example.anjian.InspectionCarSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MFragmentAdpter2 extends BaseAdapter{
	private List<InspectionCarSummary> mInspectionCarSummaries;
	private Context context;
	
	



	public MFragmentAdpter2(List<InspectionCarSummary> mInspectionCarSummaries,
			Context context) {
		this.mInspectionCarSummaries = mInspectionCarSummaries;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInspectionCarSummaries.size();
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
		InspectionCarSummary inspectionCarSummary=mInspectionCarSummaries.get(position);
		View mView=LayoutInflater.from(context).inflate(R.layout.main_tab_instation_item, null);
		TextView licence=(TextView) mView.findViewById(R.id.licence);
		licence.setText(inspectionCarSummary.getVehicle_ID());
		TextView date=(TextView) mView.findViewById(R.id.date);
		date.setText(inspectionCarSummary.getDateTime());
		return mView;
	}

}
