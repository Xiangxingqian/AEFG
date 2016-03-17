package resource;

public class RowUi extends Row{
	private String xmlName;
	private String name;
	private String refValue;
	private String src;
	private int xmlId;
	
	public RowUi(String xmlName, String name, String refValue, String src){
		this.xmlName = xmlName;
		this.name = name;
		this.src = src;
		this.refValue = refValue;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setXmlId(int xmlId){
		this.xmlId = xmlId;
	}
	
	
	public int getXmlId(){
		return xmlId;
	}
	
	public String getXmlName(){
		return xmlName;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSrc(){
		return src;
	}
	
	public String getRefValue(){
		return refValue;
	}
		
	public String toString(){
		String row = "className "+className+" xmlName = "+xmlName+" name = "+name+" "+" refValue = "+ refValue+
				" src = "+src +" Text = "+text+" id = "+id +" xmlId = "+xmlId;
		return row;
	}
	
	public RowUi copy(){
		RowUi r = new RowUi(xmlName, name, refValue, src);
		r.setClassName(className);
		r.setId(id);
		r.setXmlId(xmlId);
		r.setText(text);
		return r;
	}
}
