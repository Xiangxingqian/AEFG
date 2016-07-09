package sootAnalysis;

import em.EM;
import em.EMTriple;
import entryPointCreator.AndroidEntryPointConstants;
import graphTemp.TempConEdge;
import graphTemp.TempEdge;
import graphTemp.TempGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.StringConstant;
import soot.tagkit.IntegerConstantValueTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.Chain;
import tags.MethodTag;
import tags.Tagger;
import util.ClassAnalysis;
import vasco.Context;
import view.ViewBuilder;
import view.ViewCondition;
import aefg.entry.AndroidConstant;
import aefg.global.Global;
import aefg.global.IGlobal;
import aefg.intent.IntentFlowAnalysis;
import analysis.methodAnalysis.InterMethodAnalysis;
import analysis.methodAnalysis.IntraMethodAnalysis;

import com.aefg.intenttoactivity.ITSPair;
import com.android.event.EventContants;
import component.Component;
import component.ComponentHandler;

public class AndroidAnalysis implements IGlobal {

	private int count = 0;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private List<InvokeStmt> startActivities = Global.v()
			.getStartActivityStmts();
	public List<TempEdge> tempEdges = TempGraph.v().getTempEdges();
	List<SootClass> allClasses = new ArrayList<SootClass>();

	/**
	 * entry of global analysis
	 * */
	public void analyze() {
		allClasses = getApplicationClasses();
		appendToGlobal();

		// 1. get all the startActivity statements.
		parseStartActivityInClass(allClasses);

		// 2. generate <intent, startActivity> pair.
		genITS();
		System.out.println("ITS has been done! Size is: "
				+ Global.v().getITSPairs().size());

		// 3. replying on CallGraph, get entry method of startActivity's
		// belonging method.
		EM.v().genEm();
		System.out.println("EM has been done! Size is: "
				+ EM.v().getEMTriples().size());

		// 4.
		analyzeEM();
		logger.info("Instrumentor analysis finishes.", getClass());
	}

	/**
	 * Extract startActivity statements in sootclasses.
	 * @param appClasses
	 */
	private void parseStartActivityInClass(List<SootClass> appClasses) {
		for (SootClass sootClass : appClasses) {
			if (!sootClass.isInterface()) {
				for (SootMethod sm : sootClass.getMethods()) {
					if (sm.isConcrete()) {
						parseStartActivityInMethod(sm);
					}
				}
			}
		}
	}

