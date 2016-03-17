package tags;

import java.util.Iterator;
import java.util.List;

import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
import soot.tagkit.IntegerConstantValueTag;

public class Tagger {

	public static int getLineNumberTag(Stmt s) {
		Object tag = getTag(s, IntegerConstantValueTag.class);
		IntegerConstantValueTag itag = (IntegerConstantValueTag)tag;
		if(itag==null) return -1;
		else
			return itag.getIntValue();
	}
	
	public static SootMethod getMethodTag(Stmt s) {
		Object tag = getTag(s, MethodTag.class);
		MethodTag mt = (MethodTag)tag;
		if(mt==null) return null;
		else
			return mt.getMethod();
	}
	
	public static ConditionTag getConditionTag(Stmt s) {
		Object tag = getTag(s, ConditionTag.class);
		ConditionTag ct = (ConditionTag)tag;
		if(ct==null) return null;
		else
			return ct.getConditionTag();
	}
	
	public static List<Stmt> getRefStmtsTag(Stmt s) {
		Object tag = getTag(s, RefStmtsTag.class);
		RefStmtsTag ct = (RefStmtsTag)tag;
		if(ct==null) return null;
		else
			return ct.getRefStmts();
	}
	
	public static Value getValueTag(Stmt s) {
		Object tag = getTag(s, ValueTag.class);
		ValueTag vt = (ValueTag)tag;
		if(vt==null) return null;
		else
			return vt.getValueK();
	}
	
	public static int getFlagTag(SootMethod sm){
		Object taObject = getTag(sm, FlagTag.class);
		FlagTag flagTag = (FlagTag)taObject;
		if(flagTag==null) return FlagTag.UNDO;
		else return flagTag.getValues();
	}
	
	private static Object getTag(Stmt s,Class<? extends Object> class1) {
		Iterator ti = s.getTags().iterator();
		while (ti.hasNext()) {
			Object o = ti.next();
			Class<? extends Object> class2 = o.getClass();
			if (class1.equals(class2)) 
				return o;
		}
		return null;
	}
	
	private static Object getTag(SootMethod sm,Class<? extends Object> class1) {
		Iterator ti = sm.getTags().iterator();
		while (ti.hasNext()) {
			Object o = ti.next();
			Class<? extends Object> class2 = o.getClass();
			if (class1.equals(class2)) 
				return o;
		}
		return null;
	}
}
