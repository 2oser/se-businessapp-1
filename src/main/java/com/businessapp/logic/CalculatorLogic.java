package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.fxgui.CalculatorGUI_Intf;
import com.businessapp.fxgui.CalculatorGUI_Intf.Token;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Stack;


/**
 * Implementation of CalculatorLogicIntf that only displays Tokens
 * received from the Calculator UI.
 *
 */
class CalculatorLogic implements CalculatorLogicIntf {
	private CalculatorGUI_Intf view;
	private StringBuffer dsb = new StringBuffer();
	private final double VAT_RATE = 19.0;
	private boolean calc;


	CalculatorLogic() {
	}

	@Override
	public void inject( ControllerIntf dep ) {
		this.view = (CalculatorGUI_Intf)dep;
	}

	@Override
	public void inject( Component parent ) {
	}

	@Override
	public void start() {
		nextToken( Token.K_C );		// reset calculator
	}

	@Override
	public void stop() {
	}


	/**
     * Process next token received from UI controller.
     * <p>
     * Tokens are transformed into output into UI properties:
     * 	- CalculatorIntf.DISPLAY for numbers and
     * 	- CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
	public void nextToken( Token tok ) {
		try {
			switch( tok ) {
			case K_0:	appendBuffer( "0" ); break;
			case K_1:	appendBuffer( "1" ); break;
			case K_2:	appendBuffer( "2" ); break;
			case K_3:	appendBuffer( "3" ); break;
			case K_4:	appendBuffer( "4" ); break;
			case K_5:	appendBuffer( "5" ); break;
			case K_6:	appendBuffer( "6" ); break;
			case K_7:	appendBuffer( "7" ); break;
			case K_8:	appendBuffer( "8" ); break;
			case K_9:	appendBuffer( "9" ); break;

			case K_1000:appendBuffer( "000" );
				break;

			case K_DIV:
				throw new ArithmeticException( "ERR: div by zero" );
			case K_MUL:	appendBuffer( "*" ); break;
			case K_PLUS:appendBuffer( "+" ); break;
			case K_MIN:	appendBuffer( "-" ); break;
			case K_EQ:
				calc = true;
				break;

			case K_VAT:
				//Display
				String input = dsb.toString();
				double input2double = Double.parseDouble(input);

				//MwSt
				double mwst = (input2double / (100 + VAT_RATE)) * VAT_RATE;
				String mwst2string = String.format(Locale.US, "%.2f", mwst);

				//Netto
				double netto = input2double - mwst;
				String netto2string = String.format(Locale.US, "%.2f", netto);

				view.writeSideArea(
						"Brutto: " + input + "\n" +
								VAT_RATE + "% MwSt: " + mwst2string + "\n" +
								"Netto: " + netto2string
				);
				break;

			case K_DOT:	appendBuffer( "." );
				break;

			case K_BACK:
				dsb.setLength( Math.max( 0, dsb.length() - 1 ) );
				break;

			case K_C:
				view.writeSideArea( "" );
			case K_CE:
				dsb.delete( 0,  dsb.length() );
				break;

			default:
			}
			String display = dsb.length()==0? "0" : dsb.toString();
			view.writeTextArea( display );

			if (calc) {
				String input = dsb.toString();
				System.out.println("please solve: " + input);

				// not allowed to div by 0
				if (input.contains("/0")) {
					calc = false;
					throw new ArithmeticException("ERR: div by zero");
				}

				// cant end with an operator
				if (input.endsWith("+") || input.endsWith("-") ||
						input.endsWith("*") ||input.endsWith("/")) {
					calc = false;
					throw new ArithmeticException("ERR: invalid input");
				}

				// formatter for double values -> 15 digits after dot
				double x = Double.parseDouble(calc(input));
				DecimalFormat df = new DecimalFormat("0.###############");

				// show result
				view.writeSideArea(df.format(x));
				System.out.println("solution: " + df.format(x));
				dsb = new StringBuffer(dsb.toString());
				calc = false;
			}


		} catch( ArithmeticException e ) {
			view.writeSideArea( e.getMessage() );
		}
	}

	/*
	 * Private method(s).
	 */
	private void appendBuffer( String d ) {
		if( dsb.length() <= CalculatorGUI_Intf.DISPLAY_MAXDIGITS ) {
			dsb.append( d );
		}
	}

	private boolean checkOperator(String c) {
		return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("(") || c.equals(")");
	}

	private boolean checkPriority(String peek, String newAdded) {
		return getPriorityOfOperator(peek) < getPriorityOfOperator(newAdded);
	}

