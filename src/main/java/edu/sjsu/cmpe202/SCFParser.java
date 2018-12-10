package edu.sjsu.cmpe202;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;


public class SCFParser {

	public static void main(String[] args) {
		
		ParseHelper helper = new ParseHelper();
		
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
					helper.parseTLVHeader(headerBytes);

					//Parse the body					
					byte[] bodyBytes = Arrays.copyOfRange(tlvDataArray, 316, tlvDataArray.length);
					helper.parseTLVBody(bodyBytes);
				}
			} catch (IOException ex) {
				System.out.println("Error reading the SCF File. " + ex.getMessage());				
			} 
		}		
	}
}
