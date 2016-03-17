package com.test.xmldata;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.test.resource.ResourceOutput;

public class LayoutData implements IXmlData{

	public static List<ResourceOutput> resources;
	public static String location;
	public static Map<Integer,String> idToCallBack;
	
	/**
	 * read resources from file
	 * */
	public LayoutData(String location){
		this.location = location;
		resources = deserializationAEFG(location);
	}
	
	public LayoutData(List<ResourceOutput> resources) {
		this.resources = resources;
	}
	
	/**
	 * Generate idToCallBack, an map = {view id, callback} 
	 * */
	@Override
	public void build() {
		genIdToCallBack();
	}

	private void genIdToCallBack() {
		idToCallBack = new HashMap<Integer, String>();
		for(ResourceOutput rOutput:resources){
			int uIid = rOutput.getUIid();
			String callBack = rOutput.getCallBackValue();
			idToCallBack.put(uIid, callBack);
		}
	}
	
	private List<ResourceOutput> deserializationAEFG(String inputFile){
		List<ResourceOutput> result = new ArrayList<ResourceOutput>();
		FileInputStream fis;
		try {
			fis = new FileInputStream(inputFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (List<ResourceOutput>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
