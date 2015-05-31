package com.example.util;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

/**
 * �ù������ڻ�ȡAndroid�ֻ���Ļ�ֱ��ʵ�ʱ��ʹ��
 * �����˻�ȡ��Ļ�ĸߺͿ��Լ���ȡ��Ļ�߿�ٷֱ�
 * @since 2015-03-30
 * @author �˽���
 *
 */
public class MyMetrics {
	
	/**
	 * ��ȡ��Ļ�ĸ߶�
	 * @param pActivity ���øú�����Activity
	 * @author �˽���
	 * @since2015-03-30
	 * @return ��Ļ�ĸ߶�
	 * 
	 */
	public static int getScreenWidth(Activity pActivity){
		WindowManager windowManager = pActivity.getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int _ScreenWidth = display.getWidth();
        
        return _ScreenWidth;
	}
	
	/**
	 * ��ȡ��Ļ�Ŀ��
	 * @param pActivity ���øú�����Activity
	 * @since 2015-03-30
	 * @author �˽���
	 * @return ��Ļ�Ŀ��
	 */
	public static int getScreenHeight(Activity pActivity){
		WindowManager windowManager = pActivity.getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int _ScreenHeight  = display.getHeight();           
        return _ScreenHeight;
	}
	
	/**
	 * ��ȡ��Ļ�߶ȵİٷֱ�
	 * @param pActivity ���øú�����Activity
	 * @param percent �ٷֱ�
	 * @author �˽���
	 * @since 2015-03-30
	 * @return ��Ļ�߶ȵİٷֱ�
	 */
	public static float getHeightByPercent(Activity pActivity,float percent){
		float _Height = (int)getScreenHeight(pActivity)*percent/100;
		return _Height;
	}
	
	/**
	 * ��ȡ��Ļ��ȵİٷֱ�
	 * @param pActivity ���øú�����Activity
	 * @param percent �ٷֱ�
	 * @author �˽���
	 * @since 2015-03-30
	 * @return ��Ļ��ȵİٷֱ�
	 */
	public static float getWidthByPercent(Activity pActivity,float percent){
		float _Width = getScreenWidth(pActivity)*percent/100;
		return _Width;
	}
}
