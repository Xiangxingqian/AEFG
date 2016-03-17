package tags;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

public class FlagTag implements Tag{
	public final static int UNDO = 0;
	public final static int DOING = 1;
	public final static int DONE = 2;
	
	int flag = UNDO;
	public FlagTag(int flag) {
		this.flag = flag;
	}
	
	@Override
	public String getName() {
		return "FlagTag";
	}

	@Override
	public byte[] getValue() throws AttributeValueException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getValues(){
		return flag;
	}
	
	public FlagTag getFlagTag(){
		return this;
	}
}
