package model.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CommandInfo;
import model.ErrorChecker;
import model.MyGenericsStack;
import model.ProgramCounter;
import model.enums.Format;
import model.tables.*;

public class Utility {

	public static String binToHex(String bin) {

		int decimal = Integer.parseInt(bin, 2);
		return convertToHexa(decimal);
	}

	public static String binToHex(String bin, Format format) {

		int decimal = Integer.parseInt(bin, 2);
		String res = convertToHexa(decimal);
		String zeros;
		if (format == Format.THREE)
			zeros = getZeros(6 - res.length());
		else
			zeros = getZeros(8 - res.length());
		res = zeros + res;
		return res;
	}

	public static String hexToBin(String hex) {

		String res = new BigInteger(hex, 16).toString(2);
		res = getZeros(20 - res.length()) + res;
		return res;
	}

	public static int hexToDecimal(String hex) {
		return Integer.parseInt(hex, 16);
	}

	public static String convertToHexa(int address) {
		return String.format("%1$04X", address);
	}

	public static void clearAll() {
		ErrorChecker.getInstance().getLabelList().clear();
		ProgramCounter.getInstance().resetAddresses();
		CommandInfo.labelList.clear();
		SymbolTable.symbolTable.clear();
		LiteralTable.literalTable.clear();
	}

	public static boolean isDirective(String directiveMnemonic) {
		if (DirectiveTable.directiveTable.containsKey(directiveMnemonic.toUpperCase()))
			return true;
		return false;
	}

	public static boolean isInstruction(String instructionMnemonic) {
		if (InstructionTable.instructionTable.containsKey(instructionMnemonic.toUpperCase()))
			return true;
		return false;
	}

	public static boolean isRegister(String registerName) {
		if (RegisterTable.registerTable.containsKey(registerName.toUpperCase()))
			return true;
		return false;
	}

	public static boolean isLabel(String labelName) {
		if (CommandInfo.labelList.contains(labelName))
			return true;
		return false;

	}

	public static int getNumberOfDigits(String value) {
		return String.valueOf(value).length();
	}

