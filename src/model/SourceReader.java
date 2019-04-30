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

	public CommandInfo processFile(ArrayList<String> fileInfo,boolean isRestricted) {
        CommandInfo CI = CommandInfo.getInstance();

        // need to match for commands , labels , ...
        if (isRestricted) {
            int len = fileInfo.size();
            for (int i = 0; i < len; i++) {
                String currentLine = fileInfo.get(i);
                if(currentLine.equals(""))continue;
                while(currentLine.length()<67)currentLine+=" ";
                CI.addWholeInstruction(currentLine);
                if (currentLine.charAt(0) == '.') {
                    CI.addDefaults();
                    CI.addComment(currentLine);
                    continue;
                }
                CI.addMatchedInstruction(Utility.removeExtraSpaces(currentLine.substring(0,66)));
                CI.addLabel(Utility.removeExtraSpaces(currentLine.substring(0,7) ));
                CI.addCommand(Utility.removeExtraSpaces(currentLine.substring(9,15)));
                String operand=Utility.removeExtraSpaces(currentLine.substring(17,35));
                String[] op1op2=operand.split(",");
                if (op1op2.length > 1) {
                    if (op1op2[0].charAt(0) == '#' || op1op2[0].charAt(0) == '@') {
                        CI.addAddressMode(op1op2[0].substring(0, 1));
                        CI.addOperand1(op1op2[0].substring(1, op1op2[0].length()));
                    } else {
                        CI.addAddressMode("");
                        CI.addOperand1(op1op2[0]);
                    }
                    CI.addOperand2(op1op2[1]);
                    CI.addComment("");
                } else {
                    CI.addOperand1(operand);
                    CI.addAddressMode("");
                    CI.addOperand2("");
                    CI.addComment("");
                }
            }
        } else {


            //old ver
            // (?:[\s|\t]+)?([a-zA-Z0-9_]+)?(?:[\s|\t]+)([a-zA-Z0-9_'+\-]+)(?:[ |\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_]+)?
            //with comments
            //(?:[\s|\t]+)?([a-zA-Z0-9_]+)?(?:[\s|\t]+)([a-zA-Z+\-]+)(?:[ |\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_']+)?(?:(?:[ |\t]+)(\S+)?)

            String regex = "(?:[\\s|\\t]+)?([a-zA-Z0-9_]+)?(?:[\\s|\\t]+)([a-zA-Z+\\-]+)(?:[ |\\t]+)?([@#]?)([a-zA-Z0-9_']+)?,?([a-zA-Z0-9_']+)?(?:(?:[ |\\t]+)(\\S+)?)";
            Pattern reg = Pattern.compile(regex);
            int len = fileInfo.size();
            for (int i = 0; i < len; i++) {
                String currentLine = fileInfo.get(i);
                CI.addWholeInstruction(currentLine);
                if (currentLine.charAt(0) == '.') {
                    CI.addDefaults();
                    CI.addComment(currentLine);
                    continue;
                }

                Matcher m = reg.matcher(currentLine);
                if (m.find()) {
                    if (m.group(4) == null && (m.group(2).equalsIgnoreCase("byte") || m.group(2).equalsIgnoreCase("resb")
                            || m.group(2).equalsIgnoreCase("word") || m.group(2).equalsIgnoreCase("resw")
                            || m.group(2).equalsIgnoreCase("equ") || m.group(2).equalsIgnoreCase("start")
                            || m.group(2).equalsIgnoreCase("org") || m.group(2).equalsIgnoreCase("base")
                            || m.group(2).equalsIgnoreCase("nobase") || m.group(2).equalsIgnoreCase("ltorg"))) {
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

        }
        return CI;
    }
    }

