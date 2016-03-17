package com.app.test.event;

import com.app.test.CallBack;

import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

public class SystemEventRecognizer extends SystemEventConstants{
	
	public static boolean isSystemEvent(InvokeStmt is){
		return isReceiverEvent(is)||isServiceEvent(is);
	}
	
	public static boolean isReceiverEvent(InvokeStmt is){
		InvokeExpr invokeExpr = is.getInvokeExpr();
		SootMethod method = invokeExpr.getMethod();
		String sub = method.getSubSignature();
		if(sub.equals(CallBack.receiver_registerReceiver)){
			return true;
		}
		return false;
	}
	
	public static boolean isServiceEvent(InvokeStmt is){
		InvokeExpr invokeExpr = is.getInvokeExpr();
		SootMethod method = invokeExpr.getMethod();
		String sub = method.getSubSignature();
		if(extenalServicesList.contains(sub)){
			return true;
		}
		return false;
	}
}
