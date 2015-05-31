package com.example.adapter;

import java.util.List;

import com.example.kubimusic.Music;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kubimusic.*;
import com.example.util.MusicUtil;

public class MusicAdapter extends AdapterBase{

	Context mContext;
	public MusicAdapter(Context context, List mList) {
		super(context, mList);
		mContext = context;
	}
	@Override
	public View getView(int position, View conView, ViewGroup arg2) {
		Music _Music = (Music) getList().get(position);
		Holder holder;
		if(conView==null){
			conView = getLayoutInflater().from(mContext).inflate(R.layout.list_item,null);
			holder = new Holder();
			holder.tv_MusicOrder = (TextView) conView.findViewById(R.id.tv_order);
			holder.tv_MusicName = (TextView) conView.findViewById(R.id.tv_List_musicName);
			holder.tv_SingerName = (TextView) conView.findViewById(R.id.tv_List_singerName);
			holder.tv_Time = (TextView) conView.findViewById(R.id.tv_list_musicTime);
			conView.setTag(holder);
		}else{
			holder = (Holder) conView.getTag();
		}
		holder.tv_MusicOrder.setText(""+(position+1));
		holder.tv_MusicName.setText(MusicUtil.getRealMusicName(_Music.name));
		holder.tv_SingerName.setText(_Music.singer);
		holder.tv_Time.setText(MusicUtil.toTime(_Music.time));
		return conView;
	}
	
	private class Holder{
		TextView tv_MusicOrder;
		TextView tv_MusicName;
		TextView tv_SingerName;
		TextView tv_Time;
	} 
}
