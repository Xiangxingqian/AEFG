package condition;

import java.util.ArrayList;
import java.util.List;

import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.PrecedenceTest;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.internal.AbstractBinopExpr;
import soot.util.Switch;


public abstract class MyExpr implements Expr{

	Value op1;
	Value op2;
	public MyExpr(Value op1,Value op2){
		this.op1 = op1;
		this.op2 = op2;
	}

	public Value getOp1(){
        return op1;
	}

	public Value getOp2(){
		return op2;
	}


	public void setOp1(Value op1){
	    this.op1 = op1;
	}

	public void setOp2(Value op2){
	    this.op2 = op2;
	}
	
	abstract protected String getSymbol();   
	abstract public Object clone();
	public String toString(){	
	    String leftOp = op1.toString(), rightOp = op2.toString();
	    return leftOp + getSymbol() + rightOp;
	}

	@Override
	public List<ValueBox> getUseBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toString(UnitPrinter up) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean equivTo(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int equivHashCode() {
		// TODO Auto-generated method stub
		return 0;
	}	    
}