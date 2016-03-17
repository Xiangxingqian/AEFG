package com.app.test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A class inserted into App.
 * */
public class Util {
	public static String[] unAnalyzeMethod = { CallBack.ONDRAG,
			CallBack.ONPREFERENCETREECLICK, CallBack.onInflate,
			CallBack.ONEDITORACTION, CallBack.ONKEY, CallBack.ONCHILDVIEWADDED,
			CallBack.ONCHILDVIEWREMOVED, CallBack.DialogInterface_onKey,
			CallBack.ONCREATECONTEXTMENU};
	
	public static List<String> unAnalyzeList = Arrays.asList(unAnalyzeMethod);
	
	public final static int viewId = 0;
	public final static int dialogId = 1;
	/**
	 * log list events, default callback is "onListItemClicked".
	 * @param eventHandler listActivity, invoker of onListItemClicked, 
	 * @return log list events.
	 * */
	public static void LogList(Object eventHandler){
		String info = "List Event in Activity: "+eventHandler.getClass().getName()+" callBack: onListItemClick";
		android.util.Log.v("EVENT", info);
	}
	
	public static void LogPreference(Object eventHandler){
		String info = "Preference Event in PreferenceActivity: "+eventHandler.getClass().getName();
		android.util.Log.v("EVENT", info);
	}
	
	/**
	 * log view, dialog and menuItem's events.
	 * @param uiElement UI elements: view, dialog or menuItem
	 * @param eventHandler Event handler of o;
	 * @param callback name of method invoking o;
	 * @return log events. Tag is EVENT.
	 * */
	public static void Log(Object uiElement, Object eventHandler, String callBack){
		String info = "";
		if(uiElement==null||eventHandler==null||callBack==null){
			throw new RuntimeException("Input parameters are invalid.");
		}
		if(uiElement instanceof View){
			View v = (View)uiElement;
			int id = v.getId();
			Context context = v.getContext();
			String activityName = context.getClass().getName();
			info = info+" View Event in Activity: "+activityName+" id: "+id;
		}
		else if(uiElement instanceof Dialog){
			Dialog dialog = (Dialog)uiElement;
			String activityName = dialog.getContext().getClass().getName();
			info = info+" Dialog Event in Activity: "+activityName;
		}
		else if(uiElement instanceof MenuItem){
			MenuItem menuItem = (MenuItem)uiElement;
			info = info+" Menu Event MenuItem: "+menuItem.getTitle();
		}
		String eventHandlerName = eventHandler.getClass().getName();
		info = info+" eventHandler: "+eventHandlerName+" callBack: "+callBack+" Happens!!!";
		android.util.Log.v("EVENT", info);
	}
	
	/**
	 * Log BroadCast Receiver event.
	 * @param broadcastReceiver first parameter of onReceiver(BroadcastReceiver, Intent);
	 * @param intent second parameter of onReceiver(BroadcastReceiver, Intent);
	 * */
	public static void LogReceiverEvent(BroadcastReceiver broadcastReceiver,Intent intent){
		String receiverName = broadcastReceiver.getClass().getName();
		String action = intent.getAction();
		String info = "BroadCastReceiver Event Receiver Name: "+receiverName+" Action: "+action+" Happens!!!!!!";
		android.util.Log.v("EVENT", info);
	}
	
