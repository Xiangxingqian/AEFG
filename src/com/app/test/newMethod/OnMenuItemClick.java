package com.app.test.newMethod;

import java.util.List;

import android.widget.ListAdapter;

import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.JastAddJ.LongType;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;


/**
 * add onMenuItemClick() method.
 * <pre>
 * onMenuItemClick(){
 *   doTest(); 
 *   doDialogTest();
 *   doMenuTest();
 *   doListItemClicked();
 * }
 * </pre>
 * 
 * */
public class OnMenuItemClick extends MethodBuilder{
	public static final String CLASSNAME = "onMenuItemClick";
	public static final String SUBSIGNATURE = "boolean onMenuItemClick(android.view.MenuItem)";	
	public OnMenuItemClick(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}
	@Override
	protected void addUnits() {
		Local currentActivity = addLocal("activity", sc_Type);
		Local menuItem = addLocal("menuItem", menuItem_Type);
		
		Local listAdapter = addLocal("listAdapter", listAdapter_Type);
		Local l = addLocal("l", soot.LongType.v());
		Local intString = addLocal("intString", string_Type);
		Local longLocal = addLocal("longLocal", Long_Type);
		Local listView = addLocal("listView", listView_Type);
		Local index = addLocal("index", IntType.v());
		Local childCount = addLocal("childCount", IntType.v());
		Local view = addLocal("view", view_Type);
//		Local intentFilter = addLocal("intentFilter", refType);
		
//		SootMethod doTest_method = sc.getMethod(DoTest.SUBSIGNATURE);
//		SootMethod doDialogTest_method = sc.getMethod(DoDialogTest.SUBSIGNATURE);
//				
		addIdentityStmt(currentActivity, Jimple.v().newThisRef(sc_Type));
		addIdentityStmt(menuItem, Jimple.v().newParameterRef(menuItem_Type, 0));
		
		SootField isVisited_field = sc.getFieldByName(ISVISITED);
		
		// isVisited=true;
		addAssignStmt(Jimple.v().newStaticFieldRef(isVisited_field.makeRef()),IntConstant.v(1));
		
		if(sc.declaresFieldByName(ACTIVITYMENU)){
			SootMethod doMenuTest_method = sc.getMethod(DoMenuTest.SUBSIGNATURE);
			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, doMenuTest_method.makeRef()));
		}
		
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, doDialogTest_method.makeRef()));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, doTest_method.makeRef()));
		//Ìæ»»³É
		Local list1,list2,list3;
		{
			list1 = addLocal("list1", linkedList_Type);
			list2 = addLocal("list2", linkedList_Type);
			list3 = addLocal("list3", linkedList_Type);
		}
		
		ReturnStmt newReturnStmt = Jimple.v().newReturnStmt(IntConstant.v(1));
		//invoke doViewTest(views,listeners,registars)
		{
			SootField viewLinkedList_field = sc.getFieldByName(VIEWLINKEDLIST);
			SootField listenerLinkedList_field = sc.getFieldByName(LISTENERLINKEDLIST);
			SootField viewRegistarLinkedList_field = sc.getFieldByName(VIEWREGISTARLINKEDLIST);
			addAssignStmt(list1, Jimple.v().newStaticFieldRef(viewLinkedList_field.makeRef()));
			addAssignStmt(list2, Jimple.v().newStaticFieldRef(listenerLinkedList_field.makeRef()));
			addAssignStmt(list3, Jimple.v().newStaticFieldRef(viewRegistarLinkedList_field.makeRef()));
			List<Value> paramValues2 = paramValues();
			paramValues2.add(list1);
			paramValues2.add(list2);
			paramValues2.add(list3);
			addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilDoViewTest_method.makeRef(),paramValues2));
		}
		
		//invoke doDialogTest(dialogs,listeners,registars)
		{
			SootField dialogLinkedList_field = sc.getFieldByName(DIALOGLINKEDLIST);
			SootField dialoglistenerLinkedList_field = sc.getFieldByName(DIALOGLISTENERLINKEDLIST);
			SootField dialogRegistarLinkedList_field = sc.getFieldByName(DIALOGREGISTARLINKEDLIST);
			addAssignStmt(list1, Jimple.v().newStaticFieldRef(dialogLinkedList_field.makeRef()));
			addAssignStmt(list2, Jimple.v().newStaticFieldRef(dialoglistenerLinkedList_field.makeRef()));
			addAssignStmt(list3, Jimple.v().newStaticFieldRef(dialogRegistarLinkedList_field.makeRef()));
			List<Value> paramValues3 = paramValues();
			paramValues3.add(list1);
			paramValues3.add(list2);
			paramValues3.add(list3);
			addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilDoDialogTest_method.makeRef(),paramValues3));
		}
		
		if(isListActivty(sc)){
			//"void onListItemClick(android.widget.ListView,android.view.View,int,long)"
			if(sc.declaresMethod(onListItemClick_Name)){
//				addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogListItemTest_method.makeRef(),currentActivity));
				List<Value> logParams = paramValues();
				logParams.add(currentActivity);
				addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogList_method.makeRef(),logParams));
				SootMethod onListItemClick_method = sc.getMethod(onListItemClick_Name);
				addAssignStmt(listView, Jimple.v().newVirtualInvokeExpr(currentActivity, getListView_method.makeRef()));
				addAssignStmt(childCount, Jimple.v().newVirtualInvokeExpr(listView, getChildCount_method.makeRef()));
				addAssignStmt(index, IntConstant.v(0));
				IfStmt ifStmt = addIfStmt(Jimple.v().newGeExpr(index, childCount), newReturnStmt);
				addAssignStmt(view, Jimple.v().newVirtualInvokeExpr(listView, getChildAt_method.makeRef(),index));
				addAssignStmt(listAdapter, Jimple.v().newVirtualInvokeExpr(currentActivity, getListAdapter_method.makeRef()));
				addAssignStmt(l, Jimple.v().newInterfaceInvokeExpr(listAdapter, adapterGetItemId_method.makeRef(),index));
				List<Value> paramValues = paramValues();
				paramValues.add(listView);
				paramValues.add(view);
				paramValues.add(index);
				paramValues.add(l);
				addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, onListItemClick_method.makeRef(),paramValues));
				addAssignStmt(index, Jimple.v().newAddExpr(index, IntConstant.v(1)));
				addGotoStmt(ifStmt);
			}
		}
		
		if(isPrefernceActivity(sc)){
			List<Value> logParams = paramValues();
			logParams.add(currentActivity);
			addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogPreference_method.makeRef(),logParams));
			addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilDoPreferenceTest_method.makeRef(), logParams));
		}
		body.getUnits().add(newReturnStmt);
		/**
		 * transfer int to long
		 * int index;
		 * String s = String.valueOf(index);
		 * Long lon = Long.valueOf(s);
		 * long l = getListAda
		*/
