package tags;

import soot.Value;
import soot.tagkit.Tag;

public class ValueTag implements Tag{

	Value ob;
	public ValueTag(Value ob){
		this.ob = ob;
	}
	@Override
	public String getName() {
		return "ObjectTag";
	}

	@Override
	public byte[] getValue() {
        throw new RuntimeException( "AnnotationTag has no value for bytecode" );
    }
	
	public Value getValueK(){
		return ob;
	}
}
