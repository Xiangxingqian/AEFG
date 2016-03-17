package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;


public class OnRestart extends MethodBuilder{

	public static final String CLASSNAME = "onRestart";
	public static final String SUBSIGNATURE = "void onRestart()";
	
	public OnRestart(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local base,intent,linkedList,flag,object,classLocal;
		{
			base =addLocal("base", RefType.v(sc));
			intent = addLocal("intent", intent_Type);
			linkedList = addLocal("linkedList", linkedList_Type);
			flag = addLocal("flag", BooleanType.v());
			object = addLocal("object", object_Type);
			classLocal = addLocal("classLocal", class_Type);
		}
		
		SootField unVisitedActivities_field = sc.getFieldByName("unVisitedActivities");
		
		//label0:
		ReturnVoidStmt label0 = Jimple.v().newReturnVoidStmt();
		
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(base, onRestart_method.makeRef()));
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(unVisitedActivities_field.makeRef()));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(linkedList, isEmpty_method.makeRef()));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label0);
		
		addAssignStmt(intent, Jimple.v().newNewExpr(intent_Type));
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(unVisitedActivities_field.makeRef()));
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList, poll_method.makeRef()));
		addAssignStmt(classLocal, Jimple.v().newCastExpr(object, class_Type));
		
		List<Value> paramValues = paramValues();
		paramValues.add(base);
		paramValues.add(classLocal);
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(intent, intentInit_method.makeRef(),paramValues));
		List<Value> paramValues1 = paramValues();
		paramValues1.add(StringConstant.v("qian"));
		paramValues1.add(StringConstant.v("OK"));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(intent, putExtra_method.makeRef(),paramValues1));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(base, startActivity_method.makeRef(),intent));
		body.getUnits().add(label0);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
		
	}
	
}
