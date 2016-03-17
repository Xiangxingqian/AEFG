package resource;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.content.res.AXmlResourceParser;

import singlton.Global;

/*
 * 4+2+1+1{layoutName,UiName,src,@Text}+{uiId,layoutId,text}+{className}
 * */
public class XmlHandler {
	
	private List<String> files ;
	
	public XmlHandler(String apk,String apktoolLocation,String outPutLocation){
		ApkToRes atr = ApkToRes.v();
		atr.loadApk(apk, apktoolLocation, outPutLocation);		
		this.files = atr.getResFiles();		
		genTable();
	}

	/**
	 * write the left three elements. className is set in setClassName. 
	 * id: layout id and Ui id.
	 * text
	 * */
	public void genTable(){
		loadFiles();			
		for(RowUi rs:IdTable.v().getRows()){
			String on = rs.getName();
			String orv = rs.getRefValue();		
			String src = rs.getSrc();
			if(on.contains("@id/")){
				String name = on.substring(on.indexOf("@id/")+4,on.length());				
				if(IdTable.v().getNameToId().containsKey(name)){
					rs.setId(IdTable.v().getNameToId().get(name));
				}
			}
			else 
				System.err.println("102: "+on);
			
			if(orv!=null){
				if(orv.contains("@string/")){
					String refValue = orv.substring(orv.indexOf("@string/")+8,orv.length());
					if(IdTable.v().getRefValueToText().containsKey(refValue)){
						rs.setText(IdTable.v().getRefValueToText().get(refValue));
					}
				}
				else rs.setText(orv);				
			}	
			else if(src!=null){
				if(src.contains("@drawable/")){
					String s =src.substring(src.indexOf("@drawable/")+10,src.length());					
					rs.setText(s+".pic");			
				}
				else rs.setText(orv);	
			}
				
			String xmlName = rs.getXmlName();
			String sub = xmlName.substring(xmlName.lastIndexOf("/")+1,xmlName.indexOf(".xml"));
			List<Object[]> la = IdTable.v().getLayoutTable();
			for(Object[] o:la){
				if(o[2].equals(sub)){
					//transfer object type to int type.
					rs.setXmlId(Integer.parseInt((String)(o[1].toString())));
				}
			}
		}		
	}
	/**
	 * RowUi contains four elements.
	 * */
	private void loadFiles(){
		for(String file:files){
			if(file.endsWith(".xml")){
				loadXml(file);
			}
		}
	}
	
	protected void loadXml(String file) {		
		SAXReader reader = new SAXReader();
		try {
			Document  doc = reader.read(new File(file));
			Element root = doc.getRootElement();
			Iterator i = root.elementIterator();
			if(file.contains("layout")){
				getAllElements(root, file);
			}
			
			//both layout and ui objects are in public.xml, so we need to distinguish them.
			else if(file.contains("values/public")){
				while(i.hasNext()){
					Element e = (Element)i.next();
					String layout = e.attributeValue("type");
					//System.out.println("layout: "+layout);
					String name = e.attributeValue("name");
					String stringId = e.attributeValue("id");
					String hexString = stringId.substring(stringId.indexOf("0x")+2,stringId.length());					
					int id = Integer.parseInt(hexString,16);
					
					//if it is a layout type, write layout table, or write nameToId.
					if("layout".equals(layout)){
						Object[] o = {layout,id,name};
						IdTable.v().getLayoutTable().add(o);
					}
					else{
						IdTable.v().getNameToId().put(name, id);
					}
				}			
			}
			else if(file.contains("values/strings")){
				while(i.hasNext()){
					Element e = (Element)i.next();
					String name = e.attributeValue("name");					
					String text = e.getData().toString();
					IdTable.v().getRefValueToText().put(name, text);							
				}			
			}	
		} catch (DocumentException e) {			
			e.printStackTrace();
		}
	}
	
	public void getAllElements(Element root,String file){
		Iterator i = root.elementIterator();
		while(i.hasNext()){
			Element e = (Element)i.next();
			if(e.getName().contains("Layout")){
				getAllElements(e, file);
			}	
			if(e.getName().equals("TextView")||
					e.getName().equals("Button")||
					e.getName().equals("ImageView")||
					e.getName().equals("ImageButton")){				
				String name = e.attributeValue("id");
				if(name==null){
					continue;
				}
				String refValue = e.attributeValue("text");
				String src = e.attributeValue("src");
				RowUi rs = new RowUi(file, name, refValue, src);
				IdTable.v().addRow(rs);
			}
		}	
	
	}
	
	/**
	 * activityName to R.id. One activity has an id, but one id may point to some activities, 
	 * we need to search the map to find all the activities.
	 * */
	public void setClassName(){	
		ArrayList<RowUi> rows = new ArrayList<RowUi>();	
		IdTable idt = IdTable.v();
		for(int i=0;i<idt.getRows().size();i++){
			Map<String,Integer> map = Global.v().getContentView();
			Set<Entry<String, Integer>> set = map.entrySet();
			Iterator<Entry<String, Integer>> it = set.iterator();
			List<Integer> visited = new ArrayList<Integer>();
			while(it.hasNext()){
				Entry<String, Integer> entry = it.next();
				if(!visited.contains(entry.getValue())){
					if(entry.getValue().equals(idt.getRows().get(i).getXmlId())){
						visited.add(entry.getValue());
						idt.getRows().get(i).setClassName(entry.getKey());
					}
				}
				else{
					if(entry.getValue().equals(idt.getRows().get(i).getXmlId())){
						RowUi r = idt.getRows().get(i).copy();
						r.setClassName(entry.getKey());
						idt.getRows().add(r);
					}
				}
			}
		}
	}
	
	public void buildTable(){
		setClassName();		
	}
	
}
