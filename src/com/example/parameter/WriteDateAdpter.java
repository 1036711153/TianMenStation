package com.example.parameter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.authentication.activity.R;
import com.authentication.entity.Register_Finger_DataBase;

public class WriteDateAdpter extends BaseAdapter{
	private List<Register_Finger_DataBase> register_Finger_DataBases;
	private Context context;
	
	public WriteDateAdpter(
			List<Register_Finger_DataBase> register_Finger_DataBases,
			Context context) {
		super();
		this.register_Finger_DataBases = register_Finger_DataBases;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return register_Finger_DataBases.size();
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
		Register_Finger_DataBase register_Finger_DataBase = register_Finger_DataBases.get(position);
		View root=LayoutInflater.from(context).inflate(R.layout.write_data_item, null);
		TextView drive_id=(TextView) root.findViewById(R.id.Drive_ID);
		TextView id_card=(TextView) root.findViewById(R.id.ID_Card);
		drive_id.setText(register_Finger_DataBase.getDriver_id());
		id_card.setText(register_Finger_DataBase.getId_card());
		return root;
	}

}
