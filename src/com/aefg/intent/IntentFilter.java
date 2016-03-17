package com.aefg.intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntentFilter {

	private static List<ActivityAndFilter> filters = null;
	
	private static Map<String, String> actionToActivity = new HashMap<String, String>();
	
    private ArrayList<String> mActions = new ArrayList<String>();
    private ArrayList<String> mCategories = new ArrayList<String>();
    private ArrayList<String> mDataSchemes = new ArrayList<String>();
    private ArrayList<String> mDataTypes = new ArrayList<String>();
    
	@Override
	public String toString() {
		return "mActions=" + mActions + "\n mCategories="
				+ mCategories + "\n mDataSchemes=" + mDataSchemes
				+ "\n mDataTypes=" + mDataTypes;
	}

	public ArrayList<String> getmActions() {
		return mActions;
	}

	public void setmActions(ArrayList<String> mActions) {
		this.mActions = mActions;
	}

	public ArrayList<String> getmCategories() {
		return mCategories;
	}

	public void setmCategories(ArrayList<String> mCategories) {
		this.mCategories = mCategories;
	}

	public ArrayList<String> getmDataSchemes() {
		return mDataSchemes;
	}

	public void setmDataSchemes(ArrayList<String> mDataSchemes) {
		this.mDataSchemes = mDataSchemes;
	}

	public ArrayList<String> getmDataTypes() {
		return mDataTypes;
	}

	public void setmDataTypes(ArrayList<String> mDataTypes) {
		this.mDataTypes = mDataTypes;
	}
	
	private String split(String tgt){
		if(tgt.contains("\"")){
			tgt = tgt.split("\"")[1];
		}
		return tgt;
	}
	
	/**
	 * 记过滤器为f，intent为i.
	 * 当f为空时，返回true。
	 * 当且仅当f中含有i时，返回true。
	 * */
	public boolean containsAction(String tgt){	
		tgt = split(tgt);	
		if(mActions.size()==0)
			return false;			
		else if(tgt=="")
			return false;			
		else if(mActions.contains(tgt))
			return true;
		return false;		
	}
	
	public boolean containsCategories(String s){
		return matchType1(mCategories, s);
	}
	
	public boolean containsDataType(String s){
		return matchType1(mDataTypes, s);
	}
	
	public boolean containsDataSchemes(String s){
		return matchType1(mDataSchemes, s);
	}
	
	//类型1：当过滤器中未定义时，定义时，也必须为空
	//use for scheme, category and type
	public boolean matchType1(List<String> source,String tgt){
		if(tgt.contains("\"")){
			tgt = tgt.split("\"")[1];
		}
		if(source.size()==0){
			if(""==tgt){
				return true;
			}
			else {
				return false;
			}
		}
		for(String s:source){
			String prefix = s;			
			if (s.contains("*")) {
				if(s.indexOf("*")==0&&tgt!="") return true;
				prefix = s.substring(0,s.indexOf("*"));
			}
			if(tgt.startsWith(prefix)){
				return true;
			}
		}
		return false;
	}
	
	//类型2：当过滤器中未定义时，返回为真
	//use for action, host, path
	public boolean matchType2(List<String> source,String tgt){		
		if(tgt.contains("\"")){
			tgt = tgt.split("\"")[1];
		}		
		if(source.size()==0){
			return true;
		}	
		for(String s:source){
			String prefix = s;			
			if (s.contains("*")) {
				prefix = s.substring(0,s.indexOf("*"));
			}
			if(tgt.startsWith(prefix)){
				return true;
			}
		}
		return false;
	}
	
	public static String matched_getActivity(String[] s) {
		String action = s[0];
//		String scheme = s[1];
//		String dataType = s[2];
//		String category = s[3];
		String string = actionToActivity.get(action);
		if(string==null||string.equals("-1")){
			return null ;
		}
		else return string;
		
//		for (ActivityAndFilter aaf : filters) {
//			List<IntentFilter> mIntentFliters = aaf.getmFliters();
//			for (IntentFilter m : mIntentFliters) {
//				if (m.containsAction(action)) {
//					return aaf.getActivity();
//				}				
//			}
//		}
	}	
	
	public List<String> calculateUniqActions(){
		List<String> arrayList = new ArrayList<String>();
		for(String s:mActions){
			if(!arrayList.contains(s)){
				arrayList.add(s);
			}
		}
		return arrayList;
	}
	
	public static void calculate_ActionToActivity(){
		for(ActivityAndFilter aaf:filters){
			for(IntentFilter iff:aaf.getmFliters()){
				for(String action:iff.calculateUniqActions()){
					if(actionToActivity.get(action)==null)
						actionToActivity.put(action, aaf.getActivity());
					else if(actionToActivity.get(action).equals("-1")){
						continue;
					}
					else if(actionToActivity.get(action).equals(aaf.getActivity())){
						continue;
					}
					else actionToActivity.put(action, "-1");
				}
			}
			
		}
	}
	
	public static Map<String, String> getActionToActivity() {
		return actionToActivity;
	}

	public static void setFilters(List<ActivityAndFilter> filters) {
		IntentFilter.filters = filters;
	}

	public static List<ActivityAndFilter> getFilters() {
		return filters;
	}
}
