package com.example.anjian.adpter;

import java.util.ArrayList;
import java.util.List;

import com.authentication.activity.R;
import com.example.anjian.Defect_description;
import com.example.anjian.InspectionItem;
import com.example.anjian.InspectionSubitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyPopListViewAdapter extends BaseAdapter{
	private Context context;
	private List<Defect_description>defect_descriptions=new	ArrayList<Defect_description>();
	
	public interface OnMyPopItemClickListener {
		void onPopItemClick(View view, int position);
	}

	public OnMyPopItemClickListener mOnItemClickListener;
	
	public void setMyOnItemClickListener(OnMyPopItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
	
	public MyPopListViewAdapter(Context context,
			List<Defect_description> defect_descriptions) {
		super();
		this.context = context;
		this.defect_descriptions = defect_descriptions;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return defect_descriptions.size();
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

	public View getView(final int position, View convertView, ViewGroup parent) {
	    final Defect_description defect_description=defect_descriptions.get(position);
	    View layout=LayoutInflater.from(context).inflate(R.layout.anjian_pop_main_item, null);
	    final CheckBox checkBox=(CheckBox) layout.findViewById(R.id.checkBox1);
	    LinearLayout linearLayout=(LinearLayout) layout.findViewById(R.id.pop_main);
	    linearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onPopItemClick(v, position);
			}
		});
	   TextView textView=(TextView) layout.findViewById(R.id.pop_textview);
	   textView.setText(defect_description.getDefect_description());
	    return layout;
	}


}
