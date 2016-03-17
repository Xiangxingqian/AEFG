package com.aefg.dataflowanalysis;




import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java_cup.action_part;

import com.app.test.AppDir;


//s instanceof String = s.getClass().getName.equals("java.lang.String")

public class Test {
	Test t;
	public static String name;
	
	static{
		Test test = new Test();
		test.isValid();
	}
	
	public static int[] i = new int[100];
	
	public static List<String> strings = new ArrayList<String>();
	
	public static void isValid(boolean flag, int t, String c,long l){
		System.out.println("hah");
	}
	
	public static boolean isValid(){
		return false;
	}
	
	public static String splitWord(String s,String orignal){
		
		String result = "";
		if(isWord(s)){
			result = s;
			String t = orignal.substring(result.toCharArray().length);
			result = result+" "+splitWord(t, t);
			return result;
		}
		else{
			if(s.toCharArray().length>0){
				s = s.substring(0, s.toCharArray().length-1);
				result = splitWord(s,orignal);
			}
		}
		return result;
	}
	
	public static boolean isWord(String s){
		String[] strings = {"I","am","dog","cat"};
		if(Arrays.asList(strings).contains(s)){
			return true;
		}
		return false;
	}
	
    static	int j = 0;
	
	public static int getI(){
		return j;
	}
	
	public static void main(String[] args) {
		
		int[] i = {1,2,3};
		ArrayList<Integer> iList = new ArrayList<Integer>();
		iList.add(1);
		iList.add(2);
		iList.add(3);
		iList.add(4);
		List<List<Integer>> ll = sort(iList);
		System.out.println(ll.size()+"\n"+ll);
		System.out.println(sortInt(5));
		
	}
	
//	public static List<List<Integer>> sortInt(int i){
//		ArrayList<Integer> ll = new ArrayList<Integer>();
//		if(list.size()==1){
//			ll.add(list);
//			return ll;
//		}
//		
//		for(Integer i:list){
//			ArrayList clone = (ArrayList)list.clone();
//			clone.remove(i);
//			ArrayList<Integer> arrayList = new ArrayList<Integer>();
//			arrayList.add(i);	
//			for(List<Integer> list2:sort(clone)){
//				ArrayList<Integer> clone2 = (ArrayList<Integer>)arrayList.clone();
//				clone2.addAll(list2);
//				ll.add(clone2);
//			}
//		}
//		return ll;
//	}
	
	public static List<Integer> random(int num, int total){
		ArrayList arr = new ArrayList<Integer>();
		for(int t = 0;t<num;t++){
			arr.add((int) (Math.random()*total));
		}
		return arr;
	}
	
	public static List<List<Integer>> sortInt(int i){
		ArrayList<Integer> integers = new ArrayList<Integer>();
		for(int t=0;t<i;t++){
			integers.add(t);
		}
		return sort(integers);
	}
	
	public static List<List<Integer>> sort(ArrayList<Integer> list){
		ArrayList<List<Integer>> ll = new ArrayList<List<Integer>>();
		if(list.size()==1){
			ll.add(list);
			return ll;
		}
		
		for(Integer i:list){
			ArrayList clone = (ArrayList)list.clone();
			clone.remove(i);
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(i);	
			for(List<Integer> list2:sort(clone)){
				ArrayList<Integer> clone2 = (ArrayList<Integer>)arrayList.clone();
				clone2.addAll(list2);
				ll.add(clone2);
			}
		}
		return ll;
	}
	
	
	
	public static HashMap<String, Set<String>> parseClassNames(List<String> methods, boolean subSignature){
		HashMap<String, Set<String>> result = new HashMap<String,  Set<String>>();
		Pattern pattern = Pattern.compile("^\\s*<(.*?):\\s*(.*?)>\\s*$");
		for(String parseString : methods){
			//parse className:
			String className = "";
	        Matcher matcher = pattern.matcher(parseString);
	        if(matcher.find()){
	        	className = matcher.group(1);
	        	String params = "";
				if(subSignature)
					params = matcher.group(2);
				else
					params = parseString;
				
				if(result.containsKey(className))
					result.get(className).add(params);
				else {
					Set<String> methodList = new HashSet<String>(); 
					methodList.add(params);
					result.put(className, methodList);
				}
	        }
		}
		return result;
	}

}