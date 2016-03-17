package test;

import java.util.List;
import java.util.Map;

import com.android.event.EventContants;

import component.Component;
import component.ComponentHandler;

import singlton.Global;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.tagkit.IntegerConstantValueTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import tags.MethodTag;
import tags.Tagger;
import util.LocalAnalysis;
import view.ViewBuilder;

public class EventExplorer {
	
	private List<SootClass> allClasses;
	private List<InvokeStmt> registrarStmts = Global.v().getAllRegistrarStmts(); 
	
	public EventExplorer(List<SootClass> allClasses) {
		this.allClasses = allClasses;
	}

	public void instrument(){
		resolveMethod();
		Map<String, String> map = EventContants.getRegistrarAndlistener();
		for(InvokeStmt is:registrarStmts){
			String subSignature = is.getInvokeExpr().getMethod().getSubSignature();
			SootMethod method = Tagger.getMethodTag(is);
			SootClass declaringClass = method.getDeclaringClass();
			ExceptionalUnitGraph graph = new ExceptionalUnitGraph(method.retrieveActiveBody());
			AssignStmt defAS_IS = LocalAnalysis.getDefAS_IS(is, graph);
			
			ComponentHandler componentHandler = new ComponentHandler(declaringClass);
			componentHandler.build();
			String sourceActivity = componentHandler.getActivity();
			Event event = new Event(sourceActivity, defAS_IS+"", map.get(subSignature));
			System.out.println("SourceActivity: "+sourceActivity+" SootMethod: "+method+" Registrar: "+is+" DefStmt: "+defAS_IS);
			Events.v().getEvents().add(event);
		}
	}
	
	public void resolveMethod(){
		for(SootClass sc:allClasses){
			for(SootMethod sm:sc.getMethods()){
				if(sm.isConcrete()){
					int index = 0;
					for(Unit unit:sm.retrieveActiveBody().getUnits()){
						if(unit instanceof InvokeStmt){
							InvokeStmt s = (InvokeStmt)unit;
							InvokeExpr ie = s.getInvokeExpr();
//							if((ie.getMethod().getSubSignature().equals("void startActivity(android.content.Intent)")||
//									ie.getMethod().getSubSignature().equals("void startActivityForResult(android.content.Intent,int)"))
//									&&ie.getMethod().getDeclaringClass().getName().startsWith("android")){
							String subSignature = ie.getMethod().getSubSignature();
							if(EventContants.getRegistrarAndlistener().containsKey(subSignature)){
								s.addTag(new MethodTag(sm));
								s.addTag(new IntegerConstantValueTag(index));
								registrarStmts.add(s);
							}
						}		
					}
				}
			}
		}
	}
	
}
