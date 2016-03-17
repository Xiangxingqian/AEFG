package entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.util.Chain;

import com.aefg.intent.IntentFilter;
import com.test.xmldata.ProcessManifest;
import component.Component;

import entryPointCreator.AndroidEntryPointConstants;
import entryPointCreator.AndroidEntryPointCreator;

public class Entry {
	
	private static CallGraph cg = null;
	private static Set<String> realActivities = null;
	public static Set<String> fakeActivities = new HashSet<String>();
	public static HashSet<String> entrypoints = null;
	
	public List<String> analyzedMethods = new ArrayList<String>();
	
	String apkFileLocation;
	String androidJar ;
	//= "D://adt-eclipse/sdk/platforms";//windows

	//String androidJar = "/home/qian/adt/sdk/platforms"; //linux
	
	public Entry(String apkFileLocation, String androidPlatformsLocation) {	
		this.apkFileLocation = apkFileLocation;
		this.androidJar = androidPlatformsLocation;
	}

	/**
	 * 入口方法的组成： 
	 * 1. 活动类
	 * 2. 活动类的父类
	 * 3. 活动类的内部类
	 * 4. Fragment类。
	 * */
	public void initSoot(){
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_android_jars(androidJar);
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_output_format(Options.v().output_format_dex);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_no_output_source_file_attribute(true);		
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		
//		Options.v().set_soot_classpath(apkFileLocation+":"+
//				"/usr/jvm/javajdk/jre/lib/jce.jar:" +
//				"/usr/jvm/javajdk/lib/tools.jar:" +
//				"/home/qian/adt/sdk/platforms/android-17/android.jar:"
//				);	//linux		
//		Options.v().set_soot_classpath(apkFileLocation+";"+
//				"C:/Program Files/Java/jre7/lib/jce.jar;" +
//				"C:/Program Files/Java/jdk1.7.0_67/lib/tools.jar;" +
//				"D://adt-eclipse/sdk/platforms/android-20/android.jar;"+
//				"D://adt-eclipse/sdk/platforms/android-17/android-support-v4.jar;"
//				);		//windows
		//String androidJarLocation = androidJar+"/android-20/android.jar";
		Options.v().set_soot_classpath(apkFileLocation+";"+
				"/lib/jce.jar;" +
				"/lib/tools.jar;" +
				//androidJar+"/android-20/android.jar;"+
				"lib/android.jar;"+
				"/lib/android-support-v4.jar;"
				);		
		Options.v().setPhaseOption("cg.spark", "on");	
		
		SootMethod entry = setEntryPoints_getMainMethod(); 
		//System.out.println("entrypoints: "+Collections.singletonList(entrypoints));
		Scene.v().setEntryPoints(Collections.singletonList(entry));
		Scene.v().addBasicClass(entry.getDeclaringClass().getName(), SootClass.BODIES);	
		Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.BODIES);
		Scene.v().addBasicClass("java.util.HashSet",SootClass.BODIES);
		Scene.v().addBasicClass("android.content.Intent",SootClass.BODIES);
		for (String className : entrypoints)		
			Scene.v().addBasicClass(className, SootClass.BODIES);			
		Scene.v().loadNecessaryClasses();	
		
		//setSparkPointsToAnalysis();
	}

	private SootMethod setEntryPoints_getMainMethod() {
		
		AndroidEntryPointCreator firstEntryPointCreator = createEntryPointCreator();
		
		//create the main method for Android App.
		firstEntryPointCreator.createDummyMain();	
		
		Chain<SootClass> chain = Scene.v().getClasses();	
		
		Chain<SootClass> classes = Scene.v().getClasses();
		for(SootClass sc:classes){
			for(SootMethod sm:sc.getMethods()){
				System.out.println("sc: "+sc+" sm: "+sm);
			}
		}
		
		//buildFakeActivities(chain);
		addSuperClasses(chain);
		addFragment(chain);
		addInnerClasses(chain);
				
		AndroidEntryPointCreator aepc = new AndroidEntryPointCreator(new ArrayList<String>(entrypoints));
		
		createFakeActivities();
//		System.out.println("EntryPoints are: "+entrypoints);
//		System.out.println("FakeActivities: "+fakeActivities);
//		System.out.println("RealActivities: "+realActivities);
		
//		List<String> activities = getActivities();
//		int i = 0;
//		for(String s:activities){
//			System.out.println(i+++" "+s);
//		}
		
		//aepc.createDummyMain();
		return aepc.createDummyMain();
	}

	
