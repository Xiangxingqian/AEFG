package com.app.test;

import soot.RefType;
import soot.Scene;
import soot.SootMethod;

/**
 * 该类不被加入到App中
 * */
public class Constants {
	
	public static final String SYSTEMEVENTLINKEDLIST = "systemEventLinkedList";
	
	public static final String TESTTHREAD = "com.example.qian.TestThread";
	public static final String FINISHTHREAD = "com.example.qian.FinishThread";

	public static final String ISMYEVENT = "isMyEvent";
	public static final String ISVISITED = "isVisited";
	
	public static final String METHODCOUNTLIST = "methodCountList";
	//view: view,registar,listener
	public static final String LISTENERLINKEDLIST = "listenerLinkedList";
	public static final String VIEWLINKEDLIST = "viewLinkedList";
	public static String VIEWREGISTARLINKEDLIST = "viewRegistarLinkedList";
	
	//dialog: dialog,registar,dialoglistener
	public static String DIALOGREGISTARLINKEDLIST = "dialogRegistarLinkedList";
	public static String DIALOGLINKEDLIST = "dialogLinkedList";
	public static String DIALOGLISTENERLINKEDLIST = "dialogListenerLinkedList";
	
	public static final String ACTIVITIES = "activities";
	public static final String UNVISITEDACTIVITIES = "unVisitedActivities";
	
	public static final String CallBack = "com.app.test.CallBack";
	public static final String VIEWTOCALLBACKS = "viewToCallBacks";
	public static final String DIALOGTOCALLBACKS = "dialogToCallBacks";
	
	
	public static final String ACTIVITYMENU = "activityMenu";
//	public static final String FRAGMENTMENU = "fragmentMenu";

	public static String onCreateOptionsMenu_activity = "boolean onCreateOptionsMenu(android.view.Menu)";
//	public static String onCreateOptionsMenu_fragment = "void onCreateOptionsMenu(android.view.Menu,android.view.MenuInflater)";
//	public static String onCreateView_fragment = "android.view.View onCreateView(android.view.MenuInflater,android.view.ViewGroup,android.os.Bundle)";
	
	public static String onMenuItemClick_Name = "boolean onMenuItemClick(android.view.MenuItem)";
	public static String onListItemClick_Name = "void onListItemClick(android.widget.ListView,android.view.View,int,long)";
	
	public static String onOptionsItemSelected_Name = "boolean onOptionsItemSelected(android.view.MenuItem)";
	public static String onContextItemSelected_Name = "boolean onContextItemSelected(android.view.MenuItem)";
	
//	public static String fragment = "android.support.v4.app.Fragment";
	
	public static String activity = "android.app.Activity";
	
	public static RefType listAdapter_Type = RefType.v("android.widget.ListAdapter");
	public static RefType listView_Type = RefType.v("android.widget.ListView");
	public static RefType systemEvent_Type = RefType.v("com.app.test.event.SystemEvent");
	public static RefType receiverEvent_Type = RefType.v("com.app.test.event.ReceiverEvent");
	public static RefType file_Type = RefType.v("java.io.File");
	public static RefType iterator_Type = RefType.v("java.util.Iterator");
	public static RefType list_Type = RefType.v("java.util.List");
	public static RefType map_Type = RefType.v("java.util.Map");
	public static RefType menu_Type = RefType.v("android.view.Menu");
	public static RefType menuItem_Type = RefType.v("android.view.MenuItem");
	public static RefType class_Type = RefType.v("java.lang.Class");
	public static RefType exception_Type = RefType.v("java.lang.Exception");
	public static RefType reflectMethod_Type = RefType.v("java.lang.reflect.Method");
	public static RefType linkedList_Type = RefType.v("java.util.LinkedList");
	public static RefType view_Type = RefType.v("android.view.View");
	public static RefType object_Type = RefType.v("java.lang.Object");
	public static RefType Long_Type = RefType.v("java.lang.Long");
	public static RefType integer_Type = RefType.v("java.lang.Integer");
	public static RefType toast_Type = RefType.v("android.widget.Toast");
	public static RefType string_Type = RefType.v("java.lang.String");
	public static RefType field_Type = RefType.v("java.lang.reflect.Field");
	public static RefType Boolean_Type = RefType.v("java.lang.Boolean");
	public static RefType context_Type = RefType.v("android.content.Context");
	public static RefType intent_Type = RefType.v("android.content.Intent");
	public static RefType testThread_Type = RefType.v("com.example.qian.TestThread");
	public static RefType finishThread_Type = RefType.v("com.example.qian.FinishThread");
	public static RefType bundle_Type = RefType.v("android.os.Bundle");
	public static RefType log_Type = RefType.v("android.util.Log");
	public static RefType stringBuilder_Type = RefType.v("java.lang.StringBuilder");
	public static RefType dialog_Type = RefType.v("android.app.Dialog");
	
	public static SootMethod onListItemClick_method = Scene.v().getMethod("<android.app.ListActivity: void onListItemClick(android.widget.ListView,android.view.View,int,long)>");
//	public static SootMethod setMenuVisibility_method_v4 = Scene.v().getMethod("<android.support.v4.app.Fragment: void setMenuVisibility(boolean)>");