	/**
	 * Extract startActivity statements in sootMethod.
	 * @param sootMethod
	 */
	private void parseStartActivityInMethod(SootMethod sootMethod) {
		Body body = sootMethod.retrieveActiveBody();
		Chain<Unit> chain = body.getUnits();
		int index = 0;
		for (Unit unit : chain) {
			index++;
			if (unit instanceof InvokeStmt) {
				InvokeStmt s = (InvokeStmt) unit;
				InvokeExpr ie = s.getInvokeExpr();
				SootMethod method = ie.getMethod();
				String signature = method.getSignature();
				if (signature.equals(AndroidConstant.Signature.STARTACTIVITY)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITYFORRESULT)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITY_CONTENT)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITYFORRESULT_CONTENT)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITYFORRESULT_FRAGMENT)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITY_FRAGMENT)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITY_FRAGMENT_v4)
						|| signature
								.equals(AndroidConstant.Signature.STARTACTIVITYFORRESULT_FRAGMENT_v4)) {
					s.addTag(new MethodTag(sootMethod));
					s.addTag(new IntegerConstantValueTag(index));
					startActivities.add(s);
				}
			}
		}
	}

	/**
	 * generate ITS pairs. ITS is short for Intent to StartActivity.
	 * */
	public void genITS() {
		System.out.println(startActivities.size());
		for (InvokeStmt is : startActivities) {
			SootMethod method = Tagger.getMethodTag(is);
			System.out.println(is+" "+method);
			IntentFlowAnalysis ifa = new IntentFlowAnalysis(method, is);
			Context<SootMethod, Unit, Map<Local, List<Value>>> context = ifa.doInterMethodAnalysis();
			Map<Local, List<Value>> valueBefore = context.getValueBefore(is);
			Value bValue = is.getInvokeExpr().getArg(0);
			List<Value> list = valueBefore.get(bValue);
			System.out.println("151: "+"Value: "+list+" Activity:" +method.getDeclaringClass());
		}
	}

	/**
	 * get app application classes, excluding android, java and org classes.
	 * */
	private List<SootClass> getApplicationClasses() {
		Chain<SootClass> ch = Scene.v().getClasses();
		List<SootClass> allClasses = new ArrayList<SootClass>();
		for (SootClass s : ch) {
			if (s.isConcrete()) {
				if (!s.getName().startsWith("android")
						&& !s.getName().startsWith("java")
						&& !s.getName().startsWith("org")) {
					allClasses.add(s);
				}
			}
		}
		return allClasses;
	}

	public void test() {
		System.out.println("Start to do test");
		SootClass sootClass = Scene.v().getSootClass(
				"com.google.android.gm.welcome.o");
		SootMethod method = sootClass.getMethodByName("<init>");
		System.out.println("开始分析:" + method + "\n直接前驱：");
		for (SootMethod sm : InterMethodAnalysis.getSourcesMethods(method)) {
			System.out.println(method + " sources: " + sm);
		}
		System.out.println("所有前驱:");
		for (SootMethod sm : InterMethodAnalysis.getAllPreviousMethods(method)) {
			System.out.println(method + " sources: " + sm);
		}

		SootClass sootClass2 = Scene.v().getSootClass("com.android.mail.ui.F");
		SootMethod method2 = sootClass2.getMethodByName("<init>");
		System.out.println("开始分析:" + method2 + "\n直接前驱：");
		for (SootMethod sm : InterMethodAnalysis.getSourcesMethods(method2)) {
			System.out.println(method2 + " sources: " + sm);
		}
		System.out.println("所有前驱:");
		for (SootMethod sm : InterMethodAnalysis.getAllPreviousMethods(method2)) {
			System.out.println(method2 + " sources: " + sm);
		}
		SootClass sootClass3 = Scene.v().getSootClass("com.android.mail.ui.bb");
		SootMethod method3 = sootClass3
				.getMethod("void <init>(com.android.mail.ui.ConversationViewFragment,byte)");
		System.out.println("开始分析:" + method3 + "\n直接前驱：");
		for (SootMethod sm : InterMethodAnalysis.getSourcesMethods(method3)) {
			System.out.println(method3 + " sources: " + sm);
		}
		System.out.println("所有前驱:");
		for (SootMethod sm : InterMethodAnalysis.getAllPreviousMethods(method3)) {
			System.out.println(method3 + " sources: " + sm);
		}
	}

	// 分析em
	public void analyzeEM() {
		List<EMTriple> emTriples = EM.v().getEMTriples();
		for (EMTriple em : emTriples) {
			SootMethod sm = em.getO1();
			ITSPair itsPair = em.getO3();
			List<Value> itsValues = new ArrayList<Value>();
			List<Value> emValues = new ArrayList<Value>();
			if (itsPair.hasTag()) {
				itsValues = itsPair.getTag().getValues();
			}
			if (em.hasTag()) {
				emValues = em.getTag().getValues();
			}
			emValues.addAll(itsValues);
			String activity = itsPair.getO1().getActivity();
			executePhaseOne(sm, emValues, activity);
		}
	}

	/**
	 * 执行阶段1分析，分析目的：对入口函数分类讨论。
	 * 
	 * @param sootMethod
	 *            是一些入口函数，或叫CallBack
	 * @param view
	 *            条件信息
	 * @param targetActivity
	 *            目标活动 次数是：em的大小
	 * */
	private void executePhaseOne(SootMethod sootMethod, List<Value> view,
			String targetActivity) {
		String signature = sootMethod.getSubSignature();
		System.out.println(count++ + " " + sootMethod + " TargetNode: "
				+ targetActivity);
		if (signature.contains(AndroidEntryPointConstants.BROADCAST_ONRECEIVE)
				|| signature.contains("void run()")
				|| signature
						.contains(AndroidEntryPointConstants.ACTIVITY_ONRESUME)) {
			return;
		} else if (signature
				.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATE)
				|| signature
						.contains(AndroidEntryPointConstants.ACTIVITY_ONSTART)) {
			buildTempGraphAuto(sootMethod, "onCreate", view, targetActivity);
			return;
		} else if (signature
				.contains(AndroidEntryPointConstants.ACTIVITY_ONITEMCLICK)
				|| signature
						.contains(AndroidEntryPointConstants.ACTIVITY_ONLISTITEMCLICK)) {
			buildTempGraphAuto(sootMethod, "ItemsList", view, targetActivity);
			return;
		} else if (signature
				.contains(AndroidEntryPointConstants.ACTIVITY_ONOPTIONSITEMSELECTED)
				|| signature
						.contains(AndroidEntryPointConstants.ACTIVITY_ONMENUITEMSELECTED)) {
			// TODO
			buildTempGraphForMenuItem(sootMethod, "OptionMenu", view,
					targetActivity);
			return;
		} else if (signature
				.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATECONTEXTMENU)
				|| signature
						.contains(AndroidEntryPointConstants.ACTIVITY_ONCONTEXTITEMSELECTED)) {
			buildTempGraphForMenuItem(sootMethod, "ContextMenu", view,
					targetActivity);
			return;
		} else if (EventContants.getCallbackAndListener()
				.containsKey(signature)) {
			String listener = EventContants.getCallbackAndListener().get(
					signature);
			String listenerMethod = EventContants
					.getListenerAndListenerMethod().get(listener);
			deepAnalyze(sootMethod, view, targetActivity, listenerMethod);
		}
		// //done
		// else if
		// (signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICK)) {
		// deepAnalyze(sootMethod,view,targetActivity,AndroidEntryPointConstants.SETONCLICKLISTENER);
		// return;
		// }
		// else if (signature.contains("onPreferenceTreeClick")) {
		// System.out.println("onPreferenceTreeClick happens!");
		// deepAnalyze(sootMethod,view,targetActivity,"setOnPreferenceClickListener");
		// return;
		// }
		// else if(signature.contains("onContextItemSelected")){
		// deepAnalyze(sootMethod,view,targetActivity,AndroidEntryPointConstants.SETONCONTEXTLISTENER);
		// return;
		// }
		// 当为onClickDialog时，onClickDialog对应的是showDialog，因而需要求showDialog所在的方法
		else if (signature
				.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICKDIALOG)) {
			// TODO
			SootClass sc = sootMethod.getDeclaringClass();
			sc = ClassAnalysis.getOutClass(sc);
			for (SootMethod sm : sc.getMethods()) {
				IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
				boolean b = ma.containsStmt("showDialog");
				if (b) {
					executePhaseOne(sm, view, targetActivity);
				}
			}
			return;
		} else if (sootMethod.getDeclaringClass().getName().contains("$")) {
			System.out.println(AndroidAnalysis.class + " 318!!$$$$$$$$$$$");
			return;
		} else {
			return;
		}
	}

	private void buildTempGraphAuto(SootMethod sootMethod, String invoker,
			List<Value> values, String targetActivity) {
		String view = ViewCondition.computeView(values);
		buildTempGraph(sootMethod, invoker, targetActivity, view);
	}

	private void buildTempGraphForMenuItem(SootMethod sootMethod,
			String invoker, List<Value> values, String targetActivity) {
		String view = ViewCondition.computeViewForMenuItem(values);
		buildTempGraph(sootMethod, invoker, targetActivity, view);
		// SootClass sootClass = sootMethod.getDeclaringClass();
		// Component component = Component.getComponent(sootClass);
		// String componentName = sootClass.getName();
		// StringConstant v = StringConstant.v(invoker+view);
		// TempConEdge tempConEdge = new TempConEdge(componentName,component,v);
		// TempEdge tempEdge = new TempEdge(componentName,
		// targetActivity,tempConEdge);
		// TempGraph.v().getTempEdges().add(tempEdge);
	}

	private void buildTempGraph(SootMethod sootMethod, String invoker,
			String targetActivity, String view) {
		SootClass sootClass = sootMethod.getDeclaringClass();
		Component com = Component.parseComponent(sootClass);
		ComponentHandler ch = new ComponentHandler(sootClass);
		ch.build();
		String activity = ch.getActivity();
		Map<String, Component> map = ch.getMap();

		if (activity == "") {
			System.err.println("Class: " + sootClass + " Type: " + com
					+ " isn't attached to an Activity!");
			return;
		}
		// Component component = Component.getComponent(sootClass);
		// String componentName = sootClass.getName();
		StringConstant v = StringConstant.v(invoker + view);
		TempConEdge tempConEdge = new TempConEdge(map, v);
		TempEdge tempEdge = new TempEdge(activity, targetActivity, tempConEdge);
		TempGraph.v().getTempEdges().add(tempEdge);
	}

	private void deepAnalyze(SootMethod sootMethod, List<Value> values,
			String targetActivity, String listenerMethodName) {
		String view = ViewCondition.computeView(values);
		SootClass listener = sootMethod.getDeclaringClass();
		ViewBuilder viewBuilder = new ViewBuilder(listenerMethodName, listener);
		viewBuilder.build();
		Map<Integer, SootClass> idToClass = viewBuilder.getIdToClass();
		Map<AssignStmt, SootClass> viewToClass = viewBuilder.getViewToClass();
		Set<Integer> keySet = idToClass.keySet();
		Set<AssignStmt> keySet2 = viewToClass.keySet();
		System.out.println("Phase deep analyze: " + "SootMethod: " + sootMethod
				+ " keySet: " + keySet.size());

		for (AssignStmt v : keySet2) {
			SootClass sootClass = viewToClass.get(v);
			Component com = Component.parseComponent(sootClass);
			System.out.println("Phase deep analyze: " + "value: " + v
					+ " defineClass: " + sootClass);
			ComponentHandler componentHandler = new ComponentHandler(sootClass);
			componentHandler.build();
			Map<String, Component> map = componentHandler.getMap();
			String sourceActivity = componentHandler.getActivity();
			if (sourceActivity == "") {
				System.err.println("Class: " + sootClass + " Type: " + com
						+ " isn't attached to an Activity!");
				continue;
			}

			StringConstant v2 = StringConstant.v(v + " " + view);
			TempConEdge tempConEdge = new TempConEdge(map, v2);
			TempEdge tempEdge = new TempEdge(sourceActivity, targetActivity,
					tempConEdge);
			TempGraph.v().getTempEdges().add(tempEdge);
		}
	}

	@Override
	public void appendToGlobal() {
		Global.v().getSootClasses().addAll(allClasses);
	}
}
