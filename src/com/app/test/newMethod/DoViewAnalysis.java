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
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;

/**
 * add doViewAnalysis(method,view,object) method, classify events
 *
 * <code> 
 * String name = method.getName();
 * if("onClick".equals(name))
 *     method.invoke(object,view);
 * else if("onItemSelected".equals(name)){
 *     params = {view, null,0,0};
 *	   method.invoke(object, params)
 *	}
 * }
 * </code>
 * 
 * */

public class DoViewAnalysis extends MethodBuilder{
	public static final String CLASSNAME = "doViewAnalysis";
	public static final String SUBSIGNATURE = "void doViewAnalysis(java.lang.reflect.Method,android.view.View,java.lang.Object)";
	public DoViewAnalysis(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local base,string1, string2,method,view,object,arrayObject,flag,booleanLocal,exception,intLocal1,integerLocal;
		Local methodParams, size;
		{
			base = addLocal("base", sc_Type);
			string1 = addLocal("string1", string_Type);
			string2 = addLocal("string2", string_Type);
			booleanLocal = addLocal("booleanLocal", Boolean_Type);
			exception = addLocal("exception", exception_Type);
			method = addLocal("method", reflectMethod_Type);
			view = addLocal("view", view_Type);
			object = addLocal("object", object_Type);
			arrayObject = addLocalArray("arrayObject", object_Type);
			flag = addLocal("flag", BooleanType.v());
			intLocal1 = addLocal("intLocal1", IntType.v());
			integerLocal = addLocal("integerLocal",integer_Type);
			
			methodParams = addLocalArray("methodParams", class_Type);
			size = addLocal("size", IntType.v());
			
			
		}
		addIdentityStmt(base, Jimple.v().newThisRef(sc_Type));
		addIdentityStmt(method, Jimple.v().newParameterRef(reflectMethod_Type, 0));
		addIdentityStmt(view, Jimple.v().newParameterRef(view_Type, 1));
		addIdentityStmt(object, Jimple.v().newParameterRef(object_Type, 2));
		
		ReturnVoidStmt returnVoidStmt = Jimple.v().newReturnVoidStmt();
		AssignStmt onLongClick = Jimple.v().newAssignStmt(string1, StringConstant.v("onLongClick"));
		AssignStmt onFocusChange = Jimple.v().newAssignStmt(string1, StringConstant.v("onFocusChange"));
		AssignStmt onSystemUiVisibilityChange = Jimple.v().newAssignStmt(string1, StringConstant.v("onSystemUiVisibilityChange"));
		AssignStmt onItemClick = Jimple.v().newAssignStmt(string1, StringConstant.v("onItemClick"));
		AssignStmt onProgressChanged = Jimple.v().newAssignStmt(string1, StringConstant.v("onProgressChanged"));
		AssignStmt onScrollStateChange = Jimple.v().newAssignStmt(string1, StringConstant.v("onScrollStateChange"));
		AssignStmt onScroll = Jimple.v().newAssignStmt(string1, StringConstant.v("onScroll"));
		AssignStmt onPageSelected = Jimple.v().newAssignStmt(string1, StringConstant.v("onPageSelected"));
		AssignStmt onPageScrolled = Jimple.v().newAssignStmt(string1, StringConstant.v("onPageScrolled"));

		AssignStmt body1 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
		AssignStmt body2 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(2)));
		AssignStmt body3 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
		AssignStmt body4 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(4)));
		AssignStmt body5 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(3)));
		AssignStmt body6 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(2)));
		AssignStmt body7 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(4)));
		AssignStmt body8 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
		AssignStmt body9 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(3)));
		
		addAssignStmt(string2, Jimple.v().newVirtualInvokeExpr(method, reflectMethodGetName_method.makeRef()));
		
		AssignStmt tryAssignStmt = Jimple.v().newAssignStmt(string1,StringConstant.v("onClick"));
		body.getUnits().add(tryAssignStmt);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onLongClick);
		
		addAssignStmt(methodParams, Jimple.v().newVirtualInvokeExpr(method, getParameterTypes_method.makeRef()));
		addAssignStmt(size, Jimple.v().newLengthExpr(methodParams));
		addIfStmt(Jimple.v().newEqExpr(size, IntConstant.v(1)), body1);
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onLongClick);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body1);
		
		addAssignStmt(string1, StringConstant.v("onNothingSelected"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body1);
		
		addAssignStmt(string1, StringConstant.v("onStartTrackingTouch"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body1);
		
		addAssignStmt(string1, StringConstant.v("onStopTrackingTouch"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onFocusChange);
		
		body.getUnits().add(body1);//Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		List<Value> param1 = paramValues();
		param1.add(object);
		param1.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param1));
		
		body.getUnits().add(returnVoidStmt);
		
		body.getUnits().add(onFocusChange);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body2);
		
		addAssignStmt(string1, StringConstant.v("onCheckedChanged"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onSystemUiVisibilityChange);
		
		body.getUnits().add(body2);
		addAssignStmt(booleanLocal, Jimple.v().newStaticInvokeExpr(booleanValueOf_method.makeRef(),IntConstant.v(1)));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), booleanLocal);
		List<Value> param2 = paramValues();
		param2.add(object);
		param2.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param2));
		addAssignStmt(booleanLocal, Jimple.v().newStaticInvokeExpr(booleanValueOf_method.makeRef(),IntConstant.v(0)));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), booleanLocal);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param2));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onSystemUiVisibilityChange);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onItemClick);
		
		body.getUnits().add(body3);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), IntConstant.v(0));
		List<Value> param3 = paramValues();
		param3.add(object);
		param3.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param3));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onItemClick);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body4);
		
		addAssignStmt(string1, StringConstant.v("onItemLongClick"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body4);
		
		addAssignStmt(string1, StringConstant.v("onItemSelected"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onProgressChanged);
		
		body.getUnits().add(body4);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), NullConstant.v());
		//已被坑死，该死的拆装箱问题。 object[2] = 1; 应该写成 object[2] = Integer.valueOf(1);
		addAssignStmt(integerLocal, Jimple.v().newStaticInvokeExpr(integerValueOf_method.makeRef(),IntConstant.v(10)));//已被坑死，该死的拆装箱问题。
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(2)), integerLocal);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(3)), NullConstant.v());//Caution: LongConstant.v(0)会报错
		List<Value> param4 = paramValues();
		param4.add(object);
		param4.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param4));
		addGotoStmt(returnVoidStmt);
		body.getUnits().add(onProgressChanged);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onScrollStateChange);
		
		body.getUnits().add(body5);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), IntConstant.v(0));
		addAssignStmt(booleanLocal, Jimple.v().newStaticInvokeExpr(booleanValueOf_method.makeRef(),IntConstant.v(1)));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(2)), booleanLocal);
		List<Value> param5 = paramValues();
		param5.add(object);
		param5.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param5));
		addAssignStmt(booleanLocal, Jimple.v().newStaticInvokeExpr(booleanValueOf_method.makeRef(),IntConstant.v(0)));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(2)), booleanLocal);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param5));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onScrollStateChange);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onScroll);
		
		body.getUnits().add(body6);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), IntConstant.v(0));
		List<Value> param6 = paramValues();
		param6.add(object);
		param6.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param6));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onScroll);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)),onPageSelected);
		
		body.getUnits().add(body7);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), IntConstant.v(0));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(2)), IntConstant.v(0));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(3)), IntConstant.v(0));
		List<Value> param7 = paramValues();
		param7.add(object);
		param7.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param7));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onPageSelected);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), body8);
		
		addAssignStmt(string1, StringConstant.v("onPageScrollStateChanged"));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), onPageScrolled);
		
		body.getUnits().add(body8);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), IntConstant.v(0));
		List<Value> param8 = paramValues();
		param8.add(object);
		param8.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param8));
		addGotoStmt(returnVoidStmt);
		
		body.getUnits().add(onPageScrolled);
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(),string2));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), returnVoidStmt);
		
		body.getUnits().add(body9);
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), IntConstant.v(0));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(1)), FloatConstant.v(0));
		addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(2)), IntConstant.v(0));
		List<Value> param9 = paramValues();
		param9.add(object);
		param9.add(arrayObject);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(method, invoke_method.makeRef(),param9));
		GotoStmt addGotoStmt = addGotoStmt(returnVoidStmt);
		
		IdentityStmt label5 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
		body.getUnits().add(label5);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogException_method.makeRef(),exception));
		addGotoStmt(returnVoidStmt);
		
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), tryAssignStmt, addGotoStmt, label5);
		body.getTraps().add(trap);
	}

	@Override
	protected void newMethodName() {
		List<Type> paramTypes = paramTypes();
		paramTypes.add(reflectMethod_Type);
		paramTypes.add(view_Type);
		paramTypes.add(object_Type);
		currentMethod = new SootMethod(CLASSNAME, paramTypes, VoidType.v(),Modifier.PUBLIC);
	}
}
