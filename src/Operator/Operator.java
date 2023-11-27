public class Operator {
  public OperatorType type;
  public double lhs;
  public double rhs;

  Operator() {

  }

  Operator(OperatorType type, double lhs, double rhs) {
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  // 실제 계산 진행
  public double run() throws ErrorHandler {
    switch (this.type) {
      case PLUS:
          return lhs + rhs;
      case MINUS:
          return lhs - rhs;
      case MULTIPLATION:
          return lhs * rhs;
      case DIVISION:
          if (rhs == 0) {
            throw new ErrorHandler(ErrorType.DIVIDE_ZERO_ERROR);
          }
          return lhs / rhs;
      case POWER:
          return Math.pow(lhs, rhs);
      default:
          throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
    }
  }

  // Operator 문자로 OperatorType인지 판별
  public boolean isOperator(char c) {
    String operatorChar = Character.toString(c);
    return OperatorType.operators.contains(operatorChar);
  }

  // Operator 문자로 OperatorType 값 반환
  public static OperatorType getType(char c) throws ErrorHandler {
    String operatorChar = Character.toString(c);
    int operatorIndex = OperatorType.operators.indexOf(operatorChar);
    if (operatorIndex == -1) throw new ErrorHandler(ErrorType.INVALID_OPERATOR_ERROR);
    return OperatorType.values()[operatorIndex];
  }
}