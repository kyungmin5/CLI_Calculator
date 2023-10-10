import java.util.Stack;

public class Calculator {
    // 전역 변수로 직전값을 저장할 변수를 선언하고 초기값을 0으로 설정
    private static int previousValue = 0;

    public static int calculate(String expression) throws ErrorHandler {
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            if (currentChar == ' ') {
                continue; // 공백 문자는 무시
            }

            if (Character.isDigit(currentChar)) {
                // 숫자를 추출하여 스택에 저장
                StringBuilder numBuilder = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                i--; // 숫자 추출 후 인덱스 조정
                int num = Integer.parseInt(numBuilder.toString());
                numbers.push(num);
                // 직전값 업데이트
                previousValue = num;
            } else if (currentChar == '(') {
                // 여는 괄호는 항상 스택에 push
                operators.push(currentChar);
            } else if (currentChar == ')') {
                // 닫는 괄호를 만날 때까지 연산자를 pop하고 계산
                while (!operators.isEmpty() && operators.peek() != '(') {
                    int b = numbers.pop();
                    int a = numbers.pop();
                    char operator = operators.pop();
                    int result = performOperation(a, b, operator);
                    numbers.push(result);
                    // 직전값 업데이트
                    previousValue = result;
                }
                operators.pop(); // 여는 괄호 제거
            } else if (isOperator(currentChar)) {
                // 연산자 우선순위를 고려하여 스택에 push
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(currentChar)) {
                    int b = numbers.pop();
                    int a = numbers.pop();
                    char operator = operators.pop();
                    int result = performOperation(a, b, operator);
                    numbers.push(result);
                    // 직전값 업데이트
                    previousValue = result;
                }
                operators.push(currentChar);
            } else if (currentChar == '_') {
                // '_'를 만날 때 직전값을 스택에 push
                numbers.push(previousValue);
            }
        }

        // 남은 연산자를 모두 처리
        while (!operators.isEmpty()) {
            int b = numbers.pop();
            int a = numbers.pop();
            char operator = operators.pop();
            int result = performOperation(a, b, operator);
            numbers.push(result);
            // 직전값 업데이트
            previousValue = result;
        }

        // 최종 결과 반환
        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        try {
            OperatorType.fromString(Character.toString(c));
        } catch (ErrorHandler e) {
            e.PrintError();
            return false;
        }
        return true;
        // return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0; // 다른 문자의 경우
    }

    private static int performOperation(int a, int b, char operator) throws ErrorHandler {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ErrorHandler(ErrorType.DivideZero_error);
                }
                return a / b;
            default:
                throw new ErrorHandler(ErrorType.ResultValueOutofBound_error);
        }
    }
}
