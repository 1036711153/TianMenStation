package com.example.anjian.adpter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.authentication.activity.R;
import com.example.anjian.InspectionItem;
import com.example.anjian.InspectionSubitem;
import com.example.anjian.LinshiInspectionCarDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter{
	private Context context;
	private List<InspectionSubitem>inspectionSubitems=new	ArrayList<InspectionSubitem>();
	private Button mHegeButton;
	private Button mBuHegeButton;
	
	
	public interface OnMyItemClickListener {
		void onTextItemClick(View view, int position);
		void onTextLongItemClick(View view, int position);
		void onhegeClick(View view, int position);
		void onbuhegeClick(View view, int position);
	}

	public OnMyItemClickListener mOnItemClickListener;
	
	public void setMyOnItemClickListener(OnMyItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
	

	

	public MyListViewAdapter(Context context,
			List<InspectionSubitem> inspectionSubitems) {
		super();
		this.context = context;
		this.inspectionSubitems = inspectionSubitems;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return inspectionSubitems.size();
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
	    final InspectionSubitem inspectionSubitem=inspectionSubitems.get(position);
	    final View layout=LayoutInflater.from(context).inflate(R.layout.anjian_listview_main, null);
        
	    TextView textView=(TextView) layout.findViewById(R.id.textview);
	    textView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mOnItemClickListener.onTextLongItemClick(v, position);
				return true;
			}
		});
	    textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onTextItemClick(v, position);
			}
		});
	    final Button hegeButton=(Button) layout.findViewById(R.id.hege);
	    final Button buhegeButton=(Button) layout.findViewById(R.id.buhege);
        List<LinshiInspectionCarDetail> linshiInspectionCarDetails=
	    	 DataSupport.where("conclusion= ?","1").find(LinshiInspectionCarDetail.class);
        List<String> conclunamesList=new ArrayList<String>();
         for (int i = 0; i < linshiInspectionCarDetails.size(); i++) {
		   conclunamesList.add(linshiInspectionCarDetails.get(i).getInspectionSubitem_name());
	   }
	    int conclusion=0;
	    if (conclunamesList.contains(inspectionSubitem.getName())) {
			conclusion=1;
		}
	    if (conclusion==0) {
	    	hegeButton.setBackgroundResource(R.drawable.btn_hs_button_pressed);
			buhegeButton.setBackgroundResource(R.drawable.btn_bhgbutton_pressed);
		}
	    else {
	    	hegeButton.setBackgroundResource(R.drawable.btn_hg_button_pressed);
			buhegeButton.setBackgroundResource(R.drawable.btn_hs_button_pressed);
			
		}
	    
	    hegeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hegeButton.setBackgroundResource(R.drawable.btn_hg_button_pressed);
				buhegeButton.setBackgroundResource(R.drawable.btn_hs_button_pressed);
				mOnItemClickListener.onhegeClick(v, position);
			}
		});
	   
	    buhegeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				hegeButton.setBackgroundResource(R.drawable.hs_button);
//				buhegeButton.setBackgroundResource(R.drawable.bhgbutton);
				
				mHegeButton=(Button) layout.findViewById(R.id.hege);
				mBuHegeButton=(Button) layout.findViewById(R.id.buhege);
				
				mOnItemClickListener.onbuhegeClick(v, position);
			}
		});
	    textView.setText(inspectionSubitem.getName());
	    return layout;
	}
	
	
	public void sethegeAndbuhegeButton(){
		mHegeButton.setBackgroundResource(R.drawable.btn_hs_button_pressed);
		mBuHegeButton.setBackgroundResource(R.drawable.btn_bhgbutton_pressed);
	}


}
