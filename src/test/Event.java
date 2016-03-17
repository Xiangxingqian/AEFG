package test;

import java.io.Serializable;

public class Event implements Serializable{
	String activity;
	String view;
	String eventType;
	public Event(String activity, String view, String eventType) {
		this.activity = activity;
		this.view = view;
		this.eventType = eventType;
	}
	public String getActivity() {
		return activity;
	}
	public String getView() {
		return view;
	}
	public String getEventType() {
		return eventType;
	}
	
}
