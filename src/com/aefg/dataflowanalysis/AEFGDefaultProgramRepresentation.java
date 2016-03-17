package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.jimple.toolkits.callgraph.Targets;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import sootAnalysis.Instrumentor;
import vasco.ProgramRepresentation;

public class AEFGDefaultProgramRepresentation implements ProgramRepresentation<SootMethod, Unit>{
	
	private SootMethod sm;
	private CallGraph callGraph;
	private Map<SootMethod, DirectedGraph<Unit>> cfgCache;
	
	AEFGDefaultProgramRepresentation(SootMethod sm, CallGraph cg){
		this.sm = sm;
		this.callGraph = cg;
		cfgCache = new HashMap<SootMethod, DirectedGraph<Unit>>();
	}
	@Override
	public List<SootMethod> getEntryPoints() {
		return Collections.singletonList(sm);
	}

	@Override
	public DirectedGraph<Unit> getControlFlowGraph(SootMethod method) {
		if (cfgCache.containsKey(method) == false) {
			if(!method.getDeclaringClass().isPhantomClass())
				cfgCache.put(method, new ExceptionalUnitGraph(method.retrieveActiveBody()));
		}
		return cfgCache.get(method);
	}

	@Override
	public boolean isCall(Unit node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SootMethod> resolveTargets(SootMethod callerMethod,
			Unit callNode) {
		// TODO Auto-generated method stub
		List<SootMethod> targets = new LinkedList<SootMethod>();
		Iterator<Edge> it = callGraph.edgesOutOf(callNode);
		while(it.hasNext()) {
			Edge edge = it.next();
			if (edge.isExplicit()) {
				targets.add(edge.tgt());
			}
		}
		return targets;
	}
	
	@Override
	public Map<SootMethod, List<Unit>> resolveSources(SootMethod calleeMethod) {
		Map<SootMethod, List<Unit>> map = initMap();
		//List<SootMethod> sources = new LinkedList<SootMethod>();
		Iterator<Edge> it = callGraph.edgesInto(calleeMethod);
		while(it.hasNext()) {
			Edge edge = it.next();
			if (edge.isExplicit()) {
				if(edge.src().equals(calleeMethod)){
					continue;
				}
				if(map.get(edge.src())==null){
					List<Unit> srcUnits = new ArrayList<Unit>();
					srcUnits.add(edge.srcUnit());
					map.put(edge.src(), srcUnits);
				}
				else {
					map.get(edge.src()).add(edge.srcUnit());
				}
				
			}
		}
		return map;
	}

	public List<SootMethod> getTargets(SootMethod sootMethod){
		List<SootMethod> methods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> targets = new Targets(callGraph.edgesOutOf(sootMethod));
		while(targets.hasNext()){
			SootMethod m = (SootMethod)targets.next();
			methods.add(m);
		}
		return methods;
	}
	
	public List<SootMethod> getSources(SootMethod sootMethod){
		List<SootMethod> methods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> sources = new Sources(callGraph.edgesInto(sootMethod));
		while(sources.hasNext()){
			SootMethod m = (SootMethod)sources.next();
			methods.add(m);
		}
		return methods;
	}
	
	@Override
	public boolean isIdentity(Unit node) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Map<SootMethod, List<Unit>> initMap() {
		List<Unit> list = new ArrayList<Unit>();
		Map<SootMethod, List<Unit>> map = new HashMap<SootMethod, List<Unit>>();
		return map;
	}
	
	
//	public static ProgramRepresentation<SootMethod, Unit> v(SootMethod sm){
//		return new AEFGDefaultProgramRepresentation(sm);
//	}
}
