package aefg.global;

public class Options {
	private static Options options = null;
	private Options(){
	}
	public static Options v(){
		if(options==null){
			options = new Options();
		}
		return options;
	}
	
	private boolean field_sensitive = false;
	private boolean path_sensitive = false;
	private boolean flow_sensitive = false;
	private boolean context_sensitive = false;
	
	public boolean isField_sensitive() {
		return field_sensitive;
	}
	public void setField_sensitive(boolean field_sensitive) {
		this.field_sensitive = field_sensitive;
	}
	public boolean isPath_sensitive() {
		return path_sensitive;
	}
	public void setPath_sensitive(boolean path_sensitive) {
		this.path_sensitive = path_sensitive;
	}
	public boolean isFlow_sensitive() {
		return flow_sensitive;
	}
	public void setFlow_sensitive(boolean flow_sensitive) {
		this.flow_sensitive = flow_sensitive;
	}
	public boolean isContext_sensitive() {
		return context_sensitive;
	}
	public void setContext_sensitive(boolean context_sensitive) {
		this.context_sensitive = context_sensitive;
	}
}
