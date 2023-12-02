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
  INVALID_EXPRESSION_TYPE_ERROR, // 올바르지 않은 수식
  INVALID_PREVIOUS_VALUE_ERROR, // 직전값 null 오류
  // Result Error
  VALUE_OUT_OF_BOUND_ERROR,  // 유효한 결과 범위 초과

  // Variable & Function  Error
  FUNCTION_DEFINE_ERROR, // 함수 정의부(@ 문자부터 = 문자까지)의 형식 오류
  FUNCTION_PARAMETER_DUPLICATE_DEFINE_ERROR, // 함수 정의 시, 중복된 이름의 매개변수 사용
  FUNCTION_PARAMETER_SIZE_UNMATCH_ERROR, // 함수 정의 시, 중복된 이름의 매개변수 사용
  FUNCTION_EXPRESSION_ERROR, // 함수 정의 시, 수식 오류
  FUNCTION_BRACKET_ERROR, // 함수 사용 시, 매개변수 괄호(대괄호) 에러
  VARIABLE_DEFINE_ERROR, // 함수 정의부($ 문자부터 = 문자까지)의 형식 오류
  VARIABLE_UNDEFINE_ERROR, // 정의되지 않은 변수 사용
}
