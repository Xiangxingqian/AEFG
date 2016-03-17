package com.app.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.util.Chain;

import android.hardware.Camera.Size;

import com.app.test.event.SystemEventCollector;
import com.app.test.event.UIEventRecognizer;
import com.app.test.exception.ClassIsNotActivityException;
import com.app.test.newMethod.CheckEvent;
import com.app.test.newMethod.CheckEventForMainActivity;
import com.app.test.newMethod.Clinit;
import com.app.test.newMethod.ClinitForAll;
import com.app.test.newMethod.DoDialogAnalysis;
import com.app.test.newMethod.DoDialogReflect;
import com.app.test.newMethod.DoDialogTest;
import com.app.test.newMethod.DoMenuTest;
import com.app.test.newMethod.DoReflect;
import com.app.test.newMethod.DoTest;
import com.app.test.newMethod.DoViewAnalysis;
import com.app.test.newMethod.OnCreateOptionsMenu;
import com.app.test.newMethod.OnMenuItemClick;
import com.app.test.newMethod.OnRestart;
import com.app.test.newMethod.RunMyThread;
import com.test.xmldata.LayoutData;
import com.test.xmldata.ManifestData;

/**
 * three phases:
 * 1. collect events
 *     1.1 menu events, add {openOptionsMenu, onOptionsItemSelected} into doMenuTest.
 *     1.2 list events, add {onListItemClicked} into onMenuItemClicked.
 *     1.3 view events, add doReflect method //search view's belonging activity, activity.viewLinkedList.add(view). 
 *     1.4 dialog events. add doDialogReflect method
 * 2. transform activity
 * 	   add methods={doTest, doDialogTest, doMenuTest, onListItemClicked}
 * 3. calling event
 *     OnMenuItemList()
 *     in OnMenuItemList, invoking four defined methods: doTest, doDialogTest,doMenuTest,onListItemClicked
 *     
 * modified classes:
 * 1. activities class declaring in AndroidManifest.xml(do not including their super classes)
 * 		Add Fields: viewLinkedList, listenerLinkerList, isMyEvent, isVisited, dialogLinkerList, dialogListenerLinkedList, menu.
 *      Add Methods: doTest,doDialog,TestdoMenuTest,onListItemClicked, OutPrint.
 *      if sc does not declare onCreateOptionsMenu, add it, or modify it. 
 *      if sc does not declare <clinit>, add it, or modify it. 
 * 2. class containing setOn[A-Za-z]+Listener, and invoker is view. 
 * 		Add doReflect(); 
 * 3. class containing setOn[A-Za-z]+Listener, and invoker is dialog. 
 * 		Add doDialogReflect();
 * 
 *继续：
 *给Activity添加doViewAnalysis(),doDialogAnalysis().
 * */
public class EventBodyTransformer extends BodyTransformer {

	/**
	 * 5 successor methods
	 * */
	List<SootClass> sClasses = new ArrayList<SootClass>();
	String lastClass = "";
	@Override
	protected void internalTransform(Body b, String phaseName,
			Map<String, String> options) {
		//before instrumenting App, add new classes and mainActivity fields first.
		
		addNewClasses();
		addMainActivityFields();
		
		final SootClass sc = b.getMethod().getDeclaringClass();
		String name = sc.getName();
		
		//if b is method of Java, Android, Org, return
//		if(name.startsWith("android")||name.startsWith("java")||name.startsWith("org"))
//			return;
		if(name.startsWith("android")||name.startsWith("java"))
			return;
		
		
		
		//if sc is activity, transform it.
		if(ManifestData.activities.contains(name)){
			transformActivity(b, sc);
			ManifestData.activities.remove(name);
		}
//		fixAdobeReaderBug(b);
		//start to collect event
		collectUIEvents(b);
		collectUIEventsFromLayout(b);
		
		SystemEventCollector systemEventCollector = new SystemEventCollector(b);
		systemEventCollector.collectEvents();
		
		if(b.getMethod().isConcrete()){
			PatchingChain<Unit> units = b.getUnits();
			Main.linesCount = Main.linesCount+units.size();;
			if(sc.getName()!=lastClass){
				lastClass = sc.getName();
				Main.classesCount++;
			}
			Main.methodsCount++;
			addMethodList(sc);
			new MethodCollector(b).parseBody();
			
		}
	}
	
