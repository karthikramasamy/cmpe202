package edu.sjsu.cmpe202.util.test;

import static org.junit.Assert.fail;

import java.util.Base64;

import org.junit.Test;

import edu.sjsu.cmpe202.util.TLVParser;
import edu.sjsu.cmpe202.util.TLVParserException;

public class TLVParserTest {

	@Test
	public void testParseTLVWithValidInput() {
		
		String tlvSample = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0FAAQAAAAB";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		try {
			TLVParser.parseTLV(tlvData);
		} catch (TLVParserException ex) {
			fail(ex.getMessage());
		}
	}
	
	@Test(expected = TLVParserException.class)
	public void testParseTLVWithInvalidInput() throws TLVParserException {
		
		String tlvSample = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0F";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		TLVParser.parseTLV(tlvData);
	}

	@Test(expected = TLVParserException.class)
	public void testParseTLVWithEmptyInput() throws TLVParserException {
		
		String tlvSample = "";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		TLVParser.parseTLV(tlvData);
	}

}
