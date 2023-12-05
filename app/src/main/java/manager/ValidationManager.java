package manager;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.units.qual.degrees;

import cli_calculator.Calculator;
import cli_calculator.Tuple;

import java.util.ArrayList;
import java.util.Arrays;

import error.*;
import cli_calculator.Tuple;
import user.FunctionForm;

public class ValidationManager {
    public ValidationManager() {

    }

    // 변수 정의부 유효성 체크
    public void checkVariableDefineExpression(String expression) throws ErrorHandler {
        String regex = "\\$[a-z0-9]+\\s=\\s+.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            throw new ErrorHandler(ErrorType.VARIABLE_DEFINE_ERROR);
        }
    }

    // 함수 정의부 유효성 체크
    public void checkFunctionDefine(String expression) throws ErrorHandler {
        String regex = "@[a-z0-9]+\\[(%[a-z0-9]+(?:,\\s*%[a-z0-9]+)*)?\\]+\\s=\\s+.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            throw new ErrorHandler(ErrorType.FUNCTION_DEFINE_ERROR);
        }
    }

    // 함수 수식부 유효성 체크
    public void checkFunctionExpression(FunctionForm function, int parameterSize) throws ErrorHandler {
        ArrayList<Double> arguments = new ArrayList<Double>();
        for (int i=0; i<parameterSize;i++)
            arguments.add(1.0);
        String expression = function.getFunction(arguments);
        try {
            (new Calculator()).calculate(expression);    
        } catch (ErrorHandler e) {
            throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
        }
    }

    // 괄호쌍 체크
    public boolean checkBracketPair(String expression) throws ErrorHandler {
        Stack<Character> smallBracket = new Stack<>();
        Stack<Character> bigBracket = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                smallBracket.push('(');
            } else if (expression.charAt(i) == ')') {
                if (smallBracket.empty())
                    return false;
                smallBracket.pop();
            }

            if (expression.charAt(i) == '[') {
                bigBracket.push('[');
            } else if (expression.charAt(i) == ']') {
                if (bigBracket.empty())
                    return false;
                bigBracket.pop();
            }
        }

        if (smallBracket.empty() && bigBracket.empty())
            return true;

        throw new ErrorHandler(ErrorType.BRACKET_ERROR);
    }

    // 수식의 연산자, 피연산자 문법확인
    public boolean checkValidExpression(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-'
                    || expression.charAt(i) == '*' || expression.charAt(i) == '/'
                    || expression.charAt(i) == '^' || expression.charAt(i) == '(' || expression.charAt(i) == ')') {
                continue;
            } else if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                continue;
            } else if (expression.charAt(i) == '_' || expression.charAt(i) == ' ') {
                continue;
            } else if (expression.charAt(i) == '$' && (i + 1) < expression.length()
                    && expression.charAt(i + 1) == 'x') {
                continue;
            } else if (expression.charAt(i) == 'x' && i > 0 && expression.charAt(i - 1) == '$') {
                continue;
            } else if (expression.charAt(i) == '.' && i > 0 && (i + 1) < expression.length() &&
                    expression.charAt(i - 1) >= '0' && expression.charAt(i - 1) <= '9'
                    && expression.charAt(i + 1) >= '0' && expression.charAt(i + 1) <= '9')

            {
                continue;
            } else {
                return false;
            }

        }

        return true;
    }

    // 피연산자와 연산 결과 값의 유효 범위 체크
    public void checkRangeInPerform(double lhs, double rhs, double result) throws ErrorHandler {
        boolean isValidLhs = (lhs >= -Double.MAX_VALUE && lhs <= Double.MAX_VALUE);
        boolean isValidRhs = (rhs >= -Double.MAX_VALUE && rhs <= Double.MAX_VALUE);
        boolean isValidResult = (result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE);
        if (isValidLhs && isValidRhs && isValidResult) {
            return;
        }
        throw new ErrorHandler(ErrorType.VALUE_OUT_OF_BOUND_ERROR);
    }

    // =, $, @ 문자로 함수 혹은 변수 대입식인지 판별
    public Tuple checkExpressionType(String expression) throws ErrorHandler {
        if (expression.length() < 1 || !expression.contains("="))
            return new Tuple("", expression); // 일반 수식
        expression = expression.trim();
        if (expression.charAt(0) == '$') { // '$'로 시작하면 변수 대입문
            checkVariableDefineExpression(expression);
            String[] splitExpression = expression.split(" =");
            String variableName = splitExpression[0].substring(1);
            return new Tuple(variableName, splitExpression[1].trim());
        } else if (expression.charAt(0) == '@') { // '@'로 시작하면 함수 대입문
            checkFunctionDefine(expression);
            String[] splitExpression = expression.split("\\[");
             // 함수 이름
            String functionName = splitExpression[0].substring(1);
            splitExpression = splitExpression[1].split("\\]");
            // Parameter Array
            ArrayList<String> parameterArray = new ArrayList<String>();
            if (!splitExpression[0].isEmpty()) {
                parameterArray = new ArrayList<String>(Arrays.stream(splitExpression[0].split(",")).map(param -> param.trim()).toList());
            }
            // 저장될 수식
            String savedExpression = splitExpression[1].split("=")[1].trim();
            return new Tuple(functionName, savedExpression, parameterArray);
        }
        throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
    }
}
