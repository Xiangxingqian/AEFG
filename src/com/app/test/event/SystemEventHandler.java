package com.app.test.event;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.app.test.AppDir;
import com.app.test.CallBack;
import com.app.test.Util;
import com.app.test.data.AndroidIntentFilter;

/**
 * Handle System events from both Manifest.xml and code.
 * 
 * */
public class SystemEventHandler{

	public static String location = AppDir.desria+AppDir.appName+".txt";
	
	/**
	 * trigger system events, includes receiver event from either code or manifest.xml, service in code.
	 * @param activity used as onReceiver's context.
	 * */
	public static void doSystemEventTest(Activity activity){
		try {
			//read mainActivity from location. 
			Class mainActivity = Class.forName(readMainActivity(location));
			Field serviceOrReceivers = mainActivity.getField("systemEventLinkedList");
			Object object = serviceOrReceivers.get(null);
			LinkedList<SystemEvent> systemEventLinkedList = (LinkedList<SystemEvent>)object;
			for(SystemEvent systemEvent:systemEventLinkedList){
				Object listener = systemEvent.getListener();
				String registar = systemEvent.getRegistar();
				Object manager = systemEvent.getManager();
				//trigger receiver events from code
				if(systemEvent instanceof ReceiverEvent){
					ReceiverEvent receiverSyetemEvent = (ReceiverEvent)systemEvent;
					IntentFilter intentFilter = receiverSyetemEvent.getIntentFilter();
					BroadcastReceiver broadcastReceiver = (BroadcastReceiver)listener;
					Intent intent = initIntent(intentFilter);
					if(intent!=null){
						Util.LogReceiverEvent(broadcastReceiver, intent);
						broadcastReceiver.onReceive(activity, intent);
					}
				}
				//trigger service events from code
				else{
					if(CallBack.serviceToCallBacks.containsKey(registar)){
						List<String> list = CallBack.serviceToCallBacks.get(registar);
						for(Method m:listener.getClass().getMethods()){
							String subsignature = Util.getSubsignature(m);
							if(list.contains(subsignature)){
								Util.LogServiceEvent(m, manager, listener);
								doServiceAnalysis(m, manager, listener);
							}
						}
					}
				}
			}
			
			//trigger receiver events from layout
			Map<String, List<AndroidIntentFilter>> receiverToFilters = readReceiverToFilters(location);
			for(String key:receiverToFilters.keySet()){
				for(AndroidIntentFilter value:receiverToFilters.get(key)){
					Class receiverClass = Class.forName(key);
					Object receiver = receiverClass.newInstance();
					BroadcastReceiver broadcastReceiver = (BroadcastReceiver)receiver;
					Intent intent = initIntent(value);
					Util.LogReceiverEvent(broadcastReceiver, intent);
					broadcastReceiver.onReceive(activity, intent);
				}
			}
		} catch (Exception e) {
			Util.LogException(e);
		}finally{
			Log.v("EVENT", AppDir.visitedMethodCount+"");
		}
	}
	
	/** 
	* add TestSystemEvent to menu
	* @param activity used by onReceiver(Context, intent);
	* @param menu Add menuItem to menu
	*/
	public static void addMenuItem(final Activity activity,Menu menu){
		SystemEventHandler systemEventHandler = new SystemEventHandler();
		MenuItem add = menu.add("TestSystemEvent");
		AppMenuItemClickListener appMenuItemClickListener = systemEventHandler.new AppMenuItemClickListener(activity);
		add.setOnMenuItemClickListener(appMenuItemClickListener);
	}
	
