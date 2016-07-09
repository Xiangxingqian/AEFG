package aefg.callgraph;

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
import soot.util.Chain;
import aefg.global.Global;
import aefg.global.IGlobal;
import aefg.global.Global.ManifestInfo;
import aefg.intent.IntentFilter;

import com.test.xmldata.ProcessManifest;
import component.Component;

import entryPointCreator.AndroidEntryPointConstants;
import entryPointCreator.AndroidEntryPointCreator;

public class CallGraphBuilder implements IGlobal{
	
	private CallGraph cg = null;
	
	/**
	 * activities are activities declared in AndroidMainfest
	 */
	private Set<String> activities = null;
	
	/**
	 * allActivities are all the classes extending android.app.Activity, including 
	 * the super classes of activities declared in AndroidManifest. 
	 */
	public Set<String> allActivities = new HashSet<String>();
	public HashSet<String> entrypoints = null;
	
	public List<String> analyzedMethods = new ArrayList<String>();
	
	String apkFileLocation;
	String mainActivity;
	
	public CallGraphBuilder(String apkFileLocation) {	
		this.apkFileLocation = apkFileLocation;
	}

	public void build(){
		SootMethod entry = createDummyMainMethod(); 
		Scene.v().setEntryPoints(Collections.singletonList(entry));
		Scene.v().addBasicClass(entry.getDeclaringClass().getName(), SootClass.BODIES);	
//		Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.BODIES);
//		Scene.v().addBasicClass("java.util.HashSet",SootClass.BODIES);
//		Scene.v().addBasicClass("android.content.Intent",SootClass.BODIES);
		for (String className : entrypoints)		
			Scene.v().addBasicClass(className, SootClass.BODIES);			
		Scene.v().loadNecessaryClasses();	
		setSparkPointsToAnalysis();
		appendToGlobal();
	}

	/**
	 * create dummy main method twice. 
	 * @return
	 */
	private SootMethod createDummyMainMethod() {
		
		AndroidEntryPointCreator firstEntryPointCreator = createEntryPointCreator();
		firstEntryPointCreator.createDummyMain();	
		Chain<SootClass> classes = Scene.v().getClasses();	
		
		//buildFakeActivities(chain);
		addSuperClasses(classes);
		addFragment(classes);
		addInnerClasses(classes);
				
		AndroidEntryPointCreator aepc = new AndroidEntryPointCreator(new ArrayList<String>(entrypoints));
		
		createFakeActivities();
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
	
	/**
	 * allActivities mean all the subclasses of android.app.Activity, including super class
	 * of activity declared in AndroidManifest.
	 */
	private void createFakeActivities(){
		for(String real:activities){
			SootClass activity = Scene.v().getSootClass(real);
			//We exclude those fake activities declared in AndroidMnifest but not extending
			//android.activity.Activity.
			if(Component.Activity.equals(Component.parseComponent(activity))){
				allActivities.add(real);
				List<SootClass> subclassesOf = Scene.v().getActiveHierarchy().getSubclassesOf(activity);
				for(SootClass sc: subclassesOf){
					if(sc.getName().startsWith("android"))
						allActivities.add(sc.getName());
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
		activities = processMan.entryPointClone();
		mainActivity = processMan.getMainActivity();
		
		AndroidEntryPointCreator entryPointCreator = new AndroidEntryPointCreator
			(new ArrayList<String>(entrypoints));
		return entryPointCreator;
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

	@Override
	public void appendToGlobal() {
		Global.ManifestInfo.activities = activities;
		ManifestInfo.allActivities = allActivities;
		ManifestInfo.entrypoints = entrypoints;
		Global.v().setCallGraph(cg);
		ManifestInfo.mainActivity = mainActivity;
	}
}
