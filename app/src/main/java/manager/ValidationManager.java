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

    // 蹂??닔 ?젙?쓽遺? ?쑀?슚?꽦 泥댄겕
    public void checkVariableDefineExpression(String expression) throws ErrorHandler {
        String regex = "\\$[a-z0-9]+\\s=\\s+.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            throw new ErrorHandler(ErrorType.VARIABLE_DEFINE_ERROR);
        }
    }

    // ?븿?닔 ?젙?쓽遺? ?쑀?슚?꽦 泥댄겕
    public void checkFunctionDefine(String expression) throws ErrorHandler {
        String regex = "@[a-z0-9]+\\[(%[a-z0-9]+(?:,\\s*%[a-z0-9]+)*)?\\]+\\s=\\s+.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            throw new ErrorHandler(ErrorType.FUNCTION_DEFINE_ERROR);
        }
    }

    // ?븿?닔 ?닔?떇遺? ?쑀?슚?꽦 泥댄겕
    public void checkFunctionExpression(FunctionForm function, int parameterSize) throws ErrorHandler {
        ArrayList<Double> arguments = new ArrayList<Double>();
        for (int i=0; i<parameterSize;i++)
            arguments.add(0.0);
        String expression = function.getFunction(arguments);
        try {
            recursiveCaluculate(expression);    
        } catch (ErrorHandler e) {
            throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
        }
    }

    private void recursiveCaluculate(String expression) throws ErrorHandler {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int stackCount = 0;

        Operator operator = new Operator();

        try {
            for (int i = 0; i < expression.length(); i++) {
                char currentChar = expression.charAt(i);

                if (currentChar == ' ') {
                    continue; // 공백 문자는 무시
                }

                if (Character.isDigit(currentChar) || currentChar == '.') {
                    // 숫자를 추출하여 스택에 저장
                    StringBuilder numBuilder = new StringBuilder();
                    while (i < expression.length()
                            && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        numBuilder.append(expression.charAt(i));
                        i++;
                    }
                    numbers.push(0.0);
                    i--; // 숫자 추출 후 인덱스 복원
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
                    i = j - 1; // 여기가 실제 ) 가 있는 인덱스. i를 ) 의 index로 보내버린다.
                }else if (operator.isOperator(currentChar)) {
                    // 연산자 우선순위를 고려하여 스택에 push
                    if (i + 1 < expression.length()) {
                        if (currentChar == '-'
                                && (Character.isDigit(expression.charAt(i + 1))
                                        || expression.charAt(i + 1) == '(')) {
                            // 이게 딱 음수 부호 피연산자의 형태. 이게 아니면 모두 연산자 처리한다
                            // 다만 이것이 음수 부호 형태가 맞지만, 연산자가 아니라는 것은 아니므로 추가 처리가 필요하다
                            boolean isOperator = false;

                            for (int index = i - 1; index >= 0; index--) {
                                if (expression.charAt(index) != ' ') {
                                    if (!operator.isOperator(expression.charAt(index))) {
                                        isOperator = true;
                                    }

                                    break;
                                }
                            }
                            if (!isOperator) // - 바로 앞에 또 연산자가 있어서 -가 부호로 사용될 수밖에 없을 떄
                            {
                                continue;
                            }
                        }
                    }

                    // currnetChar이 + - 이면 반드시 실행
                    while (!operators.isEmpty() && Operator.getType(operators.peek()).getPriority() >= Operator.getType(currentChar).getPriority()) {
                        if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                        numbers.pop();
                        if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                        numbers.pop();
                        if(operators.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                        operators.pop();
                        numbers.push(0.0);
                        // 직전값 업데이트
                    }
                    operators.push('+');
                } 
            }

            // 남은 연산자를 모두 처리
            while (!operators.isEmpty() && numbers.size() >= 2) {
                if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                numbers.pop();
                if(numbers.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                numbers.pop();
                if(operators.isEmpty()) throw new ErrorHandler(ErrorType.FUNCTION_EXPRESSION_ERROR);
                operators.pop();
                numbers.push(0.0);
                // 직전값 업데이트
            }

            numbers.pop();

            if (!numbers.isEmpty() || !operators.isEmpty())
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);

        } catch (ErrorHandler e) {
            throw e;
        }
    }

    // 愿꾪샇?뙇 泥댄겕
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

    // ?닔?떇?쓽 ?뿰?궛?옄, ?뵾?뿰?궛?옄 臾몃쾿?솗?씤
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

    // ?뵾?뿰?궛?옄??? ?뿰?궛 寃곌낵 媛믪쓽 ?쑀?슚 踰붿쐞 泥댄겕
    public void checkRangeInPerform(double lhs, double rhs, double result) throws ErrorHandler {
        boolean isValidLhs = (lhs >= -Double.MAX_VALUE && lhs <= Double.MAX_VALUE);
        boolean isValidRhs = (rhs >= -Double.MAX_VALUE && rhs <= Double.MAX_VALUE);
        boolean isValidResult = (result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE);
        if (isValidLhs && isValidRhs && isValidResult) {
            return;
        }
        throw new ErrorHandler(ErrorType.VALUE_OUT_OF_BOUND_ERROR);
    }

    // =, $, @ 臾몄옄濡? ?븿?닔 ?샊??? 蹂??닔 ????엯?떇?씤吏? ?뙋蹂?
    public Tuple checkExpressionType(String expression) throws ErrorHandler {
        if (expression.length() < 1 || !expression.contains("="))
            return new Tuple("", expression); // ?씪諛? ?닔?떇
        expression = expression.trim();
        if (expression.charAt(0) == '$') { // '$'濡? ?떆?옉?븯硫? 蹂??닔 ????엯臾?
            checkVariableDefineExpression(expression);
            String[] splitExpression = expression.split(" =");
            String variableName = splitExpression[0].substring(1);
            return new Tuple(variableName, splitExpression[1].trim());
        } else if (expression.charAt(0) == '@') { // '@'濡? ?떆?옉?븯硫? ?븿?닔 ????엯臾?
            checkFunctionDefine(expression);
            String[] splitExpression = expression.split("\\[");
             // ?븿?닔 ?씠由?
            String functionName = splitExpression[0].substring(1);
            splitExpression = splitExpression[1].split("\\]");
            // Parameter Array
            ArrayList<String> parameterArray = new ArrayList<String>();
            if (!splitExpression[0].isEmpty()) {
                parameterArray = new ArrayList<String>(Arrays.stream(splitExpression[0].split(",")).map(param -> param.trim()).toList());
            }
            // ????옣?맆 ?닔?떇
            String savedExpression = splitExpression[1].split("=")[1].trim();
            return new Tuple(functionName, savedExpression, parameterArray);
        }
        throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
    }
}