	public static boolean isNumeric(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean validateWordFormat(String operand) {
		if (operand.length() == 5) {
			if (!isNumeric(operand.substring(3, 3)))
				return false;
		}
		if (operand.charAt(3) == '-') {
			if (!isNumeric(operand.substring(4, operand.length() - 2)))
				return false;
		} else {
			if (!isNumeric(operand.substring(3, operand.length() - 2)))
				return false;
		}
		return true;
	}

	public static boolean isHex(String operand) {
		try {
			Long.parseLong(operand, 16);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean validateHexFormat(String operand) {
		for (int i = 3; i < operand.length() - 1; i++) {
			if (!isHex(operand.substring(i, i)))
				return false;
		}
		return true;
	}

	public static boolean isLiteral(String operand) {
		if (operand.charAt(0) != '=') {
			return false;
		}

		if (operand.length() <= 4) {
			return false;
		}

		if (operand.charAt(2) != '\'' || operand.charAt(operand.length() - 1) != '\'') {
			return false;
		}

		operand = operand.toUpperCase();
		char dataType = operand.charAt(1);
		switch (dataType) {
		case 'W':
			return validateWordFormat(operand);

		case 'X':
			return validateHexFormat(operand);

		case 'C': // all cases already checked before switch()
			return true;

		default:
			return false;
		}
	}

	public static boolean isComment(String line) {
		if (line.startsWith("."))
			return true;
		return false;
	}

	public static String getSpaces(int count) {
		String s = "";
		for (int i = 0; i < count; i++)
			s += " ";
		return s;
	}

	public static String getZeros(int count) {
		String s = "";
		for (int i = 0; i < count; i++)
			s += "0";
		return s;
	}

	public static String getMatch(String input, String regex) {
		Pattern reg = Pattern.compile(regex);
		Matcher m = reg.matcher(input);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public static ArrayList<String> getMatches(String input, String regex) {
		ArrayList<String> matchArr = new ArrayList<>();
		Pattern reg = Pattern.compile(regex);

		Matcher m = reg.matcher(input);
		while (m.find()) {
			matchArr.add(m.group(1));
		}
		return matchArr;
	}

	public static int getNumberOfMatches(String input, String regex) {
		/* compile the regex using Java regex engine */
		Pattern reg = Pattern.compile(regex);
		int numSpaces = 0;
		Matcher m = reg.matcher(input);

		while (m.find()) {
			numSpaces++;
		}
		return numSpaces;
	}

	public static String removeExtraSpaces(String input) {
		/*
		 * keep matching spaces if there are 3 or more spaces don't trim them and return
		 * e.g:_LABEL_LDA__ (underscores are spaces) and also return if label end has
		 * letter e.g : LABEL__LDA
		 */
		int numSpaces = getNumberOfMatches(input, "(\\s+)");
		if (numSpaces >= 3 || input.charAt(input.length() - 1) != ' ')
			return input;
		/*
		 * get the index for first char after several spaces to keep misplaced label for
		 * example exist
		 */

		int firstCharIdx = 0;
		for (int i = 0; i < input.length(); i++) {
			firstCharIdx = i;
			if (input.charAt(i) != ' ')
				break;

		}
		String s = "";
		if (firstCharIdx == input.length() - 1)
			return s;
		if (firstCharIdx > 0)
			s = input.substring(0, firstCharIdx);
		for (int i = firstCharIdx; i < input.length(); i++) {
			if (input.charAt(i) == ' ')
				break;
			s += input.charAt(i);
		}
		return s;
	}

	public static void writeFile(String s, String filePath) {
		File file = new File(filePath);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(s);
			bw.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void extractAddressingModeFromOperand(String input, CommandInfo CI) {
		if (!input.equals("")) {
			if (input.charAt(0) == '#' || input.charAt(0) == '@') {
				CI.addAddressMode(input.substring(0, 1));
				// noinspection StringOperationCanBeSimplified
				CI.addOperand1(input.substring(1, input.length()));
			} else {
				CI.addAddressMode("");
				CI.addOperand1(input);
			}
		} else {
			CI.addAddressMode("");
			CI.addOperand1("");
		}
	}

	public static void processOperand(String input, CommandInfo CI) {
		String[] op1op2 = input.split(",");
		if (op1op2.length > 1) {
			extractAddressingModeFromOperand(op1op2[0], CI);
			CI.addOperand2(op1op2[1].toUpperCase());
		} else {
			extractAddressingModeFromOperand(op1op2[0], CI);
			CI.addOperand2("");
		}
	}

	public static String processOperandValue(String input) {
		if (input.equals(""))
			return "";
		switch (input.charAt(0)) {
		case 'C':
		case 'c':
			return input.substring(2, input.length() - 1);
		case 'X':
		case 'x':
			return String.valueOf(Integer.parseInt(input.substring(2, input.length() - 1), 16));
		default:
			return input;
		}
	}

	public static boolean containsMisplacedLetter(String s) {

		int len = s.length();
		for (int i = 0; i < len; i++) {
			if (s.charAt(i) == ' ')
				return true;
		}
		return false;
	}

	public static boolean isThatStringEqualAnyDirective(String input) {

		return (input.equalsIgnoreCase("byte") || input.equalsIgnoreCase("resb") || input.equalsIgnoreCase("word")
				|| input.equalsIgnoreCase("resw") || input.equalsIgnoreCase("equ") || input.equalsIgnoreCase("start")
				|| input.equalsIgnoreCase("org") || input.equalsIgnoreCase("base") || input.equalsIgnoreCase("nobase")
				|| input.equalsIgnoreCase("ltorg"));
	}

	public static ArrayList<String> splitExpression(String expression) {
		// Remove spaces from expression
		ArrayList<String> matches = Utility.getMatches(expression, "(\\S+)");
		// Recollect expression
		StringBuilder newExpression = new StringBuilder();
		for (String str : matches) {
			newExpression.append(str);
		}
		// Split expression on operations
		String[] results = newExpression.toString().split("(?<=[-+*/()])|(?=[-+*/()])");
		return new ArrayList<>(Arrays.asList(results));
	}

	public static boolean verifyExpression(ArrayList<String> expressionList) {
		for (String s : expressionList) {
			if (isOperator(s))
				continue;
			if (!isLabel(s.toUpperCase()) && !isNumeric(s))
				return false;
		}
		return true;
	}

	public static void evaluateLabels(ArrayList<String> expressionList) {
		for (String s : expressionList) {
			if (isLabel(s)) {
				// Replace label with its address
				int value = hexToDecimal(SymbolTable.symbolTable.get(s).getAddress());
				expressionList.set(expressionList.indexOf(s), String.valueOf(value));
			}
		}
	}

	public static boolean validateNumericExpression(ArrayList<String> expressionList) {
		// first element in list is numeric not operator
		if (isOperator(expressionList.get(0)))
			return false;
		// last element in list is numeric not operator
		if (isOperator(expressionList.get(expressionList.size() - 1)))
			return false;
		// No consecutive operands => impossible to have consecutive operands due to
		// split algorithm
		// No consecutive operators => # of operators = # of numbers - 1
		int numbers = 0;
		int operators = 0;
		for (String s : expressionList) {
			if (isOperator(s) && !s.equals("(") && !s.equals(")"))
				operators++;
			else
				numbers++;
		}
		// noinspection RedundantIfStatement
		if (operators >= numbers)
			return false;
		return true;
	}

	public static String getNumericExpression(ArrayList<String> expressionList) {
		StringBuilder expression = new StringBuilder();
		expression.append("(");
		for (String s : expressionList) {
			expression.append(s);
		}
		expression.append(")");
		return expression.toString();
	}

	private static boolean isOperator(String string) {
		switch (string) {
		case "*":
		case "/":
		case "-":
		case "+":
		case "(":
		case ")":
			return true;

		default:
			return false;
		}
	}

	public static boolean isExpression(String operand) {
		ArrayList<String> operandComponents = splitExpression(operand);
		// If operand is not an expression, size after splitting will be 1
		if (operandComponents.size() == 1)
			return false;
		// noinspection RedundantIfStatement
		if (verifyExpression(operandComponents)) {
			return true;
		}
		return false;
	}

	public static String evaluateExpression(String expression) {
		MyGenericsStack<String> stack = new MyGenericsStack<>(expression.length());
		// break the expression into tokens
		StringTokenizer tokens = new StringTokenizer(expression, "{}()*/+-", true);
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			// read each token and take action
			if (token.equals("(") || token.matches("[0-9]+") || token.equals("*") || token.equals("/")
					|| token.equals("+") || token.equals("-")) {
				// push token to the stack
				stack.push(token);
			} else if (token.equals("}") || token.equals(")")) {
				try {
					int op2 = Integer.parseInt(stack.pop());
					String operand = stack.pop();
					int op1 = Integer.parseInt(stack.pop());
					// Below pop removes either } or ) from stack
					if (!stack.isStackEmpty()) {
						stack.pop();
					}
					int result = 0;
					switch (operand) {
					case "*":
						result = op1 * op2;
						break;
					case "/":
						result = op1 / op2;
						break;
					case "+":
						result = op1 + op2;
						break;
					case "-":
						result = op1 - op2;
						break;
					}
					// push the result to the stack
					stack.push(result + "");
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
		String finalResult = "";
		try {
			finalResult = stack.pop();
		} catch (Exception e) {
			System.out.println("Stack is empty");
		}
		return finalResult;
	}
}
