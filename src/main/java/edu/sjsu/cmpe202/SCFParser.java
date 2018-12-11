package edu.sjsu.cmpe202;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

public class SCFParser {

	private static final PathMatcher tlvFileMatcher = FileSystems.getDefault().getPathMatcher("glob:*.tlv");

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			printErrorAndExit("Usage SCFParser <filename>", 101);
		} else {

			byte[] scfDataArray = null;
			try {
				scfDataArray = readSCF(args[0]);
			} catch (IOException ex) {
				printErrorAndExit("Error reading SCF. " + ex.getMessage(), 102);
			}

			if (scfDataArray == null || scfDataArray.length < 315) {
				printErrorAndExit("Invalid TLV File.", 103);
			} else {
				// Parse the header
				byte[] headerBytes = Arrays.copyOfRange(scfDataArray, 0, 315);
				parseHeader(headerBytes);

				// Parse the body
				byte[] bodyBytes = Arrays.copyOfRange(scfDataArray, 316, scfDataArray.length);
				parseCertificates(bodyBytes);
			}
		}
	}

	public static byte[] readSCF(String scfPath) throws IOException {

		File tlvFile = new File(scfPath);

		if (!tlvFile.exists()) {
			throw new IllegalArgumentException("Couldn't load SCF. File doesnt exisit.");
		}

		Path tlvPath = tlvFile.toPath();

		if (!tlvFileMatcher.matches(tlvPath.getFileName())) {
			throw new IllegalArgumentException("Couldn't load SCF. Given input is not a '.tlv' file.");
		}

		return Files.readAllBytes(tlvFile.toPath());
	}

	public static void parseHeader(byte[] tlvDataArray) {

		if (tlvDataArray.length < 315) {
			throw new IllegalArgumentException("Invalid Input. Not enough data to parse header.");
		}

		try {
			List<TLV> tlvHeader = TLVParser.parseTLV(Arrays.copyOfRange(tlvDataArray, 0, 315));

			printMessage("\n SCF Header:");
			printMessage(" ===========");

			for (TLV tlv : tlvHeader) {
				if (tlv.getTag().equalsIgnoreCase("0x04") || tlv.getTag().equalsIgnoreCase("0x06") || tlv.getTag().equalsIgnoreCase("0x0E")) {
					printMessage(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
				} else {
					printMessage(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			printMessage("Error parsing the TLV from SCF File. " + ex.getMessage());
		}
	}

	public static void parseCertificates(byte[] bodyBytes) {
		try {
			List<TLV> tlvBody = TLVParser.parseTLV(bodyBytes);

			printMessage("\n SCF Body:");
			printMessage(" =========");

			int i = 0;

			for (TLV tlv : tlvBody) {

				if (tlv.getTag().equalsIgnoreCase("0x01")) {
					printMessage("\n Certificate: " + ++i);
					printMessage(" ---------------");
				}

				if (tlv.getTag().equalsIgnoreCase("0x02") || tlv.getTag().equalsIgnoreCase("0x03") || tlv.getTag().equalsIgnoreCase("0x05")) {
					printMessage(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
				} else {
					printMessage(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			printMessage("Error parsing the TLV from SCF File. " + ex.getMessage());
		}

	}

	private static void printErrorAndExit(String message, int statusCode) {
		System.err.println(message);
		System.exit(statusCode);
	}

	private static void printMessage(String message) {
		System.out.println(message);
	}
}
