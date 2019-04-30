package model;

import model.utility.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceReader {

	private static SourceReader instance = null;

	private SourceReader() {
	}

	/**
	 *
	 * @Author BlueLort SINGLETON DESIGN PATTERN
	 * @return the only single instance from this class.
	 */
	public static SourceReader getInstance() {
		if (instance == null)
			instance = new SourceReader();
		return instance;
	}

	/**
	 * @Author BlueLort
	 * @param filePath
	 *            the read file path.
	 *
	 * @return array list which each index contains the same line order from the
	 *         file.
	 */
	@SuppressWarnings("resource")
	public ArrayList<String> readFile(String filePath) {
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			ArrayList<String> lines = new ArrayList<>();
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				lines.add(currentLine);
			}
			return lines;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	/**
	 * @Author BlueLort
	 * @param fileInfo
	 *            the lines read from file usually after using this.readFile()
	 * @param isRestricted
	 *            to determine should i use restricted or free format mode in
	 *            reading the source code.
	 *
	 * @return array list which each index contains the same line order from the
	 *         file.
	 */
	public CommandInfo processFile(ArrayList<String> fileInfo, boolean isRestricted) {
		if (isRestricted) {
			return processRestricted(fileInfo);
		}
		return processFreeFormat(fileInfo);
	}

	/**
	 * @Author BlueLort usually make a sub strings from each line and assign them
	 *         accordingly to the suitable instruction info. (using zero indexing)
	 *         0-> 7 label 8 empty/space 9-> 15 mnemonic 16 empty/space 17->34
	 *         operand 35->65 comment
	 * @param fileInfo
	 *            the lines read from file usually after using this.readFile()
	 *
	 * @return command info which have arraylists with correct instructions
	 *         information later converted to Line.
	 */
	private CommandInfo processRestricted(ArrayList<String> fileInfo) {
		CommandInfo CI = new CommandInfo();
		int len = fileInfo.size();
		String Spaces = "";
		/**
		 * Temporary variable that holds 67 spaces to check if line have 1 space at
		 * least as anyline that has less than 67 chars will be filled with spaces
		 */
		while (Spaces.length() < 67)
			Spaces += " ";

		for (int i = 0; i < len; i++) {

			String currentLine = fileInfo.get(i);
			while (currentLine.length() < 67)
				currentLine += " ";
			if (currentLine.equals("") || currentLine.equals(Spaces))
				continue;
			CI.addWholeInstruction(currentLine);
			/**
			 * this line is prefixed with comment so should be avoided however i had to add
			 * defaults to CI so it can be processed normally and so comment is printed not
			 * ignored
			 *
			 * TL;DR:All ArrayLists should have same size.
			 */
			if (currentLine.charAt(0) == '.') {
				CI.addDefaults();
				CI.addComment(currentLine);
				continue;
			}
			CI.addMatchedInstruction(Utility.removeExtraSpaces(currentLine.substring(0, 66)));
			CI.addLabel(Utility.removeExtraSpaces(currentLine.substring(0, 8)));
			CI.addCommand(Utility.removeExtraSpaces(currentLine.substring(9, 16)));
			String operand = Utility.removeExtraSpaces(currentLine.substring(17, 35));
			/**
			 * get addressing mode - operand 1 - operand 2 values from operand string and
			 * them to CI Accordingly
			 **/
			Utility.processOperand(operand, CI);
			CI.addComment(currentLine.substring(35, 67));
		}
		return CI;
	}

	/**
	 * @Author BlueLort tries to match and get instruction information from a line
	 *         using regex. to test regex i recommend going into this site
	 *         https://regexr.com/ paste regex in expression field and test case in
	 *         text field then look at explanation & details to know how the regex
	 *         used or what did it match. TODO make it loop on each string in word
	 *         instead of this way. TODO REFACTOR THE MANY IFs ELSEs statements
	 * @param fileInfo
	 *            the lines read from file usually after using this.readFile()
	 *
	 * @return command info which have arraylists with correct instructions
	 *         information later converted to Line.
	 */
	private CommandInfo processFreeFormat(ArrayList<String> fileInfo) {
		CommandInfo CI = new CommandInfo();

		/** regex which is used to match the instruction information **/
		String regex = "(?:[\\s|\\t]+)?([a-zA-Z0-9_]+)?(?:[\\s|\\t]+)([a-zA-Z+\\-]+)(?:[ |\\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_']+)?(?:(?:[ |\\t]+)(\\S+)?)";
		/** compile the regex using Java regex engine */
		Pattern reg = Pattern.compile(regex);
		int len = fileInfo.size();
		for (int i = 0; i < len; i++) {
			String currentLine = fileInfo.get(i);
			CI.addWholeInstruction(currentLine);
			/**
			 * this line is prefixed with comment so should be avoided however i had to add
			 * defaults to CI so it can be processed normally and so comment is printed not
			 * ignored
			 *
			 * TL;DR:All ArrayLists should have same size.
			 */
			if (currentLine.charAt(0) == '.') {
				CI.addDefaults();
				CI.addComment(currentLine);
				continue;
			}

			Matcher m = reg.matcher(currentLine);
			if (m.find()) {
				if (m.group(4) == null && Utility.isThatStringEqualAnyDirective(m.group(2))) {
					CI.addMatchedInstruction(m.group(0));
					CI.addLabel(m.group(1));
					CI.addCommand(m.group(2));
					CI.addOperand1(m.group(6));
					CI.addAddressMode("");
					CI.addOperand2("");
					CI.addComment("");

				} else if (m.group(4) == null && m.group(1) == null) {
					CI.addMatchedInstruction(m.group(0));
					CI.addLabel(m.group(1));
					CI.addCommand(m.group(2));
					String[] match6 = m.group(6).split(",");
					if (match6.length > 1) {
						if (match6[0].charAt(0) == '#' || match6[0].charAt(0) == '@') {
							CI.addAddressMode(match6[0].substring(0, 1));
							CI.addOperand1(match6[0].substring(1, match6[0].length()));
						} else {
							CI.addAddressMode("");
							CI.addOperand1(match6[0]);
						}
						CI.addOperand2(match6[1]);
						CI.addComment("");
					} else {
						CI.addOperand1(m.group(6));
						CI.addAddressMode("");
						CI.addOperand2(m.group(5));
						CI.addComment("");
					}

				} else if (m.group(4) == null) {
					CI.addMatchedInstruction(m.group(0));
					CI.addLabel("");
					CI.addCommand(m.group(1));
					CI.addOperand1(m.group(2));
					CI.addAddressMode("");
					CI.addOperand2(m.group(5));
					CI.addComment(m.group(6));
				} else {
					CI.addLabel(m.group(1));
					CI.addCommand(m.group(2));
					CI.addAddressMode(m.group(3));
					CI.addOperand1(m.group(4));
					CI.addOperand2(m.group(5));
					CI.addComment(m.group(6));
				}
			} else {
				CI.addDefaults();
				CI.addComment("");
			}
		}
		return CI;

	}
}
