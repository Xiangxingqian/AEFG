package tags;

import soot.SootMethod;
import soot.jimple.Stmt;
import soot.tagkit.Tag;

/**
 * We cannot trace a statement to its belonging method through Soot. Thus, 
 * we define a MethodTag to record the belonging method of a statement.    
 * */
public class MethodTag implements Tag{

	SootMethod sm;
	public MethodTag(SootMethod sm) {
		this.sm = sm;
	}
	@Override
	public String getName() {
		return "MethodTag";
	}

	@Override
	public byte[] getValue() {
        throw new RuntimeException( "AnnotationTag has no value for bytecode" );
    }
	
	public SootMethod getMethod(){
		return sm;
	}
	
}
