package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;


/**
 * if activity does not define onCreateOptionsMenu(), add it.
 * <code>
 * boolean onCreateOptionsMenu(){
 * super.onCreateOptionsMenu(menu);
 * MenuItem menuItem = menu.add("Test");
 * menuItem.setOnMenuItemClickListener(this);
 * return true;
 * }
 * </code>
 * */

public class OnCreateOptionsMenu extends MethodBuilder{

	public static final String CLASSNAME = "onCreateOptionsMenu";
	public static final String SUBSIGNATURE = "boolean onCreateOptionsMenu(android.view.Menu)";
	
	
	public OnCreateOptionsMenu(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}
	
	@Override
	protected void addUnits() {
		Local activity,menu,menuItem;
		activity = addLocal("activity", sc_Type);
		menu = addLocal("menu", menu_Type);
		menuItem = addLocal("menuItem", menuItem_Type);
		
		addIdentityStmt(activity, new ThisRef(sc_Type));
		addIdentityStmt(menu, new ParameterRef(menu_Type,0));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(activity, onCreateOptionsMenu_method.makeRef(),menu));
		addAssignStmt(menuItem, Jimple.v().newInterfaceInvokeExpr(menu, menuAdd_method.makeRef(),StringConstant.v("TEST")));
		addInvokeStmt(Jimple.v().newInterfaceInvokeExpr(menuItem, setOnMenuItemClickListener_method.makeRef(),activity));
		
		//add insert method: SystemEventHandler: void addMenuItem(activity,menu);
		List<Value> paramValues = paramValues();
		paramValues.add(activity);
		paramValues.add(menu);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(systemEventHandleraddMenuItem_method.makeRef(),paramValues));
		
		addReturnTypeStmt(IntConstant.v(1));
	}

	@Override
	protected void newMethodName() {
		List<Type> emptyTypes = paramTypes();
		emptyTypes.add(menu_Type);
		currentMethod = new SootMethod(CLASSNAME, emptyTypes, BooleanType.v(),Modifier.PUBLIC);
	}	
}
