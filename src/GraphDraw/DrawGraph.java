package GraphDraw;

import graph.ActivityEventFlowGraph;
import graph.ActivityEventFlowGraphEdge;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawGraph {
	public static void drawGraph(ActivityEventFlowGraph aeg, String temp_dir, String dot, String appName){
		List<ActivityEventFlowGraphEdge> aees = aeg.getEdges();
		GraphViz gv = new GraphViz(temp_dir,dot);
		gv.addln(gv.start_graph());
		int index = 0;
		Map<Integer, String> map = new HashMap<Integer, String>();
		for(ActivityEventFlowGraphEdge ee:aees){
			String src = ee.getSrc();
			String tgt = ee.getTgt();	
			//String label = "qian";
			String lString = "index";
			
			{
				gv.addln(src+" -> "+tgt+"[label="+index+"]");
			}
			index++;
		}
		
		gv.addln(gv.end_graph());
	    System.out.println(gv.getDotSource());	      
	    String type = "pdf";	    
	    //File out = new File("/tmp/out." + type);   // Linux
	    //File out = new File("C:/tmp/out." + type);		//windows
	    File out = new File(temp_dir+"/"+appName+"."+type);
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
}
