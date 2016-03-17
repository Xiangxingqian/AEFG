package com.app.test.newMethod;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;


public class CheckEvent extends MethodBuilder{
	
	public static final String CLASSNAME = "CheckEvent";
	public static final String SUBSIGNATURE = "void CheckEvent()";
	
	public CheckEvent(SootClass sc, String subSignature) {
		super(sc, subSignature);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addUnits() {
		Local base, bundle,intent,object,flag;
		SootField isMyEvent = sc.getFieldByName(ISMYEVENT);
		{
			base = addLocal("base", sc_Type);
			bundle = addLocal("bundle", bundle_Type);
			intent = addLocal("intent", intent_Type);
			object = addLocal("object", object_Type);
			flag = addLocal("flag", BooleanType.v());
		}
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addAssignStmt(intent, Jimple.v().newVirtualInvokeExpr(base, getIntent_method.makeRef()));
		addAssignStmt(bundle, Jimple.v().newVirtualInvokeExpr(intent, getExtras_method.makeRef()));

		//label0
		ReturnVoidStmt label0 = Jimple.v().newReturnVoidStmt();
		
		addIfStmt(Jimple.v().newEqExpr(bundle, NullConstant.v()), label0);
//		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(bundle, bundleGet_method.makeRef(),StringConstant.v("qian")));
		addIfStmt(Jimple.v().newEqExpr(object, NullConstant.v()), label0);
		
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(object, objectEquals_method.makeRef(),StringConstant.v("OK")));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label0);
		addAssignStmt(Jimple.v().newStaticFieldRef(isMyEvent.makeRef()), IntConstant.v(1));
		body.getUnits().add(label0);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
	}

}
