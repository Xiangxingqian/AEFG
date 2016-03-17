package condition;

import soot.RefType;
import soot.Type;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.internal.AbstractBinopExpr;
import soot.util.Switch;

public class MyNeExpr extends MyExpr{
	

	public MyNeExpr(Value op1, Value op2) {
		super(op1, op2);
	}

	@Override
	public Type getType() {
		return RefType.v("MyNeExpr");
	}

	@Override
	public void apply(Switch sw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSymbol() {
		return " != ";
	}

	@Override
	public Object clone() {
		return null;
	}

}
