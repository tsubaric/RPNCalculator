import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;
import java.util.regex.Pattern;

public class RPNCalculator {
    private Stack<Double> stack;
    private Pattern identToken;
    private Pattern binopToken;

    public RPNCalculator() {
        // Initialize stack to be an empty Stack to hold double data values
        stack = new Stack<Double>();

        // these are for processing text input
        identToken = Pattern.compile("\\p{Alpha}+");
        binopToken = Pattern.compile("\\+|\\*|-|\\/|\\^");
    }

    /**
     * Push the element x into the stack(x should be on the top of the stack), and return x.
     */
    public double enter(double x) {
        stack.push(x);
        return x;
    }

    public double add() {
        return stack.push(stack.pop() + stack.pop());
    }

    /**
     * Example: calculate 5 - 2 = ?, push the output to stack, and return the output
     * Input (you should enter): 5 2 -
     * Output: 3.0
     */
    public double subtract() {
        double y = stack.pop();
        double x = stack.pop();
        return stack.push(x - y);
    }

    /**
     * Example: calculate 2 * 5 = ?, push the output to stack, and return the output
     * Input (you should enter): 2 5 *
     * Output: 10.0
     */
    public double multiply() {
        return stack.push(stack.pop() * stack.pop());
    }

    /**
     * Example: calculate 8 / 2 = ?, push the output to stack, and return the output
     * Input (you should enter): 8 2 /
     * Output: 4.0
     */
    public double divide() {
        double y = stack.pop();
        double x = stack.pop();
        return stack.push(x / y);
    }

    /**
     * Example: calculate 2^3 = ?, push the output to stack, and return the output
     * Input (you should enter): 2 3 ^
     * Output: 8.0
     * Hint: Use Math.pow()
     */
    public double power() {
        double y = stack.pop();
        double x = stack.pop();
        return stack.push(Math.pow(x, y));
    }

    /**
     * Example: calculate e^2 = ?, push the output to stack, and return the output
     * Input (you should enter): 2 exp
     * Output: 7.389056099
     * Hint: Use Math.exp()
     */
    public double exp() {
        double x = stack.pop();
        return stack.push(Math.exp(x));
    }

    /**
     * Example: calculate ln e = ?, push the output to stack, and return the output
     * Input (you should enter): e log
     * Output: 1
     * Hint: Use Math.log()
     */
    public double log() {
        double x = stack.pop();
        return stack.push(Math.log(x));
    }

    /**
     * Example: calculate sin(0) = ?, push the output to stack, and return the output
     * Input (you should enter): 0 sin
     * Output: 0
     * Hint: Use Math.sin()
     */
    public double sin() {
        double x = stack.pop();
        return stack.push(Math.sin(x));
    }

    /**
     * Example: calculate cos(0) = ?, push the output to stack, and return the output
     * Input (you should enter): 0 cos
     * Output: 1
     * Hint: Use Math.cos()
     */
    public double cos() {
        double x = stack.pop();
        return stack.push(Math.cos(x));
    }

    /**
     * Example: calculate √100 = ?, push the output to stack, and return the output
     * Input (you should enter): 100 sqrt
     * Output: 10
     * Hint: Use Math.sqrt()
     */
    public double sqrt() {
        double x = stack.pop();
        return stack.push(Math.sqrt(x));
    }

    /**
     * Return the element at the top of the stack and remove it.
     */
    public double pop() {
        return stack.pop();
    }

    /**
     * Clear your stack, expect your stack to be empty
     */
    public void clear() {
        stack.clear();
    }

    public void processLine(String line) throws CalculatorException {
        Scanner linescan = new Scanner(line);
        while (linescan.hasNext()) {
            if (linescan.hasNextDouble()) {
                enter(linescan.nextDouble());
            } else if (linescan.hasNext(binopToken)) {
                String binop = linescan.next(binopToken);
                switch (binop) {
                    case "+":
                        add();
                        break;
                    case "-":
                        subtract();
                        break;
                    case "*":
                        multiply();
                        break;
                    case "/":
                        divide();
                        break;
                    case "^":
                        power();
                        break;
                    default:
                        throw (new CalculatorException("Unknown operation: " + binop));

                }
            } else if (linescan.hasNext(identToken)) {
                String func = linescan.next(identToken);
                switch (func) {
                    case "exp":
                        exp();
                        break;
                    case "log":
                        log();
                        break;
                    case "sin":
                        sin();
                        break;
                    case "cos":
                        cos();
                        break;
                    case "sqrt":
                        sqrt();
                        break;
                    case "inspect":
                        System.out.println("Stack: " + stack);
                        break;
                    case "end":
                    case "break":
                    case "stop":
                    case "quit":
                        System.exit(0);
                        break;
                    default:
                        throw (new CalculatorException("Unknown function: " + func));
                }
            } else {
                throw (new CalculatorException("Cannot process \"" + line + "\""));
            }
        }
    }

        public static void main (String[]args){
            Scanner scnr = null;
            if (args.length > 0) {
                try {
                    scnr = new Scanner(new File(args[0]));
                } catch (FileNotFoundException e) {
                    System.out.println("Could not find file \"" + args[0] + "\"");
                    System.out.println("Exception: " + e);
                    System.exit(1);
                } catch (IOError e) {
                    System.out.println("IOException opening file \"" + args[0] + "\"");
                    System.out.println("Exception: " + e);
                    System.exit(1);
                }
            } else {
                scnr = new Scanner(System.in);
                // Some helpful instructions
                System.out.println("RPN calculator");
                System.out.println("Separate inputs by spaces");
                System.out.println("Enter \"stop\", \"quit\", \"end\" to end program" +
                        " and \"inspect\" to see the state of the stack");
                System.out.println("Operators: +, -, *, /, ^");
                System.out.println("Functions: sin, cos, exp, log, sqrt");
            }

            RPNCalculator calc = new RPNCalculator();
            while (scnr.hasNextLine()) {
                // process each line separately
                try {
                    String line = scnr.nextLine();
                    System.out.println("Processing line: " + line);
                    calc.processLine(line);
                    System.out.println("Result is " + calc.pop());
                } catch (CalculatorException e) {
                    System.out.println("" + e);
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
                calc.clear();
            }
        }
    }

