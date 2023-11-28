import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
// import java.util.Scanner;

class Tuple {
        private String x;
        private String y;
        private ArrayList<String> z = null;

    
        Tuple(String x, String y) {
            this.x = x;
            this.y = y;
        }

        Tuple(String x, String y, ArrayList<String> z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    
        public String first(){
            return x;
        }
    
        public String second(){
            return y;
        }

        public ArrayList<String> third(){
            return z;
        }
    }

public class Calculator {
    private double previousValue = Double.NaN;
    private UserVariable variable = new UserVariable();
    private UserFunction function = new UserFunction();

    private ValidationManager validationManager = new ValidationManager();

    // 직접 값 업데이트
    public void setPreviousValue(double value) {
        previousValue = value;
    }

    public double calculate(String expression) throws ErrorHandler {

        Object[] RESULT = preProcessing(expression);
        //expression = expression.replaceAll(" ", ""); // 입력 문자열에서 공백 제거

        int isSubstitution =(int)RESULT[0]; // 대입식인지 그냥 수식인지를 판별
        expression = (String)RESULT[1]; // 수식이면 수식이 ^에 대해 () 처리만 하고 나오고, 대입이라면 = 오른쪽 부분만이 ^처리 후에 나오게 된다
        String variableName = (String)RESULT[2];
        ArrayList<String> paras = (ArrayList<String>)RESULT[3];

        // 결국, 수식이면 계산 한 값을 반환하면 되는 것이고, 대입이라면 계산 한 값을 x에 대입하면 되는 것이다

        if (isSubstitution == 1) {
            // 변수 대입식인 경우
            double result = RecursiveCaluculate(expression);
            variable.setVariable(variableName, result);
            return result;
        }else if(isSubstitution == 2)
        {
            // 함수 대입식인 경우
            function.setFunction(variable, previousValue, variableName, paras, expression);
            return Double.NaN;
        }else
        {
            // 대입식이 아닌 경우
            double result = RecursiveCaluculate(expression);
            return result;
        }

        // 직전 값에 대입하는 식
        

    }

    private double RecursiveCaluculate(String expression) throws ErrorHandler
    {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int stackCount = 0;

        double finalResult = 0;
        int isOperandShouldMinus = 1;
        boolean isDollarX = false;

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
                }else if(currentChar == '@')
                {
                    int index = 1;
                    String functionName = "";
                    for(; index + i< expression.length() && expression.charAt(index + i) != '['; index++)
                    {
                        functionName += expression.charAt(index + i);
                    }

                    index = index + 1;
                    String paraString = "";
                    for( ; index + i<expression.length(); index++)
                    {
                        if(expression.charAt(index + i) == ']')
                        {
                            break;
                        }
                        paraString += expression.charAt(index + i);
                    }

                    ArrayList<Double> para = new ArrayList<Double>();
                    String[] paras = paraString.split(",");
                    if(paras[0].length() != 0)
                    {
                        for (String string : paras) {
                            string = string.trim();
                            para.add(Double.valueOf(string));
                        }
                    }
                    
                    
                    String functionExpression = function.getFunction(functionName, para);
                    System.out.println("[functionExpression]" + "\n" + functionExpression );
                    Double result = RecursiveCaluculate(functionExpression);
                    System.out.println("[result]" + "\n" + result );

                    numbers.push( result * isOperandShouldMinus);
                    isOperandShouldMinus = 1;
                    i = index + i + 1;
                }else if (operator.isOperator(currentChar)) {
                    // 연산자 우선순위를 고려하여 스택에 push
                    if (currentChar == '-' && i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '(' || expression.charAt(i + 1) == '_' || expression.charAt(i + 1) == '$')  || expression.charAt(i + 1) == '@') {
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
                        char operatorChar = operators.pop();
                        OperatorType operatorType = Operator.getType(operatorChar);
                        operator = new Operator(operatorType, a, b);
                        double result = operator.run();
                        validationManager.checkRangeInPerform(a, b, result);
                        numbers.push(result * isOperandShouldMinus);
                        // previousValue = result  * isOperandShouldMinus;

                        isOperandShouldMinus = 1;

                        // 직전값 업데이트
                    }
                    operators.push(currentChar);
                }else if (currentChar == '_') {
                    // 직전값이 배정되지 않았을 경우
                    if (Double.isNaN(previousValue)) {
                        throw new ErrorHandler(ErrorType.INVALID_OPERAND_ERROR);
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

            if(!numbers.isEmpty()) throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);

        } catch (EmptyStackException e) {
            throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
        }

