package com.aefg.intent;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aefg.dataflowanalysis.AEFGLocalInterproceduralAnalysis;
import com.aefg.intenttoactivity.ITS;
import com.aefg.intenttoactivity.ITSPair;
import com.aefg.intenttoactivity.StartActivity;


import pair.PairFactory;
import singlton.Global;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.Pair;
import tags.ConditionTag;
import tags.Tagger;
import util.ArrayUtil;
import util.LocalAnalysis;
import vasco.Context;
import view.ViewCondition;
import analysis.methodAnalysis.InterMethodAnalysis;

/**
 * 分析步骤： 1. 解析startActivity语句，求得参数intent。 2. 求得intent定义所在的方法。 3.
 * 分析该方法，求出intent局部变量 4. 求解出与intent有关的所有语句ss 5. 从ss中得出目标intent，即有效的定义语句。 6.
 * 将有效信息，添加到ITS中。
 * */

public class IntentFlowAnalysis  {
	static int intentNum;
	static int num;
	public UnitGraph ug;
	private  List<SootMethod> visitedMethods = new ArrayList<SootMethod>();

	public IntentFlowAnalysis(UnitGraph ug) {
		this.ug = ug;
	}
	//获得与type有关的所有语句，包括InvokeStmt, AssignStmt等
	public List<Stmt> getIntentStmts(SootMethod sm, String type) {
		Local l = LocalAnalysis.parseLocal(sm, type);
		List<Stmt> stmts = InterMethodAnalysis.getStmtsWithLocal(l,
				new ExceptionalUnitGraph(sm.retrieveActiveBody()));
		return stmts;
	}

	public void genITS(InvokeStmt is) {
		Value vv = is.getInvokeExpr().getArgs().get(0);
		SootMethod inMethod  = Tagger.getMethodTag(is);	
		System.out.println("StartActivity: "+is+"----Method: "+inMethod);
		List<SootMethod> defineMethods = LocalAnalysis.parseDMBL((Local)vv, inMethod);
		
		//System.out.println(defineMethods.size());
		for (SootMethod sm : defineMethods) {
			if (!visitedMethods.contains(sm)) {
				visitedMethods.add(sm);
				List<Stmt> intentStmts = getIntentStmts(sm, "android.content.Intent");
				Intent iBuilder = new Intent(intentStmts, sm);
				iBuilder.buildIntent();
				List<Intent> intents = iBuilder.getIntents();
				System.out.println("IntentStmts: "+sm+"----Size: "+intents.size());
				for(Intent concreteIntent:intents)
					if(concreteIntent!=null){
						StartActivity startActivity = new StartActivity(is);
						ITSPair itsPair = new ITSPair(concreteIntent, startActivity);
						
						ConditionTag intentTag = (ConditionTag)concreteIntent.getTag("ConditionTag");
						ConditionTag tag = (ConditionTag)startActivity.getTag("ConditionTag");
						
						//优先考虑intent的tag
						if(intentTag!=null&&ViewCondition.isValid(intentTag.getValues())){
							itsPair.addTag(intentTag);
						}
						else if(tag!=null&&ViewCondition.isValid(tag.getValues())){
							itsPair.addTag(tag);
						}
						ITS.v().getITSPairs().add(itsPair);
					}
			}
		}	
	}
	
	public List<Value> parseIntent(Value v, SootMethod sm, InvokeStmt is){
		Type type = v.getType();
		AEFGLocalInterproceduralAnalysis analysis = new AEFGLocalInterproceduralAnalysis(sm, ArrayUtil.toList(type));
		Context<SootMethod, Unit, Map<Local, List<Value>>> context = analysis.doInterprocedure(sm,4,null,null);
		Map<Local, List<Value>> valueBefore = context.getValueBefore(is);
		List<Value> intents = valueBefore.get(v);
		return intents;
	}	
}
