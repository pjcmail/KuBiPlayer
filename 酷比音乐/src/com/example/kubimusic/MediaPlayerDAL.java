package com.example.kubimusic;

import java.util.List;

import com.example.activity.MainActivity;
import com.example.util.LrcUtil;
import com.example.util.MusicUtil;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.sax.StartElementListener;
import android.widget.Toast;

public class MediaPlayerDAL implements MediaPlayer.OnCompletionListener{
	
	private static MediaPlayer player;
	private static Context mContext;
	private static MediaPlayerDAL INSTANCE; 
	//state==0,表示初始状态；state==1，表示已播放，state = -1，表示暂停
	private static int state = 0;
	//isLoadMusic为 0 表示没有为player加载音乐，为 1 则表示已加载音乐
	private static int isLoadMusic = 0; 
	//当前播放的音乐的位置
	private static int currentMusicPosition = -1;
	//包含所有音乐的列表
	private static List<Music> mp3List;
	private musicCompletionListener mMusicCompletion;

	public interface musicCompletionListener{
		void onPlayNext(int position);
	}
	public void setMusicCompletionListener(musicCompletionListener listener){
		mMusicCompletion = listener;
	}
	public static MediaPlayer getPlayer(){
		return player;
	}
	public static Music getCurrentMusic(){
		Music _Music = mp3List.get(currentMusicPosition);
		return _Music;
		
	}
	public void setCurrentMusicPosition(int position){
		currentMusicPosition = position;
	}
	public static int getIsLoadMusic(){
		return isLoadMusic;
	}
	public static List<Music> getMp3List(){
		return mp3List;
	}
	public static int getCurrentMusicPosition(){
		return currentMusicPosition;
	}
	public static int getState(){
		return state;
	}
	private MediaPlayerDAL(){
		player = new MediaPlayer();
		mp3List = MusicUtil.getMusicsInCard(mContext);
		
	}
	public static MediaPlayerDAL getInstance(Context pContext){
		if(INSTANCE==null){
			mContext = pContext;
			INSTANCE =  new MediaPlayerDAL();
		}
		return INSTANCE;
	}
	public static long getCurrentPlayerPosition(){
		if(player!=null){
			return player.getCurrentPosition();
		}
		return 0;
	}
	public void pause(){
		if(isLoadMusic == 1){
			if(state==1){
				player.pause();
				state = -1;
				
			}
		}
	}
	public void start(){
		if(isLoadMusic==1){
			if(state==-1){
				player.start();
				state = 1;
			}
		}
	}
	public void stop(){
		if(isLoadMusic==1){
			player.stop();
		}
	}
	public void next(){
		if(currentMusicPosition+1<mp3List.size()){
			currentMusicPosition++;
			startMusic(mp3List.get(currentMusicPosition));
		}
	}
	public void prev(){
		if(currentMusicPosition-1>=0){
			currentMusicPosition--;
			startMusic(mp3List.get(currentMusicPosition));
		}
	}
	public void startOrPause(){
		if(state==1){
			pause();
			state = -1;
		}else if(state==-1){
			start();
			state = 1;
		}
	}
	public void startMusic(Music pMusic){
		Uri myUri = Uri.parse(pMusic.url);
		if(isLoadMusic==1){
			player.stop();
			player.release();
			player = new MediaPlayer();
			player.setOnCompletionListener(this);
			setPlayerPrepare(myUri);
			state = 1;
		}else{
			player.setOnCompletionListener(this);
			setPlayerPrepare(myUri);
			state = 1;
			isLoadMusic = 1;
		}
	}
	private void setPlayerPrepare(Uri uri){
		try {
			player.setDataSource(mContext, uri);
			player.prepare();
			player.start();
		} catch (Exception e) {}
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		player = mp;
		currentMusicPosition = ++currentMusicPosition<=mp3List.size()?currentMusicPosition:mp3List.size();
		mMusicCompletion.onPlayNext(currentMusicPosition);
	}
}
