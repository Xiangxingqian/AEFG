package singlton;

import graph.ActivityEventFlowGraph;
import graphTemp.TempGraph;

import com.aefg.dataflowanalysis.Contexts;
import com.aefg.intenttoactivity.ITS;
import pair.PairFactory;
import resource.ApkToRes;
import sootAnalysis.Instrumentor;
import test.Events;
import view.View;
import em.EM;

public class SingltonFactory {	
	
	private static SingltonFactory singlton = new SingltonFactory();
	private SingltonFactory(){}
	public static SingltonFactory v(){
		return singlton;
	}
	G g = new G();
	
	private Instrumentor instrumentor;
	public Instrumentor getInstrumentor(){
		if(instrumentor==null) instrumentor = new Instrumentor(g);
		return instrumentor;
	}
	
	private Global goa;
	public Global getGOA(){
		if(goa==null) goa = new Global(g);
		return goa;
	}
		
	private ITS its;
	public ITS getITS(){
		if(its==null) its = new ITS(g);
		return its;
	}
	
	private EM em;
	public EM getEM(){
		if(em==null) em = new EM(g);
		return em;
	}
	
	private ApkToRes atr;
	public ApkToRes getApkToRes(){
		if(atr==null) atr = new ApkToRes(g);
		return atr;
	}
	
	private View view;
	public View getView(){
		if(view==null) view = new View(g);
		return view;
	}
	
	private PairFactory pairFactory;
	public PairFactory getPairFactory(){
		if(pairFactory==null) pairFactory = new PairFactory(g);
		return pairFactory;
	}
	
	private TempGraph tempGraph;
	public TempGraph getTempGraph(){
		if(tempGraph==null) tempGraph = new TempGraph(g);
		return tempGraph;
	}
	
	private Contexts contexts;
	public Contexts getContexts(){
		if(contexts==null) contexts = new Contexts(g);
		return contexts;
	}
	
	private ActivityEventFlowGraph activityEventFlowGraph;
	public ActivityEventFlowGraph getActivityEventFlowGraph(){
		if(activityEventFlowGraph==null) activityEventFlowGraph = new ActivityEventFlowGraph(g);
		return activityEventFlowGraph;
	}
	
	private Events events;
	public Events getEvents(){
		if(events==null) events = new Events(g);
		return events;
	}
	
	public class G{
		private G() { }
	}
}
