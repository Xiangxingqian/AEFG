package com.app.test;

import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public class MethodCollector {
	Body body;
	public static SootClass previousClass = null;
	public static int methodCount = 0;
	
	public MethodCollector(Body body) {
		this.body = body;
	}
	
	//if current class == previous class, method++;
	//else method =0; start to count
	public void parseBody(){
		SootMethod method = body.getMethod();
		SootClass declaringClass = method.getDeclaringClass();
		if(declaringClass.equals(previousClass)){
			methodCount++;
		}
		else{
			previousClass = declaringClass;
			methodCount = 0;
		}
		talkAbout(methodCount, body);
	}
	
	/**
	 * this method aims to count visited method. We mark each method in a class in a way. like 
	 * <init> a[0];
	 * <clinit> a[1];
	 * getName a[2];
	 * 
	 * Insert a[0], a[1], a[2] to method according to the No of method.
	 * 
	 * For visiting phase, we should make sure one method cannot be counted for one more time.
	 * if method <init> has been visited, we set a[0] = 1 and visitedMethodCount++, if this method is visited again, do nothing. 
	 * */
	public void talkAbout(int count, Body b){
		SootField sField = b.getMethod().getDeclaringClass().getFieldByName(Constants.METHODCOUNTLIST);
		SootField visitedMethodCount_field = Scene.v().getSootClass("com.app.test.AppDir").getFieldByName("visitedMethodCount");
		PatchingChain<Unit> units = b.getUnits();
//		List<Unit> tails = new ExceptionalUnitGraph(b).getTails();
		Unit tail = units.getLast();
		Local methodCountListUniqueLocal = Jimple.v().newLocal("methodCount_l", Constants.linkedList_Type);
		Local value_index = Jimple.v().newLocal("hasVisited", IntType.v());
		Local useless = Jimple.v().newLocal("useless", IntType.v());
		Local visitedMethodCountLocal = Jimple.v().newLocal("visitedCount_l", IntType.v());
		Local get_object = Jimple.v().newLocal("get_object", Constants.object_Type);
		Local integer_l = Jimple.v().newLocal("integer_l", Constants.integer_Type);
		b.getLocals().add(useless);
		b.getLocals().add(visitedMethodCountLocal);
		b.getLocals().add(value_index);
		b.getLocals().add(methodCountListUniqueLocal);
		b.getLocals().add(get_object);
		b.getLocals().add(integer_l);
		units.insertBefore(Jimple.v().newAssignStmt(methodCountListUniqueLocal, Jimple.v().newStaticFieldRef(sField.makeRef())),tail);
		List<Value> values = new ArrayList<Value>();
		AssignStmt useLess =Jimple.v().newAssignStmt(useless, IntConstant.v(0));
		AssignStmt assignStmt = Jimple.v().newAssignStmt(visitedMethodCountLocal,Jimple.v().newStaticFieldRef(visitedMethodCount_field.makeRef()));
		AssignStmt plusPlus = Jimple.v().newAssignStmt(visitedMethodCountLocal, Jimple.v().newAddExpr(visitedMethodCountLocal, IntConstant.v(1)));
		AssignStmt back = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(visitedMethodCount_field.makeRef()), visitedMethodCountLocal);
		List<Unit> assignStmts = new ArrayList<Unit>();
		assignStmts.add(assignStmt);
		assignStmts.add(plusPlus);
		assignStmts.add(back);
		
		units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
				methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(count))),tail);
		insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
		values.add(IntConstant.v(count));
		
