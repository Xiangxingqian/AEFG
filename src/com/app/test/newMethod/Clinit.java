package com.app.test.newMethod;

import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.internal.JNewExpr;

import com.app.test.Constants;
import com.test.xmldata.ManifestData;

/**
 * add clinit() method.
 * {@code
 * viewLinkedList = new LinkedList<View>();
 * viewListenerLinkedList = new LinkedList<Object>();
 * viewRegistarLinkedList = new LinkedList<String>();
 * dialogLinkedList = new LinkedList<Dialog>();
 * dialogListenerLinkedList = new LinkedList<Object>();
 * dialogRegistarLinkedList = new LinkedList<String>(); 
 * isVisited = false;
 * }
 * */
public class Clinit extends MethodBuilder{
	public static final String CLASSNAME = "<clinit>";
	public static final String SUBSIGNATURE = "void <clinit>()";
	SootField viewLinkedList;
	SootField listenerLinkedList;
	SootField isMyEvent;
	SootField activities;
	SootField unVisitedActivities;
	SootClass sootClass;
	SootField unVisitedActivities_mainActivity;
	SootField activities_mainActivity;
	SootField isVisited,systemEventLinkedList;
	Local linkedList;
	boolean main = false;
	public Clinit(SootClass sc, String subSignature, boolean main) {
		super(sc, subSignature);
		this.main = main;
	}

	@Override
	protected void addUnits() {
		linkedList = addLocal("linkedList", linkedList_Type);
		viewLinkedList = sc.getFieldByName(VIEWLINKEDLIST);
		listenerLinkedList = sc.getFieldByName(LISTENERLINKEDLIST);
		isMyEvent = sc.getFieldByName(ISMYEVENT);
		isVisited = sc.getFieldByName(ISVISITED);
		activities = sc.getFieldByName(ACTIVITIES);
		unVisitedActivities = sc.getFieldByName(UNVISITEDACTIVITIES);
		
		SootField dialogLinkedList = sc.getFieldByName(Constants.DIALOGLINKEDLIST);
		SootField dialogListenerLinkedList = sc.getFieldByName(Constants.DIALOGLISTENERLINKEDLIST);
		SootField dialogRegistarLinkedList = sc.getFieldByName(Constants.DIALOGREGISTARLINKEDLIST);
		SootField viewRegistarLinkedList = sc.getFieldByName(Constants.VIEWREGISTARLINKEDLIST);
		
		sootClass = Scene.v().getSootClass(ManifestData.mainActivity);
		unVisitedActivities_mainActivity = sootClass.getFieldByName(UNVISITEDACTIVITIES);
		activities_mainActivity = sootClass.getFieldByName(ACTIVITIES);

		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(viewLinkedList.makeRef()), linkedList);
		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(listenerLinkedList.makeRef()), linkedList);
		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(dialogLinkedList.makeRef()), linkedList);
		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(dialogListenerLinkedList.makeRef()), linkedList);
		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(dialogRegistarLinkedList.makeRef()), linkedList);
		addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
		addAssignStmt(Jimple.v().newStaticFieldRef(viewRegistarLinkedList.makeRef()), linkedList);
		
		addAssignStmt(Jimple.v().newStaticFieldRef(isMyEvent.makeRef()),IntConstant.v(0));
		addAssignStmt(Jimple.v().newStaticFieldRef(isVisited.makeRef()),IntConstant.v(0));
		
		addQueue(main);
		
		addReturnVoidStmt();
	}

	private void addQueue(boolean main) {
		if(main){
			systemEventLinkedList = sc.getFieldByName(SYSTEMEVENTLINKEDLIST);
			addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(activities.makeRef()), linkedList);
			addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(unVisitedActivities.makeRef()), linkedList);
			addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(systemEventLinkedList.makeRef()), linkedList);
		}
		else {
			addAssignStmt(linkedList,Jimple.v().newStaticFieldRef(unVisitedActivities_mainActivity.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(unVisitedActivities.makeRef()), linkedList);
			addAssignStmt(linkedList,Jimple.v().newStaticFieldRef(activities_mainActivity.makeRef()) );
			addAssignStmt(Jimple.v().newStaticFieldRef(activities.makeRef()), linkedList);
		}
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.STATIC);
	}
}
