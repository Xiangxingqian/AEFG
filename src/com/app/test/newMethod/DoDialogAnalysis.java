package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;


/**
 * add doDialogAnalysis(method,dialog,object) method.
 * 
 * {@code
 * String name = method.getName();
 * if("onDismiss".equals(name)){
 * 		method.invoke(object,dialog);
 * }
 * }
 * */

public class DoDialogAnalysis extends MethodBuilder{
	public static final String CLASSNAME = "doDialogAnalysis";
	public static final String SUBSIGNATURE = "void doDialogAnalysis(java.lang.reflect.Method,android.app.Dialog,java.lang.Object)";
	public DoDialogAnalysis(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local base,string1, string2,method,dialog,object,arrayObject,flag,booleanLocal,exception;
		Local methodParams, size;
		{
			base = addLocal("base", sc_Type);
			string1 = addLocal("string1", string_Type);//constants like "onClick"...
			string2 = addLocal("string2", string_Type);//methodName
			booleanLocal = addLocal("booleanLocal", Boolean_Type);
			exception = addLocal("exception", exception_Type);
			method = addLocal("method", reflectMethod_Type);
			dialog = addLocal("dialog", dialog_Type);
			object = addLocal("object", object_Type);
			arrayObject = addLocalArray("arrayObject", object_Type);
			flag = addLocal("flag", BooleanType.v());
			
			methodParams = addLocalArray("methodParams", class_Type);
			size = addLocal("size", IntType.v());
		}
		
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addIdentityStmt(method, Jimple.v().newParameterRef(reflectMethod_Type, 0));
		addIdentityStmt(dialog, Jimple.v().newParameterRef(dialog_Type, 1));
		addIdentityStmt(object, Jimple.v().newParameterRef(object_Type, 2));
		
		ReturnVoidStmt returnVoidStmt = Jimple.v().newReturnVoidStmt();
		AssignStmt body1 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
		AssignStmt body2 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(2)));
		AssignStmt onClick = Jimple.v().newAssignStmt(string1, StringConstant.v("onClick"));
		
		addAssignStmt(string2, Jimple.v().newVirtualInvokeExpr(method, reflectMethodGetName_method.makeRef()));
		
		AssignStmt tryAssignStmt = Jimple.v().newAssignStmt(string1,StringConstant.v("onShow"));
		
		body.getUnits().add(tryAssignStmt);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body1);
		
		addAssignStmt(string1, StringConstant.v("onCancel"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onClick);
		
		body.getUnits().add(body1);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), dialog);
		List<Value> param1 = paramValues();
		param1.add(object);
		param1.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param1));
		body.getUnits().add(returnVoidStmt);
		
		//else if(onClick.equals(name));
		body.getUnits().add(onClick);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), returnVoidStmt);
		
		body.getUnits().add(body2);//AssignStmt body2 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(2)));
		
		addAssignStmt(methodParams, Jimple.v().newVirtualInvokeExpr(method, getParameterTypes_method.makeRef()));
		addAssignStmt(size, Jimple.v().newLengthExpr(methodParams));
		addIfStmt(Jimple.v().newNeExpr(size, IntConstant.v(2)), returnVoidStmt);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), dialog);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), IntConstant.v(0));
		List<Value> param2 = paramValues();
		param2.add(object);
		param2.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param2));
		GotoStmt addGotoStmt = addGotoStmt(returnVoidStmt);
		
		IdentityStmt label5 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
		body.getUnits().add(label5);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogException_method.makeRef(),exception));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(exception,printStackTrace_method.makeRef()));
		addGotoStmt(returnVoidStmt);
		
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), tryAssignStmt, addGotoStmt, label5);
		body.getTraps().add(trap);
		
	}

	@Override
	protected void newMethodName() {
		List<Type> paramTypes = paramTypes();
		paramTypes.add(reflectMethod_Type);
		paramTypes.add(dialog_Type);
		paramTypes.add(object_Type);
		currentMethod = new SootMethod(CLASSNAME, paramTypes, VoidType.v(),Modifier.PUBLIC);
	}


}
