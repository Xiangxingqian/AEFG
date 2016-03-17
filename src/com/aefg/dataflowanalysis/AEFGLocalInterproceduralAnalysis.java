package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import tags.FlagTag;
import util.ArrayUtil;
import vasco.Context;
import analysis.methodAnalysis.IntraMethodAnalysis;

public class AEFGLocalInterproceduralAnalysis extends AEFGInterAnalysis<Map<Local, List<Value>>>{
	
	public AEFGLocalInterproceduralAnalysis(SootMethod m, List<Type> types) {
		super(m, types);
	}

	public AEFGLocalInterproceduralAnalysis(SootMethod m, List<Type> types, int model) {
		super(m, types, model);
	}
	
	//计算local lhs的值
	private void assign(Local lhs, Value rhs,
			Map<Local, List<Value>> input, Map<Local, List<Value>> output) {
		if (rhs instanceof CastExpr) {
			rhs = ((CastExpr) rhs).getOp();
		}
		if (rhs instanceof Constant) {
			output.put(lhs, ArrayUtil.toList(rhs));
		} 
		else if (rhs instanceof Local) {
			if(input.containsKey(rhs)) {
				output.put(lhs, input.get(rhs));
			}
		}
		//当local指向Field时，将Field的值赋值给local。
		else if(rhs instanceof FieldRef){
			if(field_anal_or_not!=no_field_anal){
				updateToLocal(lhs, rhs, output);
			}
			if(output.get(lhs)==null){
				output.put(lhs, ArrayUtil.toList(rhs));
			}
		}
		else {
			output.put(lhs, new ArrayList<Value>());
		}			
	}

	/**将FieldRef指向值，更新给Local lhs
	 * @param lhs 待更新的Local
	 * @param rhs lhs指向的field
	 * @param out 待写的map
	 * */
	private void updateToLocal(Local lhs, Value rhs,
			Map<Local, List<Value>> output) {
		FieldRef fr = (FieldRef)rhs;
		Map<FieldRef, List<Value>> map = parseField(fr);
		//System.out.println("fr所在的类是： "+fr.getField().getDeclaringClass()+" Field is: "+fr.getField());
		for(FieldRef f:map.keySet()){
			if(f.getField().equals(fr.getField())){
				List<Value> list = map.get(f);
				//测试执行代码
				if(list!=null&&list.size()>0){
					if(output.get(lhs)!=null)
						output.get(lhs).addAll(list);
					else 
						output.put(lhs, list);
				}
			}
		}
	}
	
	/**
	 * 解析field思路：先找到field的定义方法，再求解该field指向的值。
	 * 
	 * 步骤：
	 * 1 得到fr所在的类的methods。
	 * 
	 * 2 逐个检测method，当field出现在左值时，添加unit到Map中
	 * 
	 * 3 得到Map<SootMethod, List<Unit>>，遍历该map。
	 * 
	 * 4 使用AEFGFieldAnalysis 求解field值。
	 * 
	 * 
	 * 使用map的原因是： 该fr会在多个方法内定义
	 * */
	public Map<FieldRef, List<Value>> parseField(FieldRef fr){
		Map<FieldRef, List<Value>> output = new HashMap<FieldRef, List<Value>>();
		List<SootMethod> methods = fr.getField().getDeclaringClass().getMethods();
		for(SootMethod sm:methods){
			if(sm.isConcrete()){
				IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
				List<AssignStmt> assignStmts = ma.getAssignStmts();			
				for(AssignStmt s:assignStmts){	
					if(s.getLeftOp() instanceof FieldRef){
						FieldRef frRef = (FieldRef)s.getLeftOp();
						if(frRef.getField().equals(fr.getField())){
							AEFGFieldAnalysis analysis = new AEFGFieldAnalysis(sm, ArrayUtil.toList(fr.getField().getType()));
							analysis.assign(fr, s.getRightOp(), null, output, s);
						}
					}
				}
			}
		}
		return output;
	}
	
	/**
	 * 简单分析，当输入是一条简单的赋值语句时
	 * */
	@Override
	public Map<Local, List<Value>> compute_Def_ByConstant(
			Context<SootMethod, Unit, Map<Local, List<Value>>> context,
			Unit unit, Map<Local, List<Value>> inValue) {
		// Initialize result to input
		Map<Local, List<Value>> outValue = copy(inValue);
		// Only statements assigning locals matter
		if (unit instanceof AssignStmt) {
			// Get operands
			Value lhsOp = ((AssignStmt) unit).getLeftOp();
			Value rhsOp = ((AssignStmt) unit).getRightOp();
			if (lhsOp instanceof Local) {
				assign((Local) lhsOp, rhsOp, inValue, outValue);		
			}
		}
		return outValue;
	}
	
