package jBasic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Editor.Main;

public class JBasicRunner {

	JVariableContainer variables = new JVariableContainer();
	
	public JBasicRunner(String fileName) {
		try {
			Scanner input = new Scanner(new File(fileName));
			runFile(input);
			System.out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A method that takes a line and removes quotes and formats
	 * it into a usable string.
	 * @param lineSplit - the array of args from the line
	 * @return A formated string
	 */
	private String processIntoString(String[] lineSplit, int startingPos) {
		// Loop through line and print all variables / statements in quotes
		boolean midQuote = false;
		String printStatement = "";
		for(int i = startingPos; i < lineSplit.length; i++) {
			lineSplit[i] = lineSplit[i].trim();
			if(lineSplit[i].equals("")) {
				printStatement += " ";
			}
			else if(lineSplit[i].charAt(0) == '"' && lineSplit[i].length() > 1 && lineSplit[i].charAt(lineSplit[i].length() - 1) == '"') { //Then this is a one word quote, remove both
				printStatement += lineSplit[i].substring(1, lineSplit[i].length() - 1);
			}
			else if(lineSplit[i].contains("\"")) { // Then this is a non var
				midQuote = !midQuote;
				if(midQuote) printStatement += lineSplit[i].substring(1) + " "; // The start of the quotes
				else printStatement += lineSplit[i].substring(0, lineSplit[i].length() - 1); //At the end of the quotes
			}
			else if(midQuote) { //If we are mid quote, then its NOT a variable.
				printStatement += lineSplit[i] + " "; // Add a space since split removed it
			}
			else { // This should be a var, verify.
				if (variables.getVariable(lineSplit[i]) != null) { //If this is not null, than the variable name does exist
					if(variables.isInit(lineSplit[i])) { //If this returns false, then the variable was never initialized
						printStatement += variables.getVariable(lineSplit[i]);
					}
					else {
						System.out.println("Variable is not init");
						break;
					}
				}
				else {
					System.out.println("Variable does not exist");
					break;
				}
			}
		}
		return printStatement;
	}
	
	private void runFile(Scanner input) {
		int lineCount = 0;
		while(input.hasNextLine()) {
			String theLine = input.nextLine();
			theLine = theLine.replace(';', ' ');
			String[] lineSplit = theLine.split(" "); // Split the line by " "
			lineCount++; //Increment the line that we're on
			
			//J-Basic Version Reader
			if(lineCount == 1 && theLine.charAt(1) == '#') {
				double version = Double.parseDouble(lineSplit[3]);
				if(version != Main.languageVersion) {
					System.out.println("WARNING: Version Mismatch Warning,");
					System.out.println("\tYour Version: " + version);
					System.out.println("\tInterpreter Version: " + Main.languageVersion);
					System.out.println("This mismatch could cause errors in your program.");
					System.out.println("Continuing...\t");
				}
				continue;
			}
			
			if(!theLine.equals("")) {
				// INTEGERS
				if(lineSplit[0].toLowerCase().equalsIgnoreCase("int") || lineSplit[0].toLowerCase().equalsIgnoreCase("integer")) {
					try {
						if(lineSplit.length > 2) { // in this case the variable was also initialized
							int newVar = Integer.parseInt(lineSplit[3]); // Grab int value
							variables.addInteger(lineSplit[1], newVar); // Add integer to container
						}
						else { //The variable was not initialized
							variables.addUninitVar(lineSplit[1], 'i'); //Add uninitialized int variable 
						}
						
					} catch (Exception e) {
						System.out.println("int defined error");
						e.printStackTrace();
						break;
					}	
				}
				// STRINGS
				else if(lineSplit[0].toLowerCase().contains("str")) {
					try {
						if(lineSplit.length > 2) { // in this case the variable was also initialized
							variables.addString(lineSplit[1], processIntoString(lineSplit, 3)); // Add integer to container
						}
						else { //The variable was not initialized
							variables.addUninitVar(lineSplit[1], 's'); //Add uninitialized int variable 
						}
						
					} catch (Exception e) {
						System.out.println("string defined error");
						e.printStackTrace();
						break;
					}	
				}
				// PRINT STATEMENTS
				else if(lineSplit[0].toLowerCase().contains("print")) {
					String printStatement = processIntoString(lineSplit, 1);
					
					if(!printStatement.equals(""))
						System.out.println(printStatement);
				}
				else if(variables.getVariable(lineSplit[0]) != null) {
					if(variables.getInteger(lineSplit[0]) != null) { //The value must be an int, process accordingly
						if(theLine.contains("+") || theLine.contains("-") || theLine.contains("*") || theLine.contains("/") || theLine.contains("%")) {
							String expression = "";
							for(String s : lineSplit) {
								if(variables.getInteger(s) != null) {
									expression += variables.getInteger(s).getValue() + "";
								}
								else expression += s;
							}
							expression = expression.substring(expression.indexOf("=") + 1, expression.length());
							
							InfixExpression ie = new InfixExpression(expression);
							ie.getPostfixExpression();
							variables.updateVariable(lineSplit[0], ie.evaluatePostfix() + "");
						}
						else {
							variables.updateVariable(lineSplit[0], lineSplit[2]);	
						}
					}
					else if(variables.getString(lineSplit[0]) != null) {
						if(theLine.contains(" - ")) {
							if(theLine.contains(" = ")) { //Set the "removed" characters to the string
								int subPos = theLine.lastIndexOf("-");
								int value = Integer.parseInt(theLine.split("- ")[theLine.split("- ").length - 1]); //This gets the number to subtract
								theLine = theLine.substring(0, subPos - 1);
								lineSplit = theLine.split(" ");
								String expression = processIntoString(lineSplit, 2);
								if(value > expression.length()) value = expression.length();
								expression = expression.substring(expression.length() - value, expression.length());
								variables.updateVariable(lineSplit[0], expression);
							}
							else { //Remove that many characters
								int subPos = theLine.lastIndexOf("-");
								int value = Integer.parseInt(theLine.split("- ")[theLine.split("- ").length - 1]); //This gets the number to subtract
								theLine = theLine.substring(0, subPos - 1);
								lineSplit = theLine.split(" ");
								String expression = processIntoString(lineSplit, 0);
								if(value > expression.length()) value = expression.length();
								expression = expression.substring(0, expression.length() - value);
								variables.updateVariable(lineSplit[0], expression);		
							}
							
						}
						else {
							String expression = theLine.substring(theLine.indexOf("=") + 1, theLine.length()).trim();
							variables.updateVariable(lineSplit[0], processIntoString(expression.split(" "), 0));	
						}
					}
					else System.out.println("Invalid variable type");
				}
				else System.out.println("Unknown command at Line: " + lineCount);	
			}
		}
	}
	
}
