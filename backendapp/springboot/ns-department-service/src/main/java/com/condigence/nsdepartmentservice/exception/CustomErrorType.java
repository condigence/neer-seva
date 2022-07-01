package com.condigence.nsdepartmentservice.exception;

public class CustomErrorType {

	private String errorMessage;

	private String errorType;

	public CustomErrorType(String errorMessage, String errorType) {
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}