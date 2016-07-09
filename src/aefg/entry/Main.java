package aefg.entry;

import graphTemp.TempEdge;
import graphTemp.TempGraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import sootAnalysis.AndroidAnalysis;
import test.Events;
import GraphDraw.DrawGraph;
import aefg.aefgraph.ActivityEventFlowGraph;
import aefg.aefgraph.ActivityEventFlowGraphBuilder;
import aefg.callgraph.CallGraphBuilder;
import aefg.global.Global;

public class Main {
	public static String apkName;
	public static String apkLocation;
	public static String androidJar;
	public static String output = "output/";
	public static void main(String[] args){
		Logger log = LoggerFactory.getLogger(Main.class);
		long start = System.currentTimeMillis();
		apkLocation = args[1];
		androidJar = args[3];
		apkName = parseApkName(apkLocation);
		
		String seriDir = output + apkName + ".txt";
		String edges = output+ apkName + "Edges.txt";
		String eventsDir = output + apkName + "Events.txt";

		initSoot();
		buildCallGraph();
		aefgAnalyze();
		serializeEvents(Events.v(), eventsDir);
		List<TempEdge> tempEdges = TempGraph.v().getTempEdges();
		//print manifest and generated graph info
		{
			System.out.println("MainActivity: "+Global.ManifestInfo.mainActivity);
			System.out.println("Activities: "+Global.ManifestInfo.activities);
			System.out.println(" "+tempEdges.size());
			System.out.println("Graph: ");
			for(TempEdge te:tempEdges){
				System.out.println("Temp Edge: "+te.toString());
			}
		}
		Set<String> activities = Global.ManifestInfo.activities;
		List<TempEdge> realEdges = parseRealEdge(tempEdges, activities);
		ActivityEventFlowGraph graph = new ActivityEventFlowGraphBuilder(realEdges).build();
		graph.genNodeAndEdge();
		System.out.println("Nodes: "+graph.getNodes().size());
		System.out.println("Edges: "+graph.getEdges().size());
		serializeAEFG(graph,seriDir);
//		deserializationAEFG(seriDir,edges);
		DrawGraph.drawGraph(graph,output,args[5],apkName);
		long end = System.currentTimeMillis();
		log.info("RunningTime: " + (end - start) + "ms", Main.class);
	}

	private static void aefgAnalyze() {
		AndroidAnalysis androidAnalysis = new AndroidAnalysis();
		androidAnalysis.analyze();
	}

	private static void buildCallGraph() {
		CallGraphBuilder cgb = new CallGraphBuilder(apkLocation);
		cgb.build();
	}
	
	private static void testCallGraph() {
		CallGraph callGraph = Scene.v().getCallGraph();
		SootClass dummyMain = Scene.v().getSootClass("dummyMainClass");
		SootMethod dummyMainMethod = dummyMain.getMethodByName("dummyMainMethod");
		for(Unit unit: dummyMainMethod.getActiveBody().getUnits()){
			System.out.println(unit);
		}
		
		Iterator<Edge> edgesInto = callGraph.edgesOutOf(dummyMainMethod);
		while(edgesInto.hasNext()){
			Edge next = edgesInto.next();
			System.out.println("Src: "+next.src()+" Tgt: "+next.tgt());
		}
	}
	
	private static void initSoot(){
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_android_jars(androidJar);
//		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_keep_line_number(true);
//		Options.v().set_no_output_source_file_attribute(true);		
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_soot_classpath(apkLocation+";"+
				"/lib/jce.jar;" +
				"/lib/tools.jar;" +
				"lib/android.jar;"+
				"/lib/android-support-v4.jar;"
				);		
		Options.v().setPhaseOption("cg.spark", "on");
	}
	
	private static String parseApkName(String apkLocation) {
		return  apkLocation.substring(apkLocation.lastIndexOf("\\") + 1, apkLocation.indexOf("."));
	}

