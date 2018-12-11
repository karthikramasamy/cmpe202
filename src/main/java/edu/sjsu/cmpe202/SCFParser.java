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

		if (args.length == 0) {
			throw new IllegalArgumentException("File name cannot be empty");
		}

		if (args.length < 1) {
			System.out.println("Usage SCFParser <filename>");
			System.exit(0);
		} else {

			byte[] scfDataArray = null;
			try {
				scfDataArray = readSCF(args[0]);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(0);
			}

			// Parse the header
			if (scfDataArray == null || scfDataArray.length < 315) {
				System.err.println("Invalid TLV File.");
			}

			byte[] headerBytes = Arrays.copyOfRange(scfDataArray, 0, 315);
			parseHeader(headerBytes);

			// Parse the body
			byte[] bodyBytes = Arrays.copyOfRange(scfDataArray, 316, scfDataArray.length);
			parseCertificates(bodyBytes);
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
		try {
			if (tlvDataArray.length < 315) {
				System.err.println("Invalid TLV File.");
			}

			byte[] headerBytes = Arrays.copyOfRange(tlvDataArray, 0, 315);

			List<TLV> tlvHeader = TLVParser.parseTLV(headerBytes);

			System.out.println("\n SCF Header:");
			System.out.println(" ===========");

			for (TLV tlv : tlvHeader) {
				if (tlv.getTag().equalsIgnoreCase("0x04") || tlv.getTag().equalsIgnoreCase("0x06")
						|| tlv.getTag().equalsIgnoreCase("0x0E")) {
					System.out.println(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
				} else {
					System.out.println(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			System.out.println("Error parsing the TLV from SCF File. " + ex.getMessage());
		}
	}

	public static void parseCertificates(byte[] bodyBytes) {
		try {
			List<TLV> tlvBody = TLVParser.parseTLV(bodyBytes);

			System.out.println("\n SCF Body:");
			System.out.println(" =========");

			int i = 0;

			for (TLV tlv : tlvBody) {

				if (tlv.getTag().equalsIgnoreCase("0x01")) {
					System.out.println("\n Certificate: " + ++i);
					System.out.println(" ---------------");
				}

				if (tlv.getTag().equalsIgnoreCase("0x02") || tlv.getTag().equalsIgnoreCase("0x03")
						|| tlv.getTag().equalsIgnoreCase("0x05")) {
					System.out.println(tlv.getTag() + " : " + new String((byte[]) tlv.getValue()));
				} else {
					System.out.println(tlv.getTag() + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			System.out.println("Error parsing the TLV from SCF File. " + ex.getMessage());
		}

	}
}
