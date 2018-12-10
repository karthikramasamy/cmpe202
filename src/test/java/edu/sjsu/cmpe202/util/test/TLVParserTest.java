package edu.sjsu.cmpe202.util.test;

import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Base64;

import org.junit.Test;

import edu.sjsu.cmpe202.ParseHelper;
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
	public void testParseTLVWithInvalidInput1() throws TLVParserException {
		
		String tlvSample2 = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu";
		byte[] tlvData2 = Base64.getDecoder().decode(tlvSample2);		
		
		String tlvSample1 = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0F";
		byte[] tlvData1 = Base64.getDecoder().decode(tlvSample1);
		
		TLVParser.parseTLV(tlvData2);
		TLVParser.parseTLV(tlvData1);		
	}
	
	@Test(expected = TLVParserException.class)
	public void testParseTLVWithInvalidInput2() throws TLVParserException {
		
		String tlvSample = "AQAEAAAAAQECAAQAAAAAAwAIECAwQCBQYkwEABCt8R8wM802N17aL2DcDDY=";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		TLVParser.parseTLV(tlvData);
	}

	@Test(expected = TLVParserException.class)
	public void testParseTLVWithEmptyInput() throws TLVParserException {
		
		String tlvSample = "";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		TLVParser.parseTLV(tlvData);
	}
	
	@Test
	public void testParseHelper() {
		ParseHelper helper = new ParseHelper();
		String tlvSample1 = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0FAAQAAAAB";
		byte[] tlvData1 = Base64.getDecoder().decode(tlvSample1);
		
		helper.parseTLVHeader(tlvData1);
		helper.parseTLVBody(tlvData1);
		
		try{
			File temp = File.createTempFile("tempfile", ".tmp"); 			
			BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		    bw.write(tlvSample1);
		    helper.parse(temp);
		    bw.close();
		} catch(Exception e) {
			
		}	    		
	}

}
