package jBasic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	private void runFile(Scanner input) {
		int lineCount = 0;
		while(input.hasNextLine()) {
			String theLine = input.nextLine();
			String[] lineSplit = theLine.split(" "); // Split the line by " "
			lineCount++; //Increment the line that we're on
			
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
							variables.addString(lineSplit[1], lineSplit[3].substring(1, lineSplit[3].length() - 1)); // Add integer to container
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
					String printStatement = "";
					// Loop through line and print all variables / statements in quotes
					boolean midQuote = false;
					for(int i = 1; i < lineSplit.length; i++) {
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
					if(!printStatement.equals(""))
						System.out.println(printStatement);
				}
				else if(variables.getVariable(lineSplit[0]) != null) {
					variables.updateVariable(lineSplit[0], lineSplit[2]);
				}
				else System.out.println("Unknown command at Line: " + lineCount);	
			}
		}
	}
	
}
