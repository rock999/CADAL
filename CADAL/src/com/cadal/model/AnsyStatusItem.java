package com.cadal.model;

import java.io.Serializable;

public class AnsyStatusItem implements Serializable{
	long taskID;
	long templateID;
	String userID;
	long maxNodeNum;
	int maxLayerDepth;
	int currentLayerDepth;
	boolean finished;
    String serviceType;
    boolean isAnsy;
    String configXML;
    
	public String getConfigXML() {
		return configXML;
	}

	public void setConfigXML(String configXML) {
		this.configXML = configXML;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public boolean getIsAnsy() {
		return isAnsy;
	}

	public void setIsAnsy(boolean isAnsy) {
		this.isAnsy = isAnsy;
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	public long getTemplateID() {
		return templateID;
	}

	public void setTemplateID(long templateID) {
		this.templateID = templateID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public long getMaxNodeNum() {
		return maxNodeNum;
	}

	public void setMaxNodeNum(long maxNodeNum) {
		this.maxNodeNum = maxNodeNum;
	}

	public int getMaxLayerDepth() {
		return maxLayerDepth;
	}

	public void setMaxLayerDepth(int maxLayerDepth) {
		this.maxLayerDepth = maxLayerDepth;
	}

	public int getCurrentLayerDepth() {
		return currentLayerDepth;
	}

	public void setCurrentLayerDepth(int currentLayerDepth) {
		this.currentLayerDepth = currentLayerDepth;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
