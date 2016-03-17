package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import vasco.Context;

public class AEFG_LocalResolver {
	Local l;
	SootMethod sm;
	List<Type> types;
	Unit currentUnit;
	List<Value> values = new ArrayList<Value>();
	public AEFG_LocalResolver(Local l, SootMethod sm, List<Type> types,
			Unit currentUnit) {
		this.l = l;
		this.sm = sm;
		this.types = types;
		this.currentUnit = currentUnit;
		
	}
	
	public void build(){
		AEFGLocalInterproceduralAnalysis analysis = new AEFGLocalInterproceduralAnalysis(sm, types);
		Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure 
			= analysis.doInterprocedure(sm, new HashMap<Local, List<Value>>());
		Map<Local, List<Value>> valueBefore = doInterprocedure.getValueBefore(currentUnit);
		values = valueBefore.get(l);
	}
	
	public List<Value> getValues() {
		return values;
	}
	
	
}
