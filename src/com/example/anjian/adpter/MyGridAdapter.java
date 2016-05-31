package com.example.anjian.adpter;

import java.util.ArrayList;
import java.util.List;

import com.authentication.activity.R;
import com.example.anjian.InspectionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter{
	private Context context;
	private List<InspectionItem>inspectionItems=new	ArrayList<InspectionItem>();
	
	public interface OnMyItemClickListener {
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	public OnMyItemClickListener mOnItemClickListener;
	
	public void setMyOnItemClickListener(OnMyItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
	
	public MyGridAdapter(Context context, List<InspectionItem> inspectionItems) {
		super();
		this.context = context;
		this.inspectionItems = inspectionItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return inspectionItems.size();
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

	public View getView(final int position, View convertView, final ViewGroup parent) {
	    final InspectionItem inspectionItem=inspectionItems.get(position);
	    final View layout=LayoutInflater.from(context).inflate(R.layout.anjian_gridview_main, null);
	    TextView textView=(TextView) layout.findViewById(R.id.textview);
	    final LinearLayout linearLayout=(LinearLayout) layout.findViewById(R.id.linear);
	    textView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mOnItemClickListener.onItemLongClick(v, position);
				return true;
			}
		});
	    textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(v, position);
			}
		});
	    textView.setText(inspectionItem.getCname());
	    return layout;
	}
}
