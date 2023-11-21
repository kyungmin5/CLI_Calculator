import java.text.DecimalFormat;
import java.util.EmptyStackException;
import java.util.Stack;
// import java.util.Scanner;

public class Calculator {

    class Pair {
        private String x;
        private String y;
    
        Pair(String x, String y) {
            this.x = x;
            this.y = y;
        }
    
        public String first(){
            return x;
        }
    
        public String second(){
            return y;
        }
    }

    
    private double previousValue = Double.NaN;
    private UserVariable variable = new UserVariable();

    public void setPreviousValue(double value) {
        previousValue = value;
    }
    public double calculate(String expression) throws ErrorHandler {

        Object[] RESULT = preProcessing(expression);
        //expression = expression.replaceAll(" ", ""); // 입력 문자열에서 공백 제거

        boolean isSubstitution =(boolean)RESULT[0]; // 대입식인지 그냥 수식인지를 판별
        expression = (String)RESULT[1]; // 수식이면 수식이 ^에 대해 () 처리만 하고 나오고, 대입이라면 = 오른쪽 부분만이 ^처리 후에 나오게 된다
        String variableName = (String)RESULT[2];
        // 결국, 수식이면 계산 한 값을 반환하면 되는 것이고, 대입이라면 계산 한 값을 x에 대입하면 되는 것이다

        double result = RecursiveCaluculate(expression);

        if (isSubstitution) {
            // 대입식인 경우
            variable.setVariable(variableName, result);
        }

        // 직전 값에 대입하는 식
        return result;

    }

    private double RecursiveCaluculate(String expression) throws ErrorHandler
    {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int stackCount = 0;

        double finalResult = 0;
        int isOperandShouldMinus = 1;
        boolean isDollarX = false;

        try {
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
                    numbers.push(num * isOperandShouldMinus);
                    // 직전값 업데이트
                    // previousValue = num * isOperandShouldMinus;
                    isOperandShouldMinus = 1;
                    i--;    // 숫자 추출 후 인덱스 복원
                }
                else if (currentChar == '(') {

                    stackCount++;
                    int  j= i+1;
                    for (; j < expression.length() && stackCount > 0; j++)
                    {
                        if(expression.charAt(j) == '(')
                        {
                            stackCount++;
                        }else if(expression.charAt(j) == ')')
                        {
                            stackCount--;
                        }
                    }
                    numbers.push(RecursiveCaluculate(expression.substring(i+1, j-1)) * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                    i = j-1;  // 여기가 실제 ) 가 있는 인덱스. i를 ) 의 index로 보내버린다.
                }else if(currentChar == '$')
                {
                    int index = 1;
                    String variableName = "";
                    for(; index + i< expression.length() && expression.charAt(index + i) != ' '; index++)
                    {
                        variableName += expression.charAt(index + i);
                    }
                    numbers.push(variable.getVariable(variableName) * isOperandShouldMinus);
                    isOperandShouldMinus = 1;

                    i = index + i;
                }else if (isOperator(currentChar)) {
                    // 연산자 우선순위를 고려하여 스택에 push
                    if (currentChar == '-' && i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '(' || expression.charAt(i + 1) == '_' || expression.charAt(i + 1) == '$')) {
                        // 이게 딱 음수 부호 피연산자의 형태. 이게 아니면 모두 연산자 처리한다
                        // 다만 이것이 음수 부호 형태가 맞지만, 연산자가 아니라는 것은 아니므로 추가 처리가 필요하다
                        boolean isOperator = false;

                        for (int index = i - 1; index >= 0; index--) {
                            if (expression.charAt(index) != ' ') {
                                if (!isOperator(expression.charAt(index))) {
                                    isOperator = true;
                                }

                                break;
                            }
                        }


                        if(!isOperator) // - 바로 앞에 또 연산자가 있어서 -가 부호로 사용될 수밖에 없을 떄
                        {
                            isOperandShouldMinus = -1; // 뒤에 push될 피연산자가 - 화 되야 한다는 표시를 남기고 다음으로 넘어간다
                            continue;
                        }
                    }


                    // currnetChar이 + - 이면 반드시 실행
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(currentChar)) {

                        double b = numbers.pop();
                        double a = numbers.pop();
                        check_Range_In_Perform(a);
                        check_Range_In_Perform(b);
                        char operator = operators.pop();
                        double result = performOperation(a, b, operator);
                        check_Range_In_Perform(result);
                        numbers.push(result * isOperandShouldMinus);
                        // previousValue = result  * isOperandShouldMinus;

                        isOperandShouldMinus = 1;

                        // 직전값 업데이트
                    }
                    operators.push(currentChar);
                }else if (currentChar == '_') {
                    // 직전값이 배정되지 않았을 경우
                    if (Double.isNaN(previousValue)) {
                        throw new ErrorHandler(ErrorType.InValidOperand_error);
                    }
                    // '_'를 만날 때 직전값을 스택에 push
                    numbers.push(previousValue * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                }
            }

