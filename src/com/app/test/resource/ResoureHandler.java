package com.app.test.resource;

import infoflow.android.resources.ARSCFileParser;
import infoflow.android.resources.ARSCFileParser.AbstractResource;
import infoflow.android.resources.ARSCFileParser.ResPackage;
import infoflow.android.resources.ARSCFileParser.ResType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParserException;

import com.content.res.xmlprinter.AXmlResourceParser;

public class ResoureHandler {
	
//	Set<PublicElement> publicElements = new HashSet<ResoureHandler.PublicElement>();
//	Set<Resource_Public> resource_Public
	
	String apkLocation;
	List<String> activities = new ArrayList<String>();

	List<ResourceOutput> resources = new ArrayList<ResourceOutput>();
	public ResoureHandler(String apkLocation) {
		this.apkLocation = apkLocation;
	}
	
	/**
	 * parse layout to get callbacks defined in xml.
	 * @return write List<ResourceOutput> resources.
	 * */
	public void parseResourceLayout(){
		
		File apkFile = new File(apkLocation);
		if(!apkLocation.endsWith(".apk"))
			throw new RuntimeException("Input file is not a apk file.");
		
		try {
			ZipFile zipFile = new ZipFile(apkFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()){
				ZipEntry nextElement = entries.nextElement();
				final String layoutName = nextElement.getName();
				InputStream inputStream = zipFile.getInputStream(nextElement);
				final AXmlResourceParser parser = new AXmlResourceParser();
				if(layoutName.startsWith("res/layout")&&layoutName.endsWith(".xml")){
//				if(nextElement.getName().equals("AndroidManifest.xml")){
					parseXml(layoutName, inputStream, parser, new ElementParser() {
						
						@Override
						public void handleElement() {
							String viewName = parser.getName();
							String callBack = "onClick";
							String callBackValue = parseAttributeValueString(parser, callBack);
							if(callBackValue!=null){
								String shortLayoutName = layoutName.substring(layoutName.lastIndexOf("/")+1, layoutName.indexOf(".xml"));
								int layoutId = parseARSC(shortLayoutName);
								int viewId = parseAttributeValueData(parser, "id");
								resources.add(new ResourceOutput(layoutName, layoutId, callBack, callBackValue, viewName,viewId));
							}
						}
					});
				}
				
				//在Apk中并不存在public.xml，R.java存放在Resource.arsc.
//				else if("res/values/public.xml".equals(layoutName)){
//					parseXml(layoutName, inputStream, parser, new ElementParser() {
//						
//						@Override
//						public void handleElement() {
//							String type = parseAttributeValueString(parser, "type");
//							String name = parseAttributeValueString(parser, "name");
//							String id = parseAttributeValueString(parser, "id");
//							PublicElement publicElement = new PublicElement(type, name, id);
//							publicElements.add(publicElement);
//						}
//					});
//				}
			}
			zipFile.close();
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param resourceName
	 * @return resourceId. 
	 * */
	private int parseARSC(String resourceName){
		ARSCFileParser resParser = new ARSCFileParser();
		try {
			resParser.parse(apkLocation);
			List<ResPackage> packages = resParser.getPackages();
			
			for(ResPackage rp:packages){
				List<ResType> declaredTypes = rp.getDeclaredTypes();
				for(ResType resType:declaredTypes){
					AbstractResource resource = resType.getResourceByName(resourceName);
					if(resource!=null){
						return resource.getResourceID();
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Cannot find resourceId");
	}
	
	private void parseXml(String layoutName, InputStream inputStream,
			AXmlResourceParser parser, ElementParser em) throws XmlPullParserException,
			IOException {
		parser.open(inputStream);
		int next = 0;
		while((next = parser.next())!=AXmlResourceParser.END_DOCUMENT){
			switch (next) {
			case AXmlResourceParser.START_TAG:
				em.handleElement();
				break;
			case AXmlResourceParser.END_TAG:
				break;
			}
		}
	}
	
	private String parseAttributeValueString(AXmlResourceParser parser,String name){
		for(int i=0;i<parser.getAttributeCount();i++){
			String attributeName = parser.getAttributeName(i);
			if(name.equals(attributeName)){
				return parser.getAttributeValue(i);
			}
		}
		return null;
	}
	
	private int parseAttributeValueData(AXmlResourceParser parser,String name){
		for(int i=0;i<parser.getAttributeCount();i++){
			String attributeName = parser.getAttributeName(i);
			if(name.equals(attributeName)){
				return parser.getAttributeValueData(i);
			}
		}
		return -1;
	}
	
	public List<String> getActivities() {
		return activities;
	}

	public List<ResourceOutput> getResources() {
		return resources;
	}
	
	public static void main(String[] args){

		System.out.println("Start to analyze");
		ResoureHandler rHandler = new ResoureHandler("L:/APKDecompiled/AdobeReader/AdobeReader.apk");
		rHandler.parseResourceLayout();
		for(ResourceOutput rOutput:rHandler.resources){
			System.out.println(rOutput.toString());
		}
		serializationObject(rHandler.resources, "D:/WorkSpace/Administrator/workspace/svn/AdobeReaderResource.txt");
//		List<String> activities2 = rHandler.getActivities();
	}
	
	private static void serializationObject(Object g, String edgeStorageLocation) {
		File file = new File(edgeStorageLocation);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Graph序列化
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(g);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
