package com.aefg.intent;

import java.util.ArrayList;
import java.util.List;

import pair.PairFactory;

import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import tags.ConditionTag;
import util.StringHandler;
import condition.Condition;

public class Intent extends AbstractHost {
	Type type = RefType.v("android.content.Intent");
	Intent intent;
	
	List<Intent> intents = new ArrayList<Intent>();
	
	protected List<String> explicitIntents = IntentConstants.getExplicitIntent();
	protected List<String> implicitIntents = IntentConstants.getImplicitIntent();
	
	//outputs:
	protected String activity;
	protected InvokeStmt intentWithActivity;
	
	//inputs:
	protected List<Stmt> intentDefs;
	protected SootMethod sm;
	
	public Intent(){
		
	}
	
	public Intent(List<Stmt> intentDefs,SootMethod sm) {
		this.intentDefs = intentDefs;
		this.sm = sm;
	}
	
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public void setIntentWithActivity(InvokeStmt intentWithActivity) {
		this.intentWithActivity = intentWithActivity;
	}

	public List<Stmt> analyzeIntentDefs(List<Stmt> intentDefs, List<String> intentType){
		List<Stmt> intentStmts = new ArrayList<Stmt>();
		for (Stmt stmt: intentDefs) {
			if (stmt instanceof InvokeStmt) {
				InvokeStmt invokeStmt = (InvokeStmt) stmt;
				SootMethod sm = invokeStmt.getInvokeExpr().getMethod();
				String subSignature = sm.getSubSignature();
				if(intentType.contains(subSignature)){
					intentStmts.add(stmt);
				}
			}
		}
		PairFactory.v().getStmtsToMethod().put(intentStmts, sm);
		return intentStmts;
	}
	
	public boolean isExlicitIntent(){
		return analyzeIntentDefs(intentDefs,IntentConstants.getExplicitIntent()).size()>0;
	}
	
	private boolean isImplicitIntent() {
		return analyzeIntentDefs(intentDefs,IntentConstants.getImplicitIntent()).size()>0;
	}

	//解析intent, 判断intent是隐式或是显式
	public void buildIntent(){
		if(isExlicitIntent()){
			List<Stmt> analyzeIntentDefs = analyzeIntentDefs(intentDefs,explicitIntents);
			ExplicitIntentBuilder explicitIntentBuilder = 
					new ExplicitIntentBuilder(analyzeIntentDefs, sm);
			List<Intent> build = explicitIntentBuilder.build();
			intents.addAll(build);
		}
		else if(isImplicitIntent()){
			List<Stmt> analyzeIntentDefs = analyzeIntentDefs(intentDefs,implicitIntents);
			intent = new ImplicitIntentBuilder(analyzeIntentDefs, sm);
			intents.add(intent);
		}
		else {
			intent = null;
		}
		
	}
	
	public List<Intent> getIntents() {
		return intents;
	}

	protected ConditionTag computeTag(Stmt stmt, SootMethod sm){
		List<Value> conditionOfUnit = 
				Condition.getConditionOfUnit(sm,stmt);
		ConditionTag ct = new ConditionTag(conditionOfUnit);
		return ct;
	}

	public String parseActivityName(String src){
		String tgt = "";
		if(src.contains("$")){
			src = src.substring(0, src.indexOf("$"));
		}
		tgt = StringHandler.getShortActivityName(src);							
		return tgt;
	}
	
	public String parseActivityName(Value v){
		return parseActivityName(v+"");
	}
	
	public InvokeStmt getIntentWithActivity() {
		return intentWithActivity;
	}
	
	public String getActivity() {
		return activity;
	}

	public List<Stmt> getIntentDefs() {
		return intentDefs;
	}

	@Override
	public Intent getValue() {
		return null;
		
		//return intent;
	}

	@Override
	public String toString() {
		return "Intent [activity=" + activity + "]";
	}
	
	
}
