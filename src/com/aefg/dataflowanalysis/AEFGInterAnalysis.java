package com.aefg.dataflowanalysis;

import java.util.List;

import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeStmt;
import tags.FlagTag;
import vasco.Context;
import vasco.ProgramRepresentation;
import entry.Entry;

public abstract class AEFGInterAnalysis<A> extends AEFGInterproceduralAnalysis<SootMethod, Unit, A>{

	public AEFGInterAnalysis(SootMethod m, List<Type> types) {
		super(m, types);
	}
	
	public AEFGInterAnalysis(SootMethod m, List<Type> types, int model) {
		super(m, types, model);
	}

	//若ds的左值类型是types或者String类型，返回true
	@Override
	protected boolean isAnalyzedType(DefinitionStmt ds, List<Type> types) {
		Type type = ds.getLeftOp().getType();
		if(type ==null){
			return false;
		}
		if(type.toString().equals("java.lang.String")){
			return true;
		}
		if(types.contains(type)){
			return true;
		}
		return false;
	}

	@Override
	protected SootMethod resolveMethod(Unit node) {
		if(isInvokeStmt(node)){
			InvokeStmt iStmt = (InvokeStmt)node;
			return iStmt.getInvokeExpr().getMethod();
		}
		return null;
	}

	@Override
	public boolean isTargetMethod(SootMethod m, List<Type> types) {
		String subSignature = m.getSubSignature();
		for(Type t:types){
			if(TypeAndRepresentation.getTypes().contains(t)){
				List<String> s = (List<String>)TypeAndRepresentation.getTypeToFormat().get(t);
				//System.out.println("c: "+c+" "+t+" "+subSignature);
				//return subSignature.equals(c);
				for(String c:s){
					if(subSignature.equals(c)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ProgramRepresentation<SootMethod, Unit> programRepresentation() {
		return new AEFGDefaultProgramRepresentation(m, Entry.getCallGraph());
	}
	
	/**
	 * 目的：m是否是一个应用类，当m是android，java或org开头返回false
	 * @param m 待分析的方法
	 * @return true 当且仅当 m是应用类
	 * */
	@Override
	public boolean isApplicationMethod(SootMethod m) {
		SootClass in = m.getDeclaringClass();
		return !(in.getName().startsWith("android"))&&!(in.getName().startsWith("java"))&&!(in.getName().startsWith("org"));
	}

	@Override
	public abstract A compute_Def_ByConstant(Context<SootMethod, Unit, A> context,
			Unit node, A inValue) ;

	
	@Override
	protected abstract A compute_Def_ByMethod(
			Context<SootMethod, Unit, A> currentContext, Unit node, A in) ;
	
	@Override
	public abstract A compute_Entry_OfMethod(Context<SootMethod, Unit, A> context,
			SootMethod targetMethod, Unit node, A inValue) ;

	@Override
	public A boundaryValue(SootMethod entryPoint){
		return topValue();
	}
	
	protected boolean paramContainsType(SootMethod method, List<Type> types2) {
		//Type type = ds.getLeftOp().getType();
		List<Type> parameterTypes = method.getParameterTypes();
		for(Type t:parameterTypes){
			if(types2.contains(t)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected void addTag(SootMethod m, FlagTag flagTag) {
		m.addTag(flagTag);
	}

	@Override
	protected void removeTag(SootMethod m, String tagName) {
		m.removeTag(tagName);
	}

	@Override
	public abstract A copy(A src) ;

	@Override
	public abstract A meet(A op1, A op2) ;
	
	@Override
	public abstract A topValue() ;
	

	

}
