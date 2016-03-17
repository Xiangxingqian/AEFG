package graphTemp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import component.Component;


import soot.RefType;
import soot.Value;
import soot.jimple.Jimple;

public class TempConEdge {
	public static final Value DEFAULTVALEU_VALUE = Jimple.v().newLocal("default", RefType.v("java.lang.String")) ;
	//private Component component;
	
	private Value invoker = DEFAULTVALEU_VALUE;
	
	Map<String, Component> map = new HashMap<String, Component>();
	
	public TempConEdge(String componentName, Component component, Value invoker) {
		map.put(componentName, component);
		this.invoker = invoker;
	}
	
	public TempConEdge(Map<String, Component> map, Value invoker) {
		this.map = map;
		this.invoker = invoker;
	}
	
//	public List<DetailEdge> getDetailEdges() {
//		return detailEdges;
//	}
//	public void setDetailEdges(List<DetailEdge> detailEdges) {
//		this.detailEdges = detailEdges;
//	}
	
	public Map<String, Component> getMap() {
		return map;
	}

	public Value getInvoker() {
		return invoker;
	}
	public void setInvoker(Value invoker) {
		this.invoker = invoker;
	}
	
	@Override
	public String toString() {
		return "TempConEdge [invoker=" + invoker
				+ ", map=" + map + "]";
	}

	public TempConEdge clone(){
		TempConEdge tempConEdge = new TempConEdge(map,invoker);
		return tempConEdge;

	}
	
	public boolean equals(Object other){
		if(!(other instanceof TempConEdge))return false;
		else{
			TempConEdge o = (TempConEdge) other;
	        if(o.getMap().equals(map)) return false;
	        if(o.getInvoker().equals(invoker)) return false;
	        return true;
		}
	}
//	public void setEdgeInfo(Component com, String comName,Value invoker){
//		this.component = com;
//		this.componentName = comName;
//		this.invoker = invoker;
//		//this.detailEdges = des;
//	}

//	@Override
//	public String toString() {
////		return "com:" + com +"\ncomName:"+comName+ " \nid:" + invoker
////				+ "\n edge:" + detailEdges;
//		return " \nid:" + invoker;
//	}
	
	
	
//	public static TempConEdge genEdgeStorageByEdge(Component com, String comName, Value invoker, Edge e){
//		DetailEdge de = DetailEdge.edgeToDetailEdge(e);
//		List<DetailEdge> des = ArrayUtil.toList(de);
//		return new TempConEdge(com,comName, invoker, des);
//	}
	
}
