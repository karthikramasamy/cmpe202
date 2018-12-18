package edu.sjsu.cmpe202;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SCFParser {

	private static final Logger LOG = LoggerFactory.getLogger(SCFParser.class);

	private static final PathMatcher tlvFileMatcher = FileSystems.getDefault().getPathMatcher("glob:*.tlv");
	
	private static final TLVParser tlvParser = new TLVParser();
	
	private static final String BYTE_STRING = "bytes";

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			LOG.error("Usage SCFParser <filename>");
		} else {
			try {
				byte[] scfDataArray = readSCF(args[0]);
				
				if (scfDataArray == null || scfDataArray.length < 315) {
					LOG.error("Invalid TLV File.");
				} else {
					// Parse the header
					byte[] headerBytes = Arrays.copyOfRange(scfDataArray, 0, 315);
					parseHeader(headerBytes);

					// Parse the body
					byte[] bodyBytes = Arrays.copyOfRange(scfDataArray, 316, scfDataArray.length);
					parseCertificates(bodyBytes);
				}
			} catch (IOException ex) {
				LOG.error("Error reading SCF. {}", ex.getMessage());
			} catch (Exception ex) {
				LOG.error(ex.getMessage());
			}
		}
	}

	public static byte[] readSCF(String scfPath) throws IOException {

		Path tlvPath = Paths.get(scfPath);
		
		if (!tlvFileMatcher.matches(tlvPath.getFileName())) {
			throw new IllegalArgumentException("Couldn't load SCF. Given input is not a '.tlv' file.");
		}
		
		File tlvFile = tlvPath.toFile();

		if (!tlvFile.exists()) {
			throw new IllegalArgumentException("Couldn't load SCF. File doesnt exisit.");
		}

		return Files.readAllBytes(tlvFile.toPath());
	}

	public static void parseHeader(byte[] tlvDataArray) {

		if (tlvDataArray.length < 315) {
			throw new IllegalArgumentException("Invalid Input. Not enough data to parse header.");
		}

		try {
			List<TLV> tlvHeader = tlvParser.parse(Arrays.copyOfRange(tlvDataArray, 0, 315));

			printMessage("\n SCF Header:");
			printMessage(" ===========");

			for (TLV tlv : tlvHeader) {
				if (tlv.getTag().equalsIgnoreCase("0x04") || tlv.getTag().equalsIgnoreCase("0x06") || tlv.getTag().equalsIgnoreCase("0x0E")) {
					printMessage(tlv.getTag() + " : " + tlv.getLength() + BYTE_STRING + " : " + new String((byte[]) tlv.getValue()));
				} else {
					printMessage(tlv.getTag() + " : " + tlv.getLength() + BYTE_STRING + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			throw new IllegalArgumentException("Error parsing the SCF Header from SCF File. " + ex.getMessage());
		}
	}

	public static void parseCertificates(byte[] bodyBytes) {
		try {
			List<TLV> tlvBody = tlvParser.parse(bodyBytes);

			printMessage("\n SCF Body:");
			printMessage(" =========");

			int i = 0;

			for (TLV tlv : tlvBody) {

				if (tlv.getTag().equalsIgnoreCase("0x01")) {
					printMessage("\n Certificate: " + ++i);
					printMessage(" ---------------");
				}

				if (tlv.getTag().equalsIgnoreCase("0x02") || tlv.getTag().equalsIgnoreCase("0x03") || tlv.getTag().equalsIgnoreCase("0x05")) {
					printMessage(tlv.getTag() + " : " + tlv.getLength() + BYTE_STRING + " : " + new String((byte[]) tlv.getValue()));
				} else {
					printMessage(tlv.getTag() + " : " + tlv.getLength() + BYTE_STRING + " : " + Hex.encodeHexString((byte[]) tlv.getValue()));
				}
			}
		} catch (TLVParserException ex) {
			throw new IllegalArgumentException("Error parsing the SCF Body from SCF File. " + ex.getMessage());
		}
	}

	private static void printMessage(String message) {
		LOG.info(message);
	}
}
