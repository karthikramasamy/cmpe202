package edu.sjsu.cmpe202;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static List<TLV> parseTLV(byte[] tlvData) throws TLVParserException {

		List<TLV> parsedTLV = new ArrayList<TLV>();
		
		if (tlvData == null || tlvData.length == 0) {
			return parsedTLV;
		} 
		
		byte[] remainingTLV = tlvData;

		do {
			// To start with, we need at least 3 bytes for parsing the tag and length
			if (remainingTLV.length < TAG_PLUS_LEGNTH_BYTE_SIZE) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			String tag = Hex.encodeHexString(Arrays.copyOfRange(remainingTLV, 0, TAG_BYTE_SIZE), false);
			int length = new BigInteger(Arrays.copyOfRange(remainingTLV, TAG_BYTE_SIZE, TAG_PLUS_LEGNTH_BYTE_SIZE)).intValue();

			// Now check if we have enough bytes for the value of specified length
			if (remainingTLV.length < TAG_PLUS_LEGNTH_BYTE_SIZE + length) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			byte[] value = Arrays.copyOfRange(remainingTLV, TAG_PLUS_LEGNTH_BYTE_SIZE, TAG_PLUS_LEGNTH_BYTE_SIZE + length);

			parsedTLV.add(new TLV("0x" + tag, length, value));

			remainingTLV = Arrays.copyOfRange(remainingTLV, TAG_PLUS_LEGNTH_BYTE_SIZE + length, remainingTLV.length);
		} while (remainingTLV.length > 0);

		return parsedTLV;
	}
}
