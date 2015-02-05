package com.cadal.model;
public class OperationStatus {

	// outOfBound:1 operationFailed:-1 InvalidAccount:-100 NogLogin:-2
	public static int COMMONFAILED = -1;
	public static int INVALIDACCOUNT = -100;
	public static int INVALIDIP = -200;
	public static int NOTLOGIN = -2;
	public static int OUTOFBOUND = -1;

	// 操作状态
	int status = 0;
	// 提示信息
	String message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
