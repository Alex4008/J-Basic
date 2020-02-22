/**
 * Originally written by me, for Dr. Gani's CSE 274 class at Miami University Spring 2019
 * Reused for this project.
 * @author Alex Gray
 */
package jBasic;

import java.util.Stack;

public class InfixExpression {

	String infix;
	
	/**
	 * Constructor for the class. Accepts an infix expression as a string
	 * 
	 * @param s
	 * @throws IllegalArgumentException
	 */
	
	public InfixExpression(String s) throws IllegalArgumentException{
		infix = s;
		clean();
		try {
			if(isValid() == false) throw new IllegalArgumentException();
		}
		catch(Exception EmptyStackException) {
			System.out.println("Invalid Infix Expression");
		}
	}
	
	/**
	 * Returns the infix expression as a string
	 */
	
	public String toString() {
		return infix;
	}
	
	/**
	 * Checks to see if infix expression has balanced parentheses, if it does it returns true
	 * otherwise it returns false.
	 * @returns boolean
	 */
	
	private boolean isBalanced() {
		Stack<Character> openDelimiterStack = new Stack<>();
		int characterCount = infix.length();
		boolean isBalanced = true;
		int index = 0;
		char nextCharacter = ' ';
		
		while(isBalanced && (index < characterCount)) {
			nextCharacter = infix.charAt(index);
			switch(nextCharacter) {
			case '(' :
				openDelimiterStack.push(nextCharacter);
				break;
			case ')' :
				if(openDelimiterStack.isEmpty()) isBalanced = false;
				else {
					openDelimiterStack.pop();
					isBalanced = true;
				}
				break;
			default: break;
			}
			index++;
		}
		if(!openDelimiterStack.isEmpty()) isBalanced = false;
		return isBalanced;
		
	}
	
	/**
	 * Determines if infix expression is a valid infix expression. If it is it returns true
	 * otherwise it returns false.
	 * @return boolean
	 */
	
	private boolean isValid() {
		if(isBalanced() == false) return false;
		if(infix.contains(")(")) return false;
		
		int pos = 1;
		boolean lastOp = false;
		boolean lastNum = false;
		boolean space = false;
		while(pos < infix.length()) {
			if(infix.charAt(pos) != ' ') {
				if(infix.charAt(pos) == '+' || infix.charAt(pos) == '-' || infix.charAt(pos) == '*' || infix.charAt(pos) == '/' || infix.charAt(pos) == '%' || infix.charAt(pos) == '^') {
					if(lastOp) return false;
					lastOp = true;
					lastNum = false;
					space = false;
				}
				else if(Character.isDigit(infix.charAt(pos))) {
					if(lastNum && space) return false;
					lastOp = false;
					lastNum = true;
					space = false;
				}
			}
			else {
				space = true;
			}
			pos++;
		}
		
		return true;
	}
	
	/**
	 * private methods that is used to help the constructor. Cleans up the given infix expression by
	 * adding and removing spaces where needed to make the infix expression work.
	 */
	
	private void clean() {
		String newInfix = infix;
		while(newInfix.charAt(0) == ' ') {
			newInfix = newInfix.substring(1, newInfix.length());
		}
		int pos = 1;
		while(pos < newInfix.length()) {
			if(newInfix.charAt(pos - 1) == ' ' && newInfix.charAt(pos) == ' ') {
				newInfix = newInfix.substring(0, pos - 1) + newInfix.substring(pos, newInfix.length());
				pos--;
			}
			
			else if (newInfix.charAt(pos - 1) != ' ' && newInfix.charAt(pos) != ' ') {
				if(!Character.isDigit(newInfix.charAt(pos - 1)) || !Character.isDigit(newInfix.charAt(pos))) {
					newInfix = newInfix.substring(0, pos) + ' ' + newInfix.substring(pos, newInfix.length());
				}
			}
			pos++;
		}
		infix = newInfix;
	}
	
