package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import com.app.test.AppDir;
import com.app.test.data.AndroidIntentFilter;

public class SerializeTest {
	public static void main(String[] args){
		File files = new File("sootOutput/");
		for(File f:files.listFiles() ){
			if(f.getName().endsWith(".txt")){
//				System.out.println(f.getName());
				readFile(f.getAbsolutePath());
			}
//		String location = "sootOutput/"+AppDir.appName+".txt";
////		String location = "D:/serial.txt";
		
		}
	}
	
	private static void readFile(String location) {
		try{
			
			FileInputStream fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			//read the first object. There are four objects in location: callback.class, activityToIntentFilters, serviceToIntentFilters and receiverToIntentFilters
			Object object =  ois.readObject();
			Map<String, List<String>> viewToCallBacks = (Map<String, List<String>>)object;
//			for(String key:viewToCallBacks.keySet()){
//				for(String value:viewToCallBacks.get(key)){
//					System.out.println("key: "+key+"Value: "+value);
//				}
//			}
			
			Map<String, List<AndroidIntentFilter>> o1 = (Map<String, List<AndroidIntentFilter>>) ois.readObject();
			Map<String, List<AndroidIntentFilter>> o2 = (Map<String, List<AndroidIntentFilter>>) ois.readObject();
			Map<String, List<AndroidIntentFilter>> o3 = (Map<String, List<AndroidIntentFilter>>) ois.readObject();
//			System.out.println("Activity: ");
//			outPut(o1);
//			System.out.println("Service: ");
//			outPut(o2);
//			System.out.println("Receiver: ");
			if(!(o3.keySet().size()>0)){
				System.out.println(location);
//				outPut(o3);
			}
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void outPut(Map<String,List<AndroidIntentFilter>> map){
		for(String key:map.keySet()){
			for(AndroidIntentFilter value:map.get(key)){
				System.out.println("Key: "+key+" Value actions:"+value.getmActions()+" dataTypes: "+value.getmDataTypes()+" category: "+value.getmCategories());
			}
		}
	}
}