            // 남은 연산자를 모두 처리
            while (!operators.isEmpty()) {
                double b = numbers.pop();
                double a = numbers.pop();

                check_Range_In_Perform(a);
                check_Range_In_Perform(b);
                char operator = operators.pop();
                double result = performOperation(a, b, operator);
                check_Range_In_Perform(result);
                numbers.push(result * isOperandShouldMinus);
                // 직전값 업데이트
                // previousValue = result * isOperandShouldMinus;

                isOperandShouldMinus = 1;
            }

            finalResult = numbers.pop();

            if(!numbers.isEmpty()) throw new ErrorHandler(ErrorType.InValidExperssion_error);

        } catch (EmptyStackException e) {
            throw new ErrorHandler(ErrorType.InValidExperssion_error);
        }

        // 최종 결과 반환
        DecimalFormat df = new DecimalFormat("#.######");
        check_Range_Result(finalResult);

                if(finalResult == (int) finalResult){
                    finalResult = (int)finalResult;
                }else{
                    finalResult = Double.parseDouble(df.format(finalResult));
                }
        previousValue = finalResult;
        return finalResult;
    }

    private boolean isOperator(char c) {
        try {
            OperatorType.fromString(Character.toString(c));
        } catch (ErrorHandler e) {
            return false;
        }
        return true;
    }


    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }else if (operator == '^') {
            return 3;
        }
        return 0; // 다른 문자의 경우
    }

    private double performOperation(double a, double b, char operator) throws ErrorHandler {
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

    private Object[] preProcessing(String rawexpression) throws ErrorHandler // 
    {   // 반환은, ^를 위한 괄호 처리가 완료된 문자열과, 식이 대입식인지 아닌지 저장하는 boolean이다.
        if(rawexpression.length() > 200) throw new ErrorHandler(ErrorType.Length_error);

        rawexpression = rawexpression.trim();

        boolean isSubstitution = true;
        Pair resultPair = checksubstitution(rawexpression);
        String variableName = resultPair.first();
        String expression = resultPair.second();

        if(variableName == "") // 대입 식이 아님
        {
            isSubstitution = false;
            expression = rawexpression;
        }

        if(!checkBracket(expression)) throw new ErrorHandler(ErrorType.Bracket_error);

        //if(!check_Operand_Operator_Char(expression)) throw new ErrorHandler(ErrorType.InValidExperssion_error);

        expression = optimizeForPower(expression);

        return new Object[] {isSubstitution, expression, variableName};
    }


    private String optimizeForPower(String expression)
    {
        StringBuilder sb = new StringBuilder(expression);

        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) == '^') {
                int end = i + 1;

                // ^ 뒤의 공백 무시
                while (end < sb.length() && sb.charAt(end) == ' ') {
                    end++;
                }

                // 음수인 경우 확인
                boolean isNegative = false;
                if (end < sb.length()-1 && sb.charAt(end) == '-') {
                    isNegative = true;
                    end++;
                }

                // 오른쪽 피연산자를 찾아서 ')'를 추가합니다.
                if (sb.charAt(end) == '(') {
                    int openParensCount = 1;
                    while (end < sb.length() && openParensCount != 0) {
                        end++;
                        if (sb.charAt(end) == '(') openParensCount++;
                        if (sb.charAt(end) == ')') openParensCount--;
                    }
                    end++;  // 닫는 괄호 다음 위치로 이동
                } else {
                    while (end < sb.length() && (Character.isDigit(sb.charAt(end)) || sb.charAt(end) == '.' || sb.charAt(end) == ' ' || Character.isAlphabetic(sb.charAt(end)) || sb.charAt(end) == '$' || sb.charAt(end) == '_'))  {
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
                        if (sb.charAt(start) == '(') closeParensCount--;
                        if (sb.charAt(start) == ')') closeParensCount++;
                    }
                } else {
                    while (start >= 0 && (Character.isDigit(sb.charAt(start)) || sb.charAt(start) == '.' || sb.charAt(start) == ' '  || Character.isAlphabetic(sb.charAt(start)) || sb.charAt(start) == '$' || sb.charAt(start) == '_')) {
                        start--;
                    }
                }
                if (start >= 0 && sb.charAt(start) == '-') {
                    start--;
                }
                sb.insert(start + 1, "(");
                i = start + 1;  // '(' 위치 다음으로 이동
            }
        }

        return sb.toString();
    }

    private Pair checksubstitution(String expression)
    {
        Pair failurePair = new Pair("", "");
        String variableName = "";

        if(expression.length() < 6 || expression.charAt(0) != '$') 
        {
            return failurePair;
        }

        int i=1;
        for(; i<expression.length(); i++)
        {
            if(expression.charAt(i) == ' ')
            {
                break;
            }
            variableName += expression.charAt(i);
        }

        if((i+4 > expression.length()) || (expression.charAt(i+1) != '=')  || (expression.charAt(i+2) != ' ')) return failurePair;

        return new Pair(variableName, expression.substring(i+3));
    }

    private boolean checkBracket(String expression)
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

    private boolean checkStrArrangemnet(String expression) // 피연산자와 연산자가 제대로 떨어져 있는지 확인하는 함수입니다
    {
        StringBuilder sb = new StringBuilder(expression);

        for(int i=0; i<expression.length(); i++)
        {
            if(sb.charAt(i)== '(' || sb.charAt(i)== ')' || ((sb.charAt(i)== '-' || sb.charAt(i)== '+' || sb.charAt(i)== '*' || sb.charAt(i)== '/' || sb.charAt(i)== '^') && i+1 < expression.length() && sb.charAt(i+1) != ' '))
            {
                sb.setCharAt(i, ' ');
            }
        }
        sb = new StringBuilder(sb.toString().trim());

        int isTopOperator = 0; // 0 -> empty, 1-> operator, 2->operand
        String[] words = sb.toString().split("\\s+");  

        // System.out.println();
        for (String string : words) {
            string = string.trim();
            // System.out.println(string);

            if(string.equals("+") || string.equals("-") ||string.equals("*") ||string.equals("/") ||string.equals("^")) // 연산자인 경우
            {
                if(isTopOperator == 0 || isTopOperator == 1) return false; // 연산자가 들어왔는데 또 연산자가 번달아 왔으므로 오류 
                isTopOperator = 1;
            }else //피연산자 이 경우
            {
                if(isTopOperator == 2) return false; // 피연산자가 들어왔는데 또 피연산자가 번달아 왔으므로 오류 

                isTopOperator = 2;
            }
        }

        return isTopOperator != 1;
          
    }


    private boolean check_Operand_Operator_Char(String expression)
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
            }else if(expression.charAt(i) == '$' && (i+1) < expression.length() && expression.charAt(i+1) == 'x')
            {
                continue;
            }else if(expression.charAt(i) == 'x' && i>0 && expression.charAt(i-1) == '$')
            {
                continue;
            }else if(expression.charAt(i) == '.' && i>0 && (i+1) < expression.length() &&
                    expression.charAt(i-1) >= '0' && expression.charAt(i-1) <= '9' && expression.charAt(i+1) >= '0' && expression.charAt(i+1) <= '9')

            {
                continue;
            }else
            {
                return false;
            }

        }

        return true;
    }

    private double check_Range_In_Perform(double num) throws ErrorHandler{
        if (num >= -Double.MAX_VALUE && num <= Double.MAX_VALUE) {
            return num;
        }else{
            throw new ErrorHandler(ErrorType.TempValueOutofBound_error);

        }
    }

    private double check_Range_Result(double result) throws ErrorHandler{
        if (result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE) {
            return result;
        }else{
            throw new ErrorHandler(ErrorType.ResultValueOutofBound_error);
        }
    }


}