        // 최종 결과 반환
        DecimalFormat df = new DecimalFormat("#.######");
        // check_Range_Result(finalResult);

                if(finalResult == (int) finalResult){
                    finalResult = (int)finalResult;
                }else{
                    finalResult = Double.parseDouble(df.format(finalResult));
                }
        previousValue = finalResult;
        return finalResult;
    }

    private int precedence(char operator) throws ErrorHandler{
        return Operator.getType(operator).getPriority();
    }

    private Object[] preProcessing(String rawexpression) throws ErrorHandler {   // 반환은, ^를 위한 괄호 처리가 완료된 문자열과, 식이 대입식인지 아닌지 저장하는 boolean이다.
        if(rawexpression.length() > 200) throw new ErrorHandler(ErrorType.LENGTH_ERROR);

        rawexpression = rawexpression.trim();

        int isSubstitution = 0; // 0 : 대입식 아님, 1 : 변수 대입식임, 2 : 함수 대입식임
        Tuple resultPair = checksubstitution(rawexpression);
        String variableName = resultPair.first();
        String expression = resultPair.second();
        ArrayList<String> paras = resultPair.third();

        if(variableName == "") // 대입 식이 아님
        {
            isSubstitution = 0;
            expression = rawexpression;
        }else if(paras == null) // 이 경우 변수 대입
        {
            isSubstitution = 1;

        }else if(paras != null) // 이 경우 함수를 대입
        {
            isSubstitution = 2;

        }

        System.out.println("[isSubstitution]\n" + isSubstitution);

        validationManager.checkBracketPair(expression);

        expression = optimizeForPower(expression);

        return new Object[] {isSubstitution, expression, variableName, paras};
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
                    while (end < sb.length() && (Character.isDigit(sb.charAt(end)) || sb.charAt(end) == '.' || sb.charAt(end) == ' ' 
                    || Character.isAlphabetic(sb.charAt(end)) || sb.charAt(end) == '$' || sb.charAt(end) == '_' || sb.charAt(end) == '%') )  {
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
                    while (start >= 0 && (Character.isDigit(sb.charAt(start)) || sb.charAt(start) == '.' || sb.charAt(start) == ' '  
                     || Character.isAlphabetic(sb.charAt(start)) || sb.charAt(start) == '$' || sb.charAt(start) == '_' || sb.charAt(start) == '%')) {
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

    private Tuple checksubstitution(String expression) throws ErrorHandler
    {
        if(expression.length() < 1 || !expression.contains("=") ) return new Tuple("", "");

        if(expression.charAt(0) == '$')
        {
            String variableName = "";
            int i=1;
            for(; i<expression.length(); i++)
            {
                if(expression.charAt(i) == ' ')
                {
                    break;
                }
                variableName += expression.charAt(i);
            }

            if((i+4 > expression.length()) || (expression.charAt(i+1) != '=')  || (expression.charAt(i+2) != ' ')) throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
            return new Tuple(variableName, expression.substring(i+3));

        }else if(expression.charAt(0) == '@')
        {
            String functionName = "";
            ArrayList<String> para = new ArrayList<String>();
            int i=1;
            for(; i<expression.length(); i++)
            {
                if(expression.charAt(i) == '[')
                {
                    break;
                }
                functionName += expression.charAt(i);
            }

            if(i + 1 == expression.length())
            {
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
            }

            i++;
            String paraString = "";
            for(; i<expression.length(); i++)
            {
                if(expression.charAt(i) == ']')
                {
                    break;
                }
                paraString += expression.charAt(i);
            }

            if(i + 1 == expression.length())
            {
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
            }

            if((i+4 > expression.length()) || (expression.charAt(i+1) != ' ') || (expression.charAt(i+2) != '=')  || (expression.charAt(i+3) != ' ')) throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);

            // 여기까지 매개변수가 담겨있는 문자열 뺴내기
            String[] paras = paraString.split(",");
            if(paras[0].length() != 0)
            {
                for (String string : paras) {
                    string = string.trim();
                    if(string.length() < 2 || string.charAt(0) != '%')
                    {
                        throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
                    }
                    para.add(string.substring(1));
                }
            }
        
            return new Tuple(functionName, expression.substring(i+4), para);

        }else{
            throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
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
}