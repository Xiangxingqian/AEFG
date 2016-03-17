package com.app.test.newMethod;

import java.util.List;

import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;


/**
 *  outprint: 输出每个事件
 */

public class OutPrint extends MethodBuilder{

	public static final String CLASSNAME = "outPrint";
	public static final String SUBSIGNATURE = "void outPrint(java.lang.reflect.Method,android.view.View)";
	public OutPrint(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local string,intLocal,stringBuilder,log, method,view,base, classLocal, string1;
		{
			method = addLocal("method", reflectMethod_Type);
			view = addLocal("view", view_Type);
			string = addLocal("string", string_Type);
			classLocal = addLocal("classLocal", class_Type);
			string1 = addLocal("string1", string_Type);
			intLocal = addLocal("intLocal", IntType.v());
			stringBuilder = addLocal("stringBuilder", stringBuilder_Type);
			log = addLocal("log", log_Type);
			base = addLocal("base", sc_Type);
		}
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addIdentityStmt(method, Jimple.v().newParameterRef(reflectMethod_Type, 0));
		addIdentityStmt(view, Jimple.v().newParameterRef(view_Type, 1));
		
		addAssignStmt(classLocal, Jimple.v().newVirtualInvokeExpr(base, getClass_method.makeRef()));
		addAssignStmt(string1, Jimple.v().newVirtualInvokeExpr(classLocal, classGetName_method.makeRef()));
		
		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(method, reflectMethodGetName_method.makeRef()));
		addAssignStmt(intLocal, Jimple.v().newVirtualInvokeExpr(view, getId_method.makeRef()));
		addAssignStmt(stringBuilder, Jimple.v().newNewExpr(stringBuilder_Type));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(stringBuilder, stringBuilderInit_method.makeRef()));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v("Event Happens in: ")));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),string1));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v("<View id: ")));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendInt_method.makeRef(),intLocal));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v(" Method Name: ")));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),string));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v(">")));
		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderToString_method.makeRef()));
		
		List<Value> paramValues = paramValues();
		paramValues.add(StringConstant.v("EVENT"));
		paramValues.add(string);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(logV_method.makeRef(),paramValues));
		addReturnVoidStmt();
	}

	@Override
	protected void newMethodName() {
		List<Type> paramTypes = paramTypes();
		paramTypes.add(reflectMethod_Type);
		paramTypes.add(view_Type);
		currentMethod = new SootMethod(CLASSNAME, paramTypes, VoidType.v(),Modifier.PUBLIC);
	}

}
