package com.condigence.imageservice.util;

/**
 * Legacy error type used in some responses. Prefer {@link com.condigence.imageservice.exception.ErrorResponse}
 * for new APIs.
 */
public class CustomErrorType {

	private String errorMessage;

	private String errorType;

	public CustomErrorType(String errorMessage) {
		this.errorMessage = errorMessage;
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