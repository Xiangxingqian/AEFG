package com.app.test.newClass;

import java.util.List;

import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.internal.JInstanceFieldRef;

import com.app.test.newMethod.MethodBuilder;

public class FinishThread extends ClassBuilder{

	public FinishThread(String className) {
		super(className);
	}

	SootField object_field;
	final RefType CLASSNAME_TYPE = RefType.v(className);
	@Override
	public void addMethods() {
		addMethod(new InitMethod(sc, "void <init>(java.lang.Object)"));
		addMethod(new RunMethod(sc, "void run()"));
	}

	@Override
	public void addFields() {
		object_field = addField("object", object_Type);
	}
	
	class InitMethod extends MethodBuilder{
		
		public InitMethod(SootClass sc, String subSignature) {
			super(sc, subSignature);
		}

		@Override
		protected void addUnits() {
			Local base,o;
			base = addLocal("base", CLASSNAME_TYPE);
			o = addLocal("o", object_Type);
			
			addIdentityStmt(base, new ThisRef(CLASSNAME_TYPE));
			addIdentityStmt(o, new ParameterRef(object_Type,0));
			
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(base, threadInit_method.makeRef()));
			addAssignStmt(new JInstanceFieldRef(base, object_field.makeRef()), o);
			addReturnVoidStmt();
		}

		@Override
		protected void newMethodName() {
			List<Type> types = paramTypes();
			types.add(object_Type);
			currentMethod = new SootMethod("<init>",types,VoidType.v(),Modifier.PUBLIC);;
		}
	}
	
	class RunMethod extends MethodBuilder{
		public RunMethod(SootClass sc, String subSignature) {
			super(sc, subSignature);
		}

		@Override
		protected void addUnits() {
			Local base,classLocal,exception, reflectMethod, object;
			{
				base = addLocal("base", CLASSNAME_TYPE);
				classLocal = addLocal("classLocal", class_Type);
				exception = addLocal("exception", exception_Type);
				reflectMethod = addLocal("reflectMethod", reflectMethod_Type);
				object = addLocal("object",object_Type);
			}
			SootField objectField = sc.getFieldByName("object");
			addIdentityStmt(base, new ThisRef(CLASSNAME_TYPE));
			
			//label0, 
			InvokeStmt label0 = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(sleep_method.makeRef(),LongConstant.v(4000)));
			//label1, 
			ReturnVoidStmt label1 = Jimple.v().newReturnVoidStmt();
			//label2, 
			IdentityStmt label2 = Jimple.v().newIdentityStmt(exception,Jimple.v().newCaughtExceptionRef());
			
			body.getUnits().add(label0);
			addAssignStmt(object, Jimple.v().newInstanceFieldRef(base, objectField.makeRef()));
			addAssignStmt(classLocal, Jimple.v().newVirtualInvokeExpr(object,getClass_method.makeRef()));
			
			List<Value> paramValues = paramValues();
			paramValues.add(StringConstant.v("finish"));
			paramValues.add(NullConstant.v());
			addAssignStmt(reflectMethod, Jimple.v().newVirtualInvokeExpr(classLocal,getMethod_method.makeRef(),paramValues));
			addAssignStmt(object, Jimple.v().newInstanceFieldRef(base, objectField.makeRef()));

			List<Value> paramValues3 = paramValues();
			paramValues3.add(object);
			paramValues3.add(NullConstant.v());
			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod,invoke_method.makeRef(),paramValues3));

			body.getUnits().add(label1);
			
			body.getUnits().add(label2);
			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(exception,printStackTrace_method.makeRef()));
			addGotoStmt(label1);
			
			Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), label0, label1, label2);
			body.getTraps().add(trap);
			
		}

		@Override
		protected void newMethodName() {
			currentMethod = new SootMethod("run",paramTypes(),VoidType.v(),Modifier.PUBLIC);
		}
	}
	
	@Override
	public void addInterface() {
		
	}
}
