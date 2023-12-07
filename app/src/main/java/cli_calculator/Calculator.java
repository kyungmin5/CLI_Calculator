package cli_calculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import manager.*;
import error.*;
import user.*;
import operator.*;

public class Calculator {
    private double previousValue = Double.NaN;
    private UserVariable variable = new UserVariable();
    private UserFunction function = new UserFunction();

    private ValidationManager validationManager = new ValidationManager();

    // 직접 값 업데이트
    private void setPreviousValue(double value) {
        this.previousValue = value;
    }

    // 계산하기 main 함수
    public double calculate(String expression) throws ErrorHandler {
        if (expression.isBlank()) {
            throw new ErrorHandler(ErrorType.EMPTY_ERROR);
        }
        Object[] RESULT = preProcessing(expression);

        // 입력된 문자열의 타입을 판별
        ExpressionType expressionType = (ExpressionType) RESULT[0];
        expression = (String) RESULT[1]; // 수식이면 수식이 ^에 대해 () 처리만 하고 나오고, 대입이라면 = 오른쪽 부분만이 ^처리 후에 나오게 된다
        String variableName = (String) RESULT[2];

        double result = 0;
 
        switch (expressionType) {
            case MATHEMATICAL: // 대입식이 아닌 경우
                result = recursiveCaluculate(expression);
                setPreviousValue(result);
                return result;
            case VARIABLE: // 변수 대입식인 경우
                result = recursiveCaluculate(expression);
                variable.setVariable(variableName, result);
                return result;
            case FUNCTION: // 함수 대입식인 경우
                ArrayList<String> paras = (ArrayList<String>) RESULT[3];
                function.setFunction(variable, previousValue, variableName, paras, expression);
                return Double.NaN;
            default:
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_TYPE_ERROR);
        }
        
    }

    // 재귀 계산
    private double recursiveCaluculate(String expression) throws ErrorHandler {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int stackCount = 0;


        double finalResult = 0;
        int isOperandShouldMinus = 1;

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
                    try {
                        double num = Double.parseDouble(numBuilder.toString());    
                        numbers.push(num * isOperandShouldMinus);
                    } catch  (NumberFormatException e) {
                        throw new ErrorHandler(ErrorType.INVALID_OPERAND_ERROR);
                    }
                    // 직전값 업데이트
                    // previousValue = num * isOperandShouldMinus;
                    isOperandShouldMinus = 1;
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
                    numbers.push(recursiveCaluculate(expression.substring(i + 1, j - 1)) * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                    i = j - 1; // 여기가 실제 ) 가 있는 인덱스. i를 ) 의 index로 보내버린다.
                } else if (currentChar == '$') {
                    int index = 1;
                    String variableName = "";
                    for (; index + i < expression.length() && expression.charAt(index + i) != ' '; index++) {
                        variableName += expression.charAt(index + i);
                    }
                    numbers.push(variable.getVariable(variableName) * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                    i = index + i;
                } else if (currentChar == '@') {
                    String subExpression = expression.substring(i+1);
                    // 함수 이름 파싱
                    String functionName = subExpression;
                    if (functionName.contains("[") && functionName.contains("]")) {
                        functionName = functionName.substring(0, functionName.indexOf("["));
                    } else {
                        throw new ErrorHandler(ErrorType.FUNCTION_BRACKET_ERROR);
                    }
                    // int index = i + subExpression.indexOf("]")+1;
                    
                    // @r[%d] = %d + 1
                    //  @t[%q,%w] = %q + %w
                    // @d[%a,%s,%d,     %f] = %a + %a + %s + %f
                    // @d[@t[@r[2],@r[2]],(5-1),@r[1234],@d[1,3,    5   , 7]]  - 10 + @t[   2,4]

                    // 파라미터 파싱
                    int endIndex = closeBracketIndex(subExpression);
                    String argumentsString = subExpression.substring(subExpression.indexOf("["), endIndex);
                    ArrayList<Double> argumentList = new ArrayList<Double>();
                    if (argumentsString.length() > 2) {
                        argumentsString = argumentsString.substring(1, argumentsString.length() - 1).trim();
                        argumentList = calculateArguments(argumentsString);
                    }

                    // 함수 값 
                    String functionExpression = function.getFunction(functionName, argumentList);
                    Double result = recursiveCaluculate(functionExpression);

                    numbers.push(result * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                    i = i + endIndex;
                } else if (operator.isOperator(currentChar)) {
                    // 연산자 우선순위를 고려하여 스택에 push
                    if (i + 1 < expression.length()) {
                        if (currentChar == '-'
                                && (Character.isDigit(expression.charAt(i + 1))
                                        || expression.charAt(i + 1) == '('
                                        || expression.charAt(i + 1) == '_'
                                        || expression.charAt(i + 1) == '$')
                                || expression.charAt(i + 1) == '@') {
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
                                isOperandShouldMinus = -1; // 뒤에 push될 피연산자가 - 화 되야 한다는 표시를 남기고 다음으로 넘어간다
                                continue;
                            }
                        }
                    }

                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(currentChar)) {

                        double b = numbers.pop();
                        double a = numbers.pop();
                        char operatorChar = operators.pop();
                        OperatorType operatorType = Operator.getType(operatorChar);
                        operator = new Operator(operatorType, a, b);
                        double result = operator.run();
                        validationManager.checkRangeInPerform(a, b, result);
                        numbers.push(result * isOperandShouldMinus);
                        // previousValue = result * isOperandShouldMinus;

                        isOperandShouldMinus = 1;

                        // 직전값 업데이트
                    }
                    operators.push(currentChar);
                } else if (currentChar == '_') {
                    // 직전값이 배정되지 않았을 경우
                    if (Double.isNaN(previousValue)) {
                        throw new ErrorHandler(ErrorType.INVALID_PREVIOUS_VALUE_ERROR);
                    }
                    // '_'를 만날 때 직전값을 스택에 push
                    numbers.push(previousValue * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                }
            }

            // 남은 연산자를 모두 처리
            while (!operators.isEmpty() && numbers.size() >= 2) {
                double b = numbers.pop();
                double a = numbers.pop();

                char operatorChar = operators.pop();
                OperatorType operatorType = Operator.getType(operatorChar);
                operator = new Operator(operatorType, a, b);
                double result = operator.run();
                validationManager.checkRangeInPerform(a, b, result);
                numbers.push(result * isOperandShouldMinus);
                // 직전값 업데이트
                // previousValue = result * isOperandShouldMinus;
                isOperandShouldMinus = 1;
            }

            finalResult = numbers.pop();

            if (!numbers.isEmpty() || !operators.isEmpty())
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);

        } catch (ErrorHandler e) {
            throw e;
        }

        // 최종 결과 반환
        DecimalFormat df = new DecimalFormat("#.######");
        // check_Range_Result(finalResult);

        if (finalResult == (int) finalResult) {
            finalResult = (int) finalResult;
        } else {
            finalResult = Double.parseDouble(df.format(finalResult));
        }
        // previousValue = finalResult;
        return finalResult;
    }

    // 연산자 우선순위 반환
    private int precedence(char operator) throws ErrorHandler {
        return Operator.getType(operator).getPriority();
    }

    // 함수 닫힘 대괄호 Index 계산
    private int closeBracketIndex(String subExpression) {
        int endIndex = subExpression.indexOf("[");
        int bracketFlag = 1;
        while (bracketFlag > 0) {
            endIndex += 1;
            if (subExpression.charAt(endIndex) == '[')
                bracketFlag++;
            else if (subExpression.charAt(endIndex) == ']')
                bracketFlag--;
        }
        return endIndex + 1;
    }

    // 인자 값 계산하기
    private ArrayList<Double> calculateArguments(String argumentsString) throws ErrorHandler {
        ArrayList<Double> argumentList = new ArrayList<Double>();
        int index = 0;
        int bracketFlag = 0;
        for (int i = index; i < argumentsString.length(); i++) {
            if (argumentsString.charAt(i) == '[') {
                bracketFlag++;
            }
            if (argumentsString.charAt(i) == ']') {
                bracketFlag--;
            }
            if ((argumentsString.charAt(i) == ',')) {
                if (bracketFlag == 0) {
                    String argumentString = argumentsString.substring(index, i);
                    double argumentValue = recursiveCaluculate(argumentString);
                    argumentList.add(argumentValue);
                    index = i + 1;
                }
            }
            if (i == argumentsString.length() - 1) {
                String argumentString = argumentsString.substring(index);
                double argumentValue = recursiveCaluculate(argumentString);
                argumentList.add(argumentValue);
            }
        }
        return argumentList;
    }
    
    // Object[수식의 타입, 표현식, 변수 혹은 함수 이름, 매개변수]
    private Object[] preProcessing(String rawExpression) throws ErrorHandler {         
        rawExpression = rawExpression.trim();

        if (rawExpression.length() > 200)
            throw new ErrorHandler(ErrorType.LENGTH_ERROR);
            
        // 입력된 문자열의 타입
        ExpressionType expressionType = ExpressionType.MATHEMATICAL;
        Tuple resultPair = validationManager.checkExpressionType(rawExpression);
        String variableName = resultPair.first();
        String expression = resultPair.second();
        ArrayList<String> paras = resultPair.third();

        // 대입 식이 아니면서
        if (variableName != "") { 
            if (paras == null) { // 이 경우 변수 대입
                expressionType = ExpressionType.VARIABLE;
            } else if (paras != null) { // 이 경우 함수를 대입
                expressionType = ExpressionType.FUNCTION;
            }
        }

        if (!validationManager.checkBracketPair(expression)) {
            throw new ErrorHandler(ErrorType.BRACKET_ERROR);
        }
        expression = optimizeForPower(expression);

        return new Object[] { expressionType, expression, variableName, paras };
    }

    // 제곱 연산에 소괄호
    private String optimizeForPower(String expression) {
        StringBuilder sb = new StringBuilder(expression);

        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) == '^') {
                int end = i + 1;

                // ^ 뒤의 공백 무시
                while (end < sb.length() && sb.charAt(end) == ' ') {
                    end++;
                }

                // 음수인 경우 확인
                // boolean isNegative = false;
                if (end < sb.length() - 1 && sb.charAt(end) == '-') {
                    // isNegative = true;
                    end++;
                }

                // 오른쪽 피연산자를 찾아서 ')'를 추가합니다.
                if (sb.charAt(end) == '(') {
                    int openParensCount = 1;
                    while (end < sb.length() && openParensCount != 0) {
                        end++;
                        if (sb.charAt(end) == '(')
                            openParensCount++;
                        if (sb.charAt(end) == ')')
                            openParensCount--;
                    }
                    end++; // 닫는 괄호 다음 위치로 이동
                } else {
                    while (end < sb.length() && (Character.isDigit(sb.charAt(end)) || sb.charAt(end) == '.' || sb.charAt(end) == ' ' 
                    || Character.isAlphabetic(sb.charAt(end)) || sb.charAt(end) == '$' || sb.charAt(end) == '_' || sb.charAt(end) == '%'
                    || sb.charAt(end) == '@' || sb.charAt(end) == '[' || sb.charAt(end) == ']' || sb.charAt(end) == ','
                    ) )  {
                        end++;
                    }
                }
                sb.insert(end, ")");

                // ^ 앞의 공백 무시
                int start = i - 1;
                while (start >= 0 && sb.charAt(start) == ' ') {
                    start--;
                }

                // 왼쪽 피연산자를 찾아서 '('를 추가합니다.
                if (sb.charAt(start) == ')') {
                    int closeParensCount = 1;
                    while (start >= 0 && closeParensCount != 0) {
                        start--;
                        if (sb.charAt(start) == '(')
                            closeParensCount--;
                        if (sb.charAt(start) == ')')
                            closeParensCount++;
                    }
                } else {
                    while (start >= 0 && (Character.isDigit(sb.charAt(start)) || sb.charAt(start) == '.' || sb.charAt(start) == ' '  
                     || Character.isAlphabetic(sb.charAt(start)) || sb.charAt(start) == '$' || sb.charAt(start) == '_' || sb.charAt(start) == '%'
                     || sb.charAt(start) == '@' || sb.charAt(start) == '[' || sb.charAt(start) == ']' || sb.charAt(start) == ','
                     )) {
                        start--;
                    }
                }
                if (start >= 0 && sb.charAt(start) == '-') {
                    start--;
                }
                sb.insert(start + 1, "(");
                i = start + 1; // '(' 위치 다음으로 이동
            }
        }

        return sb.toString();
    }

}