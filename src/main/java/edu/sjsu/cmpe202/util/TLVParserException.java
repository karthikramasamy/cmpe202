package edu.sjsu.cmpe202.util;

/**
 * 
 * Generic exception to represent any errors occurred in TLVParser
 * 
 * @author Karthik Ramasamy
 * 
 */

public class TLVParserException extends Exception {

	private static final long serialVersionUID = -614547789942051993L;

	public TLVParserException() {
		super();
	}

	public TLVParserException(String message) {
		super(message);
	}

	public TLVParserException(Throwable cause) {
		super(cause);
	}

	public TLVParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