	private static void serializeObj(Object obj, String edgeStorageLocation) {
		File file = new File(edgeStorageLocation);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serializeAEFG(ActivityEventFlowGraph aefg, String edgeDir) {
		serializeObj(aefg, edgeDir);
	}

	public static void serializeEvents(Events events, String eventDir) {
		serializeObj(events, eventDir);
	}

//	private static void deserializationAEFG(String app, String edges){
//		ActivityEventFlowGraph aeg;
//		FileInputStream fis = new FileInputStream(app);
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		aeg = (ActivityEventFlowGraph) ois.readObject();
//		ois.close();
//		fis.close();
//
//		List<List<String>> ssList = aeg.getStrEdges();
//		Graph g = new Graph(ssList);
//		FileOutputStream out = null;
//		File file2 = new File(edges);
//		try {
//			file2.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		out = new FileOutputStream(file2);
//		int count = ssList.size();
//		for (int i = 0; i < count; i++) {
//			List<String> strings = ssList.get(i);
//			out.write((i + " src: " + strings.get(0) + " tgt: "
//					+ strings.get(1) + " \n" + "view: " + strings.get(2) + "\n")
//					.getBytes());
//		}
//		out.close();
//	}

//	public static void deserializationEvents(String eventsDir, String eventDir)
//			throws IOException, ClassNotFoundException {
//		Events events;
//		FileInputStream fis = new FileInputStream(eventsDir);
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		events = (Events) ois.readObject();
//		ois.close();
//		fis.close();
//
//		File file2 = new File(eventDir);
//		file2.createNewFile();
//		FileOutputStream out = new FileOutputStream(file2);
//		for (Event e : events.getEvents()) {
//			String activity = e.getActivity();
//			String eventType = e.getEventType();
//			String view = e.getView();
//			int viewId = StringHandler.splitNumsForViews(view);
//			if ((activity != null || activity != "") && eventType != ""
//					&& viewId != -1)
//				out.write(("('" + activity + "','" + viewId + "','" + eventType + "')\n")
//						.getBytes());
//		}
//		out.close();
//	}
	
	/**
	 * Activities declared in AndroidManifest are real activities. 
	 * The source and target activitis in TempGraph may not be real activities, 
	 * for example, the target activity may not be declared in AndroidManifest. This method
	 * is going to excluding all the unreal activities. 
	 * @param outTempEdges
	 * @param activityClasses
	 * @return
	 */
	public static List<TempEdge> parseRealEdge(List<TempEdge> outTempEdges,Set<String> activityClasses){
		List<TempEdge> inTempEdges = new ArrayList<TempEdge>();
		for(int i = 0;i<outTempEdges.size();i++){
			//SootMethod sm = gss.get(i).getBodyMethod();
			String sourceNode = outTempEdges.get(i).getSourceNode();
			SootClass sc = Scene.v().getSootClass(sourceNode);
			List<SootClass> subClasses = new ArrayList<SootClass>();
			if(Scene.v().getActiveHierarchy().getSubclassesOf(sc).size()>0){
				subClasses = Scene.v().getActiveHierarchy().getSubclassesOf(sc);
			}
			boolean flag = false;
			for(SootClass s:subClasses){
//				List<String> strings = getMethods(s.getMethods());
//				if(activityClasses.contains(s.getName())&&!strings.contains(sm.getSubSignature())){
//					GraphStorage is = gss.get(i).clone();
//					is.setClassName(s);
//					if(!isss.contains(is))
//						isss.add(is);
//					//flag = true;
//				}
				
				//修改。不再判断当前的s类中是否含有sm。之前的考虑若子类重写父类的方法怎么办，现在不考虑
				if(activityClasses.contains(s.getName())){
					TempEdge is = outTempEdges.get(i).clone();
					is.setSourceNode(s.getName());
					if(!inTempEdges.contains(is))
						inTempEdges.add(is);
				}
			}	
			if(!activityClasses.contains(sc.getName())){			
				flag = true;
			}
			if(flag){
				outTempEdges.remove(i);
				i--;
				continue;
			}
		}
		outTempEdges.addAll(inTempEdges);	
		return outTempEdges;
	}
	
	public static void initAEFG(){
		aefg.global.Options.v().setContext_sensitive(true);
		aefg.global.Options.v().setField_sensitive(true);
		aefg.global.Options.v().setFlow_sensitive(true);
		aefg.global.Options.v().setPath_sensitive(true);
	}
}
