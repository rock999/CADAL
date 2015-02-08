package com.cadal.model;

public class OperationStatus {

	// outOfBound:1 operationFailed:-1 InvalidAccount:-100 NogLogin:-2
	public static int SUCESSED = 1;
	public static int COMMONFAILED = -1;
	public static int INVALIDACCOUNT = -100;
	public static int INVALIDIP = -200;
	public static int NOTLOGIN = -2;
	public static int OUTOFBOUND = 1;

	// opera result
	public Object result = 0;
	// opera status
	int status = 0;
	// opera message result
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
