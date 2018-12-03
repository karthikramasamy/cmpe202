package edu.sjsu.cmpe202;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;

import edu.sjsu.cmpe202.util.TLVParser;
import edu.sjsu.cmpe202.util.TLVParserException;

public class SCFParser {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Usage SCFParser <filename>");
			System.exit(0);
		} else {
			try {
				File tlvFile = new File(args[0]);
				if (tlvFile.exists()) {
					byte[] array = Files.readAllBytes(tlvFile.toPath());
					Map<String, Object> tlvContent = TLVParser.parseTLV(array);
					
					for (Entry<String, Object> entry : tlvContent.entrySet()) {
						System.out.println(entry.getKey() + " : " + Hex.encodeHexString((byte[]) entry.getValue()));
						
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
