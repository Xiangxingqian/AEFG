package resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.util.NumberedSet;

public class IdTable implements Table {

	
	private List<Object[]> layoutTable = new ArrayList<Object[]>();
	private Map<String,Integer> nameToId = new HashMap<String, Integer>();
	private Map<String,String> refValueToText = new HashMap<String, String>();
	private Map<Integer,String> idToName = new HashMap<Integer, String>();
	
	
	private static IdTable id = null;
	private IdTable(){	}
	public static IdTable v(){
		if(id==null) 
			id = new IdTable();
		return id;
	}
	private List<RowUi> rows = new ArrayList<RowUi>();
	
	@Override
	public void addRow(RowUi row) {
		rows.add(row);
	}

	public List<RowUi> getRows(){
		return rows;
	}
	
	public RowUi getRowByName(String xmlName,String name){
		for(RowUi r:rows){
			if(r.getXmlName().equals(xmlName)&&r.getName().equals(name)){
				return r;
			}
		}
		throw new RuntimeException(xmlName+" "+name+" does not exist.");
	}
	
	//read R.java
	public Map<String,Integer> getNameToId(){
		return nameToId;
	}
		
	//read strings.xml
	public Map<String,String> getRefValueToText(){
		return refValueToText;
	}
		
	public Map<Integer,String> getIdToName(){
		return idToName;
	}
	
	public List<Object[]> getLayoutTable(){
		return layoutTable;
	}
	
	public RowUi searchRow(String className, int uiId){
		for(RowUi r:rows){
			if(className.equals(r.getClassName())&&uiId == r.getId()){
				return r;
			}
		}
		throw new RuntimeException("cannot find Row name:"+className+" uiId:"+uiId);
	}
}
