package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import singlton.SingltonFactory;

public class Events implements Serializable{
	
	public Events(SingltonFactory.G g){}
	
	public static Events v(){
		return SingltonFactory.v().getEvents();
	}
	
	public List<Event> events = new ArrayList<Event>();

	public List<Event> getEvents() {
		return events;
	}
}
