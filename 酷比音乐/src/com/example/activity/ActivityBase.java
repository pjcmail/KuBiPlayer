package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

public class ActivityBase extends Activity{
	
	protected void ShowMsg(String msg){
		Toast.makeText(this, msg, 1000).show();
	}
	protected void OpenActivity(Class<?> pClass){
		Intent intent = new Intent();
		intent.setClass(this, pClass);
		startActivity(intent);
	}
	public LayoutInflater getLayoutInflater(){
		LayoutInflater _LayoLayoutInflater = LayoutInflater.from(this);
		return _LayoLayoutInflater;
	}
}
