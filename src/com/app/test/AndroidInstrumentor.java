package com.app.test;
import soot.PackManager;
import soot.Transform;

public class AndroidInstrumentor{
	
	public void instrument(String[] args){
        transform(args);
	}
	
	public void transform(String[] args){
		//内部类，等待调用而非即时调用
		PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new EventBodyTransformer()));
		soot.Main.main(args);
	}

}