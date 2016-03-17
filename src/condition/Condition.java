package condition;

import java.util.ArrayList;
import java.util.List;

import singlton.Global;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JEqExpr;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.SimpleDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import util.LocalAnalysis;
import util.LocalValue;

public class Condition {
	
	//private static ConditionAnalysis con;
//	public ConditionAnalysis(SingltonFactory.G g){	}
//	public static ConditionAnalysis v(){
//		return SingltonFactory.v().getCondition();
//	}
	
	public static List<Value> getConditionOfUnit(SootMethod src,Stmt srcStmt) {
		List dominator = Condition.getDominator(srcStmt,src);
		List<Value> condition = getCondition(dominator);
		return condition;
	}
	
	/**
	 * 获得sm中支配unit的语句
	 * @param unit Stmt to be analyzed
	 * @param sm SootMethod contains object.
	 * @return list A List of Stmts. 
	 * */
	public static List getDominator(Object unit,SootMethod sm){
 		SimpleDominatorsFinder sdf = new SimpleDominatorsFinder(
				new ExceptionalUnitGraph(sm.retrieveActiveBody()));
	 	Global.v().getDominatorToUnitGraph().put(sdf.getDominators(unit), (UnitGraph)sdf.getGraph());
	 	return sdf.getDominators(unit);		
 	}
	
	/**
	 * stmts中含有条件语句，本方法得到条件语句的条件表达式,仅仅考虑数字
	 * @param stmts A list of stmts who contain condition Exprs.
	 * @return List<Value> A list of MyExpr={1.MyEqExpr 2.MyNeExpr}
	 * */
	public static List<Value> getCondition(List<Stmt> stmts){
		UnitGraph ug = Global.v().getDominatorToUnitGraph().get(stmts);
		//LocalValue llv = new LocalValue(ug.getBody().getMethod());
		List<Value> conditionList = new ArrayList<Value>();
		for(int i = 0; i<stmts.size();i++){
			Stmt sUnit = stmts.get(i);
			
			//当SUnit是ifStmt时
			if(sUnit instanceof IfStmt){	
				IfStmt iStmt = (IfStmt)sUnit;
				Stmt ifTarget = iStmt.getTarget();				
				Value condition = iStmt.getCondition();							
				ConditionExpr ce = (ConditionExpr)condition;				
				Value v = LocalAnalysis.getDefValue_IF(ce.getOp1(), iStmt, ug);
				Value rigthOp = ce.getOp2();
				Value vv;
				if(rigthOp instanceof Local){
					 vv = LocalAnalysis.getDefValue_IF(ce.getOp2(), iStmt, ug);
				}
				else {
					vv = rigthOp;
				}				
				if(stmts.contains(ifTarget)){				
//					if(ce.getSymbol().equals(" != ")){
////						MyNeExpr mee = new MyNeExpr(v,vv);
////						conditionList.add(mee);				
//					}	
					if(ce.getSymbol().equals(" == ")){
						MyEqExpr mee = new MyEqExpr(v,vv);
						
						int t = MyEqExpr.getInvoker(mee);
						if(t!=-1)
							conditionList.add(mee);		
					}
				}	
				else {				
//					if(condition instanceof EqExpr){																	
////						MyNeExpr mee = new MyNeExpr(v,vv);
////						conditionList.add(mee);								
//					}
					if(condition instanceof NeExpr) {			
						MyEqExpr mee = new MyEqExpr(v,vv);
						int t = MyEqExpr.getInvoker(mee);
						if(t!=-1)
							conditionList.add(mee);
					}
				}
			}
			
			//当sUnit是LookupSwitch时
			if(sUnit instanceof LookupSwitchStmt){
				LookupSwitchStmt lss = (LookupSwitchStmt)sUnit;
				Value keyValue = lss.getKey();
				Value v = LocalAnalysis.getDefValue_IF(keyValue, lss, ug);
				List<IntConstant> lookupValues = lss.getLookupValues();
				boolean flag = false;
				for(int t = 0; t < lookupValues.size(); t++)
		        {
		           if(stmts.contains(lss.getTarget(t))){
		        	   EqExpr ee = new JEqExpr(keyValue, lookupValues.get(t));
		        	   MyEqExpr mee = new MyEqExpr(v,ee.getOp2());
		        	   int t1 = MyEqExpr.getInvoker(mee);
		        	   if(t1!=-1)
		        		   conditionList.add(mee);
		        	   flag = true;
		        	   break;
		           }
		        }				
				if(!flag){
					if(stmts.contains(lss.getDefaultTarget())){					
						EqExpr ee = new JEqExpr(keyValue, IntConstant.v(-1));
			        	MyEqExpr mee = new MyEqExpr(v,ee.getOp2());
			        	int t = MyEqExpr.getInvoker(mee);
						if(t!=-1)
							conditionList.add(mee);
					}
					else 
						continue;
				}
			}			
		}
		return conditionList;
	}
	
	public static List<Value> getConditionByEdge(Edge e) {
		return getConditionOfUnit(e.src(), e.srcStmt());
	}
	
}
