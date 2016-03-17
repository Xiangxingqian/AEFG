package com.app.test.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootClass;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

import com.app.test.Constants;
import com.test.xmldata.ManifestData;

public class SystemEventCollector {
	
	Body body;
	public SystemEventCollector(Body body) {
		super();
		this.body = body;
	}
	
	/**
	 * Collect System events, includes service and receiver events.
	 * If body contains valid system event, do the following:
	 * 1. SystemEvent systemEvent = new SystemEvent(service, listener, registar);
	 * 2. MainActivity.systemeventlinkedlist.offer(systemevent);
	 * */
	public void collectEvents(){
		final SootClass sc = body.getMethod().getDeclaringClass();
		final SootClass mainActivity = soot.Scene.v().getSootClass(ManifestData.mainActivity);
		final PatchingChain<Unit> units = body.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				public void caseInvokeStmt(InvokeStmt stmt) {
					//if stmt is a service event, SystemEvent systemevent = new SystemEvent(1,2,3);
					if(SystemEventRecognizer.isSystemEvent(stmt)){
						InvokeExpr invokeExpr = stmt.getInvokeExpr();
						String registar = invokeExpr.getMethod().getSubSignature();
						Value manager = invokeExpr.getUseBoxes().get(invokeExpr.getUseBoxes().size()-1).getValue();
						List<Value> values = new ArrayList<Value>();
						try {
							Local base = null;
							base = body.getThisLocal();
						} catch (Exception e) {
							System.err.println(stmt+" should not exist in static method");
						}
						if(SystemEventRecognizer.isServiceEvent(stmt)){
							Local linkedList = addRandomLocal(body, "linkedList", Constants.linkedList_Type);
							Local systemEvent = addRandomLocal(body, "systemEvent", Constants.systemEvent_Type);
							Value listener = getListener(invokeExpr);
							values.add(manager);
							values.add(listener);
							values.add(StringConstant.v(registar));
							units.insertBefore(Jimple.v().newAssignStmt(
									systemEvent, Jimple.v().newNewExpr(Constants.systemEvent_Type)), u);
							units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(systemEvent, Constants.systemEventinit_method.makeRef(),values)), u);
							units.insertBefore(Jimple.v().newAssignStmt(
									linkedList, Jimple.v().newStaticFieldRef(mainActivity.getFieldByName(Constants.SYSTEMEVENTLINKEDLIST).makeRef())), u);
							units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList, Constants.offer_method.makeRef(),systemEvent)), u);
							body.validate();
						}
						//else if stmt is a receiver event, SystemEvent systemevent = new SystemEvent(1,2,3,4);
						else if(SystemEventRecognizer.isReceiverEvent(stmt)){
							Local linkedList = addRandomLocal(body, "linkedList", Constants.linkedList_Type);
							Local receiverEvent = addRandomLocal(body, "receiverEvent", Constants.receiverEvent_Type);
							Value listener = invokeExpr.getArg(0);
							Value intentFilter = invokeExpr.getArg(1);
							values.add(manager);
							values.add(listener);
							values.add(StringConstant.v(registar));
							values.add(intentFilter);
							units.insertBefore(Jimple.v().newAssignStmt(
									receiverEvent, Jimple.v().newNewExpr(Constants.receiverEvent_Type)), u);
							units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(receiverEvent, Constants.receiverEventinit_method.makeRef(),values)), u);
							units.insertBefore(Jimple.v().newAssignStmt(
									linkedList, Jimple.v().newStaticFieldRef(mainActivity.getFieldByName(Constants.SYSTEMEVENTLINKEDLIST).makeRef())), u);
							units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList, Constants.offer_method.makeRef(),receiverEvent)), u);
							body.validate();
						}
					}
				}
			});
		}
	}
	
	// use random to avoid of the same name
	private Local addRandomLocal(Body body, String name, Type t){
		int i = (int)(10000*Math.random());
		Local local = Jimple.v().newLocal(name+i, t);
		body.getLocals().add(local);
		return local;
	}
	
	/**
	 * Collect invokeExpr's listener, according its location in paramters, return its value.
	 * @see{SystemEventConstants}
	 * */
	private Value getListener(InvokeExpr invokeExpr){
		String subSignature = invokeExpr.getMethod().getSubSignature();
		if(SystemEventConstants.serviceType1List.contains(subSignature)){
			return invokeExpr.getArg(0);
		}
		else if(SystemEventConstants.serviceType2List.contains(subSignature)){
			return invokeExpr.getArg(1);
		}
		else if(SystemEventConstants.serviceType4List.contains(subSignature)){
			return invokeExpr.getArg(3);
		}
		throw new RuntimeException("Invalid InvokerExpr: "+invokeExpr);
	}
}
