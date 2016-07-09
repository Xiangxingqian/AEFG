package aefg.aefgraph;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraph<A,N>{
	List<A> edges = new ArrayList<A>();

	public void addEdge(A a){
		edges.add(a);
	}
	
	public void removeEdge(A a){
		edges.remove(a);
	}
	
	public List<A> getEdges(){
		return edges;
	}
	
	public abstract List<A> getEntryEdges();
	public abstract boolean isEntryEdge(A de);
	
	public abstract List<A> findEdgeByTgt(N n);
	public abstract List<A> findEdgeBySrc(N n);
	
	public abstract List<List<A>> searchEdgesStartWithE(A e);
}
