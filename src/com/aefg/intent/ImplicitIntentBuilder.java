package com.aefg.intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aefg.dataflowanalysis.AEFGLocalInterproceduralAnalysis;
import com.aefg.dataflowanalysis.TypeAndRepresentation;
import com.aefg.dataflowanalysis.Types;

import pair.PairFactory;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.CastExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import tags.ConditionTag;
import util.StringHandler;
import vasco.Context;

/**
 * 一个隐式intent有哪些属性？
 * 四元组: action, dataScheme, dataType和category
 * */

public class ImplicitIntentBuilder extends Intent{
	
	public ImplicitIntentBuilder(List<Stmt> intentDefs, SootMethod sm) {
		super(intentDefs, sm);
		parseAttributes();
		build();
	}

	private Stmt actionStmt;
	
	private String action = "";
	private String data = "";
	private String mimeType = "";
	private String catogory = "";
	private String[] attributes = {action,data,mimeType,catogory};

	/**
	 * 分析stmts，获得intent的四个属性
	 * 分析过程：
	 * 1. setAction, setType和addCategory的参数一般都是String类型
	 * 2. setData()的参数是Uri。
	 * 对于前三者的分析，只需要考虑String的指针引用分析。
	 * */
	public ImplicitIntentBuilder parseAttributes() {		
		SootMethod defineMethod = PairFactory.v().getStmtsToMethod().get(intentDefs);
		//System.out.println("Implicit intent building..."+" in "+defineMethod);
		Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure = 
				new AEFGLocalInterproceduralAnalysis(defineMethod,Types.getTypes()).
					doInterprocedure(defineMethod,  new HashMap<Local, List<Value>>());
		for (Stmt stmt : intentDefs) {
			if (stmt instanceof InvokeStmt) {
				InvokeStmt invokeStmt = (InvokeStmt) stmt;
				List<Value> value = invokeStmt.getInvokeExpr().getArgs();
				String signature = invokeStmt.getInvokeExpr().getMethod().getSubSignature();
				if (signature.equals(IntentConstants.INTENTACTION)||
						signature.equals(IntentConstants.SETACTION)) {
					action = parseParams(defineMethod, value.get(0), stmt,doInterprocedure);
					actionStmt = stmt;
				} 
				else if (signature.equals(IntentConstants.INTENTACTIONANDDATA)) {
					action = parseParams(defineMethod, value.get(0), stmt, doInterprocedure);
					actionStmt = stmt;
					data = parseParams(defineMethod, value.get(1), stmt, doInterprocedure);
				}
				else if (signature.equals(IntentConstants.SETDATA)) {
					data = parseParams(defineMethod, value.get(0), stmt,doInterprocedure);
				} 
				else if (signature.equals(IntentConstants.SETTYPE)) {
					mimeType = parseParams(defineMethod, value.get(0), stmt,doInterprocedure);
				} 
				else if (signature.equals(IntentConstants.SETDATAANDTYPE)) {
					data = parseParams(defineMethod, value.get(0), stmt,doInterprocedure);
					mimeType = parseParams(defineMethod, value.get(1), stmt,doInterprocedure);
				}
				else if (signature.equals(IntentConstants.ADDCATEGORY)) {
					catogory = parseParams(defineMethod, value.get(0), stmt,doInterprocedure);
				}
			}
		}
		attributes[0] = action;
		attributes[1] = data;
		attributes[2] = mimeType;
		attributes[3] = catogory;
		return this;
	}
	
	private String parseParams(SootMethod m, Value v, Unit u, Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure){
		String value = "";
		//AEFGForwordInterproceduralAnalysis analysis = new AEFGForwordInterproceduralAnalysis(m);
		if(v instanceof CastExpr){
			v = ((CastExpr) v).getOp();
		}
		if(v instanceof StringConstant){
			String s = v.toString();
			value = s.substring(s.indexOf("\"")+1, s.lastIndexOf("\""));
		}
		else{
			Map<Local, List<Value>> valueBefore = doInterprocedure.getValueBefore(u);
			ArrayList<Value> list =(ArrayList) valueBefore.get(v);
			value = list+"";
		}
		return value;
	}
	
	public String[] getAttributes() {
		return attributes;
	}
	
	public void build(){
		String activityName = IntentFilter.matched_getActivity(attributes);
		if(activityName!=null){
			activityName = StringHandler.getShortActivityName(activityName);
			intentWithActivity = createFakeIntent(activityName);
			activity = activityName;
			ConditionTag ct = computeTag(actionStmt, sm);
			addTag(ct);
		}
	}
	
	private InvokeStmt createFakeIntent(String activity){
		Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));	
		SootMethod toCall = Scene.v().getMethod
				("<android.content.Intent: android.content.Intent setClassName(java.lang.String,java.lang.String)>");
		List<Value> sList = new ArrayList<Value>();		
		sList.add(StringConstant.v("$r1"));
		sList.add(StringConstant.v(activity));
		VirtualInvokeExpr vie = Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), sList);
		return Jimple.v().newInvokeStmt(vie);
	}
}