	private int getPriorityOfOperator(String operator) {
		switch (operator) {
			case "(":
				return 3;
			case ")":
				return 3;
			case "+":
				return 1;
			case "-":
				return 1;
			case "*":
				return 2;
			case "/":
				return 2;
		}
		return -1;
	}

	private String eval(String operator, String left, String right) {
		double result = 0;
		if (operator.equals("+")) result = Double.parseDouble(left) + Double.parseDouble(right);
		if (operator.equals("-")) result = Double.parseDouble(left) - Double.parseDouble(right);
		if (operator.equals("*")) result = Double.parseDouble(left) * Double.parseDouble(right);
		if (operator.equals("/")) result = Double.parseDouble(left) / Double.parseDouble(right);
		return result + "";
	}

	private String calc(String expression) {
		Stack<String> stack = new Stack<>(); // the stack
		StringBuilder pos = new StringBuilder(); // for numbers > 9
		StringBuffer postfix = new StringBuffer(); // the postfix with the correct order for evaluating
		StringBuilder substring = new StringBuilder(); // only if there are parenthesis; creating substrings
		boolean parenth = false;
		int sub; // stack-size
		int paren = 0; // parenthesis index

		// infix to postfix start
		// expression contains the input
		for (int i = 0; i < expression.length(); i++) {

			// recursive if there are parenthesis -> result will be added to the postfix string
			if (parenth) {
				if (String.valueOf(expression.charAt(i)).equals(")")) {
					if (paren == 0) {
						parenth = false;
						postfix.append(calc(substring.toString()));
						substring = new StringBuilder();
						continue;
					}
					paren--;
				}

				if (String.valueOf(expression.charAt(i)).equals("(")) paren++;

				substring.append(String.valueOf(expression.charAt(i)));
			} else {
				// putting numbers into stack in the order how they appear
				if (!checkOperator(String.valueOf(expression.charAt(i)))) {
					pos.append(String.valueOf(expression.charAt(i)));
					if (i == expression.length() - 1)
						postfix.append(pos).append(" "); // if last char then dont add gap to the string
					continue;
				}

				// append gap between numbers to maintain the correct number later when evaluating
				postfix.append(pos).append(" ");
				if (postfix.toString().trim().isEmpty())
					postfix = new StringBuffer(); // removing empty string at the beginning

				// re-init pos if next char is operator
				pos = new StringBuilder();

				// putting operators in stack
				if (checkOperator(String.valueOf(expression.charAt(i)))) {
					// if ")" is the next char put everything to postfix until reaching "("
                    /*if (String.valueOf(expression.charAt(i)).equals(")")) {
                        while (!stack.peek().equals("(")) postfix.append(stack.pop());
                        stack.pop();
                    } else {*/

					// if stack is empty can just push
					if (stack.empty() ||
							// next operator has higher prio than peek -> push
							checkPriority(stack.peek(), String.valueOf(expression.charAt(i)))) {
						if (String.valueOf(expression.charAt(i)).equals("(")) parenth = true;
						else stack.push(String.valueOf(expression.charAt(i)));
					} else {
						sub = stack.size();
						// pop all in stack if the next one has lower prio than peek
						for (int j = 0; j < sub; j++) postfix.append(stack.pop()).append(" ");
						//push
						stack.push(String.valueOf(expression.charAt(i)));
					}
				}
			}
		}

		// just pop the last operator :)
		if (stack.size() == 1) postfix.append(stack.pop());
		else { // if more than one left just pop all
			sub = stack.size();
			for (int j = 0; j < sub; j++) postfix.append(stack.pop()).append(" ");
		}
		// infix to postfix end

		// evaluate postfix start
		pos = new StringBuilder();
		for (int i = 0; i < postfix.length(); i++) {
			// for numbers
			if (!String.valueOf(postfix.charAt(i)).equals(" ") && !checkOperator(String.valueOf(postfix.charAt(i)))) {
				pos.append(String.valueOf(postfix.charAt(i)));
				continue;
			}

			if (!pos.toString().equals("")) stack.push(pos.toString());

			// re-init pos after gap
			pos = new StringBuilder();

			// if operator then start calculating...
			if (checkOperator(String.valueOf(postfix.charAt(i)))) {
				String right = stack.pop();
				String left = stack.pop();
				stack.push(eval(String.valueOf(postfix.charAt(i)), left, right));
			}
		}
		// evaluate postfix end

		// return value for substrings
		if (stack.size() == 0) return postfix.toString();

		//return value for whole expression
		return stack.pop();
	}



}
