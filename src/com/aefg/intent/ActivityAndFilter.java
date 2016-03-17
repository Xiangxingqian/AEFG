package com.aefg.intent;

import java.util.ArrayList;
import java.util.List;

public class ActivityAndFilter {
	private String activity;
	private List<IntentFilter> mFliter = new ArrayList<IntentFilter>();
	
	public ActivityAndFilter(){
		
	}
	
	public ActivityAndFilter(String activity, List<IntentFilter> mFliter) {
		this.activity = activity;
		this.mFliter = mFliter;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public List<IntentFilter> getmFliters() {
		return mFliter;
	}
	public void setmFliter(List<IntentFilter> mFliter) {
		this.mFliter = mFliter;
	}
	
	@Override
	public String toString() {
		return "activity=" + activity + ", mFliter="
				+ mFliter;
	}
}
