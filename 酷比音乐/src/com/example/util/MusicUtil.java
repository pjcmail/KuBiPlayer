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
	 * ��ȡ����android�ֻ���������ļ�
	 * @param pContext  �����ģ��������Activity
	 * @return  ���ذ������������ļ����б�
	 * @since 2015-03-30
	 * @author �˽���
	 */
	public static List<Music> getMusicsInCard(Context pContext){
		List<Music> _List = new ArrayList<Music>();
		ContentResolver cr = pContext.getContentResolver();
		if(cr!=null){
			//��ȡ���и���
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
	 * ��ȡpCursor
	 * @param cr ContentResolver�������ڲ���pCursor����
	 * @return Cursor��������������е������ļ���Ϣ
	 * @since 2015-03-30
	 * @author �˽���
	 */
	private static Cursor getCursor(ContentResolver cr){
		Cursor _pCursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,
				null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return _pCursor;
	}
	
	/**
	 * ��Music���������Ϣ
	 * @param pMusic ��ǰ��Ҫ�����Ϣ��Music����
	 * @param pCursor ��ǰ������ϢdeCursor����
	 * @since 2015-03-30
	 * @author �˽���
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
	 * ��ȡһ�׸�����֣�������׺ ����MP3�� ��Ҳ�������ֵ����ֺ� ��-��
	 * @param pName Music�����name�������������ֺͺ�׺ ����Mp3��
	 * @return ���ز���������׺ ����MP3�� ��Ҳ�������ֵ����ֺ� ��-�����ַ���
	 * @since 2015-03-30
	 * @author �˽���
	 */
	public static String getRealMusicName(String pName){
		String _FrontName = pName.substring(0, pName.lastIndexOf("."));
		String _SplitStr[] = _FrontName.split("-");
		if(_SplitStr.length>=2)
			return _SplitStr[1];
		return _FrontName;
		
	}
	
	/**
	 * �������ļ���ʱ��(���뼶)ת���� 00:00�ĸ�ʽ
	 * @param time �����ļ���ʱ��(���뼶)
	 * @return 00:00��ʽ������ʱ���ַ���
	 * @since 2015-03-30
	 * @author �˽���
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
		// ר��id�͸���idС��0˵��û��ר�������������׳��쳣
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
