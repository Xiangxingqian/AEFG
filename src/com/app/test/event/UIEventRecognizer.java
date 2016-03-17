package com.app.test.event;

import java.util.List;
import java.util.Set;

import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NullConstant;

import com.app.test.CallBack;
import com.app.test.Constants;


/**
 * Android 事件有四： 1. Dialog 2. View 3. Menu 4. ListActivity的list
* 1. Dialog
* 	setOn[A-Za-z]+Listener[]
* 	分两类：setOn[A-Za-z]+Listener and dialog.setOn[A-Za-z]+Listener 
* 	追：两者实质相同，都有调用者
* 2. View 
*   setOn[A-Za-z]+Listener[]
*   分两类：setOn[A-Za-z]+Listener and view.setOn[A-Za-z]+Listener
*   追：两者实质相同，都有调用者
* 3. Menu
* 	onOptionItemSelected
* 	分两类：Fragment and Activity
* 4. ListActivity的list
*   onListItemClick_method
*/
public class UIEventRecognizer{
	
//	public static String[] viewCallback = {"onPageSelected","onPageScrollStateChanged",
//		"onLongClick","onFocusChange","onClick","onSystemUiVisibilityChange","onItemClick","onItemLongClick","onItemSelected",
//		"onNothingSelected","onStopTrackingTouch","onCheckedChanged","onStartTrackingTouch"};
//	public static String[]  dialogCallBack = {"onCancel","onDismiss","onShow"};
	
	
//	public static String[] viewRegistars = {"setOnLongClickListener","setOnFocusChangeListener","setOnClickListener",
//		"setOnSystemUiVisibilityChangeListener","setOnItemClickListener","setOnItemLongClickListener","setOnItemSelectedListener",
//		"setOnSeekBarChangeListener","setOnCheckedChangeListener","setOnPageChangeListener"};
//	public static String[] dialogRegistars = {"setOnCancelListener","setOnDismissListener","setOnShowListener"};
	
//	public static List<String> viewRegistarsList;
//	public static List<String> dialogRegistarsList;
//	static{
//		viewRegistarsList = Arrays.asList(viewRegistars);
//		dialogRegistarsList = Arrays.asList(dialogRegistars);
//	}
	
//	public static String match = "setOn[A-Za-z]+Listener";
	public static String setOnMenuItemClickListener = "setOnMenuItemClickListener";
	
	public static boolean isUIEvent(InvokeStmt is){
		return isDialogEvent(is)||isViewEvent(is);
	}
	
	/**
	* @param is Input InvokeStmt
	* @return true if is is a view event stmt.
	*/
	public static boolean isViewEvent(InvokeStmt is){
		return handleEvent(is,"android.view.View",CallBack.getViewRegistars());
	}

	/**
	* @param is Input InvokeStmt
	* @return true if is is a dialog event stmt.
	*/
	public static boolean isDialogEvent(InvokeStmt is){
		return handleEvent(is,"android.app.Dialog",CallBack.getDialogRegistars());
	}
	
	/**
	 * is is an Event iff invoker is Dialog/view's subClass && CallBack's registar contains register && parameter is not null 
	* @param is Input InvokeStmt
	* @param dialogOrView whether the input event is dialog or view event.
	* @param registars If is's method name is in registars, return true, or false.
	* @return true if is is a dialog or view event stmt.
	*/
	private static boolean handleEvent(InvokeStmt is, String dialogOrView, Set<String> registars) {
		// is setOn[A-Za-z]+Listener
		InvokeExpr invokeExpr = is.getInvokeExpr();
		
		SootMethod callMethod = invokeExpr.getMethod();
		String subSig = callMethod.getSubSignature();
		
		//替换
		//if(callMethodName.matches(match)&&!callMethodName.equals(setOnMenuItemClickListener)) {
		if(registars.contains(subSig)){
			// is View's subClass
			Value invoker = invokeExpr.getUseBoxes().get(invokeExpr.getUseBoxes().size()-1).getValue();
			try {
				//为何invoker会是null? @exception ClassCastException
				RefType invokerType =(RefType) invoker.getType();
				SootClass invokerClass = invokerType.getSootClass();
				SootClass uiClass = Scene.v().getSootClass(dialogOrView);
				boolean isUI = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(invokerClass).contains(uiClass);
				if(isUI){
					//parameter is not null 
					Value arg = invokeExpr.getArg(0);
					if(arg!=NullConstant.v()){
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isMenuEvent(SootMethod sm){
		return isActivityMenuEvent(sm);
//				||isFragmentMenuEvent(sm);
	}
	
	public static boolean isActivityMenuEvent(SootMethod sm){
		return handleMenuEvent(sm, Constants.activity,Constants.onCreateOptionsMenu_activity);
	}
	
//	public static boolean isFragmentMenuEvent(SootMethod sm){
//		return handleMenuEvent(sm, Constants.fragment,Constants.onCreateOptionsMenu_fragment);
//	}
//	
	/**
	 * sm is a menu event
	 * @param sm An input SootMethod
	 * @return true if sm is a menu's method.
	 * 
	 * menu's methods: onOptionsItemSelected,onCreateOptionsMenu
	 * err: if sc contains onOptionsItemSelected_Name but not contains onCreateOptionsMenu_Name
	 * */
	private static boolean handleMenuEvent(SootMethod sm, String fragmentOrActivity, String onCreateOptionsMenu){
		String methodSig = sm.getSubSignature();
		if(Constants.onOptionsItemSelected_Name.equals(methodSig)){
			SootClass sc = sm.getDeclaringClass();
			boolean isDeclared = sc.declaresMethod(onCreateOptionsMenu);
			if(isDeclared){
				List<SootClass> superclasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sc);
				SootClass fragmentOrActivityClass = Scene.v().getSootClass(fragmentOrActivity);
				if(superclasses.contains(fragmentOrActivityClass)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * if sm is OnListItemClick method.
	 * @param sm An input SootMethod.
	 * @return true if sm equals to OnListItemClick, or false.
	 * */
	public static boolean isOnListItemClick(SootMethod sm) {
		String subSignature = sm.getSubSignature();
		SootClass declaringClass = sm.getDeclaringClass();
		if(declaringClass.isInterface()){
			return false;
		}
		SootClass listActivity = Scene.v().getSootClass("android.app.ListActivity");
		List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(declaringClass);
		
		if(Constants.onListItemClick_Name.equals(subSignature)&&superClasses.contains(listActivity)){
			return true;
		}
		return false;
	}

}
