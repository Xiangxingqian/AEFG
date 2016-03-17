package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;

/**
 *  add doReflect(View, listener, registar) method.
 *  
 *  add view, listener, registar into view's belonging activity.
 *  <code>
 *  	Context con = view.getContext();
 *  	String name = con.getName();
 *  	Class class = Class.forname(name);
 *      Field viewLinkedList = class.getField("viewLinkedList");
 *      Field listenerLinkedList = class.getField("listenerLinkedList");
 *      Field VIEWREGISTARLINKEDLIST = class.getField("VIEWREGISTARLINKEDLIST");
 *      LinkedList o1 = viewLinkedList.get(null);
 *      o1.add(view);
 *      LinkedList o2 = listenerLinkedList.get(null);
 *      o2.add(object);
 *      LinkedList o3 = VIEWREGISTARLINKEDLIST.get(null);
 *      o3.add(string);
 *  </code>
 * */
public class DoReflect extends MethodBuilder{

	public static final String CLASSNAME = "doReflect";
	public static final String SUBSIGNATURE = "void doReflect(android.view.View,java.lang.Object,java.lang.String)";
	public DoReflect(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		
		Local view,context,object1,thirdString,object2,exception,field1,field2,field3,field4,
		string,classLocal,linkedList1,linkedList2,linkedList3,flag,booleanLocal;
		{
//			currentActivity = addLocal("activity", sc_Type);
			object1 = addLocal("object1", object_Type);
			object2 = addLocal("object2", object_Type);
			view = addLocal("view", view_Type);
			flag = addLocal("flag", BooleanType.v());
			classLocal = addLocal("classLocal", class_Type);
			linkedList1 = addLocal("linkedList1", linkedList_Type);
			linkedList2 = addLocal("linkedList2", linkedList_Type);
			linkedList3 = addLocal("linkedList3", linkedList_Type);
			string = addLocal("string", string_Type);
			thirdString = addLocal("thirdString", string_Type);
			exception = addLocal("exception", exception_Type);
			field1 =  addLocal("field1", field_Type);
			field2 =  addLocal("field2", field_Type);
			field3 =  addLocal("field3", field_Type);
			field4 = addLocal("field4", field_Type);
			booleanLocal =  addLocal("booleanLocal", Boolean_Type);
			context =  addLocal("context", context_Type);
		}
		
//		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
		addIdentityStmt(view, new ParameterRef(view_Type,0));
		addIdentityStmt(object1, new ParameterRef(object_Type,1));
		addIdentityStmt(thirdString, new ParameterRef(string_Type,2));
		addAssignStmt(context,Jimple.v().newVirtualInvokeExpr(view,view_getContext_method.makeRef()));
		addAssignStmt(classLocal,Jimple.v().newVirtualInvokeExpr(context,getClass_method.makeRef()));
		addAssignStmt(string,Jimple.v().newVirtualInvokeExpr(classLocal,classGetName_method.makeRef()));
		
		//label0,
		AssignStmt label0 = Jimple.v().newAssignStmt(classLocal, Jimple.v().newStaticInvokeExpr(forName_method.makeRef(),string));
		//label1, 
		ReturnVoidStmt label1 = Jimple.v().newReturnVoidStmt();
		//label2, 
		IdentityStmt label2 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
	
		body.getUnits().add(label0);
		addAssignStmt(field1,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v("viewLinkedList")));
		
//		addAssignStmt(classLocal,Jimple.v().newStaticInvokeExpr(forName_method.makeRef(),string));
		addAssignStmt(field2,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v("listenerLinkedList")));
		addAssignStmt(field4,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(VIEWREGISTARLINKEDLIST)));
//		addAssignStmt(classLocal,Jimple.v().newStaticInvokeExpr(forName_method.makeRef(),string));
		addAssignStmt(field3,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v("isVisited")));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field1,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList1,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field2,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList2,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field3,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(booleanLocal,Jimple.v().newCastExpr(object2,Boolean_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field4,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList3,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(flag,Jimple.v().newVirtualInvokeExpr(booleanLocal,booleanValue_method.makeRef()));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label1);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList1,offer_method.makeRef(),view));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList2,offer_method.makeRef(),object1));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList3,offer_method.makeRef(),thirdString));
		
		body.getUnits().add(label1);
		
		body.getUnits().add(label2);
		
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogException_method.makeRef(),exception));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(exception,printStackTrace_method.makeRef()));
		addGotoStmt(label1);
		
		
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), label0, label1, label2);
		body.getTraps().add(trap);
	}

	@Override
	protected void newMethodName() {
		List<Type> emptyTypes = paramTypes();
		emptyTypes.add(view_Type);
		emptyTypes.add(object_Type);
		emptyTypes.add(string_Type);
		currentMethod = new SootMethod(CLASSNAME, emptyTypes, VoidType.v(),Modifier.PUBLIC|Modifier.STATIC);
	}
}
