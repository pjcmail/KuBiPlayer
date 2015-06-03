package com.example.activity;

import com.mylrc.tool.CLrcCtrl;
import com.example.*;
import com.example.kubimusic.MediaPlayerDAL;
import com.example.kubimusic.Music;
import com.example.kubimusic.R;
import com.example.util.MusicUtil;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LrcActivity extends ActivityBase implements OnClickListener{
	
	
	private MediaPlayer mPlayer;
	private CLrcCtrl mLrcView;
	private String name;
	private TextView mDownLoadLrc,mBack;
	private String rootLrcPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/KuBi/lrc/";
	Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mPlayer.isPlaying()) {
                mLrcView.setSongLength(mPlayer.getDuration());
                mLrcView.setcurLength(mPlayer.getCurrentPosition());
                mLrcView.invalidate();
            }
            if (mHandler != null){
                mHandler.sendEmptyMessageDelayed(100, 50);
            }
        }
    };
    
    protected void onCreate(android.os.Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_lrc);
    	name = getIntent().getExtras().getString("musicName");
    	initVariable();
    	initView();
    	initListener();
    	bindData();
    	mHandler.sendEmptyMessageDelayed(100, 50);
    }

	private void bindData() {
		readLrc(MusicUtil.getRealMusicName(name));
	}

	private void initListener() {
		mDownLoadLrc.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	private void initView() {
		mLrcView = (CLrcCtrl) findViewById(R.id.lrcView);
		mDownLoadLrc = (TextView) findViewById(R.id.tv_download_lrc);
		mBack = (TextView) findViewById(R.id.tv_back);
		mLrcView.SetTextSize(25);
		mLrcView.setTextColor(255, 255, 255);
		mLrcView.setCurrentLineColor(128, 255, 255);
		mLrcView.setMediaPlayer(MediaPlayerDAL.getPlayer());
		mLrcView.setCoding("UTF-8");

		
	}

	private void initVariable() {
		mPlayer = MediaPlayerDAL.getPlayer();
	};
	
	private void readLrc(String name) {
	mLrcView.readlrc(rootLrcPath+name+".lrc");
	mLrcView.setMediaPlayer(mPlayer);
	mHandler.sendEmptyMessageDelayed(100, 50);
}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tv_download_lrc){
			startLrcDialogActivity();
		}else if(v.getId()==R.id.tv_back){
			finish();
		}
		
	}
	
	/**
	 * 转到下载歌词的Activity
	 */
	private void startLrcDialogActivity(){
		Intent _Intent = new Intent(this,LrcDialogActivity.class);
		Bundle bundle = new Bundle();
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		bundle.putString("MusicName", MusicUtil.getRealMusicName(_Music.name));
		bundle.putString("singer", _Music.singer);
		_Intent.putExtras(bundle);
		startActivityForResult(_Intent,1);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		readLrc(MusicUtil.getRealMusicName(MediaPlayerDAL.getCurrentMusic().name));
	}
}
