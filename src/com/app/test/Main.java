package com.app.test;

import infolow.resource.ResoureHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import com.test.xmldata.LayoutData;
import com.test.xmldata.ManifestData;

/**
 * good programming<--beauty<--perfect
 * */
public class Main {
	
	public static int methodsCount;
	public static int classesCount;
	public static int linesCount;
	public static int activityCount;
	
	public static void main(String[] args){
		//windows
		String params[] = {"-android-jars","D:/adt-eclipse/sdk/platforms","-process-dir", "L:/APK/"+args[0]+".apk"};

//		String params[] = {"-android-jars","D:/adt-eclipse/sdk/platforms","-process-dir", "D:/WorkSpace/Administrator/workspace/svn/TestedApk_Simple/"+args[0]+".apk"};
//		String params[] = {"-android-jars","D:/adt-eclipse/sdk/platforms","-process-dir", "D:/WorkSpace/Administrator/workspace/svn/TestedApk_Top100/"+args[0]+".apk"};
		
		loadXmlData(params[3]);
		initSoot(params);
		System.out.println("MainActivity: "+ManifestData.mainActivity+"\nNumbers of Activities: "+ManifestData.activities.size()+"\nActivities: "+ManifestData.activities);
		for(int i: LayoutData.idToCallBack.keySet()){
			System.out.println("id: "+i+" methods: "+LayoutData.idToCallBack.get(i));
		}
		activityCount = ManifestData.activities.size();
		
		//与new AndroidInstrumentor(params)？
		AndroidInstrumentor androidInstrument = new AndroidInstrumentor();
		androidInstrument.instrument(params);
		
		//output info
		System.out.println("App "+AppDir.appName+" is instrumented successfully!!!");
		String countInfo = "Activitys Count: "+activityCount+" Methods Count:"+methodsCount+" Classes Count: "+classesCount+" LOC:"+linesCount;
		String pkg = "MOC/"+ManifestData.pkg+".txt";
		System.out.println(countInfo);
		writeFile(pkg, countInfo);
		
		String seriDir ="instrumentedApps/" + AppDir.appName + ".txt";
		serializeObject(seriDir);
	}	

	public static void initSoot(String[] args){
		Options.v().set_soot_classpath(
				"lib/rt.jar;" +
				"lib/jce.jar;" +
				"lib/tools.jar;" +
				"lib/android.jar;"+
				"lib/android-support-v4.jar;"+
				"bin"
				);	
//		Options.v().set_validate(true);
		//Options.v().set_android_jars(args[1]);
		Options.v().set_src_prec(Options.src_prec_apk);
//		Options.v().set_output_format(Options.output_format_jimple);
//		Options.v().set_output_dir("JimpleOutput");
		Options.v().set_output_format(Options.output_format_dex);
		Options.v().set_output_dir("instrumentedApps");
		Options.v().set_allow_phantom_refs(true);

		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.LinkedList",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.reflect.Field",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.reflect.Method",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Class",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Boolean",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Exception",SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.widget.Toast",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Throwable",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.AbstractCollection",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Object",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Thread",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.util.Log",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.SIGNATURES);
        Scene.v().addBasicClass("javax.crypto.KeyGenerator",SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.app.ListActivity",SootClass.SIGNATURES);
        Scene.v().addBasicClass("com.app.test.CallBack",SootClass.BODIES);
        Scene.v().addBasicClass("android.app.Dialog",SootClass.BODIES);
        Scene.v().addBasicClass("android.view.MenuItem",SootClass.BODIES);
        Scene.v().addBasicClass("android.view.View",SootClass.BODIES);
        Scene.v().addBasicClass("android.content.Context",SootClass.BODIES);
        Scene.v().addBasicClass("android.view.MenuItem$OnMenuItemClickListener",SootClass.BODIES);
        Scene.v().addBasicClass("com.app.test.Util",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.event.SystemEventHandler",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.event.SystemEventHandler$AppMenuItemClickListener",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.event.SystemEventConstants",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.event.SystemEvent",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.event.ReceiverEvent",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.AppDir",SootClass.BODIES);
		Scene.v().addBasicClass("com.app.test.data.AndroidIntentFilter",SootClass.BODIES);
	}
	public static void loadXmlData(String apk){
		ManifestData manifestData = new ManifestData(apk);
		manifestData.build();
		ResoureHandler rHandler = new ResoureHandler(apk);
		rHandler.parseResourceLayout();
		LayoutData layoutData = new LayoutData(rHandler.getResources());
		layoutData.build();
	}
	
	/**
	 * We should serialize the info we get in static analysis. 
	 * The info will be used when the program starts.<br>
	 * 
	 * The storage objects are four maps:<br>
	 * 1. viewToCallBacks<br>
	 * 2. activityToFilters<br>
	 * 3. serviceToFilters<br>
	 * 4. receiverToFilters<br>
	 * 5. mainActivity<br>
	 * */
	private static void serializeObject(String location) {
		File file = new File(location);
		try {
			if(file.exists()){
				file.delete();
				file = new File(location);
			}
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(CallBack.viewToCallBacks);
			oos.writeObject(ManifestData.activityToFilters);
			oos.writeObject(ManifestData.serviceToFilters);
			oos.writeObject(ManifestData.receiverToFilters);
			oos.writeObject(ManifestData.mainActivity);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String pathName,String content){
		File file = new File(pathName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static Map<String, String> map = new HashMap<String, String>();
}
