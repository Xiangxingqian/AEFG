package aefg.intent;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import tags.ConditionTag;
import tags.Tagger;
import util.ArrayUtil;
import vasco.Context;
import view.ViewCondition;
import aefg.dataflowanalysis.AEFGLocalInterproceduralAnalysis;
import aefg.dataflowanalysis.AEFGLocalAnalysis;
import aefg.dataflowanalysis.Types;
import aefg.global.Global;
import analysis.methodAnalysis.InterMethodAnalysis;

import com.aefg.intenttoactivity.ITSPair;
import com.aefg.intenttoactivity.StartActivity;

/**
 * This class generates ITS(Intent to StartActivity) pair. 
 * */
public class IntentFlowAnalysis {
	
	static int intentNum;
	static int num;
	private SootMethod useMethod;
	InvokeStmt is;
	private  List<SootMethod> visitedMethods = new ArrayList<SootMethod>();

	/**
	 * @param method method contains is
	 * @param is is a startActivity invoking statement.
	 */
	public IntentFlowAnalysis(SootMethod method, InvokeStmt is) {
		this.useMethod = method;
		this.is = is;
	}
	
	public void genITS() {
		Value intent = is.getInvokeExpr().getArgs().get(0);
		List<SootMethod> defMethods = AEFGLocalAnalysis.parseDMBL((Local)intent, useMethod);
		for (SootMethod sm:defMethods) {
			if (!visitedMethods.contains(sm)) {
				visitedMethods.add(sm);
				List<Stmt> intentStmts = new IntentParser().parseIntentStmts(sm);
				Intent iBuilder = new Intent(intentStmts, sm);
				iBuilder.buildIntent();
				List<Intent> intents = iBuilder.getIntents();
				System.out.println("IntentStmts: "+sm+"----Size: "+intents.size());
				for(Intent concreteIntent:intents){
					if(concreteIntent!=null){
						StartActivity startActivity = new StartActivity(is);
						ITSPair itsPair = new ITSPair(concreteIntent, startActivity);
						
						ConditionTag intentTag = (ConditionTag)concreteIntent.getTag("ConditionTag");
						ConditionTag tag = (ConditionTag)startActivity.getTag("ConditionTag");
						
						//ÓÅÏÈ¿¼ÂÇintentµÄtag
						if(intentTag!=null&&ViewCondition.isValid(intentTag.getValues())){
							itsPair.addTag(intentTag);
						}
						else if(tag!=null&&ViewCondition.isValid(tag.getValues())){
							itsPair.addTag(tag);
						}
						Global.v().getITSPairs().add(itsPair);
					}
				}
			}
		}	
	}
	
	public Context<SootMethod, Unit, Map<Local, List<Value>>> doInterMethodAnalysis(){
		Type type = RefType.v("android.content.Intent");
		List<Type> types = ArrayUtil.toList(type);
		AEFGLocalInterproceduralAnalysis analysis = 
				new AEFGLocalInterproceduralAnalysis(useMethod, types, 1);
		Context<SootMethod, Unit, Map<Local, List<Value>>> context = 
				analysis.doInterprocedure(useMethod, new HashMap<Local, List<Value>>());
		return context;
	}
}