	/**
	 * Takes the valid infix expression and converts it to a postfix expression
	 * 
	 * @return String
	 */
	
	public String getPostfixExpression() {
		Stack<String> operatorStack = new Stack<>();
		String postFix = "";
		int pos = 0;
		while(pos < infix.length()) {
			String nextStr = " ";
			while(nextStr == " ") {
				nextStr = "" + infix.charAt(pos);
				int i = 0;
				while(Character.isDigit(nextStr.charAt(i))) {
					if(pos + 1 >= infix.length()) break;
					if(!Character.isDigit(infix.charAt(pos + 1))) break;
					pos++;
					nextStr = nextStr + infix.charAt(pos);
					i++;
				}
				
				pos++;
			}
			switch (nextStr) {
			case "^" :
				operatorStack.push(nextStr);
				break;
			case "+" : case "-" : case "*" : case "/" : case "%" :
				while(!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
					if(nextStr.equals("*") || nextStr.equals("/") || nextStr.equals("%") || operatorStack.peek().equals("(")) {
						if(operatorStack.peek().equals("+") || operatorStack.peek().equals("-")) {
							break;
						}
					}

					if(!operatorStack.peek().equals("(")) {
						postFix = postFix + operatorStack.peek() + " ";
						operatorStack.pop();
					}
				}
				operatorStack.push(nextStr);
				break;
			case "(" :
				operatorStack.push(nextStr);
				break;
			case ")" :
				String topOperator = operatorStack.pop();
				while(!topOperator.equals("(")) {
					postFix = postFix + topOperator + " ";
					topOperator = operatorStack.pop();
				}
				break;
			default : 
				postFix = postFix + nextStr + " ";
				break;
			}
			pos++;
			
		}
		
		while(!operatorStack.isEmpty()) {
			String topOperator = operatorStack.pop();
			postFix = postFix + topOperator + " ";
		}
		infix = postFix;
		return postFix;
		
	}
	
	/**
	 * Evaluates the postfix expression and returns the answer in int format
	 * 
	 * @return int
	 */
	
	public int evaluatePostfix() {
		Stack<String> valueStack = new Stack<>();
		int pos = 0;
		while(pos < infix.length()) {
			String nextStr = "";
			if(infix.charAt(pos) != ' ') {
				char c = infix.charAt(pos);
				int i = 0;
				while(c != ' ') {
					c = infix.charAt(pos + i);
					i++;
				}
				if(i >= 1) {
					i--;
					nextStr = infix.substring(pos, pos + i);
					pos = pos + i;
				}
				else {
					nextStr = "" + infix.charAt(pos);
				}
				switch(nextStr) {
				case "+" : case "-" : case "*" : case "/" : case "^" : case "%" :
					int operandTwo = Integer.parseInt(valueStack.pop());
					int operandOne = Integer.parseInt(valueStack.pop());
					int result = 0;
					if(nextStr.equals("+")) {
						result = operandOne + operandTwo;
					}
					else if(nextStr.equals("-")) {
						result = operandOne - operandTwo;
					}
					else if(nextStr.equals("/")) {
						result = operandOne / operandTwo;
					}
					else if(nextStr.equals("*")) {
						result = operandOne * operandTwo;
					}
					else if(nextStr.equals("^")) {
						result = (int) Math.pow(operandOne, operandTwo);
					}
					else if(nextStr.equals("%")) {
						result = operandOne % operandTwo;
					}
					else {
						
					}
					valueStack.push(String.valueOf(result));
					break;
				default:
					valueStack.push(String.valueOf(nextStr));
					break;
				}
			}
			pos++;
		}
		return Integer.parseInt(valueStack.peek());
	}
	
	/**
	 * Evaluates the valid infix expression and returns it as an int value
	 * 
	 * @return int
	 */
	
