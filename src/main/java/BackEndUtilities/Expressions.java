package BackEndUtilities;

import Managers.ErrorManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.*;

import java.util.*;
import java.util.stream.Collectors;

public class Expressions {
    private static final Logger logger = LogManager.getLogger(Expressions.class.getName());
    private static boolean evaluate = true;
    private static final Map<String, String> arguments = new HashMap<>();

    /**
     * Expression evaluation enable.
     */
    public static void enableEvaluation() {
        logger.debug("Expression evaluation on");
        Expressions.evaluate = true;
    }

    /**
     * Expression evaluation disable.
     */
    public static void disableEvaluation() {
        logger.debug("Expression evaluation on");
        Expressions.evaluate = false;
    }


    /**
     * If the expression evaluation is on, then return true, else return false.
     *
     * @return A boolean value.
     */
    public static boolean isEvaluationOn() {
        logger.debug("Expression evaluation is " + (Expressions.evaluate ? "on" : "off"));
        return Expressions.evaluate;
    }

    /**
     * If the variable doesn't exist, add it to the arguments map
     *
     * @param variable The variable name of the argument.
     * @param value The value of the argument.
     */
    public static void addArgument(String variable, String value) {
        if(!Expressions.arguments.containsKey(variable)) {
            Expressions.arguments.put(variable, value);
            logger.debug("Argument added: " + variable);
        }
        else {
            logger.debug("Argument variable already exists.");
        }
    }

    /**
     * It adds a new argument to the arguments HashMap, or changes the value of an existing argument
     *
     * @param variable The name of the variable you want to set.
     * @param value The value of the variable.
     */
    public static void setArgument(String variable, String value){
        if(!Expressions.arguments.containsKey(variable)) {
            Expressions.arguments.put(variable, value);
            logger.debug("Argument added: " + variable);
        }
        else {
            Expressions.arguments.replace(variable, value);
            logger.debug("Changed value of variable " + variable);
        }
    }

    /**
     * It returns the value of the argument with the given name
     *
     * @param variable The name of the variable you want to get the value of.
     * @return The value of the variable in the arguments map.
     */
    public static String getArgument(String variable) {
        return Expressions.arguments.get(variable);
    }

    /**
     * If the variable exists, and it's value is not empty, and it's value is numeric, then return true
     *
     * @param variable The variable to check
     * @return A boolean value
     */
    public static boolean ensureArgument(String variable) {
        boolean contains = arguments.containsKey(variable);

        if(contains){
            String varValue = arguments.get(variable);
            if(varValue.equals("")){
                ErrorManager.sendErrorMessage("Variables", "Value of variable " + variable + " not set");
            }else if(!isNumeric(varValue)){
                ErrorManager.sendErrorMessage("Variables", "Value of variable " + variable + " not valid");
            }

        }else{
            ErrorManager.sendErrorMessage("Variables", "Variable " + variable + " does not exist");
        }

        return contains;
        //return !Expressions.arguements.stream().filter(a -> variable.equals(a.getVariable())).map(Pair::getValue).toList().isEmpty();
    }

    public static Map<String, String> getArguments() {
        return Expressions.arguments;
    }

    public static void clearArguments() {
        Expressions.arguments.clear();
    }

    public static void removeArgument(String variable) {
        Expressions.arguments.remove(variable);
    }

    /**
     * It takes a string expression and a list of arguments and returns the result of the expression
     *
     * @param expression The expression to be evaluated.
     * @return The result of the expression.
     */
    public static double eval(String expression) {
        logger.debug("Evaluating expressions");
        Expression exp = new Expression(expression);
        if(!Expressions.arguments.isEmpty()) {
            for (String key : Expressions.arguments.keySet()) {
                Argument arg = new Argument(arguments.get(key));
                exp.addArguments(arg);
            }
        }
        return exp.calculate();
    }


    /**
     * For each data point in the dataset, create an expression, add the variables as arguments, and then calculate the
     * expression
     *
     * @param dataset The dataset to be evaluated.
     * @return A list of BigDecimal values.
     */
    public static List<Double> eval(Sample dataset) {
        logger.debug("Evaluating expressions");

        List<Expression> exps = dataset.getData().stream().map(d -> {
            Expression exp = new Expression(d);
            if(!Expressions.arguments.isEmpty()) {
                for (String key : Expressions.arguments.keySet()) {
                    Argument arg = new Argument(arguments.get(key));
                    exp.addArguments(arg);
                }
            }
            return exp;
        }).toList();

        return exps.stream().map(Expression::calculate).peek(System.out::println).collect(Collectors.toList());
    }

    /**
     * Creates a list of expressions from the input list of strings, add the variables to each expression, and then
     * calculate the value of each expression and return a list of the results
     *
     * @param expressions A list of expressions to evaluate.
     * @return A list of BigDecimal values.
     */
    public static List<Double> eval(List<String> expressions) {
        logger.debug("Evaluating expressions");
        List<Expression> exps = expressions.stream().map(d -> {
            Expression exp = new Expression(d);
            if(!Expressions.arguments.isEmpty()) {
                for (String key : Expressions.arguments.keySet()) {
                    Argument arg = new Argument(arguments.get(key));
                    exp.addArguments(arg);
                }
            }
            return exp;
        }).toList();
        return exps.stream().map(Expression::calculate).collect(Collectors.toList());
    }

    /**
     * Checks if a string is numeric
     * @param str the string to analyze
     * @return whether the string is numeric
     */
    private static boolean isNumeric(String str){
        if(str == null){
            return false;
        }
        try{
            double d = Double.parseDouble(str);
        }catch(NumberFormatException e){
            return false;
        }

        return true;
    }

}
