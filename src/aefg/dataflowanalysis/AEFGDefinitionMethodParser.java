package aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import analysis.methodAnalysis.InterMethodAnalysis;

/**
 * We assume that the instance of a certain type only exists one.
 * */

public class AEFGDefinitionMethodParser {
	
	private Local local;
	private SootMethod useMethod;
	
	public List<SootMethod> parseDefinitionMethod(Local local, SootMethod sm){
		RefType refType = (RefType)(local.getType());
		String type = refType.getSootClass().getName();
		List<SootMethod> defineMethods = new ArrayList<SootMethod>();
		// 当Scene中不含有v时，则分析v所在的方法。
		if (defineMethods.size() == 0) {
			Set<SootMethod> downDefMethods = searchDownForDefineMethods(sm, type);
			if(downDefMethods.size()>0){ 
				defineMethods.addAll(downDefMethods);
				return defineMethods;
			}
			else {
				Set<SootMethod> upDefMethods = searchUpForDefineMethods(sm, type);
				if(upDefMethods.size()>0){ 
					defineMethods.addAll(upDefMethods);
					return defineMethods;
				}
				else defineMethods.add(sm);
			}
		}
		return defineMethods;
	}
	
	/**
	 * search precursor of sm for definition method
	 * @param sm 
	 * @param type
	 * @return
	 */
	private static Set<SootMethod> searchUpForDefineMethods(SootMethod sm,String type){
		Set<SootMethod> methods = new HashSet<SootMethod>();
		if(sm.isConcrete()){
			if(containsInitMethod(sm, type)){
				methods.add(sm);
			}
			else{
				List<Type> parameterTypes = sm.getParameterTypes();
				if(parameterTypes.contains(RefType.v(type))){
					List<SootMethod> sources = InterMethodAnalysis.getSourcesMethods(sm);	
					for(SootMethod src:sources){
						if(src.equals(sm))
							continue;
						if(src.isAbstract()){
							for(SootMethod smMethod:getConcreteMethods(src)){
								Set<SootMethod> methods2 = searchUpForDefineMethods(smMethod, type);
								for(SootMethod m:methods2){
									if(!methods.contains(m)){
										methods.add(m);
									}
								}
							}
						}
						else {
							Set<SootMethod> methods2 = searchUpForDefineMethods(src, type);
							for(SootMethod m:methods2){
								if(!methods.contains(m)){
									methods.add(m);
								}
							}
						}
					}
				}
			}
		}
		return methods;
	}
	
	/**
	 * search successor of sm for definition
	 * */
	private static Set<SootMethod> searchDownForDefineMethods(SootMethod sm,String type){
		Set<SootMethod> methods = new HashSet<SootMethod>();
		//1.判断sm中是否含有intent的定义信息，若有，则返回
		if(containsInitMethod(sm, type)){
			methods.add(sm);
		}
		else{
			List<SootMethod> targets = InterMethodAnalysis.getTargetsMethods(sm);	
			for(SootMethod tgt:targets){
				if(tgt.equals(sm))
					continue;
				if(tgt.getReturnType().toString().contains(type)){
					if(tgt.isAbstract()){
						for(SootMethod smMethod:getConcreteMethods(tgt)){
							Set<SootMethod> methods2 = searchDownForDefineMethods(smMethod, type);
							for(SootMethod m:methods2){
								if(!methods.contains(m)){
									methods.add(m);
								}
							}
						}
					}
					else {
						Set<SootMethod> methods2 = searchDownForDefineMethods(tgt, type);
						for(SootMethod m:methods2){
							if(!methods.contains(m)){
								methods.add(m);
							}
						}
					}
				}
			}
		}
		return methods;
	}

	/**
	 * If a method is abstract, it is override in its sub class.  
	 * */
	public static Set<SootMethod> getConcreteMethods(SootMethod abstractMethod){
		SootClass declaringClass = abstractMethod.getDeclaringClass();
		Set<SootMethod> methods = new HashSet<SootMethod>();
		List<SootClass> subclasses = Scene.v().getActiveHierarchy().getSubclassesOf(declaringClass);
		for(SootClass subClass:subclasses){
			if(subClass.declaresMethod(abstractMethod.getSubSignature())){
				SootMethod sm = subClass.getMethod(abstractMethod.getSubSignature());
				if(sm.isConcrete()){
					methods.add(sm);
				}
			}
		}
		return methods;
	}
	
	public static boolean containsInitMethod(SootMethod method,String type){
		if(type==null){
			throw new RuntimeException("Type do not have a stopStmt");
		}
		List<SootMethod> targets = InterMethodAnalysis.getTargetsMethods(method);
		for(SootMethod sm:targets){
			if(sm.getSubSignature().contains("void <init>")){
				if(sm.getDeclaringClass().getName().contains(type)){
					System.out.println(method+" SubSignature: "+sm.getSubSignature()+" Signature: "+sm.getSignature());
					return true;
				}
			}
//			if(sm.getSignature().contains("android.content.Intent: void <init>")){
//				//System.out.println(sm+" -------------------"+sm.getSignature());
//				return true;
//			}
		}
		return false;
	}
}
