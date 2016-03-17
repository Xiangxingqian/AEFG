package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;

import com.app.test.Constants;

/**
 * add doMenuTest() method
 * {@code
 * for(int i=0;i<activityMenu.size-1; i++)
 * 		onOptionsItemSelected(activityMenu.getItem(i));
 * }
 * */
public class DoMenuTest extends MethodBuilder{

	public static final String CLASSNAME = "doMenuTest";
	public static final String SUBSIGNATURE = "void doMenuTest()";
	public DoMenuTest(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local base,menu,int1,int2,menuItem,flag,exception;
		
		base = addLocal("base", sc_Type);
		menu = addLocal("menu", menu_Type);
		menuItem = addLocal("menuItem", menuItem_Type);
		int1 = addLocal("int1", IntType.v());
		int2 = addLocal("int2", IntType.v());
		flag = addLocal("flag", BooleanType.v());
		exception = addLocal("exception", exception_Type);
		
		SootField activityMenu = sc.getFieldByName(Constants.ACTIVITYMENU);
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
//		InvokeStmt openOptionsMenu = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(base, openOptionsMenu_method.makeRef()));

		InvokeStmt openOptionsMenu = addInvokeStmt(Jimple.v().newVirtualInvokeExpr(base, openOptionsMenu_method.makeRef()));
		addAssignStmt(menu, Jimple.v().newInstanceFieldRef(base, activityMenu.makeRef()));
		addAssignStmt(int2, Jimple.v().newInterfaceInvokeExpr(menu, menuSize_method.makeRef()));
		addAssignStmt(int1, IntConstant.v(0));
		
		ReturnVoidStmt returnVoidStmt = Jimple.v().newReturnVoidStmt();
		AssignStmt iplusplus = Jimple.v().newAssignStmt(int1, Jimple.v().newAddExpr(int1, IntConstant.v(1)));
		
		IfStmt label0 = Jimple.v().newIfStmt(Jimple.v().newGeExpr(int1, int2), returnVoidStmt);
		body.getUnits().add(label0);
		addAssignStmt(menuItem, Jimple.v().newInterfaceInvokeExpr(menu,Constants.getItem_method.makeRef(),int1));
		List<Value> logParams = paramValues();
		logParams.add(menuItem);
		logParams.add(base);
		logParams.add(StringConstant.v("onOptionsItemSelected"));
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLog_method.makeRef(),logParams));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(base, onOptionsItemSelected_method.makeRef(),menuItem));
//		addAssignStmt(int1, Jimple.v().newAddExpr(int1, IntConstant.v(1)));
		body.getUnits().add(iplusplus);
		addGotoStmt(label0);
		
		body.getUnits().add(returnVoidStmt);
		
		IdentityStmt label5 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
		body.getUnits().add(label5);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogException_method.makeRef(),exception));
		addGotoStmt(returnVoidStmt);
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), openOptionsMenu, returnVoidStmt, label5);
		body.getTraps().add(trap);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
	}
	


}
