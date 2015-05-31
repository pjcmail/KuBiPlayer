package com.example.activity;

import java.io.File;


import java.util.ArrayList;
import java.util.List;

import com.example.activity.LrcDialogActivity;
import com.example.adapter.MusicAdapter;
import com.example.adapter.MyPageAdapter;
import com.example.kubimusic.KuBiConfiguation;
import com.example.kubimusic.MediaPlayerDAL;
import com.example.kubimusic.Music;
import com.example.kubimusic.R;
import com.example.util.LrcUtil;
import com.example.util.MusicUtil;
import com.example.util.RoundImageDrawable;
import com.example.viewpage_anim.DepthPageTransformer;

import android.os.Bundle;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class MainActivity extends ActivityBase implements OnClickListener,OnItemClickListener,Runnable{
	private DrawerLayout mDrawerLayout;
	private ImageButton mMenutoggle;
	private TextView mMusicName,mSingerName;
	private TextView mLrc_1,mLrc_2,mLrc_3,mLrc_4,mLrc_5;
	private TextView mLrcTextView;
	private TextView mNowTime,mEndTime;
	private SeekBar mSeekbar;
	private ImageButton mPrevMusicBtn,mStartAndPauseBtn,mNextMusicBtn;
	private ImageView mPicView;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private ViewPager mViewPager;
	private ListView mMusicList;
	private List<View> mPageList;
	private static List<String> lrcList;
	private static List<Long> timeList;
	private MediaPlayerDAL mPlayerDAL;
	private MyHandler mHandler;
	private ShowLrcHandler mLrcHandler;
	private Thread mThread;
	private boolean isExit = false;
	private int oldLrcIndex = 0;
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
		mPageList = new ArrayList<View>();
		mActionBarDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, 
				R.string.open,R.string.close){
		};
		lrcList = new ArrayList<String>();
		timeList = new ArrayList<Long>();
		mHandler = new MyHandler();
		mLrcHandler = new ShowLrcHandler();
		mThread = new Thread(this);
		mThread.start();
		setLrcFloderInit();
	}
	public static List<String> getLrcList() {
		return lrcList;
	}
	public static void setLrcList(List<String> plrcList) {
		lrcList = plrcList;
	}
	public static List<Long> getTimeList() {
		return timeList;
	}
	public static void setTimeList(List<Long> ptimeList) {
		timeList = ptimeList;
	}
	private void setLrcFloderInit() {
		File sd = new File(KuBiConfiguation.LRC_ROOT_PATH);
		if(!sd.exists())
			sd.mkdirs();
	}
	private void initView(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMusicList = (ListView) findViewById(R.id.lv_musicList);
		mMenutoggle = (ImageButton)findViewById(R.id.ib_menuToggle);
		mLrcTextView = (TextView) findViewById(R.id.tv_lrc);
		mMusicName = (TextView)findViewById(R.id.tv_music_name);
		mSingerName = (TextView)findViewById(R.id.tv_music_singer);
		mNowTime = (TextView) findViewById(R.id.tv_nowTime);
		mEndTime = (TextView) findViewById(R.id.tv_endTime);
		mSeekbar = (SeekBar) findViewById(R.id.sk_seekBar);
		mViewPager = (ViewPager) findViewById(R.id.viewPage);
		mPrevMusicBtn = (ImageButton) findViewById(R.id.ib_prev);
		mStartAndPauseBtn=(ImageButton) findViewById(R.id.ib_startAndpause);
		mNextMusicBtn = (ImageButton) findViewById(R.id.ib_next);
		View v1 = LayoutInflater.from(this).inflate(R.layout.page_lrc, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.page_pic, null);
		mPicView = (ImageView) v2.findViewById(R.id.iv_pic);
		mLrc_1 = (TextView)v1.findViewById(R.id.tv_prevLrc_head);
		mLrc_2 = (TextView)v1. findViewById(R.id.tv_prevLrc);
		mLrc_3 = (TextView)v1.findViewById(R.id.tv_nowLrc);
		mLrc_4 = (TextView) v1.findViewById(R.id.tv_nextLrc);
		mLrc_5 = (TextView) v1.findViewById(R.id.tv_nextLrc_end);
		mPageList.add(v2);
		mPageList.add(v1);
		initViewPager();
		initMainView();
	}
	private void initMainView() {
		mEndTime.setText("00:00");
		mNowTime.setText("00:00");
		mSeekbar.setMax(0);
		mSeekbar.setProgress(0);
		
	}
	private void initViewPager() {
		mViewPager.setAdapter(new MyPageAdapter(mPageList));
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic8);
		mPicView.setImageDrawable(new RoundImageDrawable(bitmap));
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
	}
	private void initListener(){
		mMenutoggle.setOnClickListener(this);
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
		mMusicList.setOnItemClickListener(this);
		mSeekbar.setOnSeekBarChangeListener(new SeekbarChangedListener());
		mNextMusicBtn.setOnClickListener(this);
		mPrevMusicBtn.setOnClickListener(this);
		mStartAndPauseBtn.setOnClickListener(this);
		mLrcTextView.setOnClickListener(this);
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
			startLrcDialogActivity();
			
		}
	}
	private void startLrcDialogActivity(){
		if(MediaPlayerDAL.getIsLoadMusic()!=1)
			return;
		Intent _Intent = new Intent(this,LrcDialogActivity.class);
		Bundle bundle = new Bundle();
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		bundle.putString("MusicName", MusicUtil.getRealMusicName(_Music.name));
		bundle.putString("singer", _Music.singer);
		_Intent.putExtras(bundle);
		startActivity(_Intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	private void startOrPauseMusic() {
		mPlayerDAL.startOrPause();
		if(MediaPlayerDAL.getState()==1)
			mStartAndPauseBtn.setImageResource(R.drawable.start);
		else if(MediaPlayerDAL.getState()==-1)
			mStartAndPauseBtn.setImageResource(R.drawable.pause);
	}
	private void playPrevMusic() {
		mPlayerDAL.prev();
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		updateUI(_Music);
		initLrcViewWhenChangeMusic();
	}
	private void playNextMusic() {
		mPlayerDAL.next();
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		updateUI(_Music);
		initLrcViewWhenChangeMusic();
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
			if(mPlayerDAL.getIsLoadMusic()==1){
				mPlayerDAL.stop();
			}
			isExit = true;
			finish();
			return true;
		}
		return false;
		
	}
	
	private void setAlbumBitmap(long songID,long albumID){
		Bitmap _Bitmap = MusicUtil.getMusicBitemp(this, songID, albumID);
		if(_Bitmap!=null){
			mPicView.setImageDrawable(new RoundImageDrawable(_Bitmap));
		}
		else{
			_Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic8);
			mPicView.setImageDrawable(new RoundImageDrawable(_Bitmap));
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
	private void setLrcViewNULL(){
		mLrc_1.setText("");
		mLrc_2.setText("");
		mLrc_3.setText("找不到歌词");
		mLrc_4.setText("");
		mLrc_5.setText("");
		
	}
	private void showLrcOnView(int i){
		if(i-3>=0){
			mLrc_1.setText(lrcList.get(i-3));
		}
		if(i-2>=0)
			mLrc_2.setText(lrcList.get(i-2));
		mLrc_3.setText(lrcList.get(i-1));
		if(i<timeList.size())
			mLrc_4.setText(lrcList.get(i));
		if((i+1)<timeList.size())
			mLrc_5.setText(lrcList.get(i+1));
	}
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int _position = msg.what;
			updateUI(MediaPlayerDAL.getMp3List().get(_position));
			updateTimeAndSeek(_position);		
		}
	}
	private class ShowLrcHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(timeList.size() == 0){
				setLrcViewNULL();
				return;
			}
			long currentTime = MediaPlayerDAL.getCurrentPlayerPosition();
			if(MediaPlayerDAL.getState()==1){
				for(int i=oldLrcIndex;i<timeList.size();i++){
					if(currentTime<timeList.get(i)&&currentTime>timeList.get(i-1)){
						showLrcOnView(i);
						oldLrcIndex = i;
						break;
					}else{
						mLrc_3.setText(lrcList.get(0));
						mLrc_4.setText(lrcList.get(1));
						mLrc_5.setText(lrcList.get(2));
						
					}
				}
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
		mPlayerDAL.getPlayer().seekTo(pSeekBar.getProgress());
	} 
	private void updateTimeAndSeek(int _position) {
		String endTime = MusicUtil.toTime(MediaPlayerDAL.getMp3List().get(_position).time);
		mEndTime.setText(endTime);
		mSeekbar.setProgress((int)MediaPlayerDAL.getCurrentPlayerPosition());
		mNowTime.setText(MusicUtil.toTime(MediaPlayerDAL.getCurrentPlayerPosition()));
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Music _Music = MediaPlayerDAL.getMp3List().get(position);
		mPlayerDAL.startMusic(_Music);
		mPlayerDAL.setCurrentMusicPosition(position);
		updateUI(_Music);
		initLrcViewWhenChangeMusic();
		mDrawerLayout.closeDrawer(mMusicList);
	}
	private void initLrcViewWhenChangeMusic() {
		oldLrcIndex = 1;
		setLrcViewNULL();
		LrcUtil.clearList(timeList);
		LrcUtil.clearList(lrcList);
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		boolean bo = LrcUtil.setLrcTextAndtTime(lrcList, timeList, MusicUtil.getRealMusicName(_Music.name));
		if(!bo){
			mLrc_3.setText("找不到歌词");
		}
	}
	@Override
	public void run() {
		while(true&&isExit==false){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			if(MediaPlayerDAL.getState()==1){
				int nowPosition = MediaPlayerDAL.getCurrentMusicPosition();
				Message messageUI = new Message();
				Message messageLRC = new Message();
				messageUI.what = nowPosition;
				mHandler.sendMessage(messageUI);
				mLrcHandler.sendMessage(messageLRC);
			}
		}
		
	}
}