	public static SootMethod openOptionsMenu_method = Scene.v().getMethod("<android.app.Activity: void openOptionsMenu()>");
	public static SootMethod isEmpty_method = Scene.v().getMethod("<java.util.AbstractCollection: boolean isEmpty()>");
	public static SootMethod poll_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object poll()>");
	public static SootMethod getClass_method = Scene.v().getMethod("<java.lang.Object: java.lang.Class getClass()>");
	public static SootMethod getMethods_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Method[] getMethods()>");
	public static SootMethod reflectMethodGetName_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.String getName()>");
	public static SootMethod classGetName_method = Scene.v().getMethod("<java.lang.Class: java.lang.String getName()>");
	public static SootMethod startsWith_method = Scene.v().getMethod("<java.lang.String: boolean startsWith(java.lang.String)>");
	public static SootMethod invoke_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>");
	public static SootMethod printStackTrace_method = Scene.v().getMethod("<java.lang.Throwable: void printStackTrace()>");
	public static SootMethod makeText_method = Scene.v().getMethod("<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>");
	public static SootMethod show_method = Scene.v().getMethod("<android.widget.Toast: void show()>");
	public static SootMethod onCreateOptionsMenu_method = Scene.v().getMethod("<android.app.Activity: boolean onCreateOptionsMenu(android.view.Menu)>");
	public static SootMethod menuAdd_method = Scene.v().getMethod("<android.view.Menu: android.view.MenuItem add(java.lang.CharSequence)>");
	public static SootMethod setOnMenuItemClickListener_method = Scene.v().getMethod("<android.view.MenuItem: android.view.MenuItem setOnMenuItemClickListener(android.view.MenuItem$OnMenuItemClickListener)>");
	public static SootMethod sleep_method = Scene.v().getMethod("<java.lang.Thread: void sleep(long)>");
	public static SootMethod set_method = Scene.v().getMethod("<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>");
	public static SootMethod getMethod_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>");
	public static SootMethod view_getContext_method = Scene.v().getMethod("<android.view.View: android.content.Context getContext()>");
	public static SootMethod dialog_getContext_method = Scene.v().getMethod("<android.app.Dialog: android.content.Context getContext()>");
	public static SootMethod forName_method = Scene.v().getMethod("<java.lang.Class: java.lang.Class forName(java.lang.String)>");
	public static SootMethod getField_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>");
	public static SootMethod fieldGet_method = Scene.v().getMethod("<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>");
	public static SootMethod booleanValue_method = Scene.v().getMethod("<java.lang.Boolean: boolean booleanValue()>");
	public static SootMethod offer_method = Scene.v().getMethod("<java.util.LinkedList: boolean offer(java.lang.Object)>");
	public static SootMethod booleanValueOf_method = Scene.v().getMethod("<java.lang.Boolean: java.lang.Boolean valueOf(boolean)>");
	public static SootMethod onRestart_method = Scene.v().getMethod("<android.app.Activity: void onRestart()>");
	public static SootMethod putExtra_method = Scene.v().getMethod("<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>");
	public static SootMethod startActivity_method = Scene.v().getMethod("<android.app.Activity: void startActivity(android.content.Intent)>");
	public static SootMethod peek_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object peek()>");
	public static SootMethod intentInit_method = Scene.v().getMethod("<android.content.Intent: void <init>(android.content.Context,java.lang.Class)>");
	public static SootMethod getIntent_method = Scene.v().getMethod("<android.app.Activity: android.content.Intent getIntent()>");
	public static SootMethod getExtras_method = Scene.v().getMethod("<android.content.Intent: android.os.Bundle getExtras()>");
//	public static SootMethod bundleGet_method = Scene.v().getMethod("<android.os.Bundle: java.lang.Object get(java.lang.String)>");
	public static SootMethod objectEquals_method = Scene.v().getMethod("<java.lang.Object: boolean equals(java.lang.Object)>");
	public static SootMethod threadStart_method = Scene.v().getMethod("<java.lang.Thread: void start()>");
	public static SootMethod contains_method = Scene.v().getMethod("<java.util.LinkedList: boolean contains(java.lang.Object)>");
	public static SootMethod linkedListInit_method = Scene.v().getMethod("<java.util.LinkedList: void <init>()>");
	public static SootMethod threadInit_method = Scene.v().getMethod("<java.lang.Thread: void <init>()>");
	public static SootMethod getAction_method = Scene.v().getMethod("<android.content.Intent: java.lang.String getAction()>");
	public static SootMethod stringEquals_method = Scene.v().getMethod("<java.lang.String: boolean equals(java.lang.Object)>");
	public static SootMethod linkedListSize_method = Scene.v().getMethod("<java.util.LinkedList: int size()>");
	public static SootMethod getParameterTypes_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.Class[] getParameterTypes()>");
	public static SootMethod stringBuilderInit_method = Scene.v().getMethod("<java.lang.StringBuilder: void <init>()>");
	public static SootMethod stringBuilderAppendString_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>");
	public static SootMethod stringBuilderAppendInt_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.StringBuilder append(int)>");
	public static SootMethod stringBuilderToString_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.String toString()>");
	public static SootMethod logV_method = Scene.v().getMethod("<android.util.Log: int v(java.lang.String,java.lang.String)>");
	public static SootMethod getId_method = Scene.v().getMethod("<android.view.View: int getId()>");
	public static SootMethod logE_method = Scene.v().getMethod("<android.util.Log: int e(java.lang.String,java.lang.String)>");
	
