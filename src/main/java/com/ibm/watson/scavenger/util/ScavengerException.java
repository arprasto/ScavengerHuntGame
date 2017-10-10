package com.ibm.watson.scavenger.util;

public class ScavengerException extends Exception {

	public ScavengerException() {
	}

	public ScavengerException(String message) {
		super(message);
	}

	public ScavengerException(Throwable cause) {
		super(cause);
	}

	public ScavengerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScavengerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
