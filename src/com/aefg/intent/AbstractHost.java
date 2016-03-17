package com.aefg.intent;

import java.util.List;

import soot.tagkit.Host;
import soot.tagkit.Tag;

public abstract class AbstractHost implements Host {

	Tag tag;
	@Override
	public List<Tag> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag getTag(String aName) {
		return tag;
	}

	@Override
	public void addTag(Tag t) {
		tag = t;
	}

	@Override
	public void removeTag(String name) {
		tag = null;
		
	}

	@Override
	public boolean hasTag(String aName) {
		return tag!=null;
	}

	@Override
	public void removeAllTags() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllTagsOf(Host h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getJavaSourceStartLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getJavaSourceStartColumnNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public abstract Object getValue();

}
