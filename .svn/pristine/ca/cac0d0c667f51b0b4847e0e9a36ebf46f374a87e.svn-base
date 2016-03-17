package resource;

import resource.MenuHandler.MenuType;

public class RowMenu extends Row{	
	private MenuType menuType;
	private Object fouthP;	
	private int classId;
	
	public RowMenu(String className, MenuType menuType, int id, Object fouthP){
		this.className = className;
		this.menuType = menuType;
		this.id = id;
		this.fouthP = fouthP;
	}
	
	public void setClassId(int classId){
		this.classId = classId;
	}
	
	//start to get
	public MenuType getMenuType(){
		return menuType;
	}	
	
	public Object getFouthP(){
		return fouthP;
	}
	
	
	public int getClassId(){
		return classId;
	}
	
	public String toString(){
		String row = "className = "+className+" classId = "+classId+" menuType = "+menuType+" "+
				" id = "+id +" fouthP = "+fouthP+" text = "+text ;
		return row;
	}

	@Override
	public Row copy() {
		// TODO Auto-generated method stub
		return null;
	}
}
