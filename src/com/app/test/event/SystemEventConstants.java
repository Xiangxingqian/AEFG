package com.app.test.event;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.app.test.CallBack;

public class SystemEventConstants {
//	public static final String receiver_registerReceiver = "void registerReceiver(android.content.BroadcastReceiver,android.content.IntentFilter)";
//	//LocationManager
//	public static final String service_addGpsStatusListener = "boolean addGpsStatusListener(android.location.GpsStatus$Listener)";
//	public static final String service_requestLocationUpdates = "void requestLocationUpdates(java.lang.String,long,float,android.location.GpsStatus$Listener)";
//	public static final String service_addProximityAlert = "void addProximityAlert(double,double,float,long,android.app.PendingIntent)";
//	public static final String service_requestSingleUpdate = "void requestSingleUpdate(java.lang.String,android.location.LocationListener,android.os.Looper)";
//	public static final String service_addNmeaListener = "boolean addNmeaListener(android.location.GpsStatus$NmeaListener)";
//
//	//SensorManager
//	public static final String service_registerListener1 = "boolean registerListener(android.hardware.SensorEventListener,int)";
//	public static final String service_registerListener2 = "boolean registerListener(android.hardware.SensorEventListener,int,int)";
//	public static final String service_registerListener3 = "boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int)";
//	public static final String service_registerListener4 = "boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,android.os.Handler)";
//	
//	//AudioManager
//	public static final String service_registerMediaButtonEventReceiver = "void registerMediaButtonEventReceiver(android.content.ComponentName)";
//	public static final String service_requestAudioFocus = "int requestAudioFocus(android.media.AudioManager$OnAudioFocusChangeListener,int,int)";
//	
//	//TelephoneManager
//	public static final String service_listen = "void listen(android.telephony.PhoneStateListener,int)";
//
	public static final String[] serviceType1 = {
			CallBack.service_addGpsStatusListener,
			CallBack.service_addNmeaListener,
			CallBack.service_registerListener1,
			CallBack.service_registerListener2,
			CallBack.service_registerListener3,
			CallBack.service_registerListener4,
			CallBack.service_requestAudioFocus, 
			//ÔÝ²»¿¼ÂÇ
			CallBack.service_listen};
	public static final String[] serviceType2 = {CallBack.service_requestSingleUpdate};
	public static final String[] serviceType4 = {CallBack.service_requestLocationUpdates};
	
	public static final Set<String> extenalServicesList = CallBack.getSerViceRegistars();
	
	//listener in paramter 1
	public static final List<String> serviceType1List = Arrays.asList(serviceType1);
	//listener in paramter 2
	public static final List<String> serviceType2List = Arrays.asList(serviceType2);
	//listener in paramter 4
	public static final List<String> serviceType4List = Arrays.asList(serviceType4);
	
}
