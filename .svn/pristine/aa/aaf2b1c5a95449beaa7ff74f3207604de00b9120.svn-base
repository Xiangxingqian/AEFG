package tags;

import java.util.ArrayList;
import java.util.List;

import soot.Value;
import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

public class ConditionTag implements Tag {

	List<Value> values = new ArrayList<Value>();
	public ConditionTag(List<Value> values) {
		if(values!=null)
			this.values = values;
	}
	
	@Override
	public String getName() {
		return "ConditionTag";
	}

	@Override
	public byte[] getValue() throws AttributeValueException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Value> getValues(){
		return values;
	}
	
//	public boolean getFlag(){
//		return flag;
//	}
	
	public ConditionTag getConditionTag(){
		return this;
	}
}