	class AppMenuItemClickListener implements OnMenuItemClickListener{
		Activity activity;
		public AppMenuItemClickListener(Activity activity) {
			this.activity = activity;
		}
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			doSystemEventTest(activity);
			return true;
		}
		
	}
	//传感器单独考虑
	/**
	 * trigger service events, use listener.onAudioFocusChange(int);
	 * @param m Method to be Triggered.
	 * @param manager system managers used to get sensorList, providers, like LocationManager, SensorManager, AudioManager and TelephoneManager.
	 * @param listener m's declaring class.
	 * @return event triggered.
	  */
	public static void doServiceAnalysis(Method m, Object manager, Object listener){
		String mName = m.getName();
		SensorManager sManager = null;
		LocationManager lManager = null;
		if(manager instanceof SensorManager){
			 sManager =(SensorManager) manager;
		}
		else if(manager instanceof LocationManager){
		     lManager = (LocationManager)manager;
		}
		//6个listener, 10个methods
		try {
			//SensorEventListener: public void onSensorChanged(SensorEvent event);
			//难点是：无法实例化SensorEvent.
			if(mName.equals("onSensorChanged")){
				//TODO
//				List<Sensor> sensorList = sManager.get
//				for(Sensor sensorEvent:sensorList){
//					Object[] args = {sensorEvent};
//					m.invoke(listener, args);
//				}
			}
			//public void onAccuracyChanged(Sensor sensor, int accuracy);  
			else if(mName.equals("onAccuracyChanged")){
				List<Sensor> sensorList = sManager.getSensorList(Sensor.TYPE_ALL);
				for(Sensor sensorEvent:sensorList){
//					public static final int SENSOR_STATUS_UNRELIABLE = 0;
//				    public static final int SENSOR_STATUS_ACCURACY_LOW = 1;
//				    public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;
//				    public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;
					int accuracy = SensorManager.SENSOR_STATUS_ACCURACY_LOW;
					Object[] args = {sensorEvent,accuracy};
					m.invoke(listener, args);
				}
			}
			//OnAudioFocusChangeListener: public void onAudioFocusChange(int focusChange);
			else if(mName.equals("onAudioFocusChange")){
				int[] events = {AudioManager.AUDIOFOCUS_GAIN,AudioManager.AUDIOFOCUS_LOSS,AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK};
				for(int event:events){
					Object[] args = {event};
					m.invoke(listener, args);
				}
			}
			//Listener: void onGpsStatusChanged(int event);
			else if(mName.equals("onGpsStatusChanged")){
				int[] events = {GpsStatus.GPS_EVENT_STARTED,GpsStatus.GPS_EVENT_STOPPED,GpsStatus.GPS_EVENT_FIRST_FIX,GpsStatus.GPS_EVENT_SATELLITE_STATUS};
				for(int event:events){
					Object[] args = {event};
					m.invoke(listener, args);
				}
			}
			//NmeaListener: void onNmeaReceived(long timestamp, String nmea);
			else if(mName.equals("onNmeaReceived")){
				long timestamp = 1l;
				String nmea = "HelloWorld";
				Object[] args = {timestamp,nmea};
				m.invoke(listener, args);
			}
			//LocationListener: void onLocationChanged(Location location);
			else if(mName.equals("onLocationChanged")){
				List<String> allProviders = lManager.getAllProviders();
				if(allProviders.size()>0){
					Location location = lManager.getLastKnownLocation(allProviders.get(0));
					Object[] args = {location};
					m.invoke(listener, args);
				}
			}
			//void onStatusChanged(String provider, int status, Bundle extras);
			else if(mName.equals("onStatusChanged")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					int statuss[] = {LocationProvider.OUT_OF_SERVICE,LocationProvider.AVAILABLE,LocationProvider.TEMPORARILY_UNAVAILABLE};
					//TODO
					Bundle extras = null;
					for(int status:statuss){
						Object[] args = {provider,status,extras};
						m.invoke(listener, args);
					}
				}
			}
			//void onProviderEnabled(String provider);
			else if(mName.equals("onProviderEnabled")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					Object[] args = {provider};
					m.invoke(listener, args);
				}
			}
			//void onProviderDisabled(String provider);
			else if(mName.equals("onProviderDisabled")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					Object[] args = {provider};
					m.invoke(listener, args);
				}
			}
			//PhoneStateListener: TODO
//			else if(mName.equals("onGpsStatusChanged")){
//				int event;
//				Object[] args = {event};
//				m.invoke(listener, args);
//			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * according to intentFilter's data build an intent.
	 * @param intentFilter intent filter comes from manifest.xml.
	 * @return intent An intent can trigger inter-app events.
	 * */
	private static Intent initIntent(AndroidIntentFilter intentFilter) {
		String action = null;
		
		if(intentFilter.countActions()==0){
			return null;
		}
		for(String mAction: intentFilter.getmActions()){
			if(!mAction.equals(Intent.ACTION_MAIN)){
				action = mAction;
				break;
			}
		}
		if(action==null) {
			Log.v("EVENT", " Intent Action is null");
			return null;
		};
		
		Intent intent = new Intent(action);
		if(intentFilter.countCategories()>0){
			intent.addCategory(intentFilter.getCategory(0));
		}
		else 
			intent.addCategory(Intent.CATEGORY_DEFAULT);
		if(intentFilter.countDataTypes()>0){
			intent.setType(intentFilter.getDataType(0));
		}
		return intent;
	}
	
	private static Intent initIntent(IntentFilter intentFilter) {
		String action;
		if(intentFilter.countActions()>1){
			action = intentFilter.getAction(1);
		}
		else if(intentFilter.countActions()>0){
			action = intentFilter.getAction(0);
		}
		else {
			Log.v("EVENT", " Intent Action is null");
			return null;
		}
		Intent intent = new Intent(action);
		if(intentFilter.countCategories()>0){
			intent.addCategory(intentFilter.getCategory(0));
		}
		else 
			intent.addCategory(Intent.CATEGORY_DEFAULT);
		if(intentFilter.countDataTypes()>0){
			intent.setType(intentFilter.getDataType(0));
		}
		return intent;
	}
	
	private static Map<String, List<AndroidIntentFilter>> readReceiverToFilters(String location){
		Map<String, List<AndroidIntentFilter>> receiverToFilters = new HashMap<String, List<AndroidIntentFilter>>();
		try{
			FileInputStream fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			//read the forth object. There are four objects in location: callback.class, activityToIntentFilters, serviceToIntentFilters and receiverToIntentFilters
			Object readObject1 = ois.readObject();
			Object readObject2 = ois.readObject();
			Object readObject3 = ois.readObject();
			Object readObject4 = ois.readObject();
			receiverToFilters = (Map<String, List<AndroidIntentFilter>>)readObject4;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return receiverToFilters;
	}
	
	private static String readMainActivity(String location){
		String mainActivity = "";
		try{
			FileInputStream fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			//read the forth object. There are four objects in location: callback.class, activityToIntentFilters, serviceToIntentFilters and receiverToIntentFilters
			Object readObject1 = ois.readObject();
			Object readObject2 = ois.readObject();
			Object readObject3 = ois.readObject();
			Object readObject4 = ois.readObject();
			Object readObject5 = ois.readObject();
			mainActivity = (String)readObject5;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mainActivity;
	}
}
