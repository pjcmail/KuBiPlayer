package com.example.util;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.example.kubimusic.Music;

import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class MusicUtil {
	
	/**
	 * 获取所有android手机里的音乐文件
	 * @param pContext  上下文，即传入的Activity
	 * @return  返回包含所有音乐文件的列表
	 * @since 2015-03-30
	 * @author 潘建成
	 */
	public static List<Music> getMusicsInCard(Context pContext){
		List<Music> _List = new ArrayList<Music>();
		ContentResolver cr = pContext.getContentResolver();
		if(cr!=null){
			//获取所有歌曲
			Cursor pCursor = getCursor(cr);
			if(pCursor==null)
				return null;
			if(pCursor.moveToFirst()){
				do{
					Music m = new Music();
					setMusicInfo(m, pCursor);
					if(m.time>3000)
						_List.add(m);
				}while(pCursor.moveToNext());
			}
		}
		return _List;
	}
	
	/**
	 * 获取pCursor
	 * @param cr ContentResolver对象，用于产生pCursor对象
	 * @return Cursor对象，里面带有所有的音乐文件信息
	 * @since 2015-03-30
	 * @author 潘建成
	 */
	private static Cursor getCursor(ContentResolver cr){
		Cursor _pCursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,
				null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return _pCursor;
	}
	
	/**
	 * 给Music对象填充信息
	 * @param pMusic 当前需要填充信息的Music对象
	 * @param pCursor 当前包含信息deCursor对象
	 * @since 2015-03-30
	 * @author 潘建成
	 */
	private static void setMusicInfo(Music pMusic,Cursor pCursor){
		String singer = pCursor.getString(pCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
		long time = pCursor.getLong(pCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
		String url = pCursor.getString(pCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
		String name = pCursor.getString(pCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
		int albumID = pCursor.getInt(pCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
		int id =  pCursor.getInt(pCursor.getColumnIndex(MediaStore.Audio.Media._ID));
		if(time>3000){
			if(name.endsWith(".mp3")||name.endsWith(".MP3")||name.endsWith(".wav")){
				pMusic.singer = singer;
				pMusic.time=time;
				pMusic.url = url;
				pMusic.name = name;	
				pMusic.id = id;
				pMusic.albumid = albumID;
			}
		}
	}
	
	/**
	 * 获取一首歌的名字，不带后缀 “。MP3” ，也不带歌手的名字和 “-”
	 * @param pName Music对象的name，包含歌手名字和后缀 “。Mp3”
	 * @return 返回不带不带后缀 “。MP3” ，也不带歌手的名字和 “-”的字符串
	 * @since 2015-03-30
	 * @author 潘建成
	 */
	public static String getRealMusicName(String pName){
		String _FrontName = pName.substring(0, pName.lastIndexOf("."));
		String _SplitStr[] = _FrontName.split("-");
		if(_SplitStr.length>=2)
			return _SplitStr[1];
		return _FrontName;
		
	}
	
	/**
	 * 将歌曲文件的时长(毫秒级)转换成 00:00的格式
	 * @param time 歌曲文件的时长(毫秒级)
	 * @return 00:00格式的音乐时长字符串
	 * @since 2015-03-30
	 * @author 潘建成
	 */
	public static  String toTime(long time) {
		time /= 1000;
		int minute = (int) (time / 60);
		int hour = minute / 60;
		int second = (int) (time % 60);
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
	
	
	public static Bitmap getMusicBitemp(Context context, long songid,long albumid)
	{
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

		Bitmap bm = null;
		// 专辑id和歌曲id小于0说明没有专辑、歌曲，并抛出异常
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException("Must specify an album or a song id");
		}
		try {
			if (albumid < 0){
				Uri uri = Uri.parse("content://media/external/audio/media/"+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				} else {
					return null;
				}
			}
		} catch (FileNotFoundException ex) {}
		return bm;
	}
		
}
