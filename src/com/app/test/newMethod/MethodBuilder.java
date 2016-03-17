package com.app.test.newMethod;

import java.util.ArrayList;
import java.util.List;

import com.app.test.Constants;

import soot.ArrayType;
import soot.Local;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;

public abstract class MethodBuilder extends Constants {

	public Chain<Unit> units;
	public JimpleBody body;
	public SootClass sc;
	public String subSignature;
	public SootMethod currentMethod;
	public RefType sc_Type;
	
	public MethodBuilder(SootClass sc, String subSignature){
		this.sc = sc;
		this.subSignature = subSignature;
		sc_Type = RefType.v(sc);
	}
	
	public SootMethod newMethod(){
		newMethodName();
		newMethodBody();
		sc.addMethod(currentMethod);
		return currentMethod;
	}
	
	protected void newMethodBody() {
		body = Jimple.v().newBody(currentMethod);
		body.getLocals();
		currentMethod.setActiveBody(body);
		units = body.getUnits();
		addUnits();
	}

	protected abstract void addUnits() ;

	//protected abstract void addLocals();

	protected abstract void newMethodName() ;

	public Local addLocal(String name,Type refType){
		Local newLocal = Jimple.v().newLocal(name, refType);
		body.getLocals().add(newLocal);
		return newLocal;
	}
	
	public Local addLocalArray(String name,Type refType){
		Local newLocal = Jimple.v().newLocal(name, ArrayType.v(refType, 1));
		body.getLocals().add(newLocal);
		return newLocal;
	}
	
	public IdentityStmt addIdentityStmt(Value local, Value identityRef){
		IdentityStmt stmt = Jimple.v().newIdentityStmt(local, identityRef);
		units.add(stmt);
		return stmt;
	}
	
	public AssignStmt addAssignStmt(Value leftOp, Value rightOp){
		AssignStmt stmt = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.add(stmt);
		return stmt;
	}
	
	public InvokeStmt addInvokeStmt(Value op){
		InvokeStmt stmt = Jimple.v().newInvokeStmt(op);
		units.add(stmt);
		return stmt;
	}
	
	public ReturnVoidStmt addReturnVoidStmt() {
		ReturnVoidStmt stmt = Jimple.v().newReturnVoidStmt();
		units.add(stmt);
		return stmt;
	}
	
	public ReturnStmt addReturnTypeStmt(Value v) {
		ReturnStmt stmt =Jimple.v().newReturnStmt(v);
		units.add(stmt);
		return stmt;
	}
	
	public IfStmt addIfStmt(Expr expr,Unit target){
		IfStmt ifStmt = Jimple.v().newIfStmt(expr, target);
		units.add(ifStmt);
		return ifStmt;
	}
	
	public GotoStmt addGotoStmt(Unit target){
		GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
		units.add(gotoStmt);
		return gotoStmt;
	}
	
	public List<Value> paramValues(){
		return new ArrayList<Value>();
	}
	
	public List<Type> paramTypes(){
		return new ArrayList<Type>();
	}
	
	public SootMethod getMethod() {
		return currentMethod;
	}
}
