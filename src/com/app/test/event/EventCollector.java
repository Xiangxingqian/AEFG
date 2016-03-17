//package com.app.test.event;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import com.app.test.newMethod.DoDialogReflect;
//import com.app.test.newMethod.DoReflect;
//
//import soot.Body;
//import soot.Local;
//import soot.PatchingChain;
//import soot.RefType;
//import soot.SootClass;
//import soot.SootMethod;
//import soot.Unit;
//import soot.Value;
//import soot.jimple.AbstractStmtSwitch;
//import soot.jimple.IdentityStmt;
//import soot.jimple.InvokeExpr;
//import soot.jimple.InvokeStmt;
//import soot.jimple.Jimple;
//import soot.jimple.StringConstant;
//import soot.jimple.ThisRef;
//
///**
// * an collector of events, including system events and UI events
// * */
//public class EventCollector {
//	
//	Body b;
//	public EventCollector(Body b){
//		this.b = b;
//	}
//	
//    public void collectEvents(){
//    	SootMethod sm = b.getMethod();
//		final SootClass sc = sm.getDeclaringClass();
//		
//		//if units contains setOnClickListener();
//		final PatchingChain<Unit> units = b.getUnits();
//		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
//			final Unit u = iter.next();
//			u.apply(new AbstractStmtSwitch() {
//				public void caseInvokeStmt(InvokeStmt stmt) {
//					if(UIEventRecognizer.isUIEvent(stmt)){
//						collectUIEvents(sc, units, stmt);
//					}
//					else if(SystemEventRecognizer.isSystemEvent(stmt)){
//						collectSystemEvents(sc, units, stmt);
//					}
//				}
//
//				/**
//				 * 系统事件的收集与UI事件相同，也是invokeStmt包含invoke, registar and handler.
//				 * 调用也相同: handler.callback(parameter);
//				 * */
//				private void collectSystemEvents(SootClass sc,
//						PatchingChain<Unit> units, InvokeStmt stmt) {
//					
//				}
//
//				private void collectUIEvents(final SootClass sc,
//						final PatchingChain<Unit> units, InvokeStmt stmt) {
//					Local base=null;
//					
//					//if b does not contain this local, return;
//					try {
//						base = b.getThisLocal();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					InvokeExpr invokeExpr = stmt.getInvokeExpr();
//					List<Value> values = new ArrayList<Value>();
//					Local invoker = (Local)invokeExpr.getUseBoxes().get(0).getValue();
//					Local listener = (Local)invokeExpr.getArg(0);
//					String callback = invokeExpr.getMethod().getName();
//					values.add(invoker);
//					values.add(listener);
//					values.add(StringConstant.v(callback));
//					
//					//if stmt is a view event,base.doReflect(view, listener, callback);
//					if(UIEventRecognizer.isViewEvent(stmt)){
//						if(!sc.declaresMethod(DoReflect.SUBSIGNATURE)){
//							DoReflect doReflect = new DoReflect(sc, DoReflect.SUBSIGNATURE);
//							doReflect.newMethod();
//						}
//						SootMethod doReflect = sc.getMethod(DoReflect.SUBSIGNATURE);
//						units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(
//								base, doReflect.makeRef(),values)), u);
//						b.validate();
//					}
//					//else if stmt is a dialog event, base.doReflect(dialog, listener, callback);
//					else{
//						if(!sc.declaresMethod(DoDialogReflect.SUBSIGNATURE)){
//							DoDialogReflect doDialogReflect = new DoDialogReflect(sc, DoDialogReflect.SUBSIGNATURE);
//							doDialogReflect.newMethod();
//						}
//						SootMethod doDialogReflect = sc.getMethod(DoDialogReflect.SUBSIGNATURE);
//						units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(
//								base, doDialogReflect.makeRef(),values)), u);
//						b.validate();
//					}
//				}
//			});
//		}
//		
//		//if currentMethod is menu's callback.
//		if(UIEventRecognizer.isMenuEvent(sm)){
////			if(EventRecognizer.isFragmentMenuEvent(sm)){
////				System.out.println("FragmentMenuEvent: "+sm.getSignature());
//////				transformFragment(b, sc);
////			}
////			else 
//				if(UIEventRecognizer.isActivityMenuEvent(sm)){
//				System.out.println("ActivityMenuEvent: "+sm.getSignature());
//				//TODO
//			}
//		}
//		
//		//if currentMethod is OnListItemClick() in ListActivity.
//		if(UIEventRecognizer.isOnListItemClick(sm)){
//			System.out.println("ListActivity: "+sm.getName());
//		}
//    }
//}
