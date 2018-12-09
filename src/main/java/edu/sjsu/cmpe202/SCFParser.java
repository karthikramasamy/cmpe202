package edu.sjsu.cmpe202;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;

import edu.sjsu.cmpe202.util.TLV;
import edu.sjsu.cmpe202.util.TLVParser;
import edu.sjsu.cmpe202.util.TLVParserException;

public class SCFParser {

	public static void main(String[] args) {
		
		if(args.length == 0) {
			throw new IllegalArgumentException("File name cannot be empty");
		}
		if(!args[0].contains(".tlv")) {
			System.out.println(args[0]);
			throw new IllegalArgumentException("Input should be a TLV file");
		}

		if (args.length < 1) {
			System.out.println("Usage SCFParser <filename>");
			System.exit(0);
		} else {
			try {
				File tlvFile = new File(args[0]);
				if (tlvFile.exists()) {
					byte[] tlvDataArray = Files.readAllBytes(tlvFile.toPath());
					
					
					//Parse the header
					
					if (tlvDataArray.length < 315) {
						System.err.println("Invalid TLV File.");
					}
					
					byte[] headerBytes = Arrays.copyOfRange(tlvDataArray, 0, 315);
					
					List<TLV> tlvHeader = TLVParser.parseTLV(headerBytes);
					
					System.out.println("\n SCF Header:");
					System.out.println(" ===========");
					
					for (TLV tlv : tlvHeader) {
						if (tlv.getTag().equalsIgnoreCase("0x04") || tlv.getTag().equalsIgnoreCase("0x06") || tlv.getTag().equalsIgnoreCase("0x0E")) {
							System.out.println(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
						} else {
							System.out.println(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
						}
					}

					//Parse the body
					
					byte[] bodyBytes = Arrays.copyOfRange(tlvDataArray, 316, tlvDataArray.length);
					

					List<TLV> tlvBody = TLVParser.parseTLV(bodyBytes);

					System.out.println("\n SCF Body:");
					System.out.println(" =========");
					
					int i = 0;

					for (TLV tlv : tlvBody) {
						
						if (tlv.getTag().equalsIgnoreCase("0x01")) {
							System.out.println("\n Certificate: " + ++i);
							System.out.println(" ---------------");
						}
						
						if (tlv.getTag().equalsIgnoreCase("0x02") || tlv.getTag().equalsIgnoreCase("0x03") || tlv.getTag().equalsIgnoreCase("0x05")) {
							System.out.println(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
						} else {
							System.out.println(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
						}
					}
				}
			} catch (IOException ex) {
				System.out.println("Error reading the SCF File. " + ex.getMessage());
				
			} catch (TLVParserException ex) {
				System.out.println("Error parsing the TLV from SCF File. " + ex.getMessage());
			}
		}
		
	}

}
