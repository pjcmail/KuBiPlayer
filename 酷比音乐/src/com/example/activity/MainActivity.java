package com.example.activity;

import java.io.File;


import java.util.ArrayList;
import java.util.List;

import com.example.activity.LrcDialogActivity;
import com.example.adapter.MusicAdapter;
import com.example.adapter.MyPageAdapter;
import com.example.kubimusic.KuBiConfiguation;
import com.example.kubimusic.MediaPlayerDAL;
import com.example.kubimusic.MediaPlayerDAL.musicCompletionListener;
import com.example.kubimusic.Music;
import com.example.kubimusic.R;
import com.example.util.LrcUtil;
import com.example.util.MusicUtil;
import com.example.util.RoundImageDrawable;
import com.example.viewpage_anim.DepthPageTransformer;
import com.mylrc.tool.CLrcCtrl;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class MainActivity extends ActivityBase implements OnClickListener,OnItemClickListener,musicCompletionListener{
	private DrawerLayout mDrawerLayout;
	private ImageButton mMenutoggle;
	private TextView mMusicName,mSingerName,mSearchLrc;
	private TextView mNowTime,mEndTime;
	private CLrcCtrl mLrcView;
	private SeekBar mSeekbar;
	private ImageButton mPrevMusicBtn,mStartAndPauseBtn,mNextMusicBtn;
	private ImageView mImgView;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private ListView mMusicList;
	private MediaPlayerDAL mPlayerDAL;
	private ShowLrcHandler mLrcHandler;
	private UIHandler mUIHandler;
	private View mainBg ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViriable();
		initView();
		initListener();
		bindData();
	}
	private void initViriable(){
		mPlayerDAL = MediaPlayerDAL.getInstance(this);
		mPlayerDAL.setMusicCompletionListener(this);
		mActionBarDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, 
				R.string.open,R.string.close){
		};
		mLrcHandler = new ShowLrcHandler();
		mUIHandler = new UIHandler();
		mUIHandler.sendEmptyMessageDelayed(1, 100);
		setLrcFloderInit();
	}
	private void setLrcFloderInit() {
		File sd = new File(KuBiConfiguation.LRC_ROOT_PATH);
		if(!sd.exists())
			sd.mkdirs();
	}
	private void initView(){
		mainBg = findViewById(R.id.LinearLayout1);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMusicList = (ListView) findViewById(R.id.lv_musicList);
		mMenutoggle = (ImageButton)findViewById(R.id.ib_menuToggle);
		mMusicName = (TextView)findViewById(R.id.tv_music_name);
		mSearchLrc = (TextView) findViewById(R.id.tv_lrc);
		mSingerName = (TextView)findViewById(R.id.tv_music_singer);
		mNowTime = (TextView) findViewById(R.id.tv_nowTime);
		mEndTime = (TextView) findViewById(R.id.tv_endTime);
		mSeekbar = (SeekBar) findViewById(R.id.sk_seekBar);
		mPrevMusicBtn = (ImageButton) findViewById(R.id.ib_prev);
		mStartAndPauseBtn=(ImageButton) findViewById(R.id.ib_startAndpause);
		mNextMusicBtn = (ImageButton) findViewById(R.id.ib_next);
		mImgView = (ImageView) findViewById(R.id.imgView);
		initMainView();
		initImageView();
	}
	private void initMainView(){
		mEndTime.setText("00:00");
		mNowTime.setText("00:00");
		mSeekbar.setMax(0);
		mSeekbar.setProgress(0);
	}
	private void initImageView(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic9);
		mImgView.setImageDrawable(new RoundImageDrawable(bitmap));
	}
	private void initListener(){
		mMenutoggle.setOnClickListener(this);
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
		mMusicList.setOnItemClickListener(this);
		mSeekbar.setOnSeekBarChangeListener(new SeekbarChangedListener());
		mNextMusicBtn.setOnClickListener(this);
		mPrevMusicBtn.setOnClickListener(this);
		mStartAndPauseBtn.setOnClickListener(this);
		mSearchLrc.setOnClickListener(this);
	}
	private void bindData(){
		mMusicList.setAdapter(new MusicAdapter(this, MediaPlayerDAL.getMp3List()));
	}

	@Override
	public void onClick(View v){
		
		if(v.getId()==R.id.ib_menuToggle){
			openOrCloseDrawerlayout();	
		}else if(v.getId()==R.id.ib_next){
			playNextMusic();
		}else if(v.getId()==R.id.ib_prev){
			playPrevMusic();
		}else if(v.getId()==R.id.ib_startAndpause){
			startOrPauseMusic();
		}else if(v.getId()==R.id.tv_lrc){
			showLrcActivity();
		}
	}
	
	/**
	 * 查看歌词
	 */
	private void showLrcActivity() {
		if(MediaPlayerDAL.getIsLoadMusic()!=1)
			return;
		Intent intent = new Intent(this,LrcActivity.class);
		intent.putExtra("musicName", MediaPlayerDAL.getCurrentMusic().name);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	private void startOrPauseMusic(){
		mPlayerDAL.startOrPause();
		if(MediaPlayerDAL.getState()==1)
			mStartAndPauseBtn.setImageResource(R.drawable.start);
		else if(MediaPlayerDAL.getState()==-1)
			mStartAndPauseBtn.setImageResource(R.drawable.pause);
	}
	private void playPrevMusic() {
		mPlayerDAL.prev();
		updateUI(MediaPlayerDAL.getCurrentMusic());
	}

	private void playNextMusic(){
		mPlayerDAL.next();
		updateUI(MediaPlayerDAL.getCurrentMusic());
	}
	/**
	 * 点击左上角按钮，判断打开还是关闭列表
	 */
	private void openOrCloseDrawerlayout(){
		if(mDrawerLayout.isDrawerOpen(mMusicList)){
			setListViewSelectedItemWhenOPen();
			mDrawerLayout.closeDrawer(mMusicList);
			
			mMenutoggle.setImageResource(R.drawable.open_menu);
		}else{
			mDrawerLayout.openDrawer(mMusicList);
			mMenutoggle.setImageResource(R.drawable.close_menu);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			if(MediaPlayerDAL.getIsLoadMusic()==1){
				mPlayerDAL.stop();
			}
			finish();
			return true;
		}
		return false;
		
	}
	
	private void setAlbumBitmap(long songID,long albumID){
		Bitmap _Bitmap = MusicUtil.getMusicBitemp(this, songID, albumID);
		if(_Bitmap!=null){
			mImgView.setImageDrawable(new RoundImageDrawable(_Bitmap));
		}
		else{
			_Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic9);
			mImgView.setImageDrawable(new RoundImageDrawable(_Bitmap));
		}
	}
	private void setListViewSelectedItemWhenOPen() {
		int _Position = MediaPlayerDAL.getCurrentMusicPosition();
		if(_Position-5>=0)
			_Position-=5;
		mMusicList.setSelection(_Position);
	}
	private void updateUI(Music pMusic){
		setAlbumBitmap(pMusic.id, pMusic.albumid);
		mMusicName.setText(MusicUtil.getRealMusicName(pMusic.name));
		mSingerName.setText(pMusic.singer);
		mEndTime.setText(MusicUtil.toTime(pMusic.time));
		mSeekbar.setMax((int)pMusic.time);
		mStartAndPauseBtn.setImageResource(R.drawable.start);
	}
	private class MyActionBarDrawerToggle extends ActionBarDrawerToggle{

		public MyActionBarDrawerToggle(Activity activity,
				DrawerLayout drawerLayout, int drawerImageRes,
				int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
					closeDrawerContentDescRes);
		}
		@Override
		public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			mMenutoggle.setImageResource(R.drawable.close_menu);
			setListViewSelectedItemWhenOPen();
			invalidateOptionsMenu();
		}
		
		@Override
		public void onDrawerClosed(View drawerView) {
			super.onDrawerClosed(drawerView);
			mMenutoggle.setImageResource(R.drawable.open_menu);
		}
	}
	
	private class UIHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			if(MediaPlayerDAL.getPlayer().isPlaying()){
				mSeekbar.setProgress((int) MediaPlayerDAL.getCurrentPlayerPosition());
			}
			mUIHandler.sendEmptyMessageDelayed(1, 100);
		}
	}
	private class ShowLrcHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			 if (MediaPlayerDAL.getPlayer().isPlaying()){
				 	mLrcView.requestFocusFromTouch();
	                mLrcView.setSongLength(MediaPlayerDAL.getPlayer().getDuration());
	                mLrcView.setcurLength(MediaPlayerDAL.getPlayer().getCurrentPosition());
	                mLrcView.invalidate();
	                mLrcHandler.sendEmptyMessageDelayed(100, 50);
	            }
		}
	}
	private class SeekbarChangedListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2){
			mNowTime.setText(MusicUtil.toTime(arg1));
		}
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0){
			setSeekBarWhenClick(arg0);
		}
		
	}
	private void setSeekBarWhenClick(SeekBar pSeekBar){
		MediaPlayerDAL.getPlayer().seekTo(pSeekBar.getProgress());
	} 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		mPlayerDAL.setCurrentMusicPosition(position);
		mDrawerLayout.closeDrawer(mMusicList);
		playMusic();
	}
	private void playMusic() {
		Music music = MediaPlayerDAL.getCurrentMusic();
		mPlayerDAL.startMusic(music);
		mSeekbar.setMax((int) music.time);
		updateUI(music);
	}
	@Override
	public void onPlayNext(int position){
		playNextMusic();
	}
}
