package com.cadal.model;
/**
 * AnsyResultItem与AnsyStatusItem类与AnsyTaskEngine里的相同
 * 方便在传递时，使用整体对象传输，而不是将结果拼接在PageResultOfSearch里面
 * 也便于接收�?进行处理
 *
 */
public class AnsyResultItem {
	long nodeID;
	long parentID;
	long taskID;
	int layerDepth;
	boolean expendable;
	long childrenNum;
	String contentXML;
	String contentHash;
	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public long getNodeID() {
		return nodeID;
	}

	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}

	public long getParentID() {
		return parentID;
	}

	public void setParentID(long parentID) {
		this.parentID = parentID;
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	public int getLayerDepth() {
		return layerDepth;
	}

	public void setLayerDepth(int layerDepth) {
		this.layerDepth = layerDepth;
	}

	public boolean isExpendable() {
		return expendable;
	}

	public void setExpendable(boolean expendable) {
		this.expendable = expendable;
	}



	public long getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(long childrenNum) {
		this.childrenNum = childrenNum;
	}

	public String getContentXML() {
		return contentXML;
	}

	public void setContentXML(String contentXML) {
		this.contentXML = contentXML;
	}

}
