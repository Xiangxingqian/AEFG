package graphTemp;


public class TempEdge {
	
	private String sourceNode;
	private String targetNode;
	private TempConEdge tempConEdges;
	
	public TempEdge(String sourceNode, String targetNode,
			TempConEdge tempConEdges) {
		super();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.tempConEdges = tempConEdges;
	}
	
	public TempConEdge getTempConEdges() {
		return tempConEdges;
	}
	public void setTempConEdges(TempConEdge tempConEdges) {
		this.tempConEdges = tempConEdges;
	}
	public String getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	public String getTargetNode() {
		return targetNode;
	}
	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}
	
	@Override
	public String toString() {
		return "TempEdge [sourceNode=" + sourceNode + ", targetNode="
				+ targetNode + ", tempConEdges=" + tempConEdges + "]";
	}
	
	
	public TempEdge clone(){
		TempConEdge cloneConEdge = tempConEdges.clone();
		TempEdge tempEdge = new TempEdge(sourceNode,targetNode,cloneConEdge);
		return tempEdge;
	}

	public boolean equals(Object other){
	if(!(other instanceof TempEdge))return false;
	else{
		TempEdge o = (TempEdge) other;
        if( o.getSourceNode() != sourceNode ) return false;
        if( o.getTargetNode() != targetNode ) return false;
        if( o.getTempConEdges().equals(tempConEdges)) return false;
        return true;
	}
}
	
//	private SootMethod method ;
//	private SootClass className;
//	private SootMethod bodyMethod;	
//	private InvokeStmt invokeStmtContainsArgs;
//	
//	private List<TempConEdge> tempConEdges = new ArrayList<TempConEdge>();
//	
//	public List<TempConEdge> getEdges() {
//		return tempConEdges;
//	}
//
//	public void setEdges(List<TempConEdge> edgeStorages) {
//		this.tempConEdges = edgeStorages;
//	}
//
//	public void setInvokeStmt(InvokeStmt in){
//		this.invokeStmtContainsArgs = in;
//	}
//	
//	public InvokeStmt getInvokeStmt(){
//		return invokeStmtContainsArgs;
//	}
//	
//	//2
//	public void setMethod(SootMethod sootMethod){
//		this.method = sootMethod;
//	}
//	
//	public SootMethod getMethod(){
//		return method;
//	}
//
//	public void setClassName(SootClass className){
//		this.className = className;
//	}
//	
//	public SootClass getClassName(){
//		return className;
//	}
//	//5
//	public void setBodyMethod(SootMethod bodyMethod){
//		this.bodyMethod = bodyMethod;
//	}
//	
//	public SootMethod getBodyMethod(){
//		return bodyMethod;
//	}
//
//	public String toString(){
//		//InvokeStmt s = getInvokeStmt();
//		//MethodTag mt = (MethodTag) s.getTag("MethodTag");
//		return className.toString()+","+bodyMethod.getName()+","+method.getName()+","+
//				" 语句是："+getInvokeStmt()+" 在:"+Tagger.getMethodTag(getInvokeStmt());
//	}
//		
//
//	//1,2,3,4
//	public void setNodes(SootMethod bodyMethod, SootClass className, InvokeStmt is, SootMethod method){
//		this.bodyMethod = bodyMethod;
//		this.className = className;
//		this.method = method;
//		this.invokeStmtContainsArgs = is;
//	}
	
	
	//public TempEdge clone(){
//		TempEdge iss = new TempEdge();
//		iss.setNodes(bodyMethod, className,invokeStmtContainsArgs, method);
//		iss.setEdges(tempConEdges);
//		return iss;
	//}
	
	//public boolean equals(Object other){
//		if(!(other instanceof TempEdge))return false;
//		else{
//			TempEdge o = (TempEdge) other;
//	        if( o.getBodyMethod() != bodyMethod ) return false;
//	        if( o.getClassName() != className ) return false;
//	        if( o.getInvokeStmt()!=invokeStmtContainsArgs) return false;
//	        if( o.getEdges()!=tempConEdges)return false;
//	        return true;
//		}
	//}
	
//	public void removeInValidEdges(){
//		for(int i = 0;i<edgeStorages.size();i++){
//			EdgeStorage es = edgeStorages.get(i);
//			//Value invoker = es.getInvoker();
//			String string = es.getInvoker()+"";
//			
//			//当两个view都是有效时，比较俩值。有效view和有效con
//			int invokerId = splitNumbers(string);
//			List<DetailEdge> detailEdges = es.getDetailEdges();
//			
//			String conditionString = "";
//			for(DetailEdge de:detailEdges){
//				List<Value> condition = de.getCondition();
//				conditionString = conditionString+condition;
//			}
//			int conditionId = splitNumbers(conditionString);
//			if(invokerId>0&&conditionId>0){
//				if(invokerId!=conditionId){
//					edgeStorages.remove(i);
//					i--;
//				}
//			}
//		}
//	}
//	
//	public Integer splitNumbers(String string){
//		Pattern p = Pattern.compile("(\\d+)");      
//		Matcher m = p.matcher(string);  
//		List<Integer> integers = new ArrayList<Integer>();
//		while(m.find()){
//			String group = m.group();
//			int i = Integer.valueOf(group);
//			if(i>10000000)
//				integers.add(Integer.valueOf(group));
//		}
//		
//		int size = integers.size();
//		if(size>1){
//			throw new RuntimeException("One Statement has two view id");
//		}
//		else if(size==1){
//			return integers.get(0);
//		}
//		else{
//			return -1;
//		}
//	}
}
