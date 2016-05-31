package com.example.synchronization.aty;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPaperAdapter extends FragmentPagerAdapter{
    private List<Fragment>mFragments;
	public MyFragmentPaperAdapter(FragmentManager fm,List<Fragment>mFragments) {
		super(fm);
		this.mFragments=mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}
}
