package aefg.entry;

import graph.ActivityEventFlowGraph;
import graph.ActivityEventFlowGraphBuilder;
import graph.Entrying;
import graphTemp.TempEdge;
import graphTemp.TempGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GraphDraw.DrawGraph;

import com.app.test.CallBack;

import singlton.Global;
import singlton.SingltonFactory;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.util.Chain;
import sootAnalysis.AndroidAnalysis;
import test.Event;
import test.EventExplorer;
import test.Events;
import test.Graph;
import util.StringHandler;

public class Main {
	public static String appName;
	public static String apkFileLocation;
	public static String androidJar;
	public static String output = "output/";
	
	public static void main(String[] args) throws Exception {
		Logger log = LoggerFactory.getLogger(Main.class);
		long start = System.currentTimeMillis();
		apkFileLocation = args[1];
		androidJar = args[3];
		appName = getAppName(apkFileLocation);
		
		String seriDir = output + appName + ".txt";
		String edges = output+ appName + "Edges.txt";
		String eventsDir = output + appName + "Events.txt";
		String eventDir = output + appName + "Event.txt";

		initSoot();
		
		CallGraphBuilder cgb = new CallGraphBuilder(apkFileLocation, args[3]);
		cgb.build();

//		testCallGraph();
		AndroidAnalysis androidInstrumenter = new AndroidAnalysis();
		androidInstrumenter.analyze();
		serializationEvents(Events.v(), eventsDir);
		deserializationEvents(eventsDir, eventDir);
		
		List<TempEdge> tempEdges = TempGraph.v().getTempEdges();
		System.out.println(" "+tempEdges.size());
		System.err.println("Graph: ");
		for(TempEdge te:tempEdges){
			System.out.println("Temp Edge: "+te.toString());
		}
		
		Set<String> activities = cgb.getActivities();
		List<TempEdge> transform = Entrying.transform(tempEdges, activities);
		new ActivityEventFlowGraphBuilder(transform).build();
		ActivityEventFlowGraph graph = ActivityEventFlowGraph.v();
		graph.build();
		System.out.println("Nodes: "+graph.getNodes().size());
		System.out.println("Edges: "+graph.getEdges().size());
		serializationAEFG(graph,seriDir);
		deserializationAEFG(seriDir,edges);
		DrawGraph.drawGraph(graph,output,args[5],appName);
		
		
		
		long end = System.currentTimeMillis();
		log.info("RunningTime: " + (end - start) + "ms", Main.class);
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
		Options.v().set_output_format(Options.v().output_format_jimple);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_no_output_source_file_attribute(true);		
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_soot_classpath(apkFileLocation+";"+
				"/lib/jce.jar;" +
				"/lib/tools.jar;" +
				"lib/android.jar;"+
				"/lib/android-support-v4.jar;"
				);		
		Options.v().setPhaseOption("cg.spark", "on");
	}
	
	private static String getAppName(String apk) {
		return  apk.substring(apk.lastIndexOf("\\") + 1, apk.indexOf("."));
	}

	private static void serializationObject(Object g, String edgeStorageLocation) {
		File file = new File(edgeStorageLocation);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Graph–Ú¡–ªØ
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			Class c = CallBack.class;
			oos.writeObject(g);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serializationAEFG(ActivityEventFlowGraph g,
			String edgeStorageLocation) {
		serializationObject(g, edgeStorageLocation);
	}

	public static void serializationEvents(Events events, String eventDir) {
		serializationObject(events, eventDir);
	}

	private static void deserializationAEFG(String app, String edges)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ActivityEventFlowGraph aeg;
		FileInputStream fis = new FileInputStream(app);
		ObjectInputStream ois = new ObjectInputStream(fis);
		aeg = (ActivityEventFlowGraph) ois.readObject();
		ois.close();
		fis.close();

		List<List<String>> ssList = aeg.getStrEdges();
		Graph g = new Graph(ssList);
		FileOutputStream out = null;
		File file2 = new File(edges);
		try {
			file2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = new FileOutputStream(file2);
		int count = ssList.size();
		for (int i = 0; i < count; i++) {
			List<String> strings = ssList.get(i);
			out.write((i + " src: " + strings.get(0) + " tgt: "
					+ strings.get(1) + " \n" + "view: " + strings.get(2) + "\n")
					.getBytes());
		}
		out.close();
	}

	public static void deserializationEvents(String eventsDir, String eventDir)
			throws IOException, ClassNotFoundException {
		Events events;
		FileInputStream fis = new FileInputStream(eventsDir);
		ObjectInputStream ois = new ObjectInputStream(fis);
		events = (Events) ois.readObject();
		ois.close();
		fis.close();

		File file2 = new File(eventDir);
		file2.createNewFile();
		FileOutputStream out = new FileOutputStream(file2);
		for (Event e : events.getEvents()) {
			String activity = e.getActivity();
			String eventType = e.getEventType();
			String view = e.getView();
			int viewId = StringHandler.splitNumsForViews(view);
			if ((activity != null || activity != "") && eventType != ""
					&& viewId != -1)
				out.write(("('" + activity + "','" + viewId + "','" + eventType + "')\n")
						.getBytes());
		}
		out.close();
	}
}