	/**
	 * Log service event.
	 * @param m callback of event handler.
	 * @param manager Manager manage service event.
	 * @param listener event handler of m.
	 * @return log the three info.
	 * */
	public static void LogServiceEvent(Method m,Object manager,Object listener){
		String method = m.getName();
		String managerName = manager.getClass().getName();
		String listenerName = listener.getClass().getName();
		String info = "Service Event Manager: "+managerName+" Listener: "+listenerName+" Method: "+method+" Happens!!!!!!";
		android.util.Log.v("EVENT", info);
	}
	
//	public static void doPreferenceTest(Activity activity){
//		PreferenceActivity pActivity = (PreferenceActivity)activity;
//		PreferenceScreen pScreen = pActivity.getPreferenceScreen();
//		int cou = pScreen.getPreferenceCount();
//		ListView listView = pActivity.getListView();
//		ListAdapter listAdapter = pActivity.getListAdapter();
//		for(int i=0;i<cou;i++){
//			long itemId = listAdapter.getItemId(i);
//			pScreen.onItemClick(listView, listView.getChildAt(i), i, itemId);
//		}
//	}
	public static void doPreferenceTest(Activity activity){
		PreferenceActivity pActivity = (PreferenceActivity)activity;
		PreferenceScreen pScreen = pActivity.getPreferenceScreen();
//		int cou = pScreen.getPreferenceCount();
		ListView listView = pActivity.getListView();
		int count = listView.getCount();
		Log.v("EVENT", "listView count: "+count+" listView child size: "+listView.getChildCount());
		for(int i=0;i<count;i++){
			pScreen.onItemClick(listView, null, i, 0);
		}
	}
	
	/**
	 * When an exception occurs, out print it.
	 * */
	public static void LogException(Exception e){
		if(e instanceof InvocationTargetException){
			InvocationTargetException ite = (InvocationTargetException)e;
			Throwable targetException = ite.getTargetException();
			if(targetException instanceof NoSuchFieldException){
				return;
			}
			android.util.Log.v("EVENT","Bug Detected, InvocationTargetException!!! Message: "+e.getMessage()+" Caused by: "+e.getCause());
			targetException.printStackTrace();
		}
		else if(e instanceof NoSuchFieldException){
			//NoSuchFieldException is introduced by us, so ignore it.
		}
		else {
			android.util.Log.v("EVENT","Bug Detected!!! Message: "+e.getMessage()+" Caused by "+e.getCause());
			e.printStackTrace();
		}
	}
	
	/**
	 * Do testing for Android App. 
	 * @param UIs ui elements respponding to events
	 * @param handlers event handlers handling events
	 * @param registar event handler use registar to register a events.
	 * @param registarToCallBacks registar to callbacks.
	 * @param iuiTestor it is a view event or dialog event.
	 * @return events are triggerred.
	 * */
	public static void doTest(LinkedList UIs, LinkedList<Object> handlers, LinkedList<String> registarList,Map<String, List<String>> registarToCallBacks, int dialogOrView){
		for(int i =0;i<UIs.size();i++){
			Object UI = UIs.get(i);
			Object object = handlers.get(i);
			String registar = registarList.get(i);
			List<String> callbacks = registarToCallBacks.get(registar);
			Class<? extends Object> eventHanler = object.getClass();
			if(callbacks!=null){
				for(String callback:callbacks){
					Method method = getMethod(eventHanler, callback);
					//Log events
					if(!unAnalyzeList.contains(callback))
						Log(UI, object, callback);
					if(dialogOrView==viewId)
						doViewAnalysis(method, (View)UI, object);
					else if(dialogOrView==dialogId){
						doDialogAnalysis(method, (Dialog)UI, object);
					}
				}
			}
		}
	}
	
	/**
	 * This method will be instrumented into App. When invoking onMenuItemClick(), this method will be invoked.
	 * */
	public static void doDialogTest(LinkedList<Dialog> dialogs, LinkedList<Object> handlers, LinkedList<String> registarList){
		doTest(dialogs, handlers, registarList, CallBack.dialogToCallBacks, dialogId);
	}
	
	/**
	 * This method will be instrumented into App. When invoking onMenuItemClick(), this method will be invoked.
	 * */
	public static void doViewTest(LinkedList<View> views, LinkedList<Object> handlers, LinkedList<String> registarList){
		Map<String,List<String>> map = readCallBack(AppDir.desria+AppDir.appName+".txt");
		Map<String,List<String>> callbacks = map==null?CallBack.viewToCallBacks:map;
		doTest(views, handlers, registarList, callbacks, viewId);
	}

