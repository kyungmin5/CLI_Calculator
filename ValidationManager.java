import java.util.Stack;

public class ValidationManager {
  ValidationManager() {

  }

    public boolean checkBracketPair(String expression) throws ErrorHandler {
        Stack<Character> STACK = new Stack<>();

        for(int i=0; i< expression.length(); i++) {
            if (expression.charAt(i) == '(')
            {
                STACK.push( '(');
            } else if (expression.charAt(i) == ')') {
                if(STACK.empty()) return false;
                STACK.pop();
            }
        }

        if(STACK.empty()) return true;
        
        throw new ErrorHandler(ErrorType.BRACKET_ERROR);
    }

  // 수식의 연산자, 피연산자 문법확인
  public boolean checkValidExpression(String expression) {
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
          } else if (expression.charAt(i) == '.' && i>0 && (i+1) < expression.length() &&
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
}
