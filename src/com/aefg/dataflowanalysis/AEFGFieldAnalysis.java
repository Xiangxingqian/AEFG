package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import tags.FlagTag;
import tags.Tagger;
import util.ArrayUtil;
import vasco.Context;
import analysis.methodAnalysis.IntraMethodAnalysis;

public class AEFGFieldAnalysis extends AEFGInterAnalysis<Map<FieldRef, List<Value>>> {

	public AEFGFieldAnalysis(SootMethod m, List<Type> types) {
		super(m, types);
	}
	
	public AEFGFieldAnalysis(SootMethod m, List<Type> types, int model) {
		super(m, types, model);
	}

	//计算FieldRef lhs的值
	public void assign(FieldRef lhs, Value rhs,
			Map<FieldRef, List<Value>> input,
			Map<FieldRef, List<Value>> output, Unit unit) {
		if (rhs instanceof CastExpr) {
			rhs = ((CastExpr) rhs).getOp();
		}
		if (rhs instanceof Constant) {
			output.put(lhs, ArrayUtil.toList(rhs));
		} 
		//field = local;
		else if (rhs instanceof Local) {
			int flagTag = Tagger.getFlagTag(m);
			if(flagTag==FlagTag.DOING){
				Map<SootMethod, Context<SootMethod, Unit, Map<Local, List<Value>>>> analyzingContexts = 
						Contexts.v().getAnalyzingContexts();
				Map<Local, List<Value>> map = analyzingContexts.get(m).getValueBefore(unit);
				output.put(lhs, map.get((Local)rhs));
			}
			else if(flagTag==FlagTag.DONE){
				Map<SootMethod, Context<SootMethod, Unit, Map<Local, List<Value>>>> analyzedContexts = 
						Contexts.v().getAnalyzedContexts();
				Map<Local, List<Value>> map = analyzedContexts.get(m).getValueBefore(unit);
				output.put(lhs, map.get((Local)rhs));
			}
			else{
				Type type = lhs.getField().getType();
				List<Type> types;
				if(Types.getViewTypes().contains(type)){
					types = Types.getViewTypes();
				}
				else 
					types = ArrayUtil.toList(lhs.getField().getType());
				
				AEFGLocalInterproceduralAnalysis localAnalysis = 
						new AEFGLocalInterproceduralAnalysis(m, types);
				Context<SootMethod, Unit, Map<Local, List<Value>>> doDefaultInterprocedure = 
						localAnalysis.doInterprocedure(m, 7, null ,unit);
				Map<Local, List<Value>> map = doDefaultInterprocedure.getValueBefore(unit);
				//Map<Local, List<Value>> exitValueMap = doDefaultInterprocedure.getExitValue();
				output.put(lhs, map.get((Local)rhs));
			}
		}
		else {
			output.put(lhs, new ArrayList<Value>());
		}			
	}
	
	//定义1. 定义语句时常量
	@Override
	public Map<FieldRef, List<Value>> compute_Def_ByConstant(
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> context,
			Unit unit, Map<FieldRef, List<Value>> inValue) {
		// Initialize result to input
		Map<FieldRef, List<Value>> outValue = copy(inValue);
		// Only statements assigning locals matter
		if (unit instanceof AssignStmt) {
			// Get operands
			Value lhsOp = ((AssignStmt) unit).getLeftOp();
			Value rhsOp = ((AssignStmt) unit).getRightOp();
			if (lhsOp instanceof FieldRef) {
				assign((FieldRef) lhsOp, rhsOp, inValue, outValue,unit);		
			}
		}
		return outValue;
	}

	@Override
	public Map<FieldRef, List<Value>> compute_Entry_OfMethod(
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> context,
			SootMethod targetMethod, Unit unit,
			Map<FieldRef, List<Value>> inValue) {
		return inValue;
		
	}

	//定义2： 当定义是method时。Field不考虑这种情况。不存在
	@Override
	protected Map<FieldRef, List<Value>> compute_Def_ByMethod(
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> currentContext,
			Unit node, Map<FieldRef, List<Value>> in) {
		return in;
	}
	
	@Override
	public Map<FieldRef, List<Value>> copy(Map<FieldRef, List<Value>> src) {
		return new HashMap<FieldRef, List<Value>>(src);
	}

	@Override
	public Map<FieldRef, List<Value>> meet(Map<FieldRef, List<Value>> op1,
			Map<FieldRef, List<Value>> op2) {
		Map<FieldRef, List<Value>> result = new HashMap<FieldRef, List<Value>>(op1);
		for (FieldRef x : op2.keySet()) {
			if (op1.containsKey(x)) {
				// Check the values in both operands
				List<Value> c1 = op1.get(x);
				List<Value> c2 = op2.get(x);
				if (c1 != null && c1.equals(c2) == false) {
					// Set to non-constant
					c1.addAll(c2);
					result.put(x, c1);
				}
			} else {
				// Only in second operand, so add as-is
				result.put(x, op2.get(x));
			}
		}
		return result;
	}

	@Override
	public Map<FieldRef, List<Value>> topValue() {
		return new HashMap<FieldRef, List<Value>>();
	}
	
	/**
	 * 给定一个field，求field被定义的方法
	 * */
	public static Map<SootMethod, List<Unit>> parseMethodAndUnits(FieldRef fr){
		Map<SootMethod, List<Unit>> def_methodToUnits = new HashMap<SootMethod, List<Unit>>();
		//SootClass declaringClass = fr.getField().getDeclaringClass();
		List<SootMethod> methods = fr.getField().getDeclaringClass().getMethods();
		for(SootMethod sm:methods){
			if(sm.isConcrete()){
				IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
				List<AssignStmt> assignStmts = ma.getAssignStmts();			
				for(AssignStmt s:assignStmts){	
					if(s.getLeftOp() instanceof FieldRef){
						FieldRef frRef = (FieldRef)s.getLeftOp();
						if(frRef.getField().equals(fr.getField())){
							if(def_methodToUnits.get(sm)==null){
								def_methodToUnits.put(sm, ArrayUtil.toList((Unit)s));
							}
							else
								def_methodToUnits.get(sm).add(s);
						}
					}
				}
			}
		}
		return def_methodToUnits;
	}

	@Override
	protected void addToAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> currentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeFromAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> currentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addToAnalyzedContext(SootMethod m,
			Context<SootMethod, Unit, Map<FieldRef, List<Value>>> currentContext) {
		// TODO Auto-generated method stub
		
	}
}

