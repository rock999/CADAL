package com.cadal.model;

import java.io.Serializable;

public class ServiceItem implements Serializable {

	int serviceID;
	String serviceType;// 服务类型
	// String subType;// 服务子类�?
	String configInfo;
	boolean isAnsy;// 是否为异步服�?
	// String features;
	int status;// 状�?

	// double score;// 匹配�?
	// long timeStamp;// 时间�?

	public ServiceItem() {
	}

	public ServiceItem(int serviceID, String serviceType, String configInfo,
			boolean isAnsy, int status) {
		this.serviceID = serviceID;
		this.serviceType = serviceType;
		this.configInfo = configInfo;
		this.isAnsy = isAnsy;
	}

	public boolean isAnsy() {
		return isAnsy;
	}

	public void setAnsy(boolean isAnsy) {
		this.isAnsy = isAnsy;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(String configInfo) {
		this.configInfo = configInfo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
