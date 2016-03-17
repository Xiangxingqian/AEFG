package com.app.test;

import java.io.File;
import java.util.LinkedList;


/**
 * AppDir will be instrumented into Android App.
 * */
public class AppDir {
	public final static String appName = "APKNAME";
	public final static String desria = "/mnt/sdcard/";
	// list all the apps' name
//	public static void main(String[] args){
//		File file = new File("D:/WorkSpace/Administrator/workspace/svn/TestedApk_Simple/");
//		String[] list = file.list();
//		String string = "[";
//		for(String s:list){
//			if(!s.endsWith(".apk"))
//				continue;
//			s = s.substring(0,s.lastIndexOf(".apk"));
//			string = string+"\""+s+"\",";
//		}
//		string = string+"]";
//		System.out.println(string);
//	}
	public static LinkedList<Integer> linkedList = new LinkedList<Integer>();
	static{
		for(int i =0;i<500;i++)
			linkedList.add(0);
	}
	
	public static int visitedMethodCount = 0;
}