	public static SootMethod menuSize_method = Scene.v().getMethod("<android.view.Menu: int size()>");
	public static SootMethod onOptionsItemSelected_method = Scene.v().getMethod("<android.app.Activity: boolean onOptionsItemSelected(android.view.MenuItem)>");
	public static SootMethod getItem_method = Scene.v().getMethod("<android.view.Menu: android.view.MenuItem getItem(int)>");
	public static SootMethod mapGet_method = Scene.v().getMethod("<java.util.Map: java.lang.Object get(java.lang.Object)>");
	public static SootMethod iterator_method = Scene.v().getMethod("<java.util.List: java.util.Iterator iterator()>");
	public static SootMethod hasNext_method = Scene.v().getMethod("<java.util.Iterator: boolean hasNext()>");
	public static SootMethod next_method = Scene.v().getMethod("<java.util.Iterator: java.lang.Object next()>");
	public static SootMethod integerInit_method = Scene.v().getMethod("<java.lang.Integer: void <init>(int)>");
	public static SootMethod integerValueOf_method = Scene.v().getMethod("<java.lang.Integer: java.lang.Integer valueOf(int)>");
	
	public static SootMethod utilLogList_method = Scene.v().getMethod("<com.app.test.Util: void LogList(java.lang.Object)>");
	public static SootMethod utilLog_method = Scene.v().getMethod("<com.app.test.Util: void Log(java.lang.Object,java.lang.Object,java.lang.String)>");
	public static SootMethod utilLogException_method = Scene.v().getMethod("<com.app.test.Util: void LogException(java.lang.Exception)>");
	
	public static SootMethod utilDoDialogTest_method = Scene.v().getMethod("<com.app.test.Util: void doDialogTest(java.util.LinkedList,java.util.LinkedList,java.util.LinkedList)>");
	public static SootMethod utilDoViewTest_method = Scene.v().getMethod("<com.app.test.Util: void doViewTest(java.util.LinkedList,java.util.LinkedList,java.util.LinkedList)>");
	public static SootMethod outPrint_method = Scene.v().getMethod("<com.app.test.Util: void outPrint(java.lang.String)>");
	public static SootMethod systemEventinit_method = Scene.v().getMethod("<com.app.test.event.SystemEvent: void <init>(java.lang.Object,java.lang.Object,java.lang.String)>");
	public static SootMethod receiverEventinit_method = Scene.v().getMethod("<com.app.test.event.ReceiverEvent: void <init>(java.lang.Object,java.lang.Object,java.lang.String,android.content.IntentFilter)>");
	public static SootMethod systemEventHandleraddMenuItem_method = Scene.v().getMethod("<com.app.test.event.SystemEventHandler: void addMenuItem(android.app.Activity,android.view.Menu)>");
	public static SootMethod getListView_method = Scene.v().getMethod("<android.app.ListActivity: android.widget.ListView getListView()>");
	public static SootMethod getChildCount_method = Scene.v().getMethod("<android.view.ViewGroup: int getChildCount()>");
	public static SootMethod getChildAt_method = Scene.v().getMethod("<android.view.ViewGroup: android.view.View getChildAt(int)>");
	public static SootMethod stringValueOf_method = Scene.v().getMethod("<java.lang.String: java.lang.String valueOf(int)>");
	public static SootMethod longValueOf_method = Scene.v().getMethod("<java.lang.Long: java.lang.Long valueOf(java.lang.String)>");
	public static SootMethod longValue_method = Scene.v().getMethod("<java.lang.Long: long longValue()>");
	public static SootMethod getListAdapter_method = Scene.v().getMethod("<android.app.ListActivity: android.widget.ListAdapter getListAdapter()>");
	public static SootMethod adapterGetItemId_method = Scene.v().getMethod("<android.widget.Adapter: long getItemId(int)>");
	public static SootMethod utilLogPreference_method = Scene.v().getMethod("<com.app.test.Util: void LogPreference(java.lang.Object)>");
	public static SootMethod utilDoPreferenceTest_method = Scene.v().getMethod("<com.app.test.Util: void doPreferenceTest(android.app.Activity)>");
	public static SootMethod integerIntValue_method = Scene.v().getMethod("<java.lang.Integer: int intValue()>");
	public static SootMethod linkedListClone_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object clone()>");
	public static SootMethod linkedListSet_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object set(int,java.lang.Object)>");
	public static SootMethod linkedListGet_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object get(int)>");

}