	private static void doViewAnalysis(Method method, View view, Object object) {
		String subSig = getSubsignature(method);
		try{
			//void onStartTrackingTouch(android.widget.SeekBar)
			//void onStopTrackingTouch(android.widget.SeekBar)
			//void onClick(android.view.View)
			//boolean onLongClick(android.view.View)
			//void onNothingSelected(android.widget.AdapterView)
			if ((CallBack.ONLONGCLICK.equals(subSig)) || (CallBack.ONCLICK.equals(subSig))
					|| (CallBack.ONNOTHINGSELECTED.equals(subSig))
					|| (CallBack.onStartTrackingTouch.equals(subSig))
					|| (CallBack.onStopTrackingTouch.equals(subSig))) {
		        method.invoke(object, new Object[] { view });
		    }
			//void onCheckedChanged(android.widget.CompoundButton,boolean)
			//void onFocusChange(android.view.View,boolean)
			else if((CallBack.ONFOCUSCHANGE.equals(subSig)) || (CallBack.onCheckedChanged.equals(subSig))){
		        Object[] params = new Object[2];
		        params[0] = view;
		        params[1] = true;
		        method.invoke(object, params);
		        params[1] = false;
		        method.invoke(object, params);
		    }
			//void onSystemUiVisibilityChange(int)
			else if (CallBack.ONSYSTEMUIVISIBILITYCHANGE.equals(subSig)){
		        method.invoke(object, new Object[] {0});
		        method.invoke(object, new Object[] {8});
		    }
			//void onItemClick(android.widget.AdapterView,android.view.View,int,long)
			//boolean onItemLongClick(android.widget.AdapterView,android.view.View,int,long)
			//void onItemSelected(android.widget.AdapterView,android.view.View,int,long)
			else if ((CallBack.ONITEMCLICK.equals(subSig))
					|| (CallBack.ONITEMLONGCLICK.equals(subSig))
					|| (CallBack.ONITEMSELECTED.equals(subSig))) {
				AdapterView adapterView = (AdapterView) view;
				int childCount = adapterView.getChildCount()>5?5:adapterView.getChildCount();
				
				for (int i = 0; i < childCount; i++) {
					View childView = adapterView.getChildAt(i);
					Adapter adapter = adapterView.getAdapter();
					long itemId = adapter.getItemId(i);
					method.invoke(object, new Object[] { view, childView, i,
							itemId });
				}
			}
			//void onProgressChanged(android.widget.SeekBar,int,boolean)
			else if (CallBack.onProgressChanged.equals(subSig)){
		        Object[] params = new Object[3];
		        SeekBar seekBar = (SeekBar)view;
		        params[0] = view;
		        int max = seekBar.getMax(); //set process = max/2
		        params[1] = max/2;
		        params[2] = true;
		        method.invoke(object, params);
		        params[2] = false;
		        method.invoke(object, params);
		    }
			//锟斤拷锟斤拷状态锟斤拷0锟斤拷 未锟斤拷锟斤拷 1锟斤拷 锟斤拷锟节伙拷锟斤拷 2锟斤拷 锟斤拷锟斤拷锟斤拷锟斤拷
			//void onScrollStateChange(android.widget.AbsListView,int)
			else if (CallBack.onScrollStateChanged.equals(subSig)){
				for (int i = 0; i < 3; i++) {
					method.invoke(object, new Object[] { view, i });
				}
		    }
			//void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,int totalItemCount);
			else if (CallBack.onScroll.equals(subSig)){
				AbsListView listView = (AbsListView)view;
				int childCount = listView.getCount();
				int start = listView.getFirstVisiblePosition();
				int stop = listView.getLastVisiblePosition();
				method.invoke(object, new Object[] {view,start,stop,childCount});
		    }
			//状态锟侥变：锟斤拷止锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			//void onPageScrollStateChanged(int)
			else if (CallBack.onPageScrollStateChanged.equals(subSig)){
				for (int i = 0; i < 3; i++) {
					method.invoke(object, new Object[] {i});
				}
		    }
			//void onPageScrolled(int,float,int)
			else if (CallBack.onPageScrolled.equals(subSig)){
		        method.invoke(object, new Object[] {0, 0.2f, 0});
		        method.invoke(object, new Object[] {0, 0.8f, 0});
		    }
			//选锟叫第讹拷锟斤拷锟斤拷锟斤拷
			//void onPageSelected(int)
			else if (CallBack.onPageSelected.equals(subSig)){
		        method.invoke(object, new Object[] {1});
		    }
			//boolean onClose()
			else if (CallBack.onClose.equals(subSig)) {
				method.invoke(object);
			}
			//void onScrollStateChange(android.widget.NumberPicker,int)
			else if (CallBack.NumberPicker_onScrollStateChange.equals(subSig)) {
				for(int i=0;i<3;i++){
					method.invoke(object, new Object[]{view, i});
				}
			}
			//boolean onQueryTextSubmit(java.lang.String)
			else if (CallBack.onQueryTextSubmit.equals(subSig)) {
				method.invoke(object, "hello android");
			}
			//boolean onQueryTextChange(java.lang.String)
			else if (CallBack.onQueryTextChange.equals(subSig)) {
				method.invoke(object, "hello java");
			}
			//boolean onGenericMotion(android.view.View,android.view.MotionEvent)
			else if (CallBack.ONGENERICMOTION.equals(subSig)) {
				MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_SCROLL, view.getX(), view.getY(), 0);
				method.invoke(object, new Object[]{view,downEvent});
			}
			//boolean onTouch(android.view.View,android.view.MotionEvent)
			else if (CallBack.ONTOUCH.equals(subSig)) {
				MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, view.getX(), view.getY(), 0);
				method.invoke(object, new Object[]{view,downEvent});
				MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, view.getX(), view.getY(), 0);
				method.invoke(object, new Object[]{view,upEvent});
			}
			//void onRatingChanged(android.widget.RatingBar,float,boolean)
			else if (CallBack.onRatingChanged.equals(subSig)) {
				RatingBar ratingBar = (RatingBar)view;
				int numStars = ratingBar.getNumStars();
				float rating = numStars/2;
				method.invoke(object, new Object[]{view,rating,true});
			}
			//boolean onHover(android.view.View,android.view.MotionEvent)
			else if (CallBack.ONHOVER.equals(subSig)) {
				MotionEvent centerEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_HOVER_ENTER, view.getX(), view.getY(), 0);
				method.invoke(object, new Object[]{view,centerEvent});
				MotionEvent exitEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_HOVER_EXIT, view.getX(), view.getY(), 0);
				method.invoke(object, new Object[]{view,exitEvent});
			}
			//boolean onSuggestionSelect(int)
			else if (CallBack.onSuggestionSelect.equals(subSig)) {
				SearchView sView = (SearchView)view;
				CursorAdapter suggestionsAdapter = sView.getSuggestionsAdapter();
				int count = suggestionsAdapter.getCount();
				if(count!=0)
					method.invoke(object, count-1);
			}
			//boolean onSuggestionClick(int)
			else if (CallBack.onSuggestionClick.equals(subSig)) {
				SearchView sView = (SearchView)view;
				CursorAdapter suggestionsAdapter = sView.getSuggestionsAdapter();
				int count = suggestionsAdapter.getCount();
				if(count!=0)
					method.invoke(object, count-1);
			}
			//boolean onDrag(android.view.View,android.view.DragEvent)
			else if (CallBack.ONDRAG.equals(subSig)) {
				//TODO
			}
			//boolean onPreferenceTreeClick(android.preference.PreferenceScreen,android.preference.Preference)
			else if (CallBack.ONPREFERENCETREECLICK.equals(subSig)) {
				//TODO
			}
			//void onInflate(android.view.ViewStub,android.view.View)
			else if (CallBack.onInflate.equals(subSig)) {
				//TODO
			}
			//boolean onEditorAction(android.widget.TextView,int,android.view.KeyEvent)
			else if (CallBack.ONEDITORACTION.equals(subSig)) {
				//TODO
			}
			//boolean onKey(android.view.View,int,android.view.KeyEvent)
			else if (CallBack.ONKEY.equals(subSig)) {
				//TODO
			}
			//void onChildViewAdded(android.view.View,android.view.View)
			//void onChildViewRemoved(android.view.View,android.view.View)
			else if (CallBack.ONCHILDVIEWADDED.equals(subSig)
					|| CallBack.ONCHILDVIEWREMOVED.equals(subSig)) {
				//TODO
			}
			//void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenuInfo)
			else if (CallBack.ONCREATECONTEXTMENU.equals(subSig)) {
				//TODO
			}
			//used to handle event from layout event. default invoke m(view)
			else {
				method.invoke(object, new Object[] {view});
			}
		}catch (Exception localException){
			LogException(localException);
	    }		
	}
	
	public static void doDialogAnalysis(Method method, Dialog dialog, Object object){
		String subSig = getSubsignature(method);
		try {
			//void onCancel(android.content.DialogInterface)
			//void onDismiss(android.content.DialogInterface)
			//void onShow(android.content.DialogInterface)
			if ((CallBack.DialogInterface_onDismiss.equals(subSig)) || (CallBack.DialogInterface_onShow.equals(subSig))
					|| (CallBack.DialogInterface_onCancel.equals(subSig))) {
				method.invoke(object, new Object[] { dialog });
			}
			//void onClick(android.content.DialogInterface,int)
			else if (CallBack.DialogInterface_onClick.equals(subSig)) {
				method.invoke(object, new Object[] { dialog, -1 });
				method.invoke(object, new Object[] { dialog, -2 });
				method.invoke(object, new Object[] { dialog, -3 });
			}
			//void onClick(android.content.DialogInterface,int,boolean)
			else if (CallBack.DialogInterface_OnMultiChoiceClickListener_onClick.equals(subSig)) {
				method.invoke(object, new Object[] { dialog, -1, true });
				method.invoke(object, new Object[] { dialog, -2, true });
				method.invoke(object, new Object[] { dialog, -3, true });
			}
			//boolean onKey(android.content.DialogInterface,int,android.view.KeyEvent)
			else if (CallBack.DialogInterface_onKey.equals(subSig)) {
				//TODO
			}
		} catch (Exception localException) {
			LogException(localException);
		}
	}
	
	//Android锟斤拷锟睫凤拷锟斤拷Java锟侥凤拷锟斤拷锟饺ctivity锟斤拷onCreate, onStart锟饺凤拷锟斤拷锟斤拷锟斤拷烁梅锟斤拷锟斤拷锟叫�
