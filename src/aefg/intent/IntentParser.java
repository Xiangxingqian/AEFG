package aefg.intent;

import java.util.List;

import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import aefg.dataflowanalysis.AEFGLocalAnalysis;
import analysis.methodAnalysis.InterMethodAnalysis;

public class IntentParser {
	public List<Stmt> parseIntentStmts(SootMethod sm) {
		Local l = AEFGLocalAnalysis.parseLocalByType(sm, "android.content.Intent");
		List<Stmt> stmts = InterMethodAnalysis.getStmtsWithLocal(l,
				new ExceptionalUnitGraph(sm.retrieveActiveBody()));
		return stmts;
	}
}
