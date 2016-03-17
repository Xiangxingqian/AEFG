package condition;

import soot.Value;
import soot.jimple.IntConstant;

public class MyEqExpr extends MyExpr{

	public MyEqExpr(Value op1, Value op2) {
		super(op1, op2);
		// TODO Auto-generated constructor stub
	}

	public String getSymbol() {
		return " == ";
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//分析getId=9，提取9,专门提供给getId与getItemId使用
	public static int getInvoker(MyEqExpr mee){
		Value v1 = mee.getOp1();
		Value v2 = mee.getOp2();
		if(v2 instanceof IntConstant){
			int i = ((IntConstant)v2).value;
			if(((v1+"").contains("int getItemId")
					||(v1+"").contains("int getId"))
					&&i>-1){
				return i;
			}
		}
		return -1;
	}
}
