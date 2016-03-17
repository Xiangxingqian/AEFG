package resource;


import java.util.List;

import analysis.methodAnalysis.IntraMethodAnalysis;
import analysis.methodAnalysis.InterMethodAnalysis;



import singlton.Global;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

public class MenuHandler {
	private static MenuHandler mh = null;
	private MenuHandler(){}
	public static MenuHandler v(){
		if(mh==null)
			mh = new MenuHandler();
		return mh;
	}
	public static final String ADDCHAR = "android.view.MenuItem add(int,int,int,CharSequence)";
	public static final String ADDINT = "android.view.MenuItem add(int,int,int,int)";
	public static final String SETCONTENTVIEW = "void setContentView(int)";
	
	public enum MenuType{
		contextMenu,optionMenu
	}
	List<RowMenu> rfs = MenuTable.v().getRows();	
	
	/**
	 * entry method, generate menu table
	 * @param sm Method contains add(int,int,int,int)
	 * @param mt ContextMenu or OptionMenu
	 * */
	public void retriveMenuItem(SootMethod sm,MenuType mt){
		IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
		List<InvokeStmt> invokes = ma.getInvokeStmtsWithLineNumber(true);
		List<AssignStmt> assignStmts = ma.getAssignStmts();
		for(InvokeStmt is:invokes){	
			getAddMethod(is.getInvokeExpr(), sm, mt);
		}
		for(AssignStmt as:assignStmts){
			Value v = as.getRightOp();
			if(v instanceof JInterfaceInvokeExpr){
				JInterfaceInvokeExpr aie = (JInterfaceInvokeExpr)v;
				getAddMethod(aie, sm, mt);
			}
			else if(v instanceof JVirtualInvokeExpr){
				JVirtualInvokeExpr jie = (JVirtualInvokeExpr)v;
				getAddMethod(jie, sm, mt);
			}
		}
	}
	
	/**
	 * get add method. add(int,int,int,int) or  add(int,int,int,CharSequence)
	 * @param ie InvokeExpr r.add(param);
	 * @param mt MenuType
	 * */
	public void getAddMethod(InvokeExpr ie,SootMethod sm,MenuType mt){
		SootMethod method = ie.getMethod();
		List<Value> values = ie.getArgs();
		String sin = method.getSignature();		
		if(sin.contains(ADDCHAR)){					
			int two = Integer.parseInt(values.get(1).toString());				
			String fouthP = values.get(3).toString();
			String claName = sm.getDeclaringClass().getName();
			if(claName.contains("$")){
				claName = claName.substring(0,claName.indexOf("$"));
			}
			RowMenu rf = new RowMenu(claName, mt, two, fouthP);
			rf.setText(fouthP);
			rfs.add(rf);			
		}
		else if(sin.contains(ADDINT)){			
			int two = Integer.parseInt(values.get(1).toString());				
			String fouthP = values.get(3).toString();
			String claName = sm.getDeclaringClass().getName();
			if(claName.contains("$")){
				claName = claName.substring(0,claName.indexOf("$"));
			}
			
			RowMenu rf = new RowMenu(claName, mt, two, fouthP);
			int id = Integer.parseInt(fouthP);
			if(IdTable.v().getIdToName().keySet().contains(id)){
				String string = IdTable.v().getIdToName().get(id);
				String text = IdTable.v().getRefValueToText().get(string);
				if(string==null||text==null)
					throw new RuntimeException("public.xml do not have id "+id+" sm = "+sm+" string:"+string+" text:"+text);
				rf.setText(text);			
				String className = rf.getClassName();
				System.out.println(" "+className);
				if(Global.v().getContentView().containsKey(className)){
					int i = Global.v().getContentView().get(className);
					rf.setClassId(i);				
					rfs.add(rf);
				}
			}	
		}
	}
	
	/**
	 * get setContentView 
	 * @param sm All the methods who contains setContentView.    
	 * @return hashMap <activityClass, R.id.layout.main.xml>
	 * */
	public void analyzeContentView(SootMethod sm){
		IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
		List<InvokeStmt> isStmts = ma.getInvokeStmts();
		for(InvokeStmt is:isStmts){
			if(is.getInvokeExpr().getMethod().getSignature().contains(SETCONTENTVIEW)){
				String value = is.getInvokeExpr().getArg(0).toString();
				String string = sm.getDeclaringClass().getName();
				if(string.contains("$")){
					String s = string.substring(0,string.indexOf("$"));
					System.out.println("MenuHandler: "+s+" id: "+Integer.parseInt(value));
					Global.v().getContentView().put(s,Integer.parseInt(value));					
				}
				else{
					System.out.println("MenuHandler: else "+string+" id: "+Integer.parseInt(value));
					Global.v().getContentView().put(string,Integer.parseInt(value));
				}
					
				break;
			}
		}
	}
	
	public MenuTable getMenuTable(){
		return MenuTable.v();
	}
}