	private void addMethodList(SootClass sc) {
		SootField methodCountList = new SootField(Constants.METHODCOUNTLIST, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		if(!sc.declaresFieldByName(Constants.METHODCOUNTLIST)){
			sc.addField(methodCountList);
		}
		// if sc contains <clinit>, initialize methodcountList
		SootField sootField = sc.getFieldByName(Constants.METHODCOUNTLIST);
		if(sc.declaresMethodByName("<clinit>")){
			SootMethod clinitMethod = sc.getMethodByName("<clinit>");
			Body b = clinitMethod.retrieveActiveBody();
			PatchingChain<Unit> units = clinitMethod.getActiveBody().getUnits();
			if(!sClasses.contains(sc)){
				sClasses.add(sc);
				Unit last = units.getFirst();
				Local addLocal = addLocal(b, "methodCountListLocal", Constants.linkedList_Type);
				Local objectLocal = addLocal(b,"linkedListobject",Constants.object_Type);
				SootField sField = Scene.v().getSootClass("com.app.test.AppDir").getFieldByName("linkedList");
				units.insertBefore(Jimple.v().newAssignStmt(addLocal, Jimple.v().newStaticFieldRef(sField.makeRef())),last);
				units.insertBefore(Jimple.v().newAssignStmt(objectLocal,Jimple.v().newVirtualInvokeExpr(addLocal, Constants.linkedListClone_method.makeRef())),last);
				units.insertBefore(Jimple.v().newAssignStmt(addLocal, Jimple.v().newCastExpr(objectLocal, Constants.linkedList_Type)),last);
				units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sootField.makeRef()),addLocal),last);
				b.validate();
			}
		}
		else{
			new ClinitForAll(sc, ClinitForAll.SUBSIGNATURE).newMethod();
			sClasses.add(sc);
		}
	}

	/**
	 * insert new classes into App.
	 * */
	private void addNewClasses() {
		SootClass callback = Scene.v().getSootClass("com.app.test.CallBack");
		SootClass util = Scene.v().getSootClass("com.app.test.Util");
		SootClass systemEventHandler = Scene.v().getSootClass("com.app.test.event.SystemEventHandler");
		SootClass systemEventConstants = Scene.v().getSootClass("com.app.test.event.SystemEventConstants");
		SootClass systemEvent = Scene.v().getSootClass("com.app.test.event.SystemEvent");
		SootClass appDir = Scene.v().getSootClass("com.app.test.AppDir");
		SootClass androidIntentFilter = Scene.v().getSootClass("com.app.test.data.AndroidIntentFilter");
		SootClass appMenuItemClickListener = Scene.v().getSootClass("com.app.test.event.SystemEventHandler$AppMenuItemClickListener");
		SootClass receiverEvent = Scene.v().getSootClass("com.app.test.event.ReceiverEvent");
		if(!Scene.v().getApplicationClasses().contains(callback)){
			callback.setApplicationClass();
		}
		if (!Scene.v().getApplicationClasses().contains(util)) {
			util.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(systemEventHandler)){
			systemEventHandler.setApplicationClass();
		}
		if (!Scene.v().getApplicationClasses().contains(systemEventConstants)) {
			systemEventConstants.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(systemEvent)){
			systemEvent.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(appDir)){
			appDir.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(androidIntentFilter)){
			androidIntentFilter.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(appMenuItemClickListener)){
			appMenuItemClickListener.setApplicationClass();
		}
		if(!Scene.v().getApplicationClasses().contains(receiverEvent)){
			receiverEvent.setApplicationClass();
		}
	}

	/**
	 * checking whether b contains findViewById(id)，and b.getDeclaringClass contains callback.
	 * if yes, store them into a triples: view, listener, callback
	 * when used, calling listener.callback();
	 * 
	 * Caution: callback has multiple parameters and its parameters are not limited with view. Any type can be its parameter. Thus, that's difficulty.
	 * @param b Method body, if b contains findViewById(id)，and b.getDeclaringClass contains callback. 
	 * @return add into doReflect() method;
	 * */
	private void collectUIEventsFromLayout(final Body b) {
		final Map<Integer, String> idToCallBack = LayoutData.idToCallBack;
		final SootClass sc = b.getMethod().getDeclaringClass();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				public void caseAssignStmt(AssignStmt stmt) {
					Value rightValue = stmt.getRightOp();
					if(rightValue instanceof JVirtualInvokeExpr){
						InvokeExpr invokeExpr = (InvokeExpr)rightValue;
						String subSignature = invokeExpr.getMethod().getSubSignature();
						
						//if subSig == android.view.View findViewById(int)
						if("android.view.View findViewById(int)".equals(subSignature)){
							Value v = invokeExpr.getArg(0);
							// if v is local not IntConstant, return 
							if(v instanceof Local){
								return ;
							}
							IntConstant id = (IntConstant)v;
							if(idToCallBack.containsKey(id.value)){
								searchCallBack(b, sc, units, stmt, id);
							}
						}
					}
				}
				
				/**
				 * search CallBack, add DoReflect() and call doReflect(view, this, key);
				 * */
				private void searchCallBack(final Body b,
						final SootClass sc, final PatchingChain<Unit> units,
						 AssignStmt stmt, IntConstant id) {
					String callback = idToCallBack.get(id.value);
					String subSig = "void "+callback+"(android.view.View)";
					if(sc.declaresMethod(subSig)){
						//add doReflect
						if(!sc.declaresMethod(DoReflect.SUBSIGNATURE)){
							addDoReflectMethod(sc);
						}
						
						SootMethod doReflect = sc.getMethod(DoReflect.SUBSIGNATURE);
						Map<String,List<String>> viewToCallBacks = CallBack.viewToCallBacks;
						String viewNameAndId = sc.getName()+id;
						viewToCallBacks.put(viewNameAndId, Arrays.asList(subSig));
						
						//insert unit.
						{
							Local base = b.getThisLocal();
							Local view = (Local) stmt.getLeftOp();
							List<Value> params = new ArrayList<Value>();
							params.add(view);
							params.add(base);
							params.add(StringConstant.v(viewNameAndId));
							units.insertAfter(
									Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doReflect.makeRef(),params)), u);
						}
					}
				}
			});
		}
	}
	
	/**
	 * 待用
	 * */
