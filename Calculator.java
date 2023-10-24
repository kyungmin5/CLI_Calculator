import java.util.Stack;
// import java.util.Scanner;

public class Calculator {
    public static double calculate(String expression) throws ErrorHandler {

        Object[] RESULT = preProcessing(expression);

        boolean isSubstitution =(boolean)RESULT[1]; // 대입식인지 그냥 수식인지를 판별
        expression = (String)RESULT[0]; // 수식이면 수식이 ^에 대해 () 처리만 하고 나오고, 대입이라면 = 오른쪽 부분만이 ^처리 후에 나오게 된다
        // 결국, 수식이면 계산 한 값을 반환하면 되는 것이고, 대입이라면 계산 한 값을 x에 대입하면 되는 것이다다


        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            if (currentChar == ' ') {
                continue; // 공백 문자는 무시
            }

            if (Character.isDigit(currentChar) || currentChar == '.') {
                // 숫자를 추출하여 스택에 저장
                StringBuilder numBuilder = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                double num = Double.parseDouble(numBuilder.toString());
                numbers.push(num);
                i--;    //숫자 추출 후 인덱스 복원
            } else if (currentChar == '(') {
                // 여는 괄호는 항상 스택에 push
                operators.push(currentChar);
            } else if (currentChar == ')') {
                // 닫는 괄호를 만날 때까지 연산자를 pop하고 계산
                while (!operators.isEmpty() && operators.peek() != '(') {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char operator = operators.pop();
                    double result = performOperation(a, b, operator);
                    numbers.push(result);
                }
                operators.pop(); // 여는 괄호 제거
            } else if (isOperator(currentChar)) {
                // 연산자 우선순위를 고려하여 스택에 push
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(currentChar)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char operator = operators.pop();
                    double result = performOperation(a, b, operator);
                    numbers.push(result);
                }
                operators.push(currentChar);
            }
        }

        // 남은 연산자를 모두 처리
        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char operator = operators.pop();
            double result = performOperation(a, b, operator);
            numbers.push(result);
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
        }else if (operator == '^') {
            return 3;
        }
        return 0; // 다른 문자의 경우
    }

    private static double performOperation(double a, double b, char operator) throws ErrorHandler {
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
            case '^':
                return Math.pow(a, b);
            default:
                throw new ErrorHandler(ErrorType.InValidOperator_error);
        }
    }

    private static Object[] preProcessing(String expression) throws ErrorHandler
    {   // 반환은, ^를 위한 괄호 처리가 완료된 문자열과, 식이 대입식인지 아닌지 저장하는 boolean이다.
        if(expression.length() > 200) throw new ErrorHandler(ErrorType.Length_error);

        expression = expression.trim();

        boolean isSubstitution = false;

        if(checksubstitution(expression))
        {
            isSubstitution = true;
            int index = 0;
            while(expression.charAt(index) != '=')
            {
                index++;
            }

            expression = expression.substring(index+1);
        }   

        if(!checkBracket(expression)) throw new ErrorHandler(ErrorType.Bracket_error);
        if(!check_Operand_Operator_Char(expression)) throw new ErrorHandler(ErrorType.InValidExperssion_error);
        // 본래, operand operator  분리해서 오류를 보내고 싶었지만 $같은 문자가 수식에 있으면 
        // operand로 할 지 operator 로 할지 정할 수 없어거 그냥 수식 오류로 보냈습니다
        if(!checkValidExpression(expression)) throw new ErrorHandler(ErrorType.InValidExperssion_error);


        return new Object[] {expression, isSubstitution};
    }

    private static boolean checksubstitution(String expression)
    {
        String checkStr = "&x=";
        expression = expression.replace(" ", "");

        if(expression.length() >= 3 && expression.substring(0, 3) == checkStr) return true;
        
        return false;
    }

    private static boolean checkBracket(String expression)
    {   
        Stack<Character> STACK = new Stack<>();

        for(int i=0; i< expression.length(); i++)
        {
            if(expression.charAt(i) == '(')
            {
                STACK.push( '(');
            }else if(expression.charAt(i) == ')')
            {
                if(STACK.empty()) return false;

                STACK.pop();
            }
        }

        if(STACK.empty())
        {
            return true;
        }else
        {
            return false;
        }
    }

    private static boolean check_Operand_Operator_Char(String expression)
    {   
        for(int i=0; i< expression.length(); i++)
        {
            if(expression.charAt(i) == '+' || expression.charAt(i) == '-' 
                ||expression.charAt(i) == '*' ||expression.charAt(i) == '/' 
                ||expression.charAt(i) == '^' ||expression.charAt(i) == '(' ||expression.charAt(i) == ')' )
            {
                continue;
            }else if(expression.charAt(i) >= '0' && expression.charAt(i) <= '9')
            {
                continue;
            }else if(expression.charAt(i) == '_' || expression.charAt(i) == ' ')
            {
                continue;
            }else if(expression.charAt(i) == '&' && (i+1) < expression.length() && expression.charAt(i+1) == 'x')
            {
                continue;
            }else if(expression.charAt(i) == 'x' && i>0 && expression.charAt(i-1) == '&')
            {
                continue;
            }else
            {
                return false;
            }
            
        }

        return true;
    }


    private static boolean checkValidExpression(String expression)
    {   // 여기까지왔다는 것은, 모든 문자는 수식 내에 존재 가능하고 괄호 쌍까지 알 맞다는 것이다
        // 이제 연산자 / 피연산자의 선후 순서가 맞는지를 봐야 한다
        if(expression.length() == 0) return false;
        return true;
    }

}