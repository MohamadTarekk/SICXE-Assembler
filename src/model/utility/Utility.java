package model.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CommandInfo;
import model.ErrorChecker;
import model.ProgramCounter;
import model.enums.Format;
import model.tables.DirectiveTable;
import model.tables.InstructionTable;
import model.tables.RegisterTable;
import model.tables.SymbolTable;

@SuppressWarnings({})
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
		}else{
			CI.addAddressMode("");
			CI.addOperand1("");
		}
	}

	public static void processOperand(String input, CommandInfo CI) {
		String[] op1op2 = input.split(",");
		if (op1op2.length > 1) {
			extractAddressingModeFromOperand(op1op2[0], CI);
			CI.addOperand2(op1op2[1]);
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

	public static int evaluate(String expression) {
		char[] tokens = expression.toCharArray();
		// Stack for numbers: 'values'
		Stack<Integer> values = new Stack<>();
		// Stack for Operators: 'ops'
		Stack<Character> ops = new Stack<>();

		for (int i = 0; i < tokens.length; i++) {
			// Current token is a whitespace, skip it
			if (tokens[i] == ' ')
				continue;
			// Current token is a number, push it to stack for numbers
			if (tokens[i] >= '0' && tokens[i] <= '9') {
				StringBuilder stringBuffer = new StringBuilder();
				// There may be more than one digits in number
				while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
					stringBuffer.append(tokens[i++]);
				values.push(Integer.parseInt(stringBuffer.toString()));
			}
			// Current token is an opening brace, push it to 'ops'
			else if (tokens[i] == '(')
				ops.push(tokens[i]);
			// Closing brace encountered, solve entire brace
			else if (tokens[i] == ')') {
				while (ops.peek() != '(')
					values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
				ops.pop();
			}
			// Current token is an operator.
			else if (tokens[i] == '+' || tokens[i] == '-' ||
					tokens[i] == '*' || tokens[i] == '/')
			{
				// While top of 'ops' has same or greater precedence to current
				// token, which is an operator. Apply operator on top of 'ops'
				// to top two elements in values stack
				while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
					values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
				// Push current token to 'ops'.
				ops.push(tokens[i]);
			}
		}
		// Entire expression has been parsed at this point, apply remaining
		// ops to remaining values
		while (!ops.empty())
			values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
		// Top of 'values' contains result, return it
		return values.pop();
	}

	// Returns true if 'op2' has higher or same precedence as 'op1',
	// otherwise returns false.
	private static boolean hasPrecedence(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		//noinspection RedundantIfStatement
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
			return false;
		else
			return true;
	}

	// A utility method to apply an operator 'op' on operands 'a'
	// and 'b'. Return the result.
	private static int applyOperator(char op, int b, int a) {
		switch (op) {
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/':
				if (b == 0)
					throw new
							UnsupportedOperationException("Cannot divide by zero");
				return a / b;
		}
		return 0;
	}

	public static ArrayList<String> splitExpression(String expression) {
		// Remove spaces from expression
		ArrayList<String> matches = Utility.getMatches(expression,"(\\S+)");
		// Recollect expression
		StringBuilder newExpression = new StringBuilder();
		for (String str : matches) {
			newExpression.append(str);
		}
		// Split expression on operations
		String[] results = newExpression.toString().split("(?<=[-+*/()])|(?=[-+*/()])");
		return new ArrayList<>(Arrays.asList(results));
	}

	public static boolean verifyExpression(ArrayList<String> list) {
		for (String s : list) {
			if (isOperator(s))
				continue;
			if (!isLabel(s))
				return false;
		}
		return true;
	}

	public static void evaluateLabels(ArrayList<String> list) {
		for (String s : list) {
			if (isLabel(s)) {
				// Replace label with its address
				list.set(list.indexOf(s), SymbolTable.symbolTable.get(s).getAddress());
			}
			else if (!isOperator(s)) {
				// TODO: set error => Error.WRONG_OPERAND_TYPE
			}
		}
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
}