//	private void transformFragment(final Body b,final SootClass sc){
//		List<SootClass> superclasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sc);
//		boolean isFragment = superclasses.contains(Scene.v().getSootClass(Constants.fragment));
//		if(isFragment){
//			String methodSig = b.getMethod().getSubSignature();
//			if(methodSig.equals(Constants.onCreateView_fragment))
//				modifyOnCreateView(b);
//			if(methodSig.equals(Constants.onCreateOptionsMenu_fragment)){
//				modifyFragmentOnCreateOptionMenu(b);
//			}
//		}
//	}
	
	/**
	 * Fragment menu的步骤：
	 * 	FragmentActivity fa = getActivity();
	 *  fa.getField(fragment);
	 * 1. MainActivity.fragment = this;
	 * 2. Menu menu;
	 *    menu = menu;
	 * 
	 * 最终在MainActivity中，用
	 * fragment.setMenuVisibility(true);
	 * Menu fragmentMenu = fragment.menu;
	 * fragment.onOptionsItemSelected(fragmentMenu, getMenuInflater());
	 * */
//	private void modifyOnCreateView(Body b) {
//		//TODO
//	}
//
//	private void modifyFragmentOnCreateOptionMenu(Body b) {
//		//TODO
//	}

	/**
	 * sc is an Activity
	 * transform activity: add OutPrint, DoTest, OnMenuItemSelected method.
	 * if activity does not contains OnCreateOptionsMenu, <clinit> method, add them, or modify them.  
	 * @param b input body.
	 * @param sc b's declaringclass
	 * */
