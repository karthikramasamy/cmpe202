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

	private int tagByteSize = 1;
	private int lengthByteSize = 2;

	private int tagPlusLengthByteSize = tagByteSize + lengthByteSize;

	public TLVParser() {
		super();
	}
	
	public TLVParser(int tagByteSize, int lengthByteSize) {
		super();
		this.tagByteSize = tagByteSize;
		this.lengthByteSize = lengthByteSize;
		this.tagPlusLengthByteSize = tagByteSize + lengthByteSize;
	}

	/**
	 * Parse the given byte array to extract the TLV fields into a List of TLV
	 * 
	 * @param tlvData
	 * @return
	 * @throws TLVParserException
	 */
	public List<TLV> parse(byte[] tlvData) throws TLVParserException {

		List<TLV> parsedTLV = new ArrayList<>();
		
		if (tlvData == null || tlvData.length == 0) {
			return parsedTLV;
		} 
		
		byte[] remainingTLV = tlvData;

		do {
			// To start with, we need at least 3 bytes for parsing the tag and length
			if (remainingTLV.length < tagPlusLengthByteSize) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			String tag = Hex.encodeHexString(Arrays.copyOfRange(remainingTLV, 0, tagByteSize), false);
			int length = new BigInteger(Arrays.copyOfRange(remainingTLV, tagByteSize, tagPlusLengthByteSize)).intValue();
			
			// Check if length is a positive value
			if (length < 0) {
				throw new TLVParserException("Invalid Input. The length specified for tag 0x" + tag + " is not a positive integer.");
			}

			// Now check if we have enough bytes for the value of specified length
			if (remainingTLV.length < tagPlusLengthByteSize + length) {
				throw new TLVParserException("Invalid Input. The TLV data doesn't match the specified size.");
			}

			byte[] value = Arrays.copyOfRange(remainingTLV, tagPlusLengthByteSize, tagPlusLengthByteSize + length);

			parsedTLV.add(new TLV("0x" + tag, length, value));

			remainingTLV = Arrays.copyOfRange(remainingTLV, tagPlusLengthByteSize + length, remainingTLV.length);
		} while (remainingTLV.length > 0);

		return parsedTLV;
	}
}
