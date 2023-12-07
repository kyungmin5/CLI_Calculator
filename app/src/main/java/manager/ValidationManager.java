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
import operator.Operator;
import operator.OperatorType;
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
            arguments.add(0.0);
        String expression = function.getFunction(arguments);
        try {
                        Stack<Double> numbers = new Stack<>();
                        Stack<Character> operators = new Stack<>();
                        int stackCount = 0;

                        Operator operator = new Operator();

                        try {
                            for (int i = 0; i < expression.length(); i++) {
                                char currentChar = expression.charAt(i);

                                if (currentChar == ' ') {
                                    continue; // ���� ���ڴ� ����
                                }

                                if (Character.isDigit(currentChar) || currentChar == '.') {
                                    // ���ڸ� �����Ͽ� ���ÿ� ����
                                    StringBuilder numBuilder = new StringBuilder();
                                    while (i < expression.length()
                                            && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                                        numBuilder.append(expression.charAt(i));
                                        i++;
                                    }
                                    numbers.push(0.0);
                                    i--; // ���� ���� �� �ε��� ����
                                } else if (currentChar == '(') {

                                    stackCount++;
                                    int j = i + 1;
                                    for (; j < expression.length() && stackCount > 0; j++) {
                                        if (expression.charAt(j) == '(') {
                                            stackCount++;
                                        } else if (expression.charAt(j) == ')') {
                                            stackCount--;
                                        }
                                    }
                                    numbers.push(0.0);
                                    i = j - 1; // ���Ⱑ ���� ) �� �ִ� �ε���. i�� ) �� index�� ����������.
                                }else if (operator.isOperator(currentChar)) {
                                    // ������ �켱������ �����Ͽ� ���ÿ� push
                                    if (i + 1 < expression.length()) {
                                        if (currentChar == '-'
                                                && (Character.isDigit(expression.charAt(i + 1))
                                                        || expression.charAt(i + 1) == '(')) {
                                            // �̰� �� ���� ��ȣ �ǿ������� ����. �̰� �ƴϸ� ��� ������ ó���Ѵ�
                                            // �ٸ� �̰��� ���� ��ȣ ���°� ������, �����ڰ� �ƴ϶�� ���� �ƴϹǷ� �߰� ó���� �ʿ��ϴ�
                                            boolean isOperator = false;

                                            for (int index = i - 1; index >= 0; index--) {
                                                if (expression.charAt(index) != ' ') {
                                                    if (!operator.isOperator(expression.charAt(index))) {
                                                        isOperator = true;
                                                    }

                                                    break;
                                                }
                                            }
                                            if (!isOperator) // - �ٷ� �տ� �� �����ڰ� �־ -�� ��ȣ�� ���� ���ۿ� ���� ��
                                            {
                                                continue;
                                            }
                                        }
                                    }

                                    // currnetChar�� + - �̸� �ݵ�� ����
                                    while (!operators.isEmpty() && Operator.getType(operators.peek()).getPriority() >= Operator.getType(currentChar).getPriority()) {
                                        if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                        numbers.pop();
                                        if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                        numbers.pop();
                                        if(operators.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                        operators.pop();
                                        numbers.push(0.0);
                                        // ������ ������Ʈ
                                    }
                                    operators.push('+');
                                } 
                            }

                            // ���� �����ڸ� ��� ó��
                            while (!operators.isEmpty() && numbers.size() >= 2) {
                                if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                numbers.pop();
                                if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                numbers.pop();
                                if(operators.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                                operators.pop();
                                numbers.push(0.0);
                                // ������ ������Ʈ
                            }

                            numbers.pop();

                            if (!numbers.isEmpty() || !operators.isEmpty())
                                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);

                        } catch (ErrorHandler e) {
                            throw e;
                        }    
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

    //  =, $, @ 문자로 함수 혹은 변수 대입식인지 판별
    public Tuple checkExpressionType(String expression) throws ErrorHandler {
        if (expression.length() < 1 || !expression.contains("="))
            return new Tuple("", expression); // ?���? ?��?��
        expression = expression.trim();
        if (expression.charAt(0) == '$') { // '$'�? ?��?��?���? �??�� ????���?
            checkVariableDefineExpression(expression);
            String[] splitExpression = expression.split(" =");
            String variableName = splitExpression[0].substring(1);
            return new Tuple(variableName, splitExpression[1].trim());
        } else if (expression.charAt(0) == '@') { // '@'�? ?��?��?���? ?��?�� ????���?
            checkFunctionDefine(expression);
            String[] splitExpression = expression.split("\\[");
             // ?��?�� ?���?
            String functionName = splitExpression[0].substring(1);
            splitExpression = splitExpression[1].split("\\]");
            // Parameter Array
            ArrayList<String> parameterArray = new ArrayList<String>();
            if (!splitExpression[0].isEmpty()) {
                parameterArray = new ArrayList<String>(Arrays.stream(splitExpression[0].split(",")).map(param -> param.trim()).toList());
            }
            // ????��?�� ?��?��
            String savedExpression = splitExpression[1].split("=")[1].trim();
            return new Tuple(functionName, savedExpression, parameterArray);
        }
        throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
    }
}