//	private void transformActivity(final Body b, final SootClass sc) {
//		
//		//addInterface
//		if(!sc.implementsInterface("android.view.MenuItem$OnMenuItemClickListener"))
//			sc.addInterface(Scene.v().getSootClass("android.view.MenuItem$OnMenuItemClickListener"));
//		
//		addFields(sc);
//		
//		//add methods
//		if(!sc.declaresMethod(Clinit.SUBSIGNATURE))
//			addClinit(sc);
//		if(!sc.declaresMethod(OutPrint.SUBSIGNATURE))
//			addOutPrintMethod(sc);
//		if(!sc.declaresMethod(DoViewAnalysis.SUBSIGNATURE))
//			addDoViewAnalysisMethod(sc);
//		if(!sc.declaresMethod(DoDialogAnalysis.SUBSIGNATURE))
//			addDoDialogAnalysisMethod(sc);
//		if(!sc.declaresMethod(DoTest.SUBSIGNATURE))
//			addDoTestMethod(sc);
//		if(!sc.declaresMethod(OnCreateOptionsMenu.SUBSIGNATURE))
//			addOnCreateOptionsMenuMethod(sc);
//		if(!sc.declaresMethod(DoDialogTest.SUBSIGNATURE))
//			addDoDialogTestMethod(sc);
//		if(!sc.declaresMethod(OnMenuItemClick.SUBSIGNATURE))
//			addOnMenuItemClickMethod(sc);
//		
//		if(b.getMethod().getSubSignature().equals(Clinit.SUBSIGNATURE))
//			if(!isMyClinit(b))
//				modifyClinit(b, sc);
//		if(b.getMethod().getSubSignature().equals(Constants.onCreateOptionsMenu_activity)){
//			if(!"activity".equals(b.getThisLocal().getName()))
//				modifyOnCreateOptionMenu(b);
//		}
//
//	}
	
	/**
	 * Transform activity: add OutPrint, DoTest, OnMenuItemSelected method.<br>
	 * If activity does not contains OnCreateOptionsMenu, <clinit> method, add them. If contains, modify them.  
	 * @param b input body.
	 * @param sc b's declaringclass
	 * @return sc is inserted multiple methods.
	 * */
	private void transformActivity(final Body b, final SootClass sc) {
		
		//it exists: sc is declared as activity in androidManifest.xml, but it does not implement android.app.Activity.
//		if(!Scene.v().getActiveHierarchy().getSuperclassesOf(sc).contains(Scene.v().getSootClass("android.app.Activity"))){
//			System.err.println(sc+" is not an activity!!!");
//			return ;
//		}
		
		try {
			isActivity(sc);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
			
		//add OnMenuItemClickListener Interface
		if(!sc.implementsInterface("android.view.MenuItem$OnMenuItemClickListener"))
			sc.addInterface(Scene.v().getSootClass("android.view.MenuItem$OnMenuItemClickListener"));
		else {
			System.out.println(sc.getName()+" implements android.view.MenuItem$OnMenuItemClickListener, onMenuItemClick failed in adding.");
		}
		
		//add fields
		addFields(sc);
		
		//add methods
		if(!sc.declaresMethod(Clinit.SUBSIGNATURE))
			addClinit(sc);
		else {
			Body body = sc.getMethod(Clinit.SUBSIGNATURE).retrieveActiveBody();
			modifyClinit(body, sc);
		}
		if(!sc.declaresMethod(DoViewAnalysis.SUBSIGNATURE))
			addDoViewAnalysisMethod(sc);
		if(!sc.declaresMethod(DoDialogAnalysis.SUBSIGNATURE))
			addDoDialogAnalysisMethod(sc);
		if(!sc.declaresMethod(DoTest.SUBSIGNATURE))
			addDoTestMethod(sc);
		if(!sc.declaresMethod(DoDialogAnalysis.SUBSIGNATURE))
			addDoDialogTestMethod(sc);
		//if sc does not contain onCreateOptionMenu, add it; or modify it. Pay attention: add menu field. public Menu menu;
		if(!sc.declaresMethod(OnCreateOptionsMenu.SUBSIGNATURE))
			addOnCreateOptionsMenuMethod(sc);
		else{
			Body body = sc.getMethod(OnCreateOptionsMenu.SUBSIGNATURE).retrieveActiveBody();
			modifyOnCreateOptionMenu(body);
		}
		if(!sc.declaresMethod(OnMenuItemClick.SUBSIGNATURE))
			addOnMenuItemClickMethod(sc);
		
	}

	private void isActivity(SootClass sc) throws ClassIsNotActivityException{
		if(!Scene.v().getActiveHierarchy().getSuperclassesOf(sc).contains(Scene.v().getSootClass("android.app.Activity"))){
			throw new ClassIsNotActivityException(sc+"declared in manifest.xml activity lables is not an activity");
		}
	}
	
	/**
	 * collect UI element events, there are 4 categories of UI elements:<br>
	 * 1. Menu<br>
	 * 2. View<br>
	 * 3. ListActivity's list<br>
	 * 4. Dialog<br>
	 * <p>
	 * For Menu and list, we invoke them by calling onOptionsItemSelected and onListItemClicked directly, no need collecting them, 
	 * thus here we only care about Dialog and View.<br>
	 * 1. View: add doReflect(), and call.<br>
	 * 2. Dialog: add doDialogReflect() and call.<br>
	 * 
	 * @param b contains events
	 * @return insert stmt: base.doReflect(view, listener, callback) or base.doReflect(dialog, listener, callback) before setOn...Listener();
	*/
	private void collectUIEvents(final Body b) {
		SootMethod sm = b.getMethod();
		final SootClass sc = sm.getDeclaringClass();
		
		//if units contains setOnClickListener();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				public void caseInvokeStmt(InvokeStmt stmt) {
					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					if(UIEventRecognizer.isUIEvent(stmt)){
//						Local base = getThisLocal(b, sc);
						List<Value> values = new ArrayList<Value>();
						Local invoker =(Local) invokeExpr.getUseBoxes().get(invokeExpr.getUseBoxes().size()-1).getValue();
						Local listener = (Local)invokeExpr.getArg(0);
						String registar = invokeExpr.getMethod().getSubSignature();
						values.add(invoker);
						values.add(listener);
						values.add(StringConstant.v(registar));
						//if stmt is a view event,base.doReflect(view, listener, callback);
						if(UIEventRecognizer.isViewEvent(stmt)){
							if(!sc.declaresMethod(DoReflect.SUBSIGNATURE))
								addDoReflectMethod(sc);
							SootMethod doReflect = sc.getMethod(DoReflect.SUBSIGNATURE);
							units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doReflect.makeRef(),values)), u);
							b.validate();
						}
						//else if stmt is a dialog event, base.doReflect(dialog, listener, callback);
						else{
							if(!sc.declaresMethod(DoDialogReflect.SUBSIGNATURE))
								addDoDialogReflectMethod(sc);
							SootMethod doDialogReflect = sc.getMethod(DoDialogReflect.SUBSIGNATURE);
							units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doDialogReflect.makeRef(),values)), u);
							b.validate();
						}
					}
				}
			});
		}
		
		//if currentMethod is menu's callback.
		if(UIEventRecognizer.isMenuEvent(sm)){
//			if(EventRecognizer.isFragmentMenuEvent(sm)){
//				System.out.println("FragmentMenuEvent: "+sm.getSignature());
////				transformFragment(b, sc);
//			}
//			else 
				if(UIEventRecognizer.isActivityMenuEvent(sm)){
				System.out.println("ActivityMenuEvent: "+sm.getSignature());
				//TODO
			}
		}
		
		//if currentMethod is OnListItemClick() in ListActivity.
		if(UIEventRecognizer.isOnListItemClick(sm)){
			System.out.println("ListActivity: "+sm.getName());
		}
	}
	
	/**
	 *  if b is my defined <clinit>
	 *  @param b input body
	 *  @return true if b containing field linkedList means my define <clinit>
	 * */
	private boolean isMyClinit(final Body b) {
		boolean flag = false;
		for(Local l:b.getLocals()){
			if(l.getName().equals("linkedList")){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * b is onCreateOptionsMenu<br>
	 * For OnCreateOptionMenu' sootClass<br>
	 * 		add Field: public Menu activityMenu;<br>
	 * 		add Method: doMenuTestMethod();<br>
	 * For OnCreateOptionMenu<br>
	 * 		Add code: {activityMenu = menu; MenuItem menuItem = menu.add("Test"); menuItem.setOnMenuItemClickedListener(this)}<br>
	 * 
	 * Field activityMenu and Method doMenuTestMethod() only exist in class who declares onCreateOptionsMenu<br>
	 * 
	 * 在aagtl中，onPrapareMenu中执行了menu.clear导致test添加不成功
	 * */
	private void modifyOnCreateOptionMenu(final Body b) {
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				@Override			
				public void caseReturnStmt(ReturnStmt stmt) {
					Local base = b.getThisLocal();
					Local param = b.getParameterLocal(0);
					Local menuItem = Jimple.v().newLocal("menuItem", Constants.menuItem_Type);
					
					b.getLocals().add(menuItem);
					SootField menuField = addMenuField(b);
					SootClass sc = b.getMethod().getDeclaringClass();
					if(!sc.declaresMethod(DoMenuTest.SUBSIGNATURE))
						addDoMenuTestMethod(sc);
					
					AssignStmt activityMenuAS = Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(base, menuField.makeRef()), param);
					AssignStmt menuAdd = Jimple.v().newAssignStmt(menuItem, Jimple.v().newInterfaceInvokeExpr(param, Constants.menuAdd_method.makeRef(),StringConstant.v("TEST")));
					InvokeStmt setOnMenuItemClick = Jimple.v().newInvokeStmt(Jimple.v().newInterfaceInvokeExpr(menuItem,Constants.setOnMenuItemClickListener_method.makeRef(),base));
					units.insertBefore(activityMenuAS, u);
					units.insertBefore(menuAdd, u);
					units.insertBefore(setOnMenuItemClick, u);
					
					List<Value> paramValues = new ArrayList<Value>();
					paramValues.add(base);
					paramValues.add(param);
					InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Constants.systemEventHandleraddMenuItem_method.makeRef(),paramValues));
					units.insertBefore(newInvokeStmt, u);
					b.validate();
				}
			});
		}
	}
	
	private SootField addMenuField(Body b) {
		SootClass declaringClass = b.getMethod().getDeclaringClass();
		if(declaringClass.declaresFieldByName(Constants.ACTIVITYMENU)){
			System.err.println("Field menu should not exist multiple times");
			return declaringClass.getFieldByName(Constants.ACTIVITYMENU);
		}
		else {
			SootField menuField = new SootField(Constants.ACTIVITYMENU, Constants.menu_Type);
			declaringClass.addField(menuField);
			return menuField;
		}
	}
	
	/**
	 * AdobeReader.apk contains bugs, fix it.
	 * */
