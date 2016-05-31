package com.example.Utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.authentication.activity.R;
import com.authentication.entity.CarNum;

public class Input_LiceneceAdapter extends BaseAdapter{
	private List<CarNum> carNums;
	private Context context;
	

	public Input_LiceneceAdapter(List<CarNum> carNums, Context context) {
		this.carNums = carNums;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return carNums.size();
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
		View root=LayoutInflater.from(context).inflate(R.layout.intput_car_adpter, null);
		TextView intput_car=(TextView) root.findViewById(R.id.input_carnum);
		intput_car.setText(carNums.get(position).getCarnum());
		return root;
	}

}
