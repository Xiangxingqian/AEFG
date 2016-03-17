package graph;


import graphTemp.TempEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootClass;

/**
 * 将InvokeStmtStructure入口化，将不是活动的节点转化成活动节点。 比如活动节点的父类为非活动节点，将父类转化成子类。
 * */
public class Entrying {
	
	/**
	 *转换步骤：
	 * 1. 输入所有的真实活动类
	 * 2. 求gss的类名
	 * 3. 求gss类名的所有子类
	 * 4. 若子类名中有真实活动，将其复制一条给isss。
	 * @return 
	 * */
	public static List<TempEdge> transform(List<TempEdge> outTempEdges,Set<String> activityClasses){
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
				
				//修改。不在判断当前的s类中是否含有sm。之前的考虑在于，若子类重写父类的方法怎么办，现在不考虑
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
	
//	public static List<String> getMethods(List<SootMethod> sms){
//		List<String> list = new ArrayList<String>();
//		for(SootMethod sm:sms){
//			list.add(sm.getSubSignature());
//		}
//		return list;
//	}
}
