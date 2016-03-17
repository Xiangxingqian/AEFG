package com.app.test.modifyMethod;

import java.util.ArrayList;
import java.util.List;

import soot.ArrayType;
import soot.Body;
import soot.Local;
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
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;

import com.app.test.Constants;

public abstract class MethodModifier extends Constants{
	SootClass currentClass;
	SootMethod currentMethod;
	Body body;
	Chain<Unit> units;
	public MethodModifier(SootClass currentClass, SootMethod currentMethod) {
		this.currentClass = currentClass;
		this.currentMethod = currentMethod;
		body = currentMethod.retrieveActiveBody();
		units = body.getUnits();
		
	}
	
	public SootMethod modifyMethod(){
		addLocals();
		insertUnits();
		return currentMethod;
	}
	
	protected abstract void insertUnits() ;

	protected abstract void addLocals();

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
	
	public IdentityStmt insertIdentityStmt(Value local, Value identityRef,Unit u){
		IdentityStmt stmt = Jimple.v().newIdentityStmt(local, identityRef);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	public AssignStmt insertAssignStmt(Value leftOp, Value rightOp,Unit u){
		AssignStmt stmt = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	public InvokeStmt insertInvokeStmt(Value op,Unit u){
		InvokeStmt stmt = Jimple.v().newInvokeStmt(op);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	public ReturnVoidStmt insertReturnVoidStmt(Unit u) {
		ReturnVoidStmt stmt = Jimple.v().newReturnVoidStmt();
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	public ReturnStmt insertReturnTypeStmt(Value v,Unit u) {
		ReturnStmt stmt =Jimple.v().newReturnStmt(v);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	public IfStmt insertIfStmt(Expr expr,Unit target,Unit u){
		IfStmt ifStmt = Jimple.v().newIfStmt(expr, target);
		units.insertBefore(ifStmt, u);
		return ifStmt;
	}
	
	public GotoStmt insertGotoStmt(Unit target,Unit u){
		GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
		units.insertBefore(gotoStmt, u);
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
