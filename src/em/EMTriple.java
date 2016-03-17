package em;

import soot.SootMethod;
import tags.ConditionTag;
import util.Triples;

import com.aefg.intenttoactivity.ITSPair;

public class EMTriple extends Triples<SootMethod, SootMethod, ITSPair>{

	private ConditionTag condition;
	
	public EMTriple(SootMethod em, SootMethod tm, ITSPair c) {
		super(em, tm, c);
	}
	
	public void addTag(ConditionTag unit){
		this.condition = unit;
	}
	
	public ConditionTag getTag(){
		return condition;
	}
	
	public boolean hasTag(){
		return condition!=null;
	}

}
