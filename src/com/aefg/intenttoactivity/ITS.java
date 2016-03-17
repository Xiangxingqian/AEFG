package com.aefg.intenttoactivity;


import java.util.ArrayList;
import java.util.List;

import com.aefg.intent.IntentFlowAnalysis;

import singlton.Global;
import singlton.SingltonFactory;
import soot.SootMethod;
import soot.jimple.InvokeStmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import tags.MethodTag;

public class ITS {
	public ITS(singlton.SingltonFactory.G g){}
	public static ITS v(){
		return SingltonFactory.v().getITS();
	}
	
	private List<ITSPair> ITSPairs = new ArrayList<ITSPair>();
	public  List<ITSPair> getITSPairs(){
		return ITSPairs;
	}
	
	public void genITS(){
		List<InvokeStmt> allStartActivities = Global.v().getAllStartActivities();
		for(InvokeStmt is:allStartActivities){
			SootMethod method = MethodTag.parseMethod(is);
			IntentFlowAnalysis ifa = new IntentFlowAnalysis(new ExceptionalUnitGraph(method.retrieveActiveBody()));
			ifa.genITS(is);	
		}	
 	}
}
