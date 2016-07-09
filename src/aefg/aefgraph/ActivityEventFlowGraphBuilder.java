package aefg.aefgraph;

import graphTemp.TempConEdge;
import graphTemp.TempEdge;

import java.util.List;
import java.util.Map;

import soot.Value;
import util.StringHandler;

import component.Component;

public class ActivityEventFlowGraphBuilder {
	
	ActivityEventFlowGraph aefg;
	List<TempEdge> tempEdges = null;
	public ActivityEventFlowGraphBuilder(List<TempEdge> issList){
		this.tempEdges = issList;
	}
	
	public ActivityEventFlowGraph build(){
		aefg = new ActivityEventFlowGraph();
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
			aefg.addEdge(activityEventFlowGraphEdge);
			//System.out.println("sourceNode: "+sourceNode+"\ntargetNode: "+targetNode+"\nInvoker: "+invoker);
		}
		return aefg;
	}
}
