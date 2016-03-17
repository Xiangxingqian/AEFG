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
import soot.jimple.ThisRef;


/**
 * add doReflect(Dialog, Object, String) method.
 *  
 *  <code>
 *  	Context con = Dialog.getContext();
 *  	String name = con.getName();
 *  	Class class = Class.forname(name);
 *      Field DialogLinkedList = class.getField("DialogLinkedList");
 *      Field DialoglistenerLinkedList = class.getField("DialoglistenerLinkedList");
 *      Field DialogREGISTARLINKEDLIST = class.getField("DialogREGISTARLINKEDLIST");
 *      LinkedList o1 = DialogLinkedList.get(null);
 *      o1.add(dialog);
 *      LinkedList o2 = DialoglistenerLinkedList.get(null);
 *      o2.add(object);
 *      LinkedList o3 = DialogREGISTARLINKEDLIST.get(null);
 *      o3.add(string);
 *  </code>
 * */
public class DoDialogReflect extends MethodBuilder {
	
	public static final String CLASSNAME = "doDialogReflect";
	public static final String SUBSIGNATURE = "void doDialogReflect(android.app.Dialog,java.lang.Object,java.lang.String)";	
	
	public DoDialogReflect(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local dialog,context,listener,registar,object2,exception,field1,field2,field3,field4,
		string,classLocal,linkedList1,linkedList2,linkedList3,flag,booleanLocal;
		
		{
//			currentActivity = addLocal("activity", sc_Type);
			listener = addLocal("listener", object_Type);
			object2 = addLocal("object2", object_Type);
			dialog = addLocal("dialog", dialog_Type);
			flag = addLocal("flag", BooleanType.v());
			classLocal = addLocal("classLocal", class_Type);
			linkedList1 = addLocal("linkedList1", linkedList_Type);
			linkedList2 = addLocal("linkedList2", linkedList_Type);
			linkedList3 = addLocal("linkedList3", linkedList_Type);
			string = addLocal("string", string_Type);
			registar = addLocal("registar", string_Type);
			exception = addLocal("exception", exception_Type);
			field1 =  addLocal("field1", field_Type);
			field2 =  addLocal("field2", field_Type);
			field3 =  addLocal("field3", field_Type);
			field4 =  addLocal("field4", field_Type);
			booleanLocal =  addLocal("booleanLocal", Boolean_Type);
			context =  addLocal("context", context_Type);
		}
//		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
		addIdentityStmt(dialog, new ParameterRef(dialog_Type,0));
		addIdentityStmt(listener, new ParameterRef(object_Type,1));
		addIdentityStmt(registar, new ParameterRef(string_Type,2));
		addAssignStmt(context,Jimple.v().newVirtualInvokeExpr(dialog,dialog_getContext_method.makeRef()));
		addAssignStmt(classLocal,Jimple.v().newVirtualInvokeExpr(context,getClass_method.makeRef()));
		addAssignStmt(string,Jimple.v().newVirtualInvokeExpr(classLocal,classGetName_method.makeRef()));

		//label0,
		AssignStmt label0 = Jimple.v().newAssignStmt(classLocal, Jimple.v().newStaticInvokeExpr(forName_method.makeRef(),string));
		//label1, 
		ReturnVoidStmt label1 = Jimple.v().newReturnVoidStmt();
		//label2, 
		IdentityStmt label2 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
	
		body.getUnits().add(label0);
		addAssignStmt(field1,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(DIALOGLINKEDLIST)));
		addAssignStmt(field2,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(DIALOGLISTENERLINKEDLIST)));
		addAssignStmt(field3,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(ISVISITED)));
		addAssignStmt(field4,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(DIALOGREGISTARLINKEDLIST)));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field1,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList1,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field2,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList2,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field4,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(linkedList3,Jimple.v().newCastExpr(object2, linkedList_Type));
		addAssignStmt(object2,Jimple.v().newVirtualInvokeExpr(field3,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(booleanLocal,Jimple.v().newCastExpr(object2,Boolean_Type));
		addAssignStmt(flag,Jimple.v().newVirtualInvokeExpr(booleanLocal,booleanValue_method.makeRef()));
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label1);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList1,offer_method.makeRef(),dialog));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList2,offer_method.makeRef(),listener));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList3,offer_method.makeRef(),registar));
		
		body.getUnits().add(label1);
		
		body.getUnits().add(label2);
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(exception,printStackTrace_method.makeRef()));
		addGotoStmt(label1);
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), label0, label1, label2);
		body.getTraps().add(trap);
		
	}

	@Override
	protected void newMethodName() {
		List<Type> paramTypes = paramTypes();
		paramTypes.add(dialog_Type);
		paramTypes.add(object_Type);
		paramTypes.add(string_Type);
		currentMethod = new SootMethod(CLASSNAME, paramTypes, VoidType.v(),Modifier.PUBLIC|Modifier.STATIC);
	}

}
