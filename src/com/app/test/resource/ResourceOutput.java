package com.app.test.resource;

import java.io.Serializable;

/**
 * ResourceOutput aims to collect UI element info who declares callback in xml. 
 * <p>
 * An UI element Info contains six elements = {UIName, UIId, callback, callbackName, UI's belonging layoutName, layoutId};</p>
 * 
 * For example, {"com.adobe.reader.framework.ui.FWFloatingActionButton","2131231039","onClick","getFileChooser","layout/split_pane.xml","0x7f03004f"};
 * */
public class ResourceOutput implements Serializable{

	String layoutName;
	int layoutId;
	String callBack;
	String callBackValue;
	String UIName;
	int UIid;
	
	public ResourceOutput(String layoutName, int layoutId, String callBack,
			String callBackValue, String elementName,int UIid) {
		super();
		this.layoutName = layoutName;
		this.layoutId = layoutId;
		this.callBack = callBack;
		this.callBackValue = callBackValue;
		this.UIName = elementName;
		this.UIid = UIid;
	}
	
	public String getLayoutName() {
		return layoutName;
	}
	public int getLayoutId() {
		return layoutId;
	}
	public String getCallBack() {
		return callBack;
	}
	public String getCallBackValue() {
		return callBackValue;
	}
	public String getElementName() {
		return UIName;
	}

	public int getUIid() {
		return UIid;
	}

	@Override
	public String toString() {
		return "ResourceOutput [layoutName=" + layoutName + ", layoutId="
				+ layoutId + ", callBack=" + callBack + ", callBackValue="
				+ callBackValue + ", elementName=" + UIName + ", UIid="
				+ UIid + "]";
	}


}
