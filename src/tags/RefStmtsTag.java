package tags;

import java.util.ArrayList;
import java.util.List;

import soot.SootMethod;
import soot.jimple.Stmt;
import soot.tagkit.Tag;

public class RefStmtsTag implements Tag {
	List<Stmt> ssList = new ArrayList<Stmt>();
	
 	public RefStmtsTag(List<Stmt> ssList) {
		this.ssList = ssList;
	}
	@Override
	public String getName() {
		return "RefStmtsType";
	}

	
	@Override
	public byte[] getValue() {
        throw new RuntimeException( "AnnotationTag has no value for bytecode" );
    }
	
	public List<Stmt> getRefStmts(){
		return ssList;
	}
}