	/**
	 * 计算方法targetMethod的入口值。
	 * 结果是：给calleeMethod进行入口初始化。
	 * */
	@Override
	public Map<Local, List<Value>> compute_Entry_OfMethod(
			Context<SootMethod, Unit, Map<Local, List<Value>>> context,
			SootMethod calledMethod, Unit unit, Map<Local, List<Value>> inValue) {
		// Initialise result to empty map
		Map<Local, List<Value>> entryValue = topValue();
		// Map arguments to parameters
		InvokeExpr ie = ((Stmt) unit).getInvokeExpr();
		for (int i = 0; i < ie.getArgCount(); i++) {
			Value arg = ie.getArg(i);
			if(calledMethod.getDeclaringClass().isPhantomClass())
				break;
			Local param = calledMethod.retrieveActiveBody().getParameterLocal(i);
			assign(param, arg, inValue, entryValue);
		}
		// And instance of the this local
		if (ie instanceof InstanceInvokeExpr) {
			Value instance = ((InstanceInvokeExpr) ie).getBase();
			
			if(!calledMethod.getDeclaringClass().isPhantomClass()){
				Local thisLocal = calledMethod.retrieveActiveBody().getThisLocal();
				assign(thisLocal, instance, inValue, entryValue);
			}
		}
		// Return the entry value at the called method
		return entryValue;
	}


	/**
	 * 调用该方法的条件：node满足isTargetMethod方法
	 * */
	@Override
	protected Map<Local, List<Value>> compute_Def_ByMethod(
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext,
			Unit node, Map<Local, List<Value>> in) {
		Map<Local, List<Value>> afterCallValue = copy(in);
	
		if(isAssignStmt(node)){
			AssignStmt as = (AssignStmt)node;
			Local l =(Local) as.getLeftOp();
			Value rightOp = as.getRightOp();
			if(afterCallValue.get(l)==null){
				List<Value> list = new ArrayList<Value>();
				list.add(rightOp);
				afterCallValue.put(l, list);
			}
			else afterCallValue.get(l).add(rightOp);
		}
		else if(isInvokeStmt(node)){
			JInvokeStmt is = (JInvokeStmt)node;
			InstanceInvokeExpr iie = (InstanceInvokeExpr)is.getInvokeExpr();
			Value value = is.getInvokeExpr();
			Local l =(Local) iie.getBase();
			//Local l = (Local)((ValueBox)is.getUseBoxes().get(0)).getValue();;
			if(afterCallValue.get(l)==null){
				List<Value> list = new ArrayList<Value>();
				list.add(value);
				afterCallValue.put(l, list);
			}
			else {
				afterCallValue.get(l).add(value);
			}
		}
		return afterCallValue;
	}

	//问题的由来：setClassName($r1,$r2) $r2是一个变量，
	//而在解析Intent时，他作为定义语句，需要将$r2的信息反映出来
//	public void getLocalOfParam(Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext,
//			Unit node,Value v){
//		//TODO 
// 		if(v instanceof InvokeExpr){
//			List<Value> args = ((InvokeExpr) v).getArgs();
//			for(Value param:args){
//				if(param instanceof StringConstant){
//					List<Value> list = currentContext.getValueBefore(node).get(param);
//				}
//			}
//		}
//	}
	
	@Override
	public Map<Local, List<Value>> copy(Map<Local, List<Value>> src) {
		return new HashMap<Local, List<Value>>(src);
	}

	/**
	 * 取并
	 * */
	@Override
	public Map<Local, List<Value>> meet(Map<Local, List<Value>> op1,
			Map<Local, List<Value>> op2) {
		Map<Local, List<Value>> result = new HashMap<Local, List<Value>>(op1);
		// Then add everything in the second operand, bottoming out the common keys with different values
		for (Local x : op2.keySet()) {
			if (op1.containsKey(x)) {
				// Check the values in both operands
				List<Value> c1 = op1.get(x);
				List<Value> c2 = op2.get(x);
				if (c1 != null && c1.equals(c2) == false) {
					// Set to non-constant
					if(c2!=null){
						c1.addAll(c2);
						result.put(x, c1);
					}
				}
			} else {
				// Only in second operand, so add as-is
				result.put(x, op2.get(x));
			}
		}
		return result;
	}

	@Override
	public  Map<Local, List<Value>> topValue() {
		return new HashMap<Local, List<Value>>();
	}

	@Override
	protected void addToAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		addTag(m, new FlagTag(FlagTag.DOING));
		analyzingContexts.put(m, currentContext);
		Contexts.v().getAnalyzingContexts().put(m, currentContext);
	}

	@Override
	protected void removeFromAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		removeTag(m, "FlagTag");
		analyzingContexts.remove(m);
		Contexts.v().getAnalyzingContexts().remove(m);
	}

	@Override
	protected void addToAnalyzedContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		removeFromAnalyzingContext(m, currentContext);
		addTag(m, new FlagTag(FlagTag.DONE));
		analyzedContexts.put(m, currentContext);
		Contexts.v().getAnalyzedContexts().put(m, currentContext);
	}

}
