package view;

import java.util.List;

import condition.Condition;
import condition.MyEqExpr;

import soot.Value;
import soot.jimple.toolkits.callgraph.Edge;
import util.StringHandler;


public class ViewCondition extends Condition{
	
	public static int transformToInt(List<Value> values){
		if(values==null||values.size()==0){
			return -1;
		}
		else
			for(Value v:values){
				if(v instanceof MyEqExpr){
					MyEqExpr mee = (MyEqExpr)v;
					int i = MyEqExpr.getInvoker(mee);
					return i;
				}
			}
		throw new RuntimeException("No possible!");
	}
	
	public static boolean isInvoker(Edge e){
		List<Value> conditionByEdge = getConditionByEdge(e);
		int transformToInt = transformToInt(conditionByEdge);
		if(transformToInt>-1){
			return true;
		}
		else return false;
	}
	
	public static boolean isValid(List<Value> values){
		return transformToInt(values)!=-1;
	}
	
	public static String computeView(List<Value> values){
		for(Value v:values){
			Integer splitNumsForMenuItem = StringHandler.splitNumsForViews(v+"");
			if(splitNumsForMenuItem!=-1){
				return splitNumsForMenuItem+"";
			}
		}
		return "";
	}
	
	public static String computeViewForMenuItem(List<Value> values){
		for(Value v:values){
			Integer splitNumsForMenuItem = StringHandler.splitNumsForMenuItem(v+"");
			if(splitNumsForMenuItem!=-1){
				return splitNumsForMenuItem+"";
			}
		}
		return "";
	}
	
}
