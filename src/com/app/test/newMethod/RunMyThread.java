package com.app.test.newMethod;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;


public class RunMyThread extends MethodBuilder{

	public static final String CLASSNAME = "runMyThread";
	public static final String SUBSIGNATURE = "void runMyThread()";
	
	public RunMyThread(SootClass sc, String subSignature) {
		super(sc, subSignature);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addUnits() {
		SootMethod finishInit_method = Scene.v().getMethod("<com.example.qian.FinishThread: void <init>(java.lang.Object)>");
		SootMethod testInit_method = Scene.v().getMethod("<com.example.qian.TestThread: void <init>(java.lang.Object)>");
		
		SootField isMyEvent = sc.getFieldByName(ISMYEVENT);
		SootField activities = sc.getFieldByName(ACTIVITIES);
		SootField unVisitedActivities = sc.getFieldByName(UNVISITEDACTIVITIES);

		Local base,testThread,finishThread,classLocal,linkedList1,flag;
		{
			base = addLocal("base", sc_Type);
			testThread = addLocal("testThread", testThread_Type);
			finishThread = addLocal("finishThread", finishThread_Type);
			classLocal = addLocal("classLocal", class_Type);
			linkedList1 = addLocal("linkedList1", linkedList_Type);
			flag = addLocal("flag", BooleanType.v());
		}
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		
		AssignStmt label1 = Jimple.v().newAssignStmt(classLocal, Jimple.v().newVirtualInvokeExpr(base, getClass_method.makeRef()));
		ReturnVoidStmt label0 = Jimple.v().newReturnVoidStmt();
		AssignStmt label2 = Jimple.v().newAssignStmt(finishThread, Jimple.v().newNewExpr(finishThread_Type));
		
		addAssignStmt(flag, Jimple.v().newStaticFieldRef(isMyEvent.makeRef()));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label1);
		addAssignStmt(testThread, Jimple.v().newNewExpr(testThread_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(testThread, testInit_method.makeRef(),base));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(testThread, threadStart_method.makeRef()));
		
		body.getUnits().add(label0);
		
		body.getUnits().add(label1);
		addAssignStmt(linkedList1, Jimple.v().newStaticFieldRef(activities.makeRef()));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(linkedList1, contains_method.makeRef(),classLocal));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)),label2);
		addAssignStmt(linkedList1, Jimple.v().newStaticFieldRef(activities.makeRef()));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList1, offer_method.makeRef(),classLocal));
		addAssignStmt(linkedList1, Jimple.v().newStaticFieldRef(unVisitedActivities.makeRef()));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList1, offer_method.makeRef(),classLocal));

		body.getUnits().add(label2);
		//addAssignStmt(finishThread, Jimple.v().newSpecialInvokeExpr(finishThread, finishInit_method.makeRef(),base));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(finishThread, finishInit_method.makeRef(),base));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(finishThread, threadStart_method.makeRef()));
		addGotoStmt(label0);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
	}

}