	public int evaluate() {
		Stack<Character> operatorStack = new Stack<>();
		Stack<Integer> valueStack = new Stack<>();
		int pos = 0;
		while(pos < infix.length()) {
			String nextStr = "";
			if(infix.charAt(pos) != ' ') {
				char c = 'A';
				int i = 0;
				while(c != ' ') {
					if(pos + i >= infix.length()) break;
					c = infix.charAt(pos + i);
					i++;
				}
				i--;
				
				if(i >= 1) {
					nextStr = infix.substring(pos, pos + i);
				}
				else {
					nextStr = infix.charAt(pos) + "";
				}

				switch(nextStr) {
				case "^" :
					operatorStack.push(nextStr.charAt(0));
					break;
					
				case "+" : case "-" : case "*" : case "/" : case "%" :
					while(!operatorStack.isEmpty()) {
						int nextStringLevel = 0;
						int stackPeekLevel = 0;
						
						switch(operatorStack.peek()) {
						case '^' :
							stackPeekLevel = 3;
							break;
						case '/' : case '*' : case '%' :
							stackPeekLevel = 2;
							break;
						case '+' : case '-' :
							stackPeekLevel = 1;
							break;
						}
						
						switch(nextStr.charAt(0)) {
						case '^' :
							nextStringLevel = 3;
							break;
						case '/' : case '*' : case '%' :
							nextStringLevel = 2;
							break;
						case '+' : case '-' :
							nextStringLevel = 1;
							break;
						}
						
						if(nextStringLevel > stackPeekLevel) /*|| operatorStack.peek() == '(')*/ break;
						
						char topOperator = operatorStack.pop();
						int operandTwo = valueStack.pop();
						int operandOne = valueStack.pop();
						int result = 0;
						if(topOperator == '+') {
							result = operandOne + operandTwo;
						}
						else if(topOperator == '-') {
							result = operandOne - operandTwo;
						}
						else if(topOperator == '/') {
							result = operandOne / operandTwo;
						}
						else if(topOperator == '*') {
							result = operandOne * operandTwo;
						}
						else if(topOperator == '^') {
							result = (int) Math.pow(operandOne, operandTwo);
						}
						else if(topOperator == '%') {
							result = operandOne % operandTwo;
						}
						valueStack.push(result);
					}
					operatorStack.push(nextStr.charAt(0));
					break;
				case "(" :
					operatorStack.push(nextStr.charAt(0));
					break;
				case ")" :
					char topOperator = operatorStack.pop();
					while(topOperator != '(') {
						;
						int operandTwo = valueStack.pop();
						int operandOne = valueStack.pop();
						int result = 0;
						if(topOperator == '+') {
							result = operandOne + operandTwo;
						}
						else if(topOperator == '-') {
							result = operandOne - operandTwo;
						}
						else if(topOperator == '/') {
							result = operandOne / operandTwo;
						}
						else if(topOperator == '*') {
							result = operandOne * operandTwo;
						}
						else if(topOperator == '^') {
							result = (int) Math.pow(operandOne, operandTwo);
						}
						else if(topOperator == '%') {
							result = operandOne % operandTwo;
						}
						valueStack.push(result);
						topOperator = operatorStack.pop();
					}
					break;
					
				default:
					valueStack.push(Integer.parseInt(nextStr));
					break;
				}
			}
			pos++;
		}
		while(!operatorStack.isEmpty()) {
			char topOperator = operatorStack.pop();
			int operandTwo = valueStack.pop();
			int operandOne = valueStack.pop();
			int result = 0;
			if(topOperator == '+') {
				result = operandOne + operandTwo;
			}
			else if(topOperator == '-') {
				result = operandOne - operandTwo;
			}
			else if(topOperator == '/') {
				result = operandOne / operandTwo;
			}
			else if(topOperator == '*') {
				result = operandOne * operandTwo;
			}
			else if(topOperator == '^') {
				result = (int) Math.pow(operandOne, operandTwo);
			}
			else if(topOperator == '%') {
				result = operandOne % operandTwo;
			}
			valueStack.push(result);
		}
		return valueStack.peek();
	}
	
}
