package com.app.test.newMethod;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;


public class CheckEventForMainActivity extends MethodBuilder{

	public static final String CLASSNAME = "CheckEvent";
	public static final String SUBSIGNATURE = "void CheckEvent()";
	public CheckEventForMainActivity(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local intent,string,base,flag,string2;
		SootField isMyEvent = sc.getFieldByName(ISMYEVENT);
		
		{
			base = addLocal("base", sc_Type);
			intent = addLocal("intent", intent_Type);
			flag = addLocal("flag", BooleanType.v());
			string = addLocal("string", string_Type);
			string2 = addLocal("string2", string_Type);
		}
		
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addAssignStmt(intent, Jimple.v().newVirtualInvokeExpr(base, getIntent_method.makeRef()));
		
		ReturnVoidStmt label0 = Jimple.v().newReturnVoidStmt();
		
		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(intent, getAction_method.makeRef()));
		addAssignStmt(string2, StringConstant.v("android.intent.action.MAIN"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string2,stringEquals_method.makeRef(), string));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label0);
		addAssignStmt(Jimple.v().newStaticFieldRef(isMyEvent.makeRef()), IntConstant.v(1));
		body.getUnits().add(label0);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
	}

}
