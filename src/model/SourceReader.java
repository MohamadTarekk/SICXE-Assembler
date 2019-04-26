package model;

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

	public static SourceReader getInstance() {
		if (instance == null)
			instance = new SourceReader();
		return instance;
	}

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

	public CommandInfo processFile(ArrayList<String> fileInfo) {
		// need to match for commands , labels , ...
		CommandInfo CI = CommandInfo.getInstance();
		String regex = "(?:[\\s|\\t]+)?([a-zA-Z0-9_]+)?(?:[\\s|\\t]+)([a-zA-Z0-9_']+)(?:[ |\\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_]+)?";
		Pattern reg = Pattern.compile(regex);
		int len = fileInfo.size();
		for (int i = 0; i < len; i++) {

			if (fileInfo.get(i).charAt(0) == '.')
				continue;
			Matcher m = reg.matcher(fileInfo.get(i));
			CI.addWholeInstruction(fileInfo.get(i));
			if (m.find()) {
				if (m.group(4) == null) {
					CI.addMatchedInstruction(m.group(0));
					CI.addLabel("");
					CI.addCommand(m.group(1));
					CI.addOperand1(m.group(2));
					CI.addAddressMode("");
					CI.addOperand2(m.group(5));
				} else {
					CI.addLabel(m.group(1));
					CI.addCommand(m.group(2));
					CI.addAddressMode(m.group(3));
					CI.addOperand1(m.group(4));
					CI.addOperand2(m.group(5));
				}
			} else {
				CI.addDefaults();
			}
		}
		return CI;
	}
}
