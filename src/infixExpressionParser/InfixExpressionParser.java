package infixExpressionParser;

public class InfixExpressionParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static int precedence(String operator) {
		if (operator.equals("^")) { return 7; }
		if (operator.equals("*") || operator.equals("/")) { return 6; }
		if (operator.equals("+") || operator.equals("-")) { return 5; }
		if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) { return 4; }
		if (operator.equals("==") || operator.equals("!=")) { return 3; }
		if (operator.equals("&&")) { return 2; }
		if (operator.equals("||")) { return 1; }
		throw new IllegalArgumentException(String.format("Operator %s is not supported.", operator));
	} 
	
	public static String infixToPostfix(String infixExp) {
		Scanner scanner = new Scanner(infixExp);
		Stack<String> stack = new Stack<>();
		StringBuilder postfixExp = new StringBuilder();
		while (scanner.hasNext()) {
		String token = scanner.next();
		// If the current token is an operand, append it to the postfix expression string.
		if (Character.isDigit(token.charAt(0))) { postfixExp.append(token).append(' '); }
		// If the current token is opening parenthesis, push it onto the stack.
		else if (token.equals("(")) { stack.push(token); }
		else if (token.equals(")")) {
		// Pop all operators from the stack, until an opening parenthesis is seen on top of the stack.
		// Append each popped operator to the postfix expression string.
		while (!stack.peek().equals("(")) { postfixExp.append(stack.pop()).append(' '); }
		// Pop the opening parenthesis from the stack.
		stack.pop();
		} else {
		// The current token is an operator.
		// Keep popping operators (and append them to postfix expression string) from the stack, until:
		// 1) the stack is empty,
		// 2) an opening parenthesis is seen on top of the stack, or
		// 3) the current operator has higher precedence than the operator on top of the stack.
		while (!stack.isEmpty() && !stack.peek().equals("(") &&
		precedence(token) <= precedence(stack.peek())) {
		postfixExp.append(stack.pop()).append(' ');
		}
		// Push the current operator onto the stack.
		stack.push(token);
		}
		}
		// Pop the remaining operators from the stack and append them to the postfix expression string.
		while (!stack.isEmpty()) { postfixExp.append(stack.pop()).append(' '); }
		scanner.close();
		return postfixExp.toString();
		} 
	
}