//		addAssignStmt(intString, Jimple.v().newStaticInvokeExpr(stringValueOf_method.makeRef(),index));
//		addAssignStmt(longLocal, Jimple.v().newStaticInvokeExpr(longValueOf_method.makeRef(),intString));
//		addAssignStmt(l, Jimple.v().newVirtualInvokeExpr(longLocal,longValue_method.makeRef()));
//		
//		List<Value> paramValues = paramValues();
//		paramValues.add(listView);
//		paramValues.add(view);
//		paramValues.add(index);
//		paramValues.add(l);
		
		//parse long
		//ListAdapter listAdapter2 = getListAdapter();
		//long l = listAdapter2.getItemId(position);
//		for(String s:ManifestData.activityToFilters.keySet()){
//			List<Value> paramValues = paramValues();
//			addAssignStmt(intentFilter, rightOp)
//			paramValues.add(currentActivity);
//			paramValues.add(ManifestData.activityToFilters.get(s));
//			addInvokeStmt(Jimple.v().newStaticInvokeExpr(method));
//		}
	}

	@Override
	protected void newMethodName() {
		List<Type> emptyTypes = paramTypes();
		emptyTypes.add(menuItem_Type);
		currentMethod = new SootMethod(CLASSNAME, emptyTypes, BooleanType.v(),Modifier.PUBLIC);
		
	}
	
	private boolean isListActivty(SootClass sc) {
		SootClass listActivity = Scene.v().getSootClass("android.app.ListActivity");
		List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sc);
		return superClasses.contains(listActivity);
	}
	
	private boolean isPrefernceActivity(SootClass sc){
		SootClass listActivity = Scene.v().getSootClass("android.preference.PreferenceActivity");
		List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sc);
		return superClasses.contains(listActivity);
	}
}
