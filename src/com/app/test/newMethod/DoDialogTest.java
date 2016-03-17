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
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;


/**
 *  add doDialogTest() method;
 *  <code>
 *  doTest(){<br>
 *  while(!DialogLinkedlist.isEmpty()){
 *  	Dialog v = DialogLinkedList.poll();
 *  	Object o = DialoglistenerLinkedList.poll();
 *  	String registar = DialogregistarlinkedList.poll();
 *      Class class = o.getClass();
 *      Method[] methods = class.getMethods();
 *      List callbacks = map.get(registar);
 *      if(callbacks!=null){
 *      	Iterator it = callbacks.iterator();
 *      	while(it.hasNext()){
 *      		String callback = it.next();
 *      		for(int i=0;i<methods.length;i++){
 *      			if(callback.equals(method[i].getName)){
 *      				doDialogAnalysis(method,v,o);
 *      			}
 *      		}
 *      	}
 *      }
 *  }
 *  }
 *  </code>
 * */
public class DoDialogTest extends MethodBuilder{

	public static final String CLASSNAME = "doDialogTest";
	public static final String SUBSIGNATURE = "void doDialogTest()";
	
	public DoDialogTest(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local currentActivity,classLocal1,object,object1,mapObject,string,callBackName,
		dialog,linkedList,flag,int0,int1,reflectMethod,arrayReflectMethod,toast, map,list,
		iterator,iteratorViewLinkedList;
		{
			currentActivity = addLocal("activity", sc_Type);
			object = addLocal("object", object_Type);
			object1 = addLocal("object1", object_Type);
			mapObject = addLocal("mapObject", object_Type);
			dialog = addLocal("dialog", dialog_Type);
			toast = addLocal("toast", toast_Type);
			int0 = addLocal("int0", IntType.v());
			int1 = addLocal("int1", IntType.v());
			flag = addLocal("flag", BooleanType.v());
			classLocal1 = addLocal("classLocal1", class_Type);
			reflectMethod = addLocal("reflectMethod", reflectMethod_Type);
			linkedList = addLocal("linkedList", linkedList_Type);
			string = addLocal("string", string_Type);
			callBackName = addLocal("callBackString", string_Type);
			arrayReflectMethod = addLocalArray("arrayReflectMethod", reflectMethod_Type);
			map = addLocal("map", map_Type);
			list = addLocal("list", list_Type);
			iterator = addLocal("iterator", iterator_Type);
			iteratorViewLinkedList = addLocal("iteratorViewLinkedList", iterator_Type); 
		}
		
		SootField dialogListenerLinkedList_field = sc.getFieldByName(DIALOGLISTENERLINKEDLIST);
		SootField dialogLinkedList_field = sc.getFieldByName(DIALOGLINKEDLIST);
		SootField dialogRegistarLinkedList_field = sc.getFieldByName(DIALOGREGISTARLINKEDLIST);
		SootField dialogToCallBack_field = Scene.v().getSootClass(CallBack).getFieldByName(DIALOGTOCALLBACKS);
		SootMethod doDialogAnalysis_method = sc.getMethod(DoDialogAnalysis.SUBSIGNATURE);
		
		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
		
		//label0, linkedList = viewLinkedList;
		AssignStmt isEmpty = Jimple.v().newAssignStmt(flag, Jimple.v().newInterfaceInvokeExpr(iteratorViewLinkedList, hasNext_method.makeRef()));
		
		//label1, linkedList = viewLinkedList;
		AssignStmt label1 = Jimple.v().newAssignStmt(linkedList, Jimple.v().newStaticFieldRef(dialogLinkedList_field.makeRef()));
		
		//label2, int1 = arrayReflectMethod.length
		AssignStmt label2 = Jimple.v().newAssignStmt(int1,Jimple.v().newLengthExpr(arrayReflectMethod));

		//label6 int0 = int0+1
		AssignStmt iplusplus = Jimple.v().newAssignStmt(int0,Jimple.v().newAddExpr(int0, IntConstant.v(1)));
		//label7, return
		ReturnVoidStmt returnVoidStmt  = Jimple.v().newReturnVoidStmt();
		
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(dialogLinkedList_field.makeRef()));
		
