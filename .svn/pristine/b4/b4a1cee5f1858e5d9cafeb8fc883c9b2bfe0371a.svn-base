package graph;

import graphTemp.TempConEdge;
import graphTemp.TempEdge;

import java.util.List;
import java.util.Map;

import soot.Value;
import util.StringHandler;

import component.Component;

public class ActivityEventFlowGraphBuilder {
	
	List<TempEdge> tempEdges = null;
	public ActivityEventFlowGraphBuilder(List<TempEdge> issList){
		this.tempEdges = issList;
	}
	
	public void build(){
		for(TempEdge te:tempEdges){
			String sourceNode = te.getSourceNode();
			String targetNode = te.getTargetNode();
			TempConEdge tempConEdges = te.getTempConEdges();
			Value invoker = tempConEdges.getInvoker();
//			Map<String, Component> map = tempConEdges.getMap();
			
			String src = StringHandler.getShortActivityName(sourceNode);
			ActivityEventFlowGraphEdge activityEventFlowGraphEdge = 
					new ActivityEventFlowGraphEdge(src, targetNode,
							invoker+"", tempConEdges);
			ActivityEventFlowGraph.v().addEdge(activityEventFlowGraphEdge);
			//System.out.println("sourceNode: "+sourceNode+"\ntargetNode: "+targetNode+"\nInvoker: "+invoker);
		}
	}
}
