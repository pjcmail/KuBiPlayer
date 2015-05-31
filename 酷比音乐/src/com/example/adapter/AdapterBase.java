package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter{
	
	private List mList;
	private Context context;
	private LayoutInflater inflater;
	public AdapterBase(Context context,List mList) {
		this.context = context;
		this.mList = mList;
		inflater = LayoutInflater.from(context);
	}
	public LayoutInflater getLayoutInflater(){
		return inflater;
	}
	public Context getContext(){
		return context;
	}
	public List getList(){
		return mList;
	}
	
	public void setmList(List pList) {
		this.mList = pList;
	}
	@Override
	public int getCount() {
		
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
	
		return arg0;
	}
}
