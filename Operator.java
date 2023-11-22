import java.util.Arrays;

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
            throw new ErrorHandler(ErrorType.DivideZero_error);
          }
          return lhs / rhs;
      case POWER:
          return Math.pow(lhs, rhs);
      default:
          throw new ErrorHandler(ErrorType.InValidExperssion_error);
    }
  }

  // Operator 문자로 OperatorType인지 판별
  public boolean isOperator(char c) {
    String operatorChar = Character.toString(c);
    return OperatorType.operators.contains(Character.toString(c));
  }

  // Operator 문자로 OperatorType 값 반환
  public static OperatorType getType(char c) {
    String operatorChar = Character.toString(c);
    int operatorIndex = OperatorType.operators.indexOf(operatorChar);
    return OperatorType.values()[operatorIndex];
  }
}