//	private void fixAdobeReaderBug(Body b){
//		String copyFile = "<com.adobe.libs.buildingblocks.utils.BBFileUtils: java.lang.String " +
//				"copyFileFromRawResourcesToStorage(android.content.Context,java.lang.String,int,java.lang.String)>";
//		if(!b.getMethod().getSignature().equals(copyFile)){
//			return;
//		}
//		Chain<Local> locals = b.getLocals();
//		Local leftLocal = null;
//		Local rightLocal = null;
//		for(Local l:locals){
//			if(l.getName().equals("$r9")){
//				if(l.getType().equals(Constants.string_Type)){
//					l.setType(Constants.file_Type);
//					leftLocal = l;
//				}
//			}
//			else if(l.getName().equals("r22")){
//				if(l.getType().equals(Constants.file_Type)){
//					rightLocal = l;
//				}
//			}
//		}
//		
//		final AssignStmt replace = Jimple.v().newAssignStmt(leftLocal, Jimple.v().newCastExpr(rightLocal, Constants.file_Type));
//		final PatchingChain<Unit> units = b.getUnits();
//		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
//			final Unit u = iter.next();
//			u.apply(new AbstractStmtSwitch() {
//				@Override			
//				public void caseAssignStmt(AssignStmt stmt) {
//					AssignStmt as = (AssignStmt)stmt;
//					Value leftOp = as.getLeftOp();
//					Value rightOp = as.getRightOp();
//					
//					if(rightOp instanceof CastExpr&&leftOp instanceof Local){
//						Local leftLocal = (Local)leftOp;
//						CastExpr castExpr = (CastExpr)rightOp;
//						Value v = castExpr.getOp();
//						RefType type = (RefType)castExpr.getCastType();
//						if(type.equals(Constants.string_Type)&&v instanceof Local){
//							Local local = (Local)v;
//							if(local.getName().equals("r22")&&leftLocal.getName().equals("$r9")){
//								units.insertBefore(replace, u);
//								units.remove(u);
//								G.v().out.println("532: "+stmt);
//							}
//						}
//						
//					}
//				}
//			});
//		}
//	}
	
	private void modifyClinit(final Body b, final SootClass sc) {
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				@Override
				public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
					SootField viewLinkedList = sc.getFieldByName(Constants.VIEWLINKEDLIST);
					SootField listenerLinkedList = sc.getFieldByName(Constants.LISTENERLINKEDLIST);
					SootField isVisited = sc.getFieldByName(Constants.ISVISITED);
					SootField isMyEvent = sc.getFieldByName(Constants.ISMYEVENT);
					SootField activities = sc.getFieldByName(Constants.ACTIVITIES);
					SootField unVisitedActivities = sc.getFieldByName(Constants.UNVISITEDACTIVITIES);

					Local linkedList = Jimple.v().newLocal("linkedList",Constants.linkedList_Type);
					b.getLocals().add(linkedList);
					SootClass sootClass = Scene.v().getSootClass(ManifestData.mainActivity);
					SootField unVisitedActivities_mainActivity = sootClass.getFieldByName("unVisitedActivities");
					SootField activities_mainActivity = sootClass.getFieldByName("activities");
					
					SootField dialogLinkedList = sc.getFieldByName(Constants.DIALOGLINKEDLIST);
					SootField dialogListenerLinkedList = sc.getFieldByName(Constants.DIALOGLISTENERLINKEDLIST);
					SootField dialogRegistarLinkedList = sc.getFieldByName(Constants.DIALOGREGISTARLINKEDLIST);
					
					SootField viewRegistarList = sc.getFieldByName(Constants.VIEWREGISTARLINKEDLIST);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(viewLinkedList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(listenerLinkedList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(viewRegistarList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(dialogLinkedList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(dialogListenerLinkedList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(dialogRegistarLinkedList.makeRef()), linkedList),u);
					
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(isMyEvent.makeRef()),IntConstant.v(0)),u);
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(isVisited.makeRef()),IntConstant.v(0)),u);

					if(sc.getName().equals(ManifestData.mainActivity)){
						SootField systemEventLinkedList = sc.getFieldByName(Constants.SYSTEMEVENTLINKEDLIST);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
						units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(activities.makeRef()), linkedList),u);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
						units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(unVisitedActivities.makeRef()), linkedList),u);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
						units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(systemEventLinkedList.makeRef()), linkedList),u);
					}
					else{
						units.insertBefore(Jimple.v().newAssignStmt(linkedList,Jimple.v().newStaticFieldRef(activities_mainActivity.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(activities.makeRef()),linkedList),u);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList,Jimple.v().newStaticFieldRef(unVisitedActivities_mainActivity.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(unVisitedActivities.makeRef()),linkedList),u);
					}
					b.validate();
				}
				
			});
		}
	}
	
	/**
	 * add field activities and unvisitedactivity for mainActivity
	 * */
	private void addMainActivityFields() {
		SootClass mainActivity = Scene.v().getSootClass(ManifestData.mainActivity);
		if(!mainActivity.declaresFieldByName(Constants.ACTIVITIES)){
			SootField activities = new SootField(Constants.ACTIVITIES, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
			mainActivity.addField(activities);
		}
		if(!mainActivity.declaresFieldByName(Constants.UNVISITEDACTIVITIES)){
			SootField unVisitedActivities = new SootField(Constants.UNVISITEDACTIVITIES, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
			mainActivity.addField(unVisitedActivities);
		}
		if(!mainActivity.declaresFieldByName(Constants.SYSTEMEVENTLINKEDLIST)){
			SootField systemEventLinkedList = new SootField(Constants.SYSTEMEVENTLINKEDLIST, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
			mainActivity.addField(systemEventLinkedList);
		}
	}
	
	/**
	 * add 10 fields for sc
	 * 8 fields:
	 * 1. ACTIVITIES
	 * 2. UNVISITEDACTIVITIES
	 * 3. VIEWLINKEDLIST: views
	 * 4. LISTENERLINKEDLIST: views' listeners
	 * 5. ISMYEVENT
	 * 6. ISVISITED: if views or dialog has been collected, set true when start to doTest and doDialogTest. If true, views will not be added to viewLinkedlist any more. 
	 * 7. DIALOGLINKEDLIST: dialogs
	 * 8. DIALOGLISTENERLINKEDLIST: dialogs' listeners
	 * 9. DIALOGREGISTARLINKEDLIST: registar for view listener register view.
	 * 10. VIEWREGISTARLINKEDLIST: registar for dialog listener register dialog.
	 * 
	 * 7个有用: 3+3+1
	 * @param sc activity to be added field.
	 * */
	public void addFields(SootClass sc){
		SootField listenerLinkedList = new SootField(Constants.LISTENERLINKEDLIST, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		SootField viewLinkedList = new SootField(Constants.VIEWLINKEDLIST, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		SootField isMyEvent = new SootField(Constants.ISMYEVENT, BooleanType.v(), Modifier.PUBLIC|Modifier.STATIC);
		SootField activities = new SootField(Constants.ACTIVITIES, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		SootField unVisitedActivities = new SootField(Constants.UNVISITEDACTIVITIES, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		SootField isVisited = new SootField(Constants.ISVISITED, BooleanType.v(), Modifier.PUBLIC|Modifier.STATIC);
		SootField dialogLinkedList = new SootField(Constants.DIALOGLINKEDLIST, Constants.linkedList_Type,Modifier.PUBLIC|Modifier.STATIC);
		SootField dialogListenerLinkedList = new SootField(Constants.DIALOGLISTENERLINKEDLIST, Constants.linkedList_Type,Modifier.PUBLIC|Modifier.STATIC);
		SootField dialogRegistarLinkedList = new SootField(Constants.DIALOGREGISTARLINKEDLIST, Constants.linkedList_Type,Modifier.PUBLIC|Modifier.STATIC);
		SootField viewRegistarLinkedList = new SootField(Constants.VIEWREGISTARLINKEDLIST, Constants.linkedList_Type,Modifier.PUBLIC|Modifier.STATIC);

		if(!sc.declaresFieldByName(Constants.ACTIVITIES))
			sc.addField(activities);
		if(!sc.declaresFieldByName(Constants.UNVISITEDACTIVITIES))
			sc.addField(unVisitedActivities);
		if(!sc.declaresFieldByName(Constants.VIEWLINKEDLIST))
			sc.addField(viewLinkedList);
		if(!sc.declaresFieldByName(Constants.LISTENERLINKEDLIST))
			sc.addField(listenerLinkedList);
		if(!sc.declaresFieldByName(Constants.VIEWREGISTARLINKEDLIST))
		sc.addField(viewRegistarLinkedList);
		if(!sc.declaresFieldByName(Constants.ISMYEVENT))
			sc.addField(isMyEvent);
		if(!sc.declaresFieldByName(Constants.ISVISITED))
			sc.addField(isVisited);
		if(!sc.declaresFieldByName(Constants.DIALOGLINKEDLIST))
			sc.addField(dialogLinkedList);
		if(!sc.declaresFieldByName(Constants.DIALOGLISTENERLINKEDLIST))
			sc.addField(dialogListenerLinkedList);
		if(!sc.declaresFieldByName(Constants.DIALOGREGISTARLINKEDLIST))
			sc.addField(dialogRegistarLinkedList);

	}
	
//	private Local getThisLocal(final Body b,
//			final SootClass sc) {
//		Local base = null;
//		if(containsThisLocal(b)){
//			base  = b.getThisLocal();
//		}
//		else {
//			base = addLocal(b, "activity", RefType.v(sc));
//		}
//		return base;
//	}
//	
//	private boolean containsThisLocal(Body b){
//		Iterator<Unit> unitsIt = b.getUnits().iterator();
//        while (unitsIt.hasNext()){
//            Unit s = unitsIt.next();
//            if (s instanceof IdentityStmt &&
//                ((IdentityStmt)s).getRightOp() instanceof ThisRef)
//                return true;
//        }
//        return false;
//	}
	
	private void addClinit(final SootClass sc) {
		if(sc.getName().equals(ManifestData.mainActivity))
			new Clinit(sc, Clinit.SUBSIGNATURE,true).newMethod();
		else
			new Clinit(sc, Clinit.SUBSIGNATURE,false).newMethod();
	}
	
	private SootMethod addOnCreateOptionsMenuMethod(SootClass sc){
		OnCreateOptionsMenu optionMenu = new OnCreateOptionsMenu(sc, Constants.onCreateOptionsMenu_activity);
		return optionMenu.newMethod();
	}
	
	private SootMethod addOnMenuItemClickMethod(SootClass sc){
		OnMenuItemClick onMenuItemClick = new OnMenuItemClick(sc, Constants.onMenuItemClick_Name);
		return onMenuItemClick.newMethod();
	}
	
	private SootMethod addDoViewAnalysisMethod(SootClass sc){
		DoViewAnalysis doViewAnalysis = new DoViewAnalysis(sc, DoViewAnalysis.SUBSIGNATURE);
		return doViewAnalysis.newMethod();
	}
	
	private SootMethod addDoDialogAnalysisMethod(SootClass sc){
		DoDialogAnalysis doDialogAnalysis = new DoDialogAnalysis(sc, DoDialogAnalysis.SUBSIGNATURE);
		return doDialogAnalysis.newMethod();
	}
	
	private SootMethod addDoDialogTestMethod(SootClass sc){
		DoDialogTest doDialogTest = new DoDialogTest(sc, DoDialogTest.SUBSIGNATURE);
		return doDialogTest.newMethod();
	}
	
	private SootMethod addDoMenuTestMethod(SootClass sc){
		DoMenuTest doMenuTest = new DoMenuTest(sc, DoMenuTest.SUBSIGNATURE);
		return doMenuTest.newMethod();
	}
	
	private SootMethod addDoTestMethod(SootClass sc){
		DoTest doTest = new DoTest(sc, DoTest.SUBSIGNATURE);
		return doTest.newMethod();
	}
	private SootMethod addCheckEventMethod(SootClass sc){
		CheckEvent checkEvent = new CheckEvent(sc, CheckEvent.SUBSIGNATURE);
		return checkEvent.newMethod();
	}
	private SootMethod addCheckEventMethodForMainActivity(SootClass sc) {
		CheckEventForMainActivity checkEvent = new CheckEventForMainActivity(sc, CheckEvent.SUBSIGNATURE);
		return checkEvent.newMethod();
	}
	
	private SootMethod addRunMyThreadMethod(SootClass sc){
		RunMyThread runMyThread = new RunMyThread(sc, RunMyThread.SUBSIGNATURE);
		return runMyThread.newMethod();
	}
	
	private SootMethod addOnRestartMethod(SootClass sc){
		OnRestart onRestart = new OnRestart(sc, OnRestart.SUBSIGNATURE);
		return onRestart.newMethod();
	}
	
	private SootMethod addDoReflectMethod(SootClass sc){
		DoReflect doReflect = new DoReflect(sc, DoReflect.SUBSIGNATURE);
		return doReflect.newMethod();
	}
	
	private SootMethod addDoDialogReflectMethod(SootClass sc){
		DoDialogReflect doDialogReflect = new DoDialogReflect(sc, DoDialogReflect.SUBSIGNATURE);
		return doDialogReflect.newMethod();
	}
	
	
	//addLocals
	private Local addLocal(Body b, String name, Type t){
		Local local = Jimple.v().newLocal(name, t);
		b.getLocals().add(local);
		return local;
	}
	
	public void insertBeforeAssignStmt(Value leftOp,Value rightOp,Chain<Unit> units,Unit u){
		AssignStmt before = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.insertBefore(before, u);
	}
	public void insertBeforeInvokeStmt(Value op,Chain<Unit> units,Unit u){
		InvokeStmt before = Jimple.v().newInvokeStmt(op);
		units.insertBefore(before, u);
	}
	public void insertBeforeIdentityStmt(Value local,Value identityRef,Chain<Unit> units,Unit u){
		IdentityStmt before = Jimple.v().newIdentityStmt(local, identityRef);
		units.insertBefore(before, u);
	}
	public void insertBeforeIfStmt(Value expr, Unit target,Chain<Unit> units,Unit u){
		IfStmt before = Jimple.v().newIfStmt(expr, target);
		units.insertBefore(before, u);
	}
	public void insertBeforeGotoStmt(Unit target,Chain<Unit> units,Unit u){
		GotoStmt before = Jimple.v().newGotoStmt(target);
		units.insertBefore(before, u);
	}
	
	public void insertAfterAssignStmt(Value leftOp,Value rightOp,Chain<Unit> units,Unit u){
		AssignStmt after = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.insertAfter(after, u);
	}
	public void insertAfterInvokeStmt(Value op,Chain<Unit> units,Unit u){
		InvokeStmt after = Jimple.v().newInvokeStmt(op);
		units.insertAfter(after, u);
	}
	public void insertAfterIdentityStmt(Value local,Value identityRef,Chain<Unit> units,Unit u){
		IdentityStmt after = Jimple.v().newIdentityStmt(local, identityRef);
		units.insertAfter(after, u);
	}
	public void insertAfterIfStmt(Value expr, Unit target,Chain<Unit> units,Unit u){
		IfStmt after = Jimple.v().newIfStmt(expr, target);
		units.insertAfter(after, u);
	}
	public void insertAfterGotoStmt(Unit target,Chain<Unit> units,Unit u){
		GotoStmt after = Jimple.v().newGotoStmt(target);
		units.insertAfter(after, u);
	}
}
