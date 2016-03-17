package com.aefg.intent;

import java.util.Arrays;
import java.util.List;

public class IntentConstants {
	
	public static final String SETACTION = "android.content.Intent setAction(java.lang.String)";
	public static final String SETDATA = "android.content.Intent setData(android.net.Uri)";
	public static final String SETTYPE = "android.content.Intent setType(java.lang.String)";
	public static final String ADDCATEGORY = "android.content.Intent addCategory(java.lang.String)";
	public static final String SETDATAANDTYPE = "android.content.Intent setDataAndType(android.net.Uri,java.lang.String)";
	public static final String INTENTACTION = "void <init>(java.lang.String)";
	public static final String INTENTACTIONANDDATA = "void <init>(java.lang.String,android.net.Uri)";
	
	public static final String ACTION_DIAL =  "android.intent.action.DIAL";
	public static final String ACTION_CALL = "android.intent.action.CALL";
	public static final String ACTION_SEND = "android.intent.action.SEND";
	public static final String ACTION_SENDTO = "android.intent.action.SENDTO";
	public static final String ACTION_DELETE = "android.intent.action.DELETE";
	public static final String ACTION_SEARCH =  "android.intent.action.SEARCH";
	public static final String ACTION_WEB_SEARCH =  "android.intent.action.WEB_SEARCH";
	public static final String ACTION_ANSWER =  "android.intent.action.ANSWER";
		
	public static final String ACTION_VIEW = "android.intent.action.VIEW";
	public static final String ACTION_EDIT = "android.intent.action.EDIT";
	public static final String ACTION_PICK = "android.intent.action.PICK";
	public static final String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
	public static final String ACTION_INSERT =  "android.intent.action.INSERT";
	
//	public static final String INITINTENT0 = "android.content.Intent: void <init>()";
//	public static final String INITINTENT1 = "android.content.Intent: void <init>(android.content.Context,java.lang.Class)";
//	public static final String INITINTENT2 = "android.content.Intent: void <init>(android.content.Intent)";
//	public static final String INITINTENT4 = "android.content.Intent: void <init>(java.lang.String,java.net.Uri,android.content.Context,java.lang.Class)";
	
	
	public static final String STARTACTIVITY = "void startActivity(android.content.Intent)";
	public static final String STARTACTIVITYFORRESULT = "void startActivityForResult(android.content.Intent,int)";
	
	
	//setIntent MenuItem & Preference
	public static final String MENUITEMSETINTENT = "<android.view.MenuItem: android.view.MenuItem setIntent(android.content.Intent)>";
	public static final String PREFERENCESETINTENT = "<android.preference.Preference: void setIntent(android.content.Intent)>";

	
	//9种方式
	public static final String INIT1 = "void <init>(android.content.Context,java.lang.Class)";
	public static final String INIT2 = "void <init>(android.content.Intent)";
//	public static final String INIT3 = "void <init>(java.lang.String)";
//	public static final String INIT4 = "void <init>(java.lang.String,android.net.Uri)";
	public static final String INIT3 = "void <init>(java.lang.String,android.net.Uri,android.content.Context,java.lang.Class)";
	
	public static final String SETCLASS1 = "android.content.Intent setClassName(android.content.Context,java.lang.String)";
	public static final String SETCLASS2 = "android.content.Intent setClassName(java.lang.String,java.lang.String)";
	public static final String SETCLASS3 = "android.content.Intent setClass(android.content.Context,java.lang.Class)";
	public static final String SETCOMPONENT = "android.content.Intent setComponent(android.content.ComponentName)";
	
	//intent的定义信息
	//private static String[] INTENT_INITIAL = {INIT1,};
	
	//intent的信息
	private static String[] INTENT = {INIT1,INIT2,INTENTACTION,INTENTACTIONANDDATA,INIT3,SETCLASS1,SETCLASS2,SETCLASS3,
		SETCOMPONENT,SETACTION,SETDATA,SETDATAANDTYPE,ADDCATEGORY,SETTYPE};
	
	//显式intent
	private static String[] ImplicitIntent = {SETACTION,SETDATA,SETDATAANDTYPE,ADDCATEGORY,SETTYPE,
		INTENTACTION,INTENTACTIONANDDATA};
	
	//隐式intent
	private static String[] ExplicitIntent = {INIT1,INIT3,SETCLASS1,SETCLASS2,SETCLASS3,SETCOMPONENT};
	
//	private static String[] INTENT_ONEPARAM = {ACTION_DIAL,ACTION_CALL,ACTION_SEND,
//		ACTION_SENDTO,ACTION_DELETE,ACTION_SEARCH,ACTION_WEB_SEARCH,ACTION_ANSWER};
//	
//	private static String[] INTENT_TWOPARAM = {ACTION_VIEW,ACTION_EDIT,ACTION_GET_CONTENT,
//		ACTION_INSERT,ACTION_PICK};
	
	private static String[] INTENT_ACTIONS = {ACTION_DIAL,ACTION_CALL,ACTION_SEND,
		ACTION_SENDTO,ACTION_DELETE,ACTION_SEARCH,ACTION_WEB_SEARCH,ACTION_ANSWER,
		ACTION_VIEW,ACTION_EDIT,ACTION_GET_CONTENT,ACTION_INSERT,ACTION_PICK};

//	public static List<String> getOneParamIntent(){
//		return Arrays.asList(INTENT_ONEPARAM);
//	}
//	
//	public static List<String> getTwoParamIntent(){
//		return Arrays.asList(INTENT_TWOPARAM);
//	}
	
	public static List<String> getIntentActions(){
		return Arrays.asList(INTENT_ACTIONS);
	}
	
	public static List<String> getIntent_Def() {
		return Arrays.asList(INTENT);
	}

	public static List<String> getImplicitIntent() {
		return Arrays.asList(ImplicitIntent);
	}

	public static List<String> getExplicitIntent() {
		return Arrays.asList(ExplicitIntent);
	}

}
