/*******************************************************************************
 * Copyright (c) 2012 Secure Software Engineering Group at EC SPRIDE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors: Christian Fritz, Steven Arzt, Siegfried Rasthofer, Eric
 * Bodden, and others.
 ******************************************************************************/
package infoflow.android.resources;

import soot.SootClass;

/**
 * Data class representing a layout control on the android screen
 * 
 * @author Steven Arzt
 *
 */
public class LayoutControl {
	
	private final int id;
	private final SootClass viewClass;
	private boolean isSensitive;
	
	public LayoutControl(int id, SootClass viewClass) {
		this.id = id;
		this.viewClass = viewClass;
	}
	
	public LayoutControl(int id, SootClass viewClass, boolean isSensitive) {
		this(id, viewClass);
		this.isSensitive = isSensitive;
	}
	
	public int getID() {
		return this.id;
	}
	
	public SootClass getViewClass() {
		return this.viewClass;
	}
	
	public void setIsSensitive(boolean isSensitive) {
		this.isSensitive = isSensitive;
	}
	
	public boolean isSensitive() {
		return this.isSensitive;
	}

}
