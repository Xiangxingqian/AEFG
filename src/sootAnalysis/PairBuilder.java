//package sootAnalysis;
//
//import graph.DetailEdge;
//import itsResolver.ITS;
//import itsResolver.ITSPair;
//import itsResolver.StartActivity;
//import itsResolver.intent.Intent;
//
//import java.util.List;
//
//import singlton.Global;
//import soot.SootMethod;
//import soot.jimple.InvokeStmt;
//import soot.jimple.Stmt;
//import soot.jimple.toolkits.callgraph.Edge;
//import soot.toolkits.scalar.Pair;
//import tags.Tagger;
//import analysis.methodAnalysis.InterMethodAnalysis;
//
////构造显示和隐式intent，表现形式：List<ExplicitPairs> 和List<ImplicitPairs>
//public class PairBuilder {
//	public void buildExplicitIntent(){
//		//构造时，暂时不考虑TM为startActivity这个方法。
//		//TODO
//		List<ITSPair> list = ITS.v().getITSPairs();
//		for(int t=0;t<list.size();t++){			
//			Pair<Intent, StartActivity> p = list.get(t);
//			Intent intent = p.getO1();
//			InvokeStmt intentStmt = intent.getIntentWithActivity();
//			
//			StartActivity startActivity = p.getO2();
//			InvokeStmt is = startActivity.getValue();
//			SootMethod tgtM = Tagger.getMethodTag(is);
//			
//			if(tgtM.getName().contains("startActivity")){
//				continue;
//			}
//			
//			//System.out.println(t+"PB: "+tgtM+" "+edges+" "+intentStmt);
//			Edge SAE = InterMethodAnalysis.getStartActivityEdge(tgtM,is);			
//			if(SAE==null){
//				continue;			
//			}
//			DetailEdge de2 = DetailEdge.edgeToDetailEdge(SAE);
//			
//			List<Edge> edges = InterMethodAnalysis.getEntryEdges(tgtM);	
//			
//			//当TM不是入口方法时
//			if(edges.size()>0){
//				for(Edge e:edges){		
//					DetailEdge de1 = DetailEdge.edgeToDetailEdge(e);
//					ExplicitPair pp = new ExplicitPair(de1,de2);
//					pp.setIntentStmt(intentStmt);					
//					SimplePairs.v().getExplicitPairs().add(pp);					
//				}
//			}
//			
//			//当非入口方法是入口方法时
//			else{
//				ExplicitPair pp = new ExplicitPair(de2,de2);
//				pp.setIntentStmt(intentStmt);
//				SimplePairs.v().getExplicitPairs().add(pp);
//			}		
//		}
//	}
//	
//	public void buildImplicitIntent(){
//		List<Pair<InvokeStmt, String>> l =  Global.v().getImplicitITS();
//		//System.out.println(l.size());
//		for(Pair<InvokeStmt, String> p:l){
//			InvokeStmt s = p.getO1();
//			//MethodTag mTag = (MethodTag)(s.getTag("MethodTag"));
//			SootMethod sm = Tagger.getMethodTag(s);
//			
//			if(sm.getName().contains("startActivity")){
//				continue;
//			}
//			String activity = p.getO2();
//			List<Edge> edges = InterMethodAnalysis.getEntryEdges(sm);	
//			System.out.println(PairBuilder.class+" method: "+sm+ "edges size: "+edges.size());
//			if(edges.size()>0){
//				for(Edge edge:edges){
//					DetailEdge de1 = DetailEdge.edgeToDetailEdge(edge);
//					ImplicitPair iPair = new ImplicitPair(de1, activity);
//					SimplePairs.v().getImplicitPairs().add(iPair);
//				}
//			}
//			else{
//				Edge edg = InterMethodAnalysis.getStartActivityEdge(sm, s);
//				if(edg==null) continue;
//				DetailEdge de1 = DetailEdge.edgeToDetailEdge(edg);
//				ImplicitPair iPair = new ImplicitPair(de1, activity);
//				SimplePairs.v().getImplicitPairs().add(iPair);
//			}
//		}
//	}
//}
