package component;

import java.util.ArrayList;
import java.util.List;

import polyglot.ast.Return;

import soot.Scene;
import soot.SootClass;

public enum Component {
	Activity("Activity"),Fragment("Fragment"),View("View"),ViewGroup("ViewGroup");
	String name;

	Component(String name){
		this.name = name;
	}
	public String getValue(){
		return name;
	}
	private static Component parseSuperClasses(List<String> superClassesNames){
		Component c = null;
		if(superClassesNames.contains(Activity.getValue())){
			c = Activity;
		}
		else if(superClassesNames.contains(Fragment.getValue())){
			c = Fragment;
		}		
		else if(superClassesNames.contains(ViewGroup.getValue())){
			c = ViewGroup;
		}
		else if(superClassesNames.contains(View.getValue())){
			c = View;
		}
		return c;
	}
	
	public static Component parseComponent(SootClass sc){
		List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOf(sc);
		List<String> names = new ArrayList<String>();
		for(SootClass superClass:superClasses){
			String name = superClass.getShortJavaStyleName();
			names.add(name);
		}
		return Component.parseSuperClasses(names);
	}
	
//	public String toString(){
//		return getValue()+" Com is: "+getActualName();
//	}
}
