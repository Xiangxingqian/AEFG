package com.test.xmldata;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.test.data.AndroidIntentFilter;

public class ManifestData implements IXmlData{
	
	public static String apk;
	public static String mainActivity;
	public static Set<String> activities;
	public static Map<String, List<AndroidIntentFilter>> activityToFilters;
	public static Map<String, List<AndroidIntentFilter>> serviceToFilters;
	public static Map<String, List<AndroidIntentFilter>> receiverToFilters;
	public static String pkg;
	
	public ManifestData(String apk){
		this.apk = apk;
	}
	
	public void build(){
		ProcessManifest processMan = new ProcessManifest();
		processMan.loadManifestFile(apk);
		mainActivity = processMan.mainActivity;
		pkg = processMan.getPackageName();
		activities = processMan.getEntryPointClasses();
		activityToFilters = processMan.getActivityToFilters();
		serviceToFilters = processMan.getServiceToFilters();
		receiverToFilters = processMan.getReceiverToFilters();
	}
}
