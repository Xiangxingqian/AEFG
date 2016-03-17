package resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import singlton.SingltonFactory;
import singlton.SingltonFactory.G;

public class ApkToRes {
	
	private List<String> files = new ArrayList<String>();
	
	public ApkToRes(G g){
		
	}
	public static ApkToRes v(){		
		return SingltonFactory.v().getApkToRes();	
	}	
	
	/**
	 *use apktool.bat analyze apk, and output the resources file to outPutLocation
	 *@param apk The apk to be analyzed.
	 *@param apktoolLocaltion The directory of apktool
	 *@param outPutLocation The location to store resource files
	 * */
	public void retriveResFiles(String apk,String apktoolLocation,String outPutLocation){
		File f = new File(apk);
		String apkName = apk.substring(apk.lastIndexOf("/"),apk.lastIndexOf(".apk"));
		if(f.isFile()&&f.getName().endsWith(".apk")){
			try {
				String s = apktoolLocation+"/apktool.bat d "+f+" "+outPutLocation+apkName;				
				Runtime.getRuntime().exec(s);
				System.out.println("Waiting for generating files...");
				Thread.sleep(100000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else 
			throw new RuntimeException("Input file is not an apk");
	}
	
	/**
	 * get all the xml files from resource files, include *.xml and *.png
	 * @param aPath The path of resource files
	 * @return filesNames is all the files under resource files
	 * */
	public List<String> retriveFiles(String aPath){
		List<String> fileNames = new ArrayList<String>();
		File file = new File(aPath);
		File[] files = file.listFiles();
		if (files == null) {
			files = new File[1];
			files[0] = file;
		}
		for (File element : files) {
			if (element.isDirectory()) {
				List<String> l = retriveFiles(
						aPath + File.separatorChar + element.getName());
			    Iterator<String> it = l.iterator();
			    while (it.hasNext()) {
					String s = it.next();
					fileNames.add(element.getName() + "/" + s);
			    }
			} else {
			    String fileName = element.getName();			 
			    fileNames.add(fileName);		    
			}
		}	
		return fileNames;
	}
	
	public void loadApk(String apk,String apktoolLocation,String outPutLocation){
		String apkName = apk.substring(apk.lastIndexOf("/"),apk.lastIndexOf(".apk"));
		retriveResFiles(apk, apktoolLocation, outPutLocation);
		String path = outPutLocation+apkName;		
		for(String s:retriveFiles(path)){
			files.add(outPutLocation+"/"+apkName+"/"+s);
		}	
	}
	
	public List<String > getResFiles(){
		return files;
	}
	
}
