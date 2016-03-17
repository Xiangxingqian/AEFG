package com.aefg.intenttoactivity;

import java.util.List;

import com.aefg.intent.AbstractHost;

import condition.Condition;
import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import tags.ConditionTag;
import tags.Tagger;

public class StartActivity extends AbstractHost{
	
	InvokeStmt startActivity;
	public StartActivity(InvokeStmt startActivity){
		this.startActivity = startActivity;
		build();
	}
	
//	public InvokeStmt getStartActivity() {
//		return startActivity;
//	}

	@Override
	public InvokeStmt getValue() {
		return startActivity;
	}
	
	public void build(){
		setTag(startActivity, Tagger.getMethodTag(startActivity));
	}
	
	protected void setTag(Stmt stmt, SootMethod sm) {
		List<Value> conditionOfUnit = 
				Condition.getConditionOfUnit(sm,stmt);
		ConditionTag ct = new ConditionTag(conditionOfUnit);
		addTag(ct);
	}
}
