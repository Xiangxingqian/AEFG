package com.aefg.dataflowanalysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import singlton.SingltonFactory;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import vasco.Context;

public class Contexts {
	private Map<SootMethod,Context<SootMethod,Unit,Map<Local, List<Value>>>> analyzedContexts = 
			new HashMap<SootMethod,Context<SootMethod,Unit,Map<Local, List<Value>>>>();
	private Map<SootMethod,Context<SootMethod,Unit,Map<Local, List<Value>>>> analyzingContexts = 
			new HashMap<SootMethod,Context<SootMethod,Unit,Map<Local, List<Value>>>>();
	public Contexts(SingltonFactory.G g) {}
	public static Contexts v(){
		return SingltonFactory.v().getContexts();
	}
	
	public Map<SootMethod, Context<SootMethod, Unit, Map<Local, List<Value>>>> getAnalyzedContexts() {
		return analyzedContexts;
	}
	public Map<SootMethod, Context<SootMethod, Unit, Map<Local, List<Value>>>> getAnalyzingContexts() {
		return analyzingContexts;
	}
}
