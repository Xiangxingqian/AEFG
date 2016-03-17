package singlton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.Node;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.Pair;

//Global variables
public class Global {
	public Global(SingltonFactory.G g){	}
	public static Global v(){	
		return SingltonFactory.v().getGOA();	
	}

	private List<SootClass> sootClasses = new ArrayList<SootClass>();

	private List<Pair<InvokeStmt, String>> implicitITS = new ArrayList<Pair<InvokeStmt,String>>();
	
	private List<InvokeStmt> allRegistrarStmts = new ArrayList<InvokeStmt>();

	private List<InvokeStmt> allStartActivities = new ArrayList<InvokeStmt>();
	
	private Map<Local,List<SootMethod>> localToMethods = new HashMap<Local, List<SootMethod>>();
	private Map<Local,Stmt> localToStmt = new HashMap<Local, Stmt>();
	private Map<Local,List<Node>> localToAllocNodes = new HashMap<Local,List<Node>>();
	private Map<Local,List<InvokeStmt>> localToStartActivity = new HashMap<Local,List<InvokeStmt>>();
	private Map<List, UnitGraph> dominatorToUnitGraph = new HashMap<List, UnitGraph>();
	private Map<String,Integer> contentView = new HashMap<String, Integer>(); 
	List<Local> unReachingLocal = new ArrayList<Local>();
	private Map<List<Stmt>, List<SootMethod>> stmtsToMethods = new HashMap<List<Stmt>, List<SootMethod>>();
	private Map<InvokeStmt, List<Stmt>> startActivityToStmts = new HashMap<InvokeStmt, List<Stmt>>();
	
	/**
	 * merge localToStartActivity and StartActivityToStmts
	 * */
	public void merge(){
		Set entry = localToStartActivity.entrySet();
		Iterator iterator = entry.iterator();
		while(iterator.hasNext()){
			Map.Entry e = (Map.Entry)iterator.next();
			Local local = (Local)e.getKey();
			
			List li = localToMethods.get(local);
			List<InvokeStmt> list = (List<InvokeStmt>)e.getValue();
			for(InvokeStmt s:list){
				List<Stmt> list2 = startActivityToStmts.get(s);
				if(list2==null){
					break;
				}
				stmtsToMethods.put(startActivityToStmts.get(s),li);
			}
		}
	}
	
	public List<Pair<InvokeStmt, String>> getImplicitITS() {
		return implicitITS;
	}
	
	public List<SootClass> getSootClasses() {
		return sootClasses;
	}
	
	public List<Local> getUnReachingLocal(){
		return unReachingLocal;
	}
	
	public List<InvokeStmt> getAllRegistrarStmts() {
		return allRegistrarStmts;
	}
	public Map<List<Stmt>, List<SootMethod>> getStmtsToMethods(){
		return stmtsToMethods;
	}
	
	public Map<InvokeStmt, List<Stmt>> getStartActivityToStmts(){
		return startActivityToStmts;
	}
	
	public Map<Local,List<InvokeStmt>> getLocalToStartActivity(){
		return localToStartActivity;
	}
	
	public Map<Local, List<SootMethod>> getLocalToMethods(){
		return localToMethods;
	}
	
	public Map<Local, List<Node>> getLocalToAllocNodes(){
		return localToAllocNodes;
	}
	
	public Map<Local,Stmt> getLocalToStmt(){
		return localToStmt;
	}
	
	public Map<List, UnitGraph> getDominatorToUnitGraph(){
		return dominatorToUnitGraph;
	}
	
	public Map<String,Integer> getContentView(){
		return contentView;
	}

	public List<InvokeStmt> getAllStartActivities() {
		return allStartActivities;
	}	
	
}
