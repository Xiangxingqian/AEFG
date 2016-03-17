package resource;

public abstract class Row {
	protected String className;
	protected int id;
	protected String text;
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getClassName(){
		return className;
	}
	
	public int getId(){
		return id;
	}
	
	public String getText(){
		return text;
	}
	
	public String toStr(){
		String row = "className = "+className+" id = "+id +" text = "+text ;
		return row;
	}
	
	public abstract Row copy();
}
