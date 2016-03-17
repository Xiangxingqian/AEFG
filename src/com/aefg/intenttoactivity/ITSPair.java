package com.aefg.intenttoactivity;

import com.aefg.intent.Intent;

import soot.toolkits.scalar.Pair;
import tags.ConditionTag;

public class ITSPair extends Pair<Intent, StartActivity>{

	protected ConditionTag condition;
	
	public ITSPair(Intent t, StartActivity u) {
		super(t, u);
	}
	
	public void addTag(ConditionTag conditionTag){
		this.condition = conditionTag;
	}
	
	public ConditionTag getTag(){
		return condition;
	}
	
	public boolean hasTag(){
		return condition!=null;
	}

	@Override
	public String toString() {
		return "ITSPair [condition=" + condition + ", o1=" + o1 + ", o2=" + o2
				+ "]";
	}
	
	
}