//	private void buildFakeActivities(Chain<SootClass> chain){
//		for(SootClass activityClass:chain){
//			SootClass currentClass = activityClass;		
//			while(currentClass.hasSuperclass()&&
//					!(currentClass.getSuperclass().getName().startsWith("android"))&&
//					!(currentClass.getSuperclass().getName().startsWith("java"))&&
//					!(currentClass.getSuperclass().getName().startsWith("org"))){
//				SootClass  superClass = currentClass.getSuperclass();
//				if(!fakeActivities.contains(superClass))
//					fakeActivities.add(superClass);
//				currentClass = superClass;
//			}				
//		}
//	}
	
//	private List<String> getActivities(){
//		List<String> activities = new ArrayList<String>();
//		for(String fake:realActivities){
//			SootClass currentClass = Scene.v().getSootClass(fake);
//			//Component.getComponent(currentClass);
//			if(Component.Activity.equals(Component.getComponent(currentClass))){
//				activities.add(currentClass.getShortJavaStyleName());
//			}			
//		}
//		return activities;
//	}
	
	private void addSuperClasses(Chain<SootClass> chain) {
		List<String> callbacks = AndroidEntryPointConstants.getCallbacks();
		for(String s:callbacks){
			SootClass interfaceClass = Scene.v().getSootClass(s);
			if(!interfaceClass.isInterface()){
				System.out.println(interfaceClass+" is not a interface!");
				continue;
			}
			List<SootClass> interfaces = Scene.v().getActiveHierarchy().getImplementersOf(interfaceClass);
			for(SootClass in:interfaces){
				if(!entrypoints.contains(in.getName())&&
				!(in.getName().startsWith("android"))&&
				!(in.getName().startsWith("java"))&&
				!(in.getName().startsWith("org"))&&
				!entrypoints.contains(in.getName()))
					entrypoints.add(in.getName());
			}
		}
		
//		SootClass interfaceClass = Scene.v().getSootClass(AndroidEntryPointConstants.ONCLICKLISTENER);
//		List<SootClass> interfaces = Scene.v().getActiveHierarchy().getImplementersOf(interfaceClass);
//		for(SootClass in:interfaces){
//			if(!entrypoints.contains(in.getName())&&
//			!(in.getName().startsWith("android"))&&
//			!(in.getName().startsWith("java"))&&
//			!(in.getName().startsWith("org"))&&
//			!entrypoints.contains(in.getName()))
//				entrypoints.add(in.getName());
//		}
		for(SootClass activityClass:chain){
			if(activityClass.isConcrete()){
				Component com = Component.parseComponent(activityClass);
				if(com!=null){
					if(!entrypoints.contains(activityClass.getName())&&
							!(activityClass.getName().startsWith("android"))&&
							!(activityClass.getName().startsWith("java"))&&
							!(activityClass.getName().startsWith("org")))
						entrypoints.add(activityClass.getName());
				}
			}			
		}
	}
	
	private void addInnerClasses(Chain<SootClass> chain) {
		for(SootClass activityClass:chain){
			for(String string:new ArrayList<String>(entrypoints)){
				if(activityClass.getName().startsWith(string)){
					if(!entrypoints.contains(activityClass.getName()))
						entrypoints.add(activityClass.getName());								
						break;
				}					
			}
		}
	}
	
	//添加fragment的子类
	private void addFragment(Chain<SootClass> chain) {
		for(SootClass activityClass:chain){
			if (activityClass.isConcrete()) 
				if(!activityClass.getName().startsWith("android")&&!activityClass.getName().startsWith("java")&&
						!activityClass.getName().startsWith("org")){				
					List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOf(activityClass);
					for(SootClass sc:superClasses){
						if(sc.getName().equals("android.support.v4.app.Fragment")){
							entrypoints.add(activityClass.getName());
							break;
						}
					}
				}		
		}
	}
	
	private void createFakeActivities(){
		for(String fake:realActivities){
			SootClass currentClass = Scene.v().getSootClass(fake);
			//Component.getComponent(currentClass);
			if(Component.Activity.equals(Component.parseComponent(currentClass))){
				fakeActivities.add(fake);
				while(currentClass.hasSuperclass()&&
						!(currentClass.getSuperclass().getName().startsWith("android"))&&
						!(currentClass.getSuperclass().getName().startsWith("java"))&&
						!(currentClass.getSuperclass().getName().startsWith("org"))){
					SootClass  superClass = currentClass.getSuperclass();
					if(!fakeActivities.contains(superClass))
						fakeActivities.add(superClass.getName());
					currentClass = superClass;
				}	
			}			
		}
	}
	
	//该方法的两个目的：1. 将App的classes加载到Scene中 2. 获得所有的activity classes.
	private AndroidEntryPointCreator createEntryPointCreator() {
		ProcessManifest processMan = new ProcessManifest();
		processMan.loadManifestFile(apkFileLocation);
		IntentFilter.setFilters(processMan.getAFilters());
		IntentFilter.calculate_ActionToActivity();
		//System.out.println(processMan.getAFilters().size());
		entrypoints = processMan.getEntryPointClasses();	
		realActivities = processMan.entryPointClone();
		
		AndroidEntryPointCreator entryPointCreator = new AndroidEntryPointCreator
			(new ArrayList<String>(entrypoints));
		return entryPointCreator;
	}
	
	public Set<String> getFakeActivities(){
		return fakeActivities;
	}
	
	//作为本程序的入口
	public Set<String> getEntryPoint(){
		return entrypoints;
	}
	
	public static CallGraph getCallGraph(){
		return cg;
	}
	
	//app的所有活动界面
	public static Set<String> getActivities(){
		return realActivities;
	}
	
	private void setSparkPointsToAnalysis() {
		System.out.println("[spark] Starting analysis ...");
				
		HashMap opt = new HashMap();
		opt.put("enabled","true");
		opt.put("verbose","true");
		opt.put("ignore-types","false");          
		opt.put("force-gc","false");            
		opt.put("pre-jimplify","false");          
		opt.put("vta","false");                   
		opt.put("rta","false");                   
		opt.put("field-based","false");           
		opt.put("types-for-sites","false");        
		opt.put("merge-stringbuffer","true");   
		opt.put("string-constants","false");     
		opt.put("simulate-natives","true");      
		opt.put("simple-edges-bidirectional","false");
		opt.put("on-fly-cg","true");            
		opt.put("simplify-offline","false");    
		opt.put("simplify-sccs","false");        
		opt.put("ignore-types-for-sccs","false");
		opt.put("propagator","worklist");
		opt.put("set-impl","double");
		opt.put("double-set-old","hybrid");         
		opt.put("double-set-new","hybrid");
		opt.put("dump-html","false");           
		opt.put("dump-pag","false");             
		opt.put("dump-solution","false");        
		opt.put("topo-sort","false");           
		opt.put("dump-types","true");             
		opt.put("class-method-var","true");     
		opt.put("dump-answer","false");          
		opt.put("add-tags","false");             
		opt.put("set-mass","false"); 		
		
		CHATransformer.v().transform();
		cg = Scene.v().getCallGraph();
		SparkTransformer.v().transform("",opt);
		System.out.println("[spark] Done!");
	}
}
