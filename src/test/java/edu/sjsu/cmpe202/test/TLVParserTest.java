package edu.sjsu.cmpe202.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Base64;
import java.util.List;

import org.junit.Test;

import edu.sjsu.cmpe202.TLV;
import edu.sjsu.cmpe202.TLVParser;
import edu.sjsu.cmpe202.TLVParserException;

public class TLVParserTest {
	
	private final TLVParser tlvParser = new TLVParser();

	@Test
	public void testParseTLVWithValidInput() {

		String tlvSample = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0FAAQAAAAB";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		try {
			List<TLV> tlvList = tlvParser.parse(tlvData);
			assertNotNull(tlvList);
			for (TLV tlv : tlvList) {
				assertNotNull(tlv.getTag());
				assertNotNull(tlv.getLength());
				assertNotNull(tlv.getValue());
			}
		} catch (TLVParserException ex) {
			fail(ex.getMessage());
		}
	}

	@Test(expected = TLVParserException.class)
	public void testParseTLVWithInvalidInput1() throws TLVParserException {

		String tlvSample = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		tlvParser.parse(tlvData);
	}

	@Test(expected = TLVParserException.class)
	public void testParseTLVWithInvalidInput2() throws TLVParserException {

		String tlvSample = "AQAEAAAAAQECAAQAAAAAAwAIECAwQCBQYkwEABCt8R8wM802N17aL2DcDDY=";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		tlvParser.parse(tlvData);
	}

	@Test(expected = TLVParserException.class)
	public void testParseTLVWithInvalidInput3() throws TLVParserException {

		String tlvSample = "AQAEAAAAAQIABAAAAAADAAgQIDBAIFBiTAQAEK3xHzAzzTY3XtovYNwMNu0F";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		tlvParser.parse(tlvData);
	}

	public void testParseTLVWithEmptyInput() throws TLVParserException {

		String tlvSample = "";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		List<TLV> tlvList = tlvParser.parse(tlvData);
		assertTrue(tlvList.size() == 0);
	}
}
