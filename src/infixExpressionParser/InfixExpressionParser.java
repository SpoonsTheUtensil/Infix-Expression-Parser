package infixExpressionParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class InfixExpressionParser {

	public static void main(String[] args) throws IOException {
		
		File f = new File("input.txt");
		while (!f.exists()) {
			System.out.println("File does not exist!");
			System.out.println("...Exiting Program");
			System.exit(0);
		}
		
		FileInputStream inputFile = new FileInputStream(f);
		Scanner fileReader = new Scanner(inputFile);
		String evaluationFinal = "";
		
		while (fileReader.hasNext()) {
			String evaluation = fileReader.nextLine().replace(" ", "");
			char[]evalLetters = evaluation.toCharArray();
			String tempOperator = "";
			int count = 0;
			
			for (@SuppressWarnings("unused") char letter : evalLetters){
				char current = evalLetters[count];
				evaluationFinal += " " + current;
				count++;
				
				if (count >= evalLetters.length) {
					break;
				}
				char next = evalLetters[count];

				if (((current == '>' && next == '=')) || 
				    	((current == '<' && next == '=')) || 
				    		((current == '=' && next == '=')) || 
				    			((current == '!' && next == '=')) || 
				    				((current == '&' && next == '&'))|| 
				    					((current == '|' && next == '|'))) {
						
				    tempOperator += Character.toString(next);
				    evaluationFinal += tempOperator;
				    tempOperator = "";
				    count++;
				}
			}
			String postFix = infixToPostFix(evaluationFinal);
			int solution = evalPostfix(postFix);
			System.out.println(solution);
			evaluationFinal = "";
		}
		fileReader.close();
	}
	
	
		//System.out.println(infixToPostFix(evaluationFinal));
	
	//Method to establish precedence of an operator
	public static int precedence(String operator) {
		if (operator.equals("^")) { return 7; }
		if (operator.equals("*") || operator.equals("/") || operator.equals("%")) { return 6; }
		if (operator.equals("+") || operator.equals("-")) { return 5; }
		if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) { return 4; }
		if (operator.equals("==") || operator.equals("!=")) { return 3; }
		if (operator.equals("&&")) { return 2; }
		if (operator.equals("||")) { return 1; }
		throw new IllegalArgumentException(String.format("Operator %s is not supported.", operator));
	}
	
	//Infix to postfix converter used in lecture 3
	public static String infixToPostFix(String infixExp) {
		Scanner scanner = new Scanner(infixExp);
		Stack<String> stack = new Stack<>();
		StringBuilder postfixExp = new StringBuilder();
		while (scanner.hasNext()) {
			String token = scanner.next();
			// If the current token is an operand, append it to the postfix expression string.
			if (Character.isDigit(token.charAt(0))) { 
				postfixExp.append(token);
			}
			// If the current token is opening parenthesis, push it onto the stack.
			else if (token.equals("(")) {
				stack.push(token);
			}
			else if (token.equals(")")) {
				// Pop all operators from the stack, until an opening parenthesis is seen on top of the stack.
				// Append each popped operator to the postfix expression string.
				while (!stack.peek().equals("(")) {
					postfixExp.append(stack.pop());
				}
				// Pop the opening parenthesis from the stack.
				stack.pop();
			} else {
				// The current token is an operator.
				// Keep popping operators (and append them to postfix expression string) from the stack, until:
				// 1) the stack is empty,
				// 2) an opening parenthesis is seen on top of the stack, or
				// 3) the current operator has higher precedence than the operator on top of the stack.
				while (!stack.isEmpty() && !stack.peek().equals("(") && precedence(token) <= precedence(stack.peek())) {
					postfixExp.append(stack.pop());
				}
				// Push the current operator onto the stack.
				stack.push(token);
			}
		}
		// Pop the remaining operators from the stack and append them to the postfix expression string.
		while (!stack.isEmpty()) {
			postfixExp.append(stack.pop());
		}
		scanner.close();
		return postfixExp.toString();
	}
	
	public static int evalPostfix(String express) { 
		Stack<Integer> st = new Stack<>(); 
	          
	    for(int i=0; i < express.length(); i++) { 
	    	char ch = express.charAt(i); 
	             
	        if(Character.isDigit(ch))
	        	st.push(ch - '0'); 
	              
	        else {
	        	int value1 = st.pop(); 
	            int value2 = st.pop(); 
	                  
	            switch(ch) { 
	            	case '+': 
	                st.push(value2 + value1); 
	                break; 
	                      
	                case '-': 
	                st.push(value2 - value1); 
	                break; 
	                        
	                case '*': 
	                st.push(value2*value1); 
	                break;
	                
	                case '/': 
	                st.push(value2/value1); 
	                break;
	                
	                case '^': 
	                	st.push((int) Math.pow(value2, value1)); 
		                break;
		            
	                case '%': 
		                st.push(value2%value1); 
		                break;
	                
	                case '>':
	                	//Find if its >= or not and make sure its not at the end of the string
	                	if (i != express.length() - 1 && express.charAt(i+1) == '=') {
	                		
	                		if (value2 >= value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                		//Move i forward one to pass the '='
	                		i = i + 1;
	                	}
	                	else {
	                		if (value2 > value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                	}
	                	break;
	                
	                case '<':
	                	//Find if its <= or not and make sure its not at the end of the string
	                	if (i != express.length() - 1 && express.charAt(i+1) == '=') {
	                		
	                		if (value2 <= value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                		//Move i forward one to pass the '='
	                		i = i + 1;
	                	}
	                	else {
	                		if (value2 < value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                	}
	                	break;
	                	
	                case '!':
	                	
	                	if (i != express.length() - 1 && express.charAt(i+1) == '=') {
	                		if (value2 != value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		//Move i forward one to pass the '='
	                		i = i+1;
	                	}
	                	
	                	break;
	                
	                case '=':
	                	if (i != express.length() - 1 && express.charAt(i+1) == '=') {
	                		if (value2 == value1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                		//Move i forward one to pass the '='
	                		i = i+1;
	                	}
	                	
	                	break;
	                
	                case '&':
	                	if (i != express.length() - 1 && express.charAt(i+1) == '&') {
	                		if (value2 == 1 && value1 == 1) {
	                			st.push(1);
	                		}
	                		else {
	                			st.push(0);
	                		}
	                		
	                		//Move i forward one to pass the '&'
	                		i = i+1;
	                	}
	                	break;
	                	
	                	
	                case '|':
	                	if (i != express.length() - 1 && express.charAt(i+1) == '|') {
	                		if (value2 == 1 && value1 == 1 || value2 == 1 && value1 == 0 || value2 == 0 && value1 == 1 ) {
	                			st.push(1);
	                		}
	                		
	                		else {
	                			st.push(0);
	                		}
	                		
	                		//Move i forward one to pass the '|'
	                		i = i+1;
	                	}
	                	break;
	              } 
	            } 
	        } 
	        return st.pop(); 
	    } 
}
