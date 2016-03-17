package graphTemp;

import java.util.ArrayList;
import java.util.List;

import singlton.SingltonFactory;

public class TempGraph {
	public TempGraph(SingltonFactory.G g){	
	}
	
	public static TempGraph v(){
		return SingltonFactory.v().getTempGraph();
	}
	
	private List<TempEdge> tempEdges = new ArrayList<TempEdge>();

	public List<TempEdge> getTempEdges() {
		return tempEdges;
	}
}
