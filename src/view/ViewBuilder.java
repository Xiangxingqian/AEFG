package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import tags.Tagger;
import type.TypeAndFormat;
import util.LocalAnalysis;
import util.StringHandler;
import analysis.methodAnalysis.InterMethodAnalysis;
import analysis.methodAnalysis.IntraMethodAnalysis;

import com.aefg.dataflowanalysis.AEFG_LocalResolver;

public class ViewBuilder {
	
	private String listenerMethod;
	private SootClass listenerClass;
	//private List<Integer> integers = new ArrayList<Integer>();
	private Map<Integer, SootClass> idToClass = new HashMap<Integer, SootClass>();
	
	private Map<AssignStmt, SootClass> viewToClass = new HashMap<AssignStmt, SootClass>();
	
	public ViewBuilder(String listenerMethod,SootClass listener){
		this.listenerMethod = listenerMethod;
		this.listenerClass = listener;
		//build();
	}
	
	public void build(){
		//List<Integer> integers = new ArrayList<Integer>();
		List<InvokeStmt> listenerStmts = calculateListenerStmts();
		for(InvokeStmt is:listenerStmts){
			System.out.println("Listener is: "+listenerClass+" ListenerStmts is: "+is);
			//InstanceInvokeExpr iie = (InstanceInvokeExpr)is.getInvokeExpr();
			//Local base = (Local)iie.getBase();
			//Value v = is.getInvokeExpr().getUseBoxes().get(0).getValue();
			SootMethod method = Tagger.getMethodTag(is);
			//Value definition = LocalAnalysis.getDefValue(is, new ExceptionalUnitGraph(method.retrieveActiveBody()));
			
			AssignStmt defAS_IS = LocalAnalysis.getDefAS_IS(is, new ExceptionalUnitGraph(method.retrieveActiveBody()));
			if(defAS_IS!=null){
				viewToClass.put(defAS_IS, method.getDeclaringClass());
				Value definition = defAS_IS.getRightOp();
				if(definition instanceof InvokeExpr){
					InvokeExpr ie = (InvokeExpr)definition;
					boolean equals = ie.getMethod().getSubSignature().equals(TypeAndFormat.getFindviewbyid());
					if(equals){
						Value arg = ie.getArg(0);
						if(arg instanceof IntConstant){
							IntConstant iConstant = (IntConstant)arg;
							idToClass.put(iConstant.value, method.getDeclaringClass());
						}
						else{
						SootMethod sm = Tagger.getMethodTag(defAS_IS);
						List<Type> types = new ArrayList<Type>();
						AEFG_LocalResolver localResolver = new AEFG_LocalResolver((Local)arg, sm, types, defAS_IS);
						List<Value> values = localResolver.getValues();
						Integer id = StringHandler.splitNumsForViews(values+"");
						if(id!=-1)
							idToClass.put(id, method.getDeclaringClass());
						}
					}
				}
				
//				Integer id = StringHandler.splitNumsForViews(definition+"");
//				if(id!=-1)
//					idToClass.put(id, method.getDeclaringClass());
			}
//			AEFGLocalInterproceduralAnalysis analysis = 
//					new AEFGLocalInterproceduralAnalysis(method, Types.getViewTypes());
//			Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure = 
//					analysis.doInterprocedure(method, 5, new HashMap<Local, List<Value>>(),null);
//			Map<Local, List<Value>> valueBefore = doInterprocedure.getValueBefore(is);
//			List<Value> list = valueBefore.get(v);
//			System.out.println(this.getClass()+"IS: "+is+" List: "+list+" "+"Value: "+v); 
//			if(list!=null&&list.size()>0){
//				Value value = list.get(list.size()-1);
//				Integer splitNumsForViews = StringHandler.splitNumsForViews(value.toString());
//				intergerAndSootClass.put(splitNumsForViews, method.getDeclaringClass());
//			}
		}
		
		if(idToClass!=null){
			ViewTriple viewTriple = new ViewTriple(idToClass, listenerMethod, listenerClass);
			View.v().getViewTriples().add(viewTriple);
		}
		//return idToClass;
	}

	public List<InvokeStmt> calculateListenerStmts(){
		//两种处理：
		//String name = listenerClass.getName();
		List<InvokeStmt> listenerStmts = new ArrayList<InvokeStmt>();
		//当listenerClass是一个内部类
		//if(name.contains("$")){
		listenerStmts = IntraMethodAnalysis.getListenerStmtsByClass(listenerClass, listenerMethod);
		//}
		//else {
			for(SootMethod sm:listenerClass.getMethods()){
				if(sm.getName().equals("<init>")){
					if(sm!=null){
						Iterator<Edge> sourceEdges = InterMethodAnalysis.getSourceEdges(sm);
						while (sourceEdges.hasNext()) {
							Edge edge = (Edge) sourceEdges.next();
							SootMethod srcMethod = edge.src();
							if(!edge.src().getDeclaringClass().equals(sm.getDeclaringClass())){
								List<InvokeStmt> listenerStmtsByMethod = 
										IntraMethodAnalysis.getListenerStmtsByMethod(srcMethod, listenerMethod);
								listenerStmts.addAll(listenerStmtsByMethod);
							}
						}
					}
				}
			}
		//}
		return listenerStmts;
	}

	public static int checkEquals(String condition, String id) {
		Integer conditionId = StringHandler.splitNumsForViews(condition);
		Integer invokerId = StringHandler.splitNumsForViews(id);
		
		if(conditionId<0&&invokerId>0){
			return invokerId;
		}
		if(invokerId>0&&conditionId>0){
			if(invokerId==conditionId){
				return invokerId;
			}
		}
		return -1;
	}
	
	public Map<Integer, SootClass> getIdToClass() {
		return idToClass;
	}
	
	public Map<AssignStmt, SootClass> getViewToClass() {
		return viewToClass;
	}


}
