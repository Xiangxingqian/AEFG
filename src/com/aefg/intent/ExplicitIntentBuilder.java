package com.aefg.intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aefg.dataflowanalysis.AEFGLocalInterproceduralAnalysis;
import com.aefg.dataflowanalysis.Types;

import condition.Condition;

import soot.Local;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JNewExpr;
import tags.ConditionTag;
import util.ArrayUtil;
import util.StringHandler;
import vasco.Context;

public class ExplicitIntentBuilder extends Intent {

	public ExplicitIntentBuilder(List<Stmt> intentDefs,SootMethod sm) {
		super(intentDefs,sm);
	}
	public List<Intent> build(){
		List<Intent> intents = new ArrayList<Intent>();
		List<Type> types = ArrayUtil.toList(type);
		types.add(Types.getComponentType());
		AEFGLocalInterproceduralAnalysis analysis = 
				new AEFGLocalInterproceduralAnalysis(sm, types);
		Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure = 
				analysis.doInterprocedure(sm, new HashMap<Local, List<Value>>());
		
		for(Stmt stmt:intentDefs){
			Value v = null;
			if (stmt instanceof InvokeStmt) {
				InvokeStmt invokeStmt = (InvokeStmt) stmt;
				SootMethod callee = invokeStmt.getInvokeExpr().getMethod();
				String subSignature = callee.getSubSignature();
				if(IntentConstants.INIT1.equals(subSignature)||
						IntentConstants.SETCLASS3.equals(subSignature)){
					v = invokeStmt.getInvokeExpr().getArg(1);
					System.out.println("52: "+"Value: "+v);
					if(v instanceof ClassConstant){
						intentWithActivity = invokeStmt;
						activity = parseActivityName(v);
						ConditionTag ct = computeTag(stmt, sm);
						addTag(ct);
						
						Intent intent = new Intent();
						intent.setIntentWithActivity(intentWithActivity);
						intent.setActivity(activity);
						intent.addTag(ct);
						intents.add(intent);
					}
				}
				else if(IntentConstants.INIT3.equals(subSignature)){
					v = invokeStmt.getInvokeExpr().getArg(3);
					System.out.println("73: "+"Value: "+v+" Activity: "+activity);
					if(v instanceof ClassConstant){
						intentWithActivity = invokeStmt;
						activity = parseActivityName(v);
						ConditionTag ct = computeTag(stmt, sm);
						addTag(ct);
						
						Intent intent = new Intent();
						intent.setIntentWithActivity(intentWithActivity);
						intent.setActivity(activity);
						intent.addTag(ct);
						intents.add(intent);
					}
				}
				else if(IntentConstants.SETCLASS1.equals(subSignature)||
						IntentConstants.SETCLASS2.equals(subSignature)){
					v = invokeStmt.getInvokeExpr().getArg(1);
					System.out.println("90: "+"Value: "+v+" Activity: "+activity);
					if(v instanceof StringConstant){
						intentWithActivity = invokeStmt;
						activity = parseActivityName((StringHandler.removeQuot(v+"")));
						ConditionTag ct = computeTag(stmt, sm);
						addTag(ct);
						
						Intent intent = new Intent();
						intent.setIntentWithActivity(intentWithActivity);
						intent.setActivity(activity);
						intent.addTag(ct);
						intents.add(intent);
					}
					else{
						Map<Local, List<Value>> valueBefore = doInterprocedure.getValueBefore(stmt);
						List<Value> list = valueBefore.get(v);
						System.out.println("106: "+"Value: "+v+" Activity: "+activity);
						if(list!=null&&list.size()>0){
							intentWithActivity = invokeStmt;
							activity = parseActivityName(list.get(0));
							ConditionTag ct = computeTag(stmt, sm);
							addTag(ct);
							
							Intent intent = new Intent();
							intent.setIntentWithActivity(intentWithActivity);
							intent.setActivity(activity);
							intent.addTag(ct);
							intents.add(intent);
						}
						else {
							//System.out.println("Value: "+v+"SootMethod: "+sm+" Stmt: "+stmt+" List:"+(list==null));
							//System.out.println(list.size());
						}
					}
				}
				else if(IntentConstants.SETCOMPONENT.equals(subSignature)){
					//TODO
					
//					v = invokeStmt.getInvokeExpr().getArg(0);
//					Map<Local, List<Value>> valueBefore = doInterprocedure.getValueBefore(stmt);
//					List<Value> list = valueBefore.get(v);
//					if(list.size()>0){
//						if(v instanceof JNewExpr){
//							JNewExpr jne = (JNewExpr)v;
//							//Unit unit = (Unit)v;
//						}
//					}
//					
//					//setTag(stmt, sm);
					System.out.println("Component!!!");
				}
			}
		}
		return intents;
	}

}
