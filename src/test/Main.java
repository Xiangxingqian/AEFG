package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.test.CallBack;

public class Main {
	
	public static void main(String[] args){
		
		String s2 = "string";
		System.out.println(s2.matches("//w+"));
		
		String string = "[{values:1,key:2,appoint:3,origin:4,},{values:1,appoint:3,origin:4,key:2,},{values:5,appoint:3,origin:4,ow:6}]";
//		String regex = "values[^,]*,";
		System.out.println(string.replaceAll("\\S*(?=(values[^,]*,))\\S*", "1"));
		
		
		
		
		StringBuilder sBuilder = new StringBuilder();
		Pattern pattern = Pattern.compile(".*(values[^,]*,).*");
		Matcher matcher = pattern.matcher("[{values:1,key:2");
		
		while(matcher.find()){
			sBuilder.append(matcher.group()+"123");
		}
		String string2 = sBuilder.toString();
		System.out.println(string2.replaceAll(",+", ",").replace(",}", "}"));;
//		string.replaceAll(".*[,]{1}.*,", "\\}");
//		Pattern pattern = Pattern.compile("[?!(values)]");
//		Matcher matcher = pattern.matcher(string);
//		while (matcher.find()) {
//			s2 = s2+matcher.group();
//		}
//		System.out.println(s2);
//		CallBack.viewToCallBacks.put("qian", Arrays.asList("xiang"));
//		Class callBack = CallBack.class;
//		serializationObject(callBack, "D:/serial.txt");
//		try {
//			deserialization("D:/serial.txt");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static void serializationObject(Object g, String edgeStorageLocation) {
		File file = new File(edgeStorageLocation);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Graph–Ú¡–ªØ
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(CallBack.viewToCallBacks);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void deserialization(String location)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Object object;
		FileInputStream fis = new FileInputStream(location);
		ObjectInputStream ois = new ObjectInputStream(fis);
		object =  ois.readObject();
		Map<String, List<String>> viewToCallBacks = (Map<String, List<String>>)object;
		for(String key:viewToCallBacks.keySet()){
			for(String value:viewToCallBacks.get(key)){
				System.out.println("key: "+key+"Value: "+value);
			}
		}
		ois.close();
		fis.close();

//		List<List<String>> ssList = aeg.getStrEdges();
//		Graph g = new Graph(ssList);
//		FileOutputStream out = null;
//		File file2 = new File(edges);
//		try {
//			file2.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		out = new FileOutputStream(file2);
//		int count = ssList.size();
//		for (int i = 0; i < count; i++) {
//			List<String> strings = ssList.get(i);
//			out.write((i + " src: " + strings.get(0) + " tgt: "
//					+ strings.get(1) + " \n" + "view: " + strings.get(2) + "\n")
//					.getBytes());
//		}
//		out.close();
	}
}
