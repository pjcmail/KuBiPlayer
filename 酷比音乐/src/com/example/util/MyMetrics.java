package com.example.util;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

/**
 * 该工具类在获取Android手机屏幕分辨率的时候使用
 * 包括了获取屏幕的高和宽，以及获取屏幕高宽百分比
 * @since 2015-03-30
 * @author 潘建成
 *
 */
public class MyMetrics {
	
	/**
	 * 获取屏幕的高度
	 * @param pActivity 调用该函数的Activity
	 * @author 潘建成
	 * @since2015-03-30
	 * @return 屏幕的高度
	 * 
	 */
	public static int getScreenWidth(Activity pActivity){
		WindowManager windowManager = pActivity.getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int _ScreenWidth = display.getWidth();
        
        return _ScreenWidth;
	}
	
	/**
	 * 获取屏幕的宽度
	 * @param pActivity 调用该函数的Activity
	 * @since 2015-03-30
	 * @author 潘建成
	 * @return 屏幕的宽度
	 */
	public static int getScreenHeight(Activity pActivity){
		WindowManager windowManager = pActivity.getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int _ScreenHeight  = display.getHeight();           
        return _ScreenHeight;
	}
	
	/**
	 * 获取屏幕高度的百分比
	 * @param pActivity 调用该函数的Activity
	 * @param percent 百分比
	 * @author 潘建成
	 * @since 2015-03-30
	 * @return 屏幕高度的百分比
	 */
	public static float getHeightByPercent(Activity pActivity,float percent){
		float _Height = (int)getScreenHeight(pActivity)*percent/100;
		return _Height;
	}
	
	/**
	 * 获取屏幕宽度的百分比
	 * @param pActivity 调用该函数的Activity
	 * @param percent 百分比
	 * @author 潘建成
	 * @since 2015-03-30
	 * @return 屏幕宽度的百分比
	 */
	public static float getWidthByPercent(Activity pActivity,float percent){
		float _Width = getScreenWidth(pActivity)*percent/100;
		return _Width;
	}
}
