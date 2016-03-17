package test;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	List<List<String>> edges = new ArrayList<List<String>>();
	public Graph(List<List<String>> l){
		this.edges = l;
		genNodes();
	}
	
	public List<List<String>> findEdgeBySrc(String n) {
		 List<List<String>> des = new ArrayList<List<String>>();	
		 for(List<String> ss :edges){
			 if(ss.get(0).equals(n)){
				 des.add(ss);
			 }
		 }
		 return des;
	}

	public List<Integer> findEdgeBySrcNum(String n) {
		 List<Integer> des = new ArrayList<Integer>();
		 for(int i=0;i<edges.size();i++){
			 if(edges.get(i).get(0).equals(n)){
				 des.add(i);
			 }
		 }
		 return des;
	}
	
	public List<List<Integer>> getEdgesByName(String from, String to,int deep){
		List<List<Integer>> llae = new ArrayList<List<Integer>>();
		if(deep>0){
			List<Integer> dest = findEdgeBySrcNum(from);
			deep--;		
			if(dest.size()>0){				
				for(int i:dest){
					List<String> ee = edges.get(i);
					if(ee.get(1).equals(to)){
						List<Integer> back = new ArrayList<Integer>();
						back.add(i);
						llae.add(back);
					}
					else{
						List<List<Integer>> returnValue = getEdgesByName(ee.get(1), to,deep);
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
	
	private List<String> nodes = new ArrayList<String>();
	public void genNodes(){
		for(int i=0;i<edges.size();i++){
			String src = edges.get(i).get(0);
			String tgt = edges.get(i).get(1);
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
	
	public List<List<Integer>> getEdgesByName(int fromId, int toId,int deep){
		return getEdgesByName(nodes.get(fromId),nodes.get(toId),deep);
	}
}
