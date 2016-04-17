package com.aefg.intent;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import singlton.Global;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import tags.ConditionTag;
import tags.Tagger;
import util.ArrayUtil;
import util.LocalAnalysis;
import vasco.Context;
import view.ViewCondition;
import analysis.methodAnalysis.InterMethodAnalysis;

import com.aefg.dataflowanalysis.AEFGLocalInterproceduralAnalysis;
import com.aefg.intenttoactivity.ITSPair;
import com.aefg.intenttoactivity.StartActivity;

/**
 * 分析步骤： 1. 解析startActivity语句，求得参数intent。 2. 求得intent定义所在的方法。 3.
 * 分析该方法，求出intent局部变量 4. 求解出与intent有关的所有语句ss 5. 从ss中得出目标intent，即有效的定义语句。 6.
 * 将有效信息，添加到ITS中。
 * */

public class IntentFlowAnalysis {
	
	static int intentNum;
	static int num;
	public UnitGraph ug;
	private SootMethod method;
	private  List<SootMethod> visitedMethods = new ArrayList<SootMethod>();

	public IntentFlowAnalysis(SootMethod method) {
		this.method = method;
		this.ug = new ExceptionalUnitGraph(method.retrieveActiveBody());
	}
	
	public List<Stmt> getIntentStmts(SootMethod sm) {
		Local l = LocalAnalysis.parseLocal(sm, "android.content.Intent");
		List<Stmt> stmts = InterMethodAnalysis.getStmtsWithLocal(l,
				new ExceptionalUnitGraph(sm.retrieveActiveBody()));
		return stmts;
	}

	public void genITS(InvokeStmt is) {
		Value argIntent = is.getInvokeExpr().getArgs().get(0);
		SootMethod useMethod  = Tagger.getMethodTag(is);	
		
		System.out.println("StartActivity: "+is+"----Method: "+useMethod);
		
		List<SootMethod> defMethods = LocalAnalysis.parseDMBL((Local)argIntent, useMethod);
		
		//System.out.println(defineMethods.size());
		for (SootMethod sm:defMethods) {
			if (!visitedMethods.contains(sm)) {
				visitedMethods.add(sm);
				List<Stmt> intentStmts = getIntentStmts(sm);
				Intent iBuilder = new Intent(intentStmts, sm);
				iBuilder.buildIntent();
				List<Intent> intents = iBuilder.getIntents();
				System.out.println("IntentStmts: "+sm+"----Size: "+intents.size());
				for(Intent concreteIntent:intents){
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
						Global.v().getITSPairs().add(itsPair);
					}
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
