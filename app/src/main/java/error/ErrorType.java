package error;

public enum ErrorType {
  // Command Error
  COMMAND_ERROR,
  // Experssion Error
  LENGTH_ERROR, // 길이 초과
  BRACKET_ERROR, // 괄호쌍이 맞지 않음
  DIVIDE_ZERO_ERROR, // 0으로 나누기
  INVALID_OPERATOR_ERROR, // 올바르지 않은 연산자 오류
  INVALID_OPERAND_ERROR, // 올바르지 않은 피연산자 오류
  INVALID_EXPRESSION_ERROR, // 올바르지 않은 수식
  // Result Error
  VALUE_OUT_OF_BOUND_ERROR,  // 유효한 결과 범위 초과
  // Function
  FUNCTION_DEFINE_ERROR, // 함수 정의부(@ 문자부터 = 문자까지)의 형식 오류
}