//		switch (count) {
//			case 0:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(0))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(0));
//				break;
//			case 1:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(1))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(1));
//				break;
//			case 2:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(2))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(2));
//				break;
//			case 3:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(3))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(3));
//				break;
//			case 4:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(4))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(4));
//				break;
//			case 5:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(5))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(5));
//				break;
//			case 6:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(6))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(6));
//				break;
//			case 7:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(7))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(7));
//				break;
//			case 8:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(8))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(8));
//				break;
//			case 9:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(9))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(9));
//				break;
//			case 10:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(10))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(10));
//				break;
//			case 11:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(11))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(11));
//				break;
//			case 12:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(12))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(12));
//				break;
//			case 13:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(13))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(13));
//				break;
//			case 14:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(14))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(14));
//				break;
//			case 15:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(15))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(15));
//				break;
//			case 16:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(16))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(16));
//				break;
//			case 17:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(17))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(17));
//				break;
//			case 18:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(18))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(18));
//				break;
//			case 19:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(19))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(19));
//				break;
//			case 20:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(20))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(20));
//				break;
//			case 21:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(21))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(21));
//				break;
//			case 22:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(22))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(22));
//				break;
//			case 23:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(23))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(23));
//				break;
//			case 24:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(24))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(24));
//				break;
//			case 25:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(25))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(25));
//				break;
//			case 26:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(26))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(26));
//				break;
//			case 27:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(27))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(27));
//				break;
//			case 28:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(28))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(28));
//				break;
//			case 29:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(29))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(29));
//				break;
//			case 30:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(30))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(30));
//				break;
//			case 31:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(31))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(31));
//				break;
//			case 32:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(32))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(32));
//				break;
//			case 33:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(33))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(33));
//				break;
//			case 34:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(34))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(34));
//				break;
//			case 35:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(35))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(35));
//				break;
//			case 36:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(36))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(36));
//				break;
//			case 37:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(37))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(37));
//				break;
//			case 38:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(38))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(38));
//				break;
//			case 39:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(39))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(39));
//				break;
//			case 40:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(40))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(40));
//				break;
//			case 41:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(41))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(41));
//				break;
//			case 42:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(42))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(42));
//				break;
//			case 43:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(43))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(43));
//				break;
//			case 44:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(44))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(44));
//				break;
//			case 45:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(45))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(45));
//				break;
//			case 46:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(46))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(46));
//				break;
//			case 47:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(47))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(47));
//				break;
//			case 48:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(48))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(48));
//				break;
//			case 49:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(49))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(49));
//				break;
//			case 50:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(50))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(50));
//				break;
//			case 51:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(51))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(51));
//				break;
//			case 52:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(52))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(52));
//				break;
//			case 53:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(53))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(53));
//				break;
//			case 54:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(54))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(54));
//				break;
//			case 55:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(55))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(55));
//				break;
//			case 56:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(56))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(56));
//				break;
//			case 57:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(57))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(57));
//				break;
//			case 58:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(58))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(58));
//				break;
//			case 59:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(59))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(59));
//				break;
//			case 60:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(60))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(60));
//				break;
//			case 61:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(61))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(61));
//				break;
//			case 62:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(62))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(62));
//				break;
//			case 63:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(63))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(63));
//				break;
//			case 64:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(64))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(64));
//				break;
//			case 65:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(65))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(65));
//				break;
//			case 66:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(66))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(66));
//				break;
//			case 67:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(67))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(67));
//				break;
//			case 68:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(68))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(68));
//				break;
//			case 69:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(69))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(69));
//				break;
//			case 70:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(70))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(70));
//				break;
//			case 71:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(71))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(71));
//				break;
//			case 72:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(72))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(72));
//				break;
//			case 73:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(73))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(73));
//				break;
//			case 74:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(74))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(74));
//				break;
//			case 75:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(75))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(75));
//				break;
//			case 76:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(76))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(76));
//				break;
//			case 77:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(77))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(77));
//				break;
//			case 78:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(78))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(78));
//				break;
//			case 79:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(79))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(79));
//				break;
//			case 80:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(80))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(80));
//				break;
//			case 81:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(81))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(81));
//				break;
//			case 82:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(82))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(82));
//				break;
//			case 83:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(83))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(83));
//				break;
//			case 84:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(84))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(84));
//				break;
//			case 85:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(85))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(85));
//				break;
//			case 86:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(86))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(86));
//				break;
//			case 87:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(87))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(87));
//				break;
//			case 88:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(88))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(88));
//				break;
//			case 89:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(89))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(89));
//				break;
//			case 90:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(90))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(90));
//				break;
//			case 91:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(91))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(91));
//				break;
//			case 92:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(92))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(92));
//				break;
//			case 93:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(93))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(93));
//				break;
//			case 94:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(94))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(94));
//				break;
//			case 95:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(95))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(95));
//				break;
//			case 96:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(96))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(96));
//				break;
//			case 97:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(97))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(97));
//				break;
//			case 98:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(98))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(98));
//				break;
//			case 99:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(99))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(99));
//				break;
//			case 100:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(100))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(100));
//				break;
//			case 101:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(101))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(101));
//				break;
//			case 102:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(102))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(102));
//				break;
//			case 103:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(103))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(103));
//				break;
//			case 104:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(104))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(104));
//				break;
//			case 105:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(105))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(105));
//				break;
//			case 106:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(106))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(106));
//				break;
//			case 107:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(107))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(107));
//				break;
//			case 108:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(108))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(108));
//				break;
//			case 109:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(109))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(109));
//				break;
//			case 110:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(110))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(110));
//				break;
//			case 111:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(111))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(111));
//				break;
//			case 112:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(112))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(112));
//				break;
//			case 113:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(113))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(113));
//				break;
//			case 114:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(114))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(114));
//				break;
//			case 115:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(115))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(115));
//				break;
//			case 116:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(116))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(116));
//				break;
//			case 117:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(117))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(117));
//				break;
//			case 118:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(118))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(118));
//				break;
//			case 119:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(119))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(119));
//				break;
//			case 120:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(120))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(120));
//				break;
//			case 121:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(121))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(121));
//				break;
//			case 122:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(122))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(122));
//				break;
//			case 123:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(123))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(123));
//				break;
//			case 124:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(124))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(124));
//				break;
//			case 125:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(125))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(125));
//				break;
//			case 126:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(126))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(126));
//				break;
//			case 127:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(127))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(127));
//				break;
//			case 128:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(128))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(128));
//				break;
//			case 129:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(129))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(129));
//				break;
//			case 130:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(130))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(130));
//				break;
//			case 131:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(131))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(131));
//				break;
//			case 132:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(132))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(132));
//				break;
//			case 133:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(133))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(133));
//				break;
//			case 134:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(134))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(134));
//				break;
//			case 135:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(135))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(135));
//				break;
//			case 136:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(136))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(136));
//				break;
//			case 137:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(137))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(137));
//				break;
//			case 138:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(138))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(138));
//				break;
//			case 139:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(139))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(139));
//				break;
//			case 140:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(140))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(140));
//				break;
//			case 141:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(141))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(141));
//				break;
//			case 142:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(142))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(142));
//				break;
//			case 143:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(143))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(143));
//				break;
//			case 144:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(144))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(144));
//				break;
//			case 145:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(145))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(145));
//				break;
//			case 146:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(146))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(146));
//				break;
//			case 147:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(147))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(147));
//				break;
//			case 148:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(148))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(148));
//				break;
//			case 149:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(149))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(149));
//				break;
//			case 150:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(150))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(150));
//				break;
//			case 151:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(151))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(151));
//				break;
//			case 152:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(152))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(152));
//				break;
//			case 153:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(153))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(153));
//				break;
//			case 154:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(154154))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(4));
//				break;
//			case 155:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(155))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(155));
//				break;
//			case 156:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(156))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(156));
//				break;
//			case 157:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(157))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(157));
//				break;
//			case 158:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(158))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(158));
//				break;
//			case 159:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(159159))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(9));
//				break;
//			case 160:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(160))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(160));
//				break;
//			case 161:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(161))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(161));
//				break;
//			case 162:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(162))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(162));
//				break;
//			case 163:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(163))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(163));
//				break;
//			case 164:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(164164))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(14));
//				break;
//			case 165:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(165))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(165));
//				break;
//			case 166:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(166))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(166));
//				break;
//			case 167:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(167))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(167));
//				break;
//			case 168:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(168))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(168));
//				break;
//			case 169:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(169169))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(19));
//				break;
//			case 170:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(170))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(170));
//				break;
//			case 171:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(171))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(171));
//				break;
//			case 172:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(172))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(172));
//				break;
//			case 173:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(173))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(173));
//				break;
//			case 174:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(174174))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(24));
//				break;
//			case 175:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(175))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(175));
//				break;
//			case 176:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(176))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(176));
//				break;
//			case 177:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(177))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(177));
//				break;
//			case 178:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(178))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(178));
//				break;
//			case 179:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(179179))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(29));
//				break;
//			case 180:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(180))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(180));
//				break;
//			case 181:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(181))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(181));
//				break;
//			case 182:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(182))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(182));
//				break;
//			case 183:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(183))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(183));
//				break;
//			case 184:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(184184))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(34));
//				break;
//			case 185:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(185))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(185));
//				break;
//			case 186:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(186))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(186));
//				break;
//			case 187:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(187))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(187));
//				break;
//			case 188:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(188))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(188));
//				break;
//			case 189:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(189189))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(39));
//				break;
//			case 190:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(190))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(190));
//				break;
//			case 191:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(191))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(191));
//				break;
//			case 192:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(192))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(192));
//				break;
//			case 193:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(193))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(193));
//				break;
//			case 194:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(194194))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(44));
//				break;
//			case 195:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(195))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(195));
//				break;
//			case 196:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(196))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(196));
//				break;
//			case 197:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(197))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(197));
//				break;
//			case 198:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(198))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(198));
//				break;
//			case 199:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(199199))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(49));
//				break;
//			case 200:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(200))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(200));
//				break;
//			case 201:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(201))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(201));
//				break;
//			case 202:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(202))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(202));
//				break;
//			case 203:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(203))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(203));
//				break;
//			case 204:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(204204))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(54));
//				break;
//			case 205:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(205))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(205));
//				break;
//			case 206:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(206))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(206));
//				break;
//			case 207:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(207))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(207));
//				break;
//			case 208:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(208))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(208));
//				break;
//			case 209:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(209209))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(59));
//				break;
//			case 210:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(210))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(210));
//				break;
//			case 211:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(211))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(211));
//				break;
//			case 212:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(212))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(212));
//				break;
//			case 213:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(213))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(213));
//				break;
//			case 214:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(214214))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(64));
//				break;
//			case 215:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(215))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(215));
//				break;
//			case 216:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(216))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(216));
//				break;
//			case 217:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(217))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(217));
//				break;
//			case 218:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(218))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(218));
//				break;
//			case 219:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(219219))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(69));
//				break;
//			case 220:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(220))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(220));
//				break;
//			case 221:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(221))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(221));
//				break;
//			case 222:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(222))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(222));
//				break;
//			case 223:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(223))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(223));
//				break;
//			case 224:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(224224))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(74));
//				break;
//			case 225:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(225))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(225));
//				break;
//			case 226:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(226))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(226));
//				break;
//			case 227:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(227))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(227));
//				break;
//			case 228:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(228))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(228));
//				break;
//			case 229:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(229229))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(79));
//				break;
//			case 230:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(230))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(230));
//				break;
//			case 231:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(231))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(231));
//				break;
//			case 232:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(232))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(232));
//				break;
//			case 233:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(233))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(233));
//				break;
//			case 234:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(234234))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(84));
//				break;
//			case 235:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(235))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(235));
//				break;
//			case 236:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(236))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(236));
//				break;
//			case 237:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(237))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(237));
//				break;
//			case 238:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(238))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(238));
//				break;
//			case 239:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(239239))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(89));
//				break;
//			case 240:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(240))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(240));
//				break;
//			case 241:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(241))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(241));
//				break;
//			case 242:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(242))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(242));
//				break;
//			case 243:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(243))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(243));
//				break;
//			case 244:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(244244))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(94));
//				break;
//			case 245:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(245))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(245));
//				break;
//			case 246:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(246))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(246));
//				break;
//			case 247:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(247))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(247));
//				break;
//			case 248:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(248))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(248));
//				break;
//			case 249:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(249249))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(99));
//				break;
//			case 250:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(250))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(250));
//				break;
//			case 251:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(251))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(251));
//				break;
//			case 252:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(252))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(252));
//				break;
//			case 253:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(253))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(253));
//				break;
//			case 254:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(254254))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(104));
//				break;
//			case 255:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(255))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(255));
//				break;
//			case 256:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(256))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(256));
//				break;
//			case 257:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(257))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(257));
//				break;
//			case 258:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(258))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(258));
//				break;
//			case 259:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(259259))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(109));
//				break;
//			case 260:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(260))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(260));
//				break;
//			case 261:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(261))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(261));
//				break;
//			case 262:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(262))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(262));
//				break;
//			case 263:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(263))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(263));
//				break;
//			case 264:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(264264))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(114));
//				break;
//			case 265:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(265))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(265));
//				break;
//			case 266:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(266))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(266));
//				break;
//			case 267:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(267))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(267));
//				break;
//			case 268:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(268))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(268));
//				break;
//			case 269:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(269269))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(119));
//				break;
//			case 270:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(270))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(270));
//				break;
//			case 271:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(271))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(271));
//				break;
//			case 272:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(272))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(272));
//				break;
//			case 273:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(273))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(273));
//				break;
//			case 274:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(274274))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(124));
//				break;
//			case 275:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(275))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(275));
//				break;
//			case 276:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(276))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(276));
//				break;
//			case 277:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(277))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(277));
//				break;
//			case 278:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(278))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(278));
//				break;
//			case 279:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(279279))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(129));
//				break;
//			case 280:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(280))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(280));
//				break;
//			case 281:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(281))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(281));
//				break;
//			case 282:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(282))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(282));
//				break;
//			case 283:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(283))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(283));
//				break;
//			case 284:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(284284))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(134));
//				break;
//			case 285:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(285))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(285));
//				break;
//			case 286:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(286))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(286));
//				break;
//			case 287:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(287))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(287));
//				break;
//			case 288:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(288))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(288));
//				break;
//			case 289:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(289289))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(139));
//				break;
//			case 290:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(290))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(290));
//				break;
//			case 291:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(291))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(291));
//				break;
//			case 292:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(292))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(292));
//				break;
//			case 293:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(293))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(293));
//				break;
//			case 294:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(294294))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(144));
//				break;
//			case 295:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(295))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(295));
//				break;
//			case 296:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(296))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(296));
//				break;
//			case 297:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(297))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(297));
//				break;
//			case 298:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(298))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(298));
//				break;
//			case 299:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(299))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(149));
//				break;
//			case 300:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(300))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(300));
//				break;
//			case 301:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(301))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(301));
//				break;
//			case 302:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(302))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(302));
//				break;
//			case 303:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(303))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(303));
//				break;
//			case 304:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(304))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(304));
//				break;
//			case 305:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(305))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(305));
//				break;
//			case 306:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(306))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(306));
//				break;
//			case 307:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(307))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(307));
//				break;
//			case 308:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(308))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(308));
//				break;
//			case 309:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(309))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(309));
//				break;
//			case 310:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(310))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(310));
//				break;
//			case 311:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(311))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(311));
//				break;
//			case 312:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(312))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(312));
//				break;
//			case 313:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(313))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(313));
//				break;
//			case 314:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(314))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(314));
//				break;
//			case 315:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(315))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(315));
//				break;
//			case 316:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(316))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(316));
//				break;
//			case 317:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(317))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(317));
//				break;
//			case 318:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(318))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(318));
//				break;
//			case 319:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(319))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(319));
//				break;
//			case 320:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(320))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(320));
//				break;
//			case 321:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(321))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(321));
//				break;
//			case 322:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(322))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(322));
//				break;
//			case 323:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(323))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(323));
//				break;
//			case 324:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(324))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(324));
//				break;
//			case 325:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(325))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(325));
//				break;
//			case 326:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(326))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(326));
//				break;
//			case 327:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(327))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(327));
//				break;
//			case 328:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(328))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(328));
//				break;
//			case 329:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(329))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(329));
//				break;
//			case 330:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(330))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(330));
//				break;
//			case 331:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(331))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(331));
//				break;
//			case 332:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(332))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(332));
//				break;
//			case 333:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(333))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(333));
//				break;
//			case 334:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(334))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(334));
//				break;
//			case 335:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(335))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(335));
//				break;
//			case 336:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(336))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(336));
//				break;
//			case 337:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(337))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(337));
//				break;
//			case 338:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(338))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(338));
//				break;
//			case 339:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(339))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(339));
//				break;
//			case 340:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(340))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(340));
//				break;
//			case 341:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(341))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(341));
//				break;
//			case 342:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(342))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(342));
//				break;
//			case 343:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(343))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(343));
//				break;
//			case 344:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(344))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(344));
//				break;
//			case 345:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(345))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(345));
//				break;
//			case 346:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(346))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(346));
//				break;
//			case 347:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(347))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(347));
//				break;
//			case 348:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(348))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(348));
//				break;
//			case 349:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(349))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(349));
//				break;
//			case 350:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(350))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(350));
//				break;
//			case 351:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(351))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(351));
//				break;
//			case 352:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(352))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(352));
//				break;
//			case 353:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(353))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(353));
//				break;
//			case 354:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(354))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(354));
//				break;
//			case 355:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(355))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(355));
//				break;
//			case 356:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(356))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(356));
//				break;
//			case 357:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(357))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(357));
//				break;
//			case 358:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(358))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(358));
//				break;
//			case 359:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(359))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(359));
//				break;
//			case 360:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(360))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(360));
//				break;
//			case 361:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(361))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(361));
//				break;
//			case 362:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(362))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(362));
//				break;
//			case 363:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(363))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(363));
//				break;
//			case 364:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(364))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(364));
//				break;
//			case 365:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(365))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(365));
//				break;
//			case 366:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(366))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(366));
//				break;
//			case 367:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(367))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(367));
//				break;
//			case 368:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(368))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(368));
//				break;
//			case 369:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(369))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(369));
//				break;
//			case 370:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(370))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(370));
//				break;
//			case 371:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(371))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(371));
//				break;
//			case 372:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(372))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(372));
//				break;
//			case 373:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(373))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(373));
//				break;
//			case 374:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(374))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(374));
//				break;
//			case 375:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(375))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(375));
//				break;
//			case 376:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(376))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(376));
//				break;
//			case 377:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(377))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(377));
//				break;
//			case 378:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(378))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(378));
//				break;
//			case 379:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(379))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(379));
//				break;
//			case 380:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(380))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(380));
//				break;
//			case 381:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(381))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(381));
//				break;
//			case 382:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(382))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(382));
//				break;
//			case 383:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(383))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(383));
//				break;
//			case 384:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(384))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(384));
//				break;
//			case 385:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(385))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(385));
//				break;
//			case 386:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(386))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(386));
//				break;
//			case 387:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(387))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(387));
//				break;
//			case 388:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(388))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(388));
//				break;
//			case 389:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(389))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(389));
//				break;
//			case 390:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(390))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(390));
//				break;
//			case 391:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(391))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(391));
//				break;
//			case 392:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(392))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(392));
//				break;
//			case 393:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(393))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(393));
//				break;
//			case 394:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(394))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(394));
//				break;
//			case 395:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(395))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(395));
//				break;
//			case 396:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(396))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(396));
//				break;
//			case 397:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(397))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(397));
//				break;
//			case 398:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(398))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(398));
//				break;
//			case 399:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(399))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(399));
//				break;
//			case 400:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(400))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(400));
//				break;
//			case 401:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(401))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(401));
//				break;
//			case 402:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(402))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(402));
//				break;
//			case 403:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(403))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(403));
//				break;
//			case 404:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(404))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(404));
//				break;
//			case 405:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(405))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(405));
//				break;
//			case 406:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(406))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(406));
//				break;
//			case 407:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(407))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(407));
//				break;
//			case 408:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(408))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(408));
//				break;
//			case 409:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(409))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(409));
//				break;
//			case 410:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(410))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(410));
//				break;
//			case 411:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(411))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(411));
//				break;
//			case 412:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(412))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(412));
//				break;
//			case 413:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(413))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(413));
//				break;
//			case 414:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(414))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(414));
//				break;
//			case 415:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(415))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(415));
//				break;
//			case 416:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(416))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(416));
//				break;
//			case 417:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(417))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(417));
//				break;
//			case 418:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(418))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(418));
//				break;
//			case 419:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(419))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(419));
//				break;
//			case 420:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(420))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(420));
//				break;
//			case 421:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(421))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(421));
//				break;
//			case 422:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(422))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(422));
//				break;
//			case 423:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(423))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(423));
//				break;
//			case 424:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(424))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(424));
//				break;
//			case 425:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(425))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(425));
//				break;
//			case 426:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(426))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(426));
//				break;
//			case 427:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(427))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(427));
//				break;
//			case 428:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(428))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(428));
//				break;
//			case 429:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(429))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(429));
//				break;
//			case 430:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(430))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(430));
//				break;
//			case 431:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(431))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(431));
//				break;
//			case 432:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(432))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(432));
//				break;
//			case 433:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(433))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(433));
//				break;
//			case 434:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(434))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(434));
//				break;
//			case 435:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(435))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(435));
//				break;
//			case 436:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(436))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(436));
//				break;
//			case 437:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(437))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(437));
//				break;
//			case 438:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(438))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(438));
//				break;
//			case 439:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(439))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(439));
//				break;
//			case 440:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(440))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(440));
//				break;
//			case 441:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(441))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(441));
//				break;
//			case 442:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(442))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(442));
//				break;
//			case 443:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(443))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(443));
//				break;
//			case 444:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(444))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(444));
//				break;
//			case 445:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(445))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(445));
//				break;
//			case 446:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(446))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(446));
//				break;
//			case 447:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(447))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(447));
//				break;
//			case 448:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(448))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(448));
//				break;
//			case 449:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(449))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(449));
//				break;
//			case 450:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(450))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(450));
//				break;
//			case 451:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(451))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(451));
//				break;
//			case 452:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(452))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(452));
//				break;
//			case 453:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(453))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(453));
//				break;
//			case 454:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(454))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(454));
//				break;
//			case 455:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(455))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(455));
//				break;
//			case 456:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(456))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(456));
//				break;
//			case 457:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(457))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(457));
//				break;
//			case 458:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(458))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(458));
//				break;
//			case 459:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(459))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(459));
//				break;
//			case 460:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(460))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(460));
//				break;
//			case 461:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(461))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(461));
//				break;
//			case 462:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(462))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(462));
//				break;
//			case 463:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(463))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(463));
//				break;
//			case 464:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(464))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(464));
//				break;
//			case 465:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(465))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(465));
//				break;
//			case 466:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(466))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(466));
//				break;
//			case 467:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(467))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(467));
//				break;
//			case 468:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(468))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(468));
//				break;
//			case 469:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(469))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(469));
//				break;
//			case 470:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(470))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(470));
//				break;
//			case 471:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(471))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(471));
//				break;
//			case 472:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(472))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(472));
//				break;
//			case 473:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(473))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(473));
//				break;
//			case 474:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(474))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(474));
//				break;
//			case 475:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(475))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(475));
//				break;
//			case 476:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(476))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(476));
//				break;
//			case 477:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(477))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(477));
//				break;
//			case 478:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(478))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(478));
//				break;
//			case 479:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(479))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(479));
//				break;
//			case 480:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(480))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(480));
//				break;
//			case 481:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(481))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(481));
//				break;
//			case 482:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(482))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(482));
//				break;
//			case 483:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(483))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(483));
//				break;
//			case 484:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(484))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(484));
//				break;
//			case 485:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(485))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(485));
//				break;
//			case 486:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(486))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(486));
//				break;
//			case 487:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(487))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(487));
//				break;
//			case 488:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(488))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(488));
//				break;
//			case 489:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(489))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(489));
//				break;
//			case 490:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(490))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(490));
//				break;
//			case 491:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(491))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(491));
//				break;
//			case 492:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(492))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(492));
//				break;
//			case 493:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(493))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(493));
//				break;
//			case 494:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(494))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(494));
//				break;
//			case 495:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(495))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(495));
//				break;
//			case 496:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object,Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(496))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(496));
//				break;
//			case 497:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(497))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(497));
//				break;
//			case 498:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(498))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(498));
//				break;
//			case 499:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(499))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(499));
//				break;
//			default:
//				units.insertBefore(Jimple.v().newAssignStmt(get_object, Jimple.v().newVirtualInvokeExpr(
//						methodCountListUniqueLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(500))),tail);
//				insertUnits(units, tail, value_index, get_object, integer_l,useLess, assignStmts);
//				values.add(IntConstant.v(500));
//				break;
//		}
		
		units.insertBefore(Jimple.v().newAssignStmt(integer_l, Jimple.v().newStaticInvokeExpr(Constants.integerValueOf_method.makeRef(),IntConstant.v(1))), tail);
		values.add(integer_l);
		units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(methodCountListUniqueLocal, Constants.linkedListSet_method.makeRef(),values)),tail);
		units.insertBefore(useLess,tail);
		body.validate();
	}

	private void insertUnits(PatchingChain<Unit> units, Unit tail,
			Local value_index, Local get_object, Local integer_l,
			AssignStmt useLess, List<Unit> assignStmts) {
		units.insertBefore(Jimple.v().newAssignStmt(integer_l, Jimple.v().newCastExpr(get_object, Constants.integer_Type)), tail);
		units.insertBefore(Jimple.v().newAssignStmt(value_index, Jimple.v().newVirtualInvokeExpr(integer_l, Constants.integerIntValue_method.makeRef())), tail);
		units.insertBefore(Jimple.v().newIfStmt(Jimple.v().newNeExpr(value_index, IntConstant.v(0)), useLess),tail);
		units.insertBefore(assignStmts,tail);
	}
	
}
