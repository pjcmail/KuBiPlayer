package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.kubimusic.KuBiConfiguation;

public class LrcUtil{
	public static boolean saveLrcFile(List<String> pList ,String musicName){
		if(pList.size()<20)
			return false;
		try {
			FileWriter fw = new FileWriter(KuBiConfiguation.LRC_ROOT_PATH+musicName+".lrc");
			for(int i=0;i<pList.size();i++){
				fw.write(pList.get(i));
				fw.write("\n");
			}
			fw.close();
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	public static void clearList(List pList){
		if(pList!=null&&pList.size()>0)
			pList.clear();
	}
	public static boolean setLrcTextAndtTime(List<String> lrcText,List<Long> lrcTime,String musicName){
		File file = new File(KuBiConfiguation.LRC_ROOT_PATH+musicName+".lrc");
		if(file.exists()){
			Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
				String temp=null;
				while((temp=br.readLine())!=null){
					Matcher m = p.matcher(temp);
					if(m.find()){
						String str = m.group();
						long time =0;
						try{
							time=time2Long(str.substring(1, str.length()-1));
						}catch(Exception e){}
						if(time!=0){
							lrcTime.add(time);
							String s[]=temp.split("]");
							lrcText.add(s[s.length-1]);
						}
					}
				}
			} catch (Exception e) {}
			return true;
		}else {
			return false;
		}
	}
	/**
	 * º∆À„∫¡√Î
	 */
	public static int time2Long(String timestr){
		String s[] = timestr.split(":");
		int min = Integer.parseInt(s[0]);
		String ss[] =s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		
		return min*60*1000 + sec*1000 + mill*10;
	}
	
}
