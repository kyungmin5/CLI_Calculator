enum ErrorType {
  // Command Error
  Command_error,
  // Experssion Error
  Length_error, // 길이 초과
  Bracket_error, // 괄호쌍이 맞지 않음
  OperatorEnd_error, // 수식이 괄호가 아닌 연산자로 종료됨
  DivideZero_error, // 0으로 나누기
  InValidOperator_error, // 올바르지 않은 연산자 오류
  InValidOperand_error, // 올바르지 않은 피연산자 오류
  InValidExperssion_error, // 올바르지 않은 수식
  // Result Error
  ResultValueOutofBound_error, // 유효한 결과 범위 초과
  TempValueOutofBound_error_error, // 계산 과정에서 유효한 결과 범위 초과
}
