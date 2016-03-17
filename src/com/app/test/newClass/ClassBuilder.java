package com.app.test.newClass;

import com.app.test.Constants;
import com.app.test.newMethod.MethodBuilder;

import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;

public abstract class ClassBuilder extends Constants implements ClassBuildingInterface{
	
	protected SootClass sc;
	protected String className;
	
	public ClassBuilder(String className) {
		super();
		this.className = className;
	}

	public SootClass newClass() {
		sc = new SootClass(className, Modifier.PUBLIC);
		sc.setSuperclass(Scene.v().getSootClass("java.lang.Thread"));
//		sc.setApplicationClass();
		addFields();
		addMethods();
		Scene.v().addClass(sc);
		return sc;
	}

	public SootField addField(String fieldName,RefType type){
		SootField sootField = new SootField(fieldName, type);
		sc.addField(sootField);
		return sootField;
	}
	
	public SootField addField(String name, Type type, int modifiers){
		SootField sootField = new SootField(name, type,modifiers);
		sc.addField(sootField);
		return sootField;
	}
	
	public SootMethod addMethod(MethodBuilder mb){
		mb.newMethod();
		return mb.getMethod();
	}
}
