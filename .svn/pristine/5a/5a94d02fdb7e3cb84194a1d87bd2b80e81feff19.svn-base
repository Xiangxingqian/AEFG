package sootAnalysis;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import analysis.methodAnalysis.IntraMethodAnalysis;
import analysis.methodAnalysis.InterMethodAnalysis;


import singlton.Global;
import soot.Body;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.util.Chain;
import condition.Condition;

public class InnerClass {
	
	//SwitchAndIfStructure structure = new SwitchAndIfStructure();
	public void addInnerMethod(){
		List<Local> locals = Global.v().getUnReachingLocal();
		for(Local l:locals){
			for(InvokeStmt is:Global.v().getLocalToStartActivity().get(l)){
				if(Global.v().getLocalToMethods().get(l).get(0).getDeclaringClass().getName().contains("$")){
					Condition.getDominator(is, Global.v().getLocalToMethods().get(l).get(0));
					//System.out.println(getClass()+" "+ GOA.v().getLocalToMethods().get(l).get(0));
				}
			}
		}
	}
	
	public List<SootClass> getInnerClass(SootClass sc){
		String name = sc.getName();
		Chain<SootClass> chain = Scene.v().getClasses();
		List<SootClass> innClasses = new ArrayList<SootClass>();
		for(SootClass sClass:chain){
			if(sClass.getName().startsWith(name)&&!sClass.getName().equals(name))
				innClasses.add(sClass);
		}
		return innClasses;
	}

	/**
	 * Whether sc contains targetMethod.
	 * */
	public List<SootMethod> getMethods(SootClass sc,SootMethod targetMethod){		
		List<SootMethod> list = sc.getMethods();
		List<SootMethod> targetList = new ArrayList<SootMethod>();
		for(SootMethod sm:list){
			if(sm.isConcrete()){
				Body body = sm.retrieveActiveBody();
				Chain<Unit> units = body.getUnits();
				for(Unit unit:units){
					if(unit instanceof InvokeStmt){
						InvokeStmt stmt = (InvokeStmt)unit;
						SootMethod intelMethod = stmt.getInvokeExpr().getMethod();
						if(intelMethod.getName().equals(targetMethod.getName())&&intelMethod.getParameterTypes().equals(targetMethod.getParameterTypes())){
							targetList.add(sm);
							break;
						}						
					}
				}
			}
		}
		return targetList;
	}
	
	public boolean hasMethod(SootClass sc,SootMethod targetMethod){
		List<SootMethod> list = getMethods(sc, targetMethod);
		if (list.size()>0) {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * @param sootMethod: onCreate()
	 * @param localClass: abstract
	 * @param jimpleValue: $r12 in onCreate
	 * @param targetMethod: onImportFinished
	 * */
	public boolean findTargetMethod(SootMethod sootMethod, String localClass, Value jimpleValue, SootMethod targetMethod){
		boolean flag = false;
		SootClass sootClass2 = Scene.v().getSootClass(localClass);	
		List<SootMethod> invokeList = getInvokeStmt(sootMethod, jimpleValue);	
		CallGraph callGraph = Scene.v().getCallGraph();
		
		for(SootMethod sMethod:invokeList){		
			flag = isAccess(sootMethod, targetMethod);
			if(flag){
				break;
			}
			if(sMethod.getName().equals("execute")){				
				SootMethod sMethod2 = null;
				for(SootMethod s:sootClass2.getMethods()){
					if(s.getName().equals("onPostExecute")){
						sMethod2 = s;
						break;
					}
				}
				if(sMethod2!=null){					
					flag = isAccess(sMethod2, targetMethod);
					if(flag){
						break;
					}
				}
			}
			Iterator<MethodOrMethodContext> target = new Targets(callGraph.edgesOutOf(sMethod));
			while (target.hasNext()) {
				SootMethod sootMethod2 = (SootMethod) target.next();
				flag = isAccess(sootMethod2, targetMethod);
				if(flag){
					break;
				}
			}
		}
		return flag;
	}
	
	public boolean isAccess(SootMethod sootMethod, SootMethod targetMethod){
		boolean flag = false;	
		IntraMethodAnalysis ma = new IntraMethodAnalysis(sootMethod);
		List<InvokeStmt> list = ma.getInvokeStmts();
		for(InvokeStmt invokeStmt:list){		
			SootMethod sMethod = invokeStmt.getInvokeExpr().getMethod();
			if(targetMethod.getName().equals(sMethod.getName())){
				flag = true;
				break;
			}				
		}
		return flag;
	}	
	
	public List<SootMethod> getInvokeStmt(SootMethod sootMethod, final Value jimpleValue){
		final List<SootMethod> invokeMethods = new ArrayList<SootMethod>();			
		Chain<Unit> chain = getUnits(sootMethod);
		for(Unit u:chain){
			u.apply(new AbstractStmtSwitch() {
				public void caseInvokeStmt(InvokeStmt stmt){
					Value value = stmt.getInvokeExpr().getUseBoxes().get(0).getValue();
					if(value.equals(jimpleValue)){
						invokeMethods.add(stmt.getInvokeExpr().getMethod());
					}
				}
			});
		}		
		return invokeMethods;
	}
	
	public Chain<Unit> getUnits(SootMethod sootMethod){
		Chain<Unit> chain = null;
		if(sootMethod.hasActiveBody()){
			Body body = sootMethod.getActiveBody();;
			chain = body.getUnits();
		}
		return chain;
	}	
	
}
