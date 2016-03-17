package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import singlton.SingltonFactory;

public class ActivityEventFlowGraph extends AbstractGraph<ActivityEventFlowGraphEdge, String> implements Serializable{

	public ActivityEventFlowGraph(SingltonFactory.G g){}
	public static ActivityEventFlowGraph v(){
		return SingltonFactory.v().getActivityEventFlowGraph();
	}
	
	public void build(){
		genNodes();
		buildStrEdges();
	}
	
	private List<String> nodes = new ArrayList<String>();
	public void genNodes(){
		for(int i=0;i<edges.size();i++){
			String src = edges.get(i).getSrc();
			String tgt = edges.get(i).getTgt();
			if(!nodes.contains(src)){
				nodes.add(src);
			}
			if(!nodes.contains(tgt)){
				nodes.add(tgt);
			}
		}
	}
	public List<String> getNodes(){
		return nodes;
	}
	
	@Override
	public List<ActivityEventFlowGraphEdge> findEdgeByTgt(String n) {
		 List<ActivityEventFlowGraphEdge> des = new ArrayList<ActivityEventFlowGraphEdge>();	
		 for(ActivityEventFlowGraphEdge de:edges){
			 if(de.getTgt().equals(n)){
				 des.add(de);
			 }
		 }
		 return des;
	}

	@Override
	public List<ActivityEventFlowGraphEdge> findEdgeBySrc(String n) {
		 List<ActivityEventFlowGraphEdge> des = new ArrayList<ActivityEventFlowGraphEdge>();	
		 for(ActivityEventFlowGraphEdge de:edges){
			 if(de.getSrc().equals(n)){
				 des.add(de);
			 }
		 }
		 return des;
	}

	public List<Integer> findEdgeBySrcNum(String n) {
		 List<Integer> des = new ArrayList<Integer>();
		 for(int i=0;i<edges.size();i++){
			 if(edges.get(i).getSrc().equals(n)){
				 des.add(i);
			 }
		 }
		 return des;
	}
	
	@Override
	public List<List<ActivityEventFlowGraphEdge>> searchEdgesStartWithE(
			ActivityEventFlowGraphEdge e) {
		return null;
	}
	
	//通过string识别节点
	public boolean isReachable(String from, String to,int deep,List<ActivityEventFlowGraphEdge> ll){
		if(getEdgesByName(from, to, deep).size()>0){
			return true;
		}
		else return false;
	}
	
	//给定两个节点，以及遍历层数，返回两个节点之间的所有边。
	public List<List<Integer>> getEdgesByName(String from, String to,int deep){
		List<List<Integer>> llae = new ArrayList<List<Integer>>();
		if(deep>0){
			List<Integer> dest = findEdgeBySrcNum(from);
			deep--;		
			if(dest.size()>0){				
				for(int i:dest){
					ActivityEventFlowGraphEdge ee = edges.get(i);
					if(ee.getTgt().equals(to)){
						List<Integer> back = new ArrayList<Integer>();
						back.add(i);
						llae.add(back);
					}
					else{
						List<List<Integer>> returnValue = getEdgesByName(ee.getTgt(), to,deep);
						for(List<Integer> aee:returnValue){
							aee.add(i);
							llae.add(aee);
						}
					}	
				}			
			}				
		}
		return llae;
	}
	
	//通过id识别节点
	public boolean isReachable(int fromId, int toId,int deep){
		if(getEdgesById(fromId, toId, deep).size()>0){
			return true;
		}
		else return false;	
	}
	public List<List<Integer>> getEdgesById(int fromId, int toId,int deep){
		return getEdgesByName(nodes.get(fromId),nodes.get(toId),deep);
	}
	
	//将edges转化成String类.转化方式1
	private List<List<String>> strEdges = new ArrayList<List<String>>();	
	public void buildStrEdges(){
		for(ActivityEventFlowGraphEdge aee:edges){
			String s1 = aee.getSrc();
			String s2 = aee.getTgt();
			String s3 = aee.getView();
			List<String> list = new ArrayList<String>();
			list.add(s1);
			list.add(s2);
			list.add(s3);
			strEdges.add(list);
		}
	}
	public List<List<String>> getStrEdges(){
		return strEdges;
	}
	
	@Override
	public List<ActivityEventFlowGraphEdge> getEntryEdges() {
		return null;
	}
	@Override
	public boolean isEntryEdge(ActivityEventFlowGraphEdge de) {
		// TODO Auto-generated method stub
		return false;
	}
}
