package edu.sjsu.cmpe202.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

/**
 * Generic TLV Parser which uses prefixed sizes for Tag (1 byte) and length (2 bytes)
 * 
 * @author Karthik Ramasamy
 * 
 */

public class TLVParser {

	private static final int TAG_BYTE_SIZE = 1;
	private static final int LENGTH_BYTE_SIZE = 2;

	private static final int TAG_PLUS_LEGNTH_BYTE_SIZE = TAG_BYTE_SIZE + LENGTH_BYTE_SIZE;

	private TLVParser() {
		super();
	}

	/**
	 * Parse the given byte array to extract the TLV fields into a map of tag and values
	 * 
	 * @param tlvData
	 * @return
	 * @throws TLVParserException
	 */
	public static Map<String, Object> parseTLV(byte[] tlvData) throws TLVParserException {

		Map<String, Object> parsedTlvData = new HashMap<String, Object>();

		do {
			// To start with, we need at least 3 bytes for parsing the tag and length
			if (tlvData.length < TAG_PLUS_LEGNTH_BYTE_SIZE) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			String tag = Hex.encodeHexString(Arrays.copyOfRange(tlvData, 0, TAG_BYTE_SIZE), false);
			int length = new BigInteger(Arrays.copyOfRange(tlvData, TAG_BYTE_SIZE, TAG_PLUS_LEGNTH_BYTE_SIZE)).intValue();

			// Now check if we have enough bytes for the value of specified length
			if (tlvData.length < TAG_PLUS_LEGNTH_BYTE_SIZE + length) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			byte[] value = Arrays.copyOfRange(tlvData, TAG_PLUS_LEGNTH_BYTE_SIZE, TAG_PLUS_LEGNTH_BYTE_SIZE + length);

			parsedTlvData.put("0x" + tag, value);

			tlvData = Arrays.copyOfRange(tlvData, TAG_PLUS_LEGNTH_BYTE_SIZE + length, tlvData.length);
		} while (tlvData.length > 0);

		return parsedTlvData;
	}
}
