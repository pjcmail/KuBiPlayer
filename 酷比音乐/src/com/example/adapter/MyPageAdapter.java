package com.example.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MyPageAdapter extends PagerAdapter{

	private List<View> pageList;
	public MyPageAdapter(List<View> pList) {
		pageList = pList;
	}
	@Override
	public int getCount() {
		return pageList.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1){
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position){
		((ViewPager)container).addView(pageList.get(position));
		return pageList.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(pageList.get(position));
	}

}
