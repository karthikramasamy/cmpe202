package edu.sjsu.cmpe202.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

		String tlvSample = "AQACAQICAAIBOAMAAgB/BAA3Q049Z2lnYW50aWMtNi5jaXNjby5jb207T1U9VlRHO089QWxwaGE7TD1TSjtTVD1DQTtDPVVTAAUACCRJHxBwP+qbBgA3Q049Z2lnYW50aWMtNi5jaXNjby5jb207T1U9VlRHO089QWxwaGE7TD1TSjtTVD1DQTtDPVVTAAcAAgAPCAABAQkACAoAAQALAAEBDACANW3w+KrxBW73qxtdPt+EXvNJW085lXaZuwYWgOje+wikT7TD9yq+hgpwiDarChdSsM0aYIAprnpHqJU+7ydlPnxw2+BTqm6fJnflXoN81tlmrsW7bryiA+49B3qQBKIVi5vD9bLNRufjoifJGXxl6S6fvALzmat4w75c6S1yULwOAAxDVExGaWxlLnRsdgAPAARVSte3";
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

	@Test
	public void testParseTLVWithEmptyInput() throws TLVParserException {

		String tlvSample = "";
		byte[] tlvData = Base64.getDecoder().decode(tlvSample);
		List<TLV> tlvList = tlvParser.parse(tlvData);
		assertEquals(0, tlvList.size());
	}
}
