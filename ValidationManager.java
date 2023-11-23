
public class ValidationManager {
  ValidationManager() {

  }

  // 피연산자와 연산 결과 값의 유효 범위 체크
  public void checkRangeInPerform(double lhs, double rhs, double result) throws ErrorHandler {
    boolean isValidLhs = (lhs >= -Double.MAX_VALUE && lhs <= Double.MAX_VALUE);
    boolean isValidRhs = (rhs >= -Double.MAX_VALUE && rhs <= Double.MAX_VALUE);
    boolean isValidResult = (result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE);
    if (isValidLhs && isValidRhs && isValidResult) {
      return;
    }
    throw new ErrorHandler(ErrorType.TempValueOutofBound_error);
  }
}