//	public static void doListItemTest(Object object){
//		LogList(object);
//		ListActivity listActivity = (ListActivity)object;
//		Method method = getMethod(object.getClass(),"void onListItemClick(android.widget.ListView,android.view.View,int,long)");
//		try {
//			ListView listView = listActivity.getListView();
//			int childCount = listView.getChildCount();
//			for(int i=0;i<childCount;i++){
//				View childView = listView.getChildAt(i);
//				long l = i;
//				method.invoke(object, new Object[] { listView, childView, i, l });
//			}
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static String getSubsignature(Method m){
		Class<?>[] parameterTypes = m.getParameterTypes();
		String returnValue = m.getReturnType().getName();
		String sig = returnValue+" "+m.getName()+"(";
		StringBuilder sb = new StringBuilder(sig);
		for(int i=0;i<parameterTypes.length;i++){
			sb.append(parameterTypes[i].getName());
			if(i+1<parameterTypes.length){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * return an method whose sub signature equals to the given subSignature in class.
	 * */
	private static Method getMethod(Class declaringClass,String subSignature){
		Method[] methods = declaringClass.getMethods();
		for(Method m:methods){
			if(subSignature.equals(getSubsignature(m))){
				return m;
			}
		}
		throw new RuntimeException("Class "+declaringClass+" does not contain method: "+subSignature);
	}
	
	public static void outPrint(String s){
		android.util.Log.v("SystemOut", s);
	}
	
	/**
	 * read CallBack.idToCallBack from AdobeReader.txt
	 * */
	private static Map<String, List<String>> readCallBack(String location) {
		try{
			FileInputStream fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			//viewToCallBacks locate in the first object. @see{Main#serializeObject}
			Map<String, List<String>> viewToCallBacks = (Map<String, List<String>>) o;
			ois.close();
			fis.close();
			return viewToCallBacks;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
