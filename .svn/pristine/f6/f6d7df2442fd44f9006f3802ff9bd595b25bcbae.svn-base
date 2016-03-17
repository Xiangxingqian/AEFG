package resource;

import java.util.ArrayList;
import java.util.List;

import resource.MenuHandler.MenuType;

public class MenuTable{
	private static MenuTable mt = null;
	private MenuTable(){	}
	public static MenuTable v(){
		if(mt==null) 
			mt = new MenuTable();
		return mt;
	}
	
	private List<RowMenu> rows = new ArrayList<RowMenu>();
	
	//定义
	public void addRow(RowMenu row) {
		rows.add(row);
	}
	public List<RowMenu> getRows(){
		return rows;
	}
	
	
	
	//使用
	public RowMenu searchRow(String className, int uiId,MenuType m){
		for(RowMenu r:rows){
			if(r.getClassName().equals(className)&&r.getId()==uiId&&r.getMenuType().equals(m)){
				return r;
			}
		}
		throw new RuntimeException("cannot find Row name:"+className+" uiId:"+uiId);
	}
	
	public RowMenu searchRow(String className, int uiId){
		for(RowMenu r:rows){
			if(r.getClassName().equals(className)&&r.getId()==uiId){
				return r;
			}
		}
		//原因是:
		throw new RuntimeException("cannot find Row name:"+className+" uiId:"+uiId);
	}
}