		addAssignStmt(iteratorViewLinkedList,Jimple.v().newInterfaceInvokeExpr(linkedList, iterator_method.makeRef()));
		body.getUnits().add(isEmpty);
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label1);
		
		List<Value> makeText_params = paramValues();
		makeText_params.add(currentActivity);
		makeText_params.add(StringConstant.v("Test finishes"));
		makeText_params.add(IntConstant.v(0));
		addAssignStmt(toast,Jimple.v().newStaticInvokeExpr(makeText_method.makeRef(),makeText_params));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(toast, show_method.makeRef()));
		//add ReturnVoidStmt
		body.getUnits().add(returnVoidStmt);
		
		// view = viewLinkedList.poll();
		body.getUnits().add(label1);
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		addAssignStmt(dialog, Jimple.v().newCastExpr(object, dialog_Type));
		
		// string2 = viewRegistarLinkedList.poll();
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(dialogRegistarLinkedList_field.makeRef()));
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		addAssignStmt(callBackName, Jimple.v().newCastExpr(object, string_Type));
		
		// object = listenerLinkedList.poll();
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(dialogListenerLinkedList_field.makeRef()));
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		
		// methods = object.getClass().getMethods();
		addAssignStmt(classLocal1, Jimple.v().newVirtualInvokeExpr(object,getClass_method.makeRef()));
		addAssignStmt(arrayReflectMethod, Jimple.v().newVirtualInvokeExpr(classLocal1,getMethods_method.makeRef()));
		addAssignStmt(map, Jimple.v().newStaticFieldRef(dialogToCallBack_field.makeRef()));

		// if(map.get(string2)==null) goto while (!viewLinkedList.isEmpty()) 
		addAssignStmt(mapObject, Jimple.v().newInterfaceInvokeExpr(map, mapGet_method.makeRef(),callBackName));
		addAssignStmt(list, Jimple.v().newCastExpr(mapObject, list_Type));
		addIfStmt(Jimple.v().newEqExpr(list, NullConstant.v()), isEmpty);
		addAssignStmt(iterator, Jimple.v().newInterfaceInvokeExpr(list, iterator_method.makeRef()));
		
		//循环开始 iterator.hasNext()
		AssignStmt hasNext = Jimple.v().newAssignStmt(flag, Jimple.v().newInterfaceInvokeExpr(iterator, hasNext_method.makeRef()));
		body.getUnits().add(hasNext);
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), isEmpty);//跳出循环,viewLinkedList.isEmpty()
		addAssignStmt(object1, Jimple.v().newInterfaceInvokeExpr(iterator, next_method.makeRef()));
		addAssignStmt(callBackName, Jimple.v().newCastExpr(object1, string_Type));
		
		addAssignStmt(int0, IntConstant.v(0));
		body.getUnits().add(label2);//int1 = arrayReflectMethod.length
		addIfStmt(Jimple.v().newGeExpr(int0, int1), hasNext);//跳出循环, iterator.hasNext()
		
		addAssignStmt(reflectMethod, Jimple.v().newArrayRef(arrayReflectMethod, int0));
		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(reflectMethod, reflectMethodGetName_method.makeRef()));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string,stringEquals_method.makeRef(),callBackName));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), iplusplus);//调出循环, i++
		List<Value> paramValues = paramValues();
		paramValues.add(reflectMethod);
		paramValues.add(dialog);
		paramValues.add(object);
		
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, doDialogAnalysis_method.makeRef(),paramValues));
		List<Value> logParams = paramValues();
		logParams.add(dialog);
		logParams.add(object);
		logParams.add(string);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLog_method.makeRef(),logParams));
		addGotoStmt(hasNext);//break;
		
		body.getUnits().add(iplusplus);
		addGotoStmt(label2);
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(), Modifier.PUBLIC);
	}


}
