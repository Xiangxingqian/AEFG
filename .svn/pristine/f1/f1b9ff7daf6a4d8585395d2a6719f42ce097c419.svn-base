package pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import singlton.SingltonFactory;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;

public class PairFactory {
	
	public PairFactory(SingltonFactory.G g){}	
	public static PairFactory v(){
		return SingltonFactory.v().getPairFactory();
	}
	
	Map<List<Stmt>, SootMethod> stmtsToMethod = new HashMap<List<Stmt>, SootMethod>(); 
	Map<Stmt, SootMethod> stmtToMethod = new HashMap<Stmt, SootMethod>();
	Map<Value, SootMethod> valueToMethod = new HashMap<Value, SootMethod>();
	
//	public void addValueToMethod(Value value ,SootMethod defineMethod ){
//		valueToMethod.put(value, defineMethod);
//	}
	
	public Map<Value, SootMethod> getValueToMethod(){
		return valueToMethod;
	}
	
//	public void addStmtsToMethod(List<Stmt> stmts ,SootMethod defineMethod ){
//		stmtsToMethod.put(stmts, defineMethod);
//	}
//	public void addStmtToMethod(Stmt stmt ,SootMethod defineMethod ){
//		stmtToMethod.put(stmt, defineMethod);
//	}
	
	public Map<List<Stmt>, SootMethod> getStmtsToMethod(){
		return stmtsToMethod;
	}
	
	public Map<Stmt, SootMethod> getStmtToMethod(){
		return stmtToMethod;
	}
	
}
