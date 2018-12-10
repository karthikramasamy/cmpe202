package edu.sjsu.cmpe202;

import java.io.File;

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
			File tlvFile = new File(args[0]);
			if (tlvFile.exists()) {
				helper.parse(tlvFile);
			}			
		}		
	}
}
