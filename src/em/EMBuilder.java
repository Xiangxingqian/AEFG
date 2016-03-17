package em;

import java.util.ArrayList;
import java.util.List;

import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.Edge;
import tags.ConditionTag;
import analysis.methodAnalysis.EntryMethodsAnalysis;

import com.aefg.intenttoactivity.ITSPair;

import condition.Condition;

public class EMBuilder {
	private List<SootMethod> entryMethods = new ArrayList<SootMethod>();
	private SootMethod tm;

	public EMBuilder(SootMethod tm) {
		this.tm = tm;
	}
	
	public void build(ITSPair itsPair){
		List<Edge> entryEdges = EntryMethodsAnalysis.getEntryEdges(tm);
		if(entryEdges.size()==0){
			EMTriple emTriple = new EMTriple(tm, tm, itsPair);
			EM.v().getEMTriples().add(emTriple);
			entryMethods.add(tm);
		}
		else 
			for(Edge edge:entryEdges){
				SootMethod src = edge.src();
				Stmt srcStmt = edge.srcStmt();
				List<Value> conditionOfUnit = Condition.getConditionOfUnit(src, srcStmt);
				
				ConditionTag ct = new ConditionTag(conditionOfUnit);
				
				//ConditionTag tag = itsPair.getTag();
				
				EMTriple emTriple = new EMTriple(src, tm, itsPair);
				emTriple.addTag(ct);
				
	//			if(itsPair.hasTag()){
	//				emTriple.addTag(tag);
	//			}
	//			else if(ViewCondition.isValid(ct.getValues())){
	//				emTriple.addTag(ct);
	//			}
				
				EM.v().getEMTriples().add(emTriple);
				entryMethods.add(src);
			}
	}
	
	public List<SootMethod> getEntryMethods() {
		return entryMethods;
	}
}
