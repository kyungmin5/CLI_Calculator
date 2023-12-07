package error;

public class ErrorHandler extends Exception {

    ErrorType errorType;

    public ErrorHandler(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public void printError() {
      switch (errorType) {
        case COMMAND_ERROR:
          System.out.println("정해진 1 , 2 , 3 명령어만 입력해주세요.\n");
          break;
        case EMPTY_ERROR:
          System.out.println("수식 오류입니다.\n");
          break;
        case LENGTH_ERROR:
            System.out.println("길이제한 오류입니다.\n");
            break;
        case BRACKET_ERROR:
            System.out.println("괄호쌍 오류입니다.\n");
            break;
        case DIVIDE_ZERO_ERROR:
            System.out.println("나누기 0 (수식) 오류입니다.\n");
            break;
        case INVALID_OPERATOR_ERROR:
            System.out.println("존재하지 않는 연산자 (수식) 오류입니다.\n");
            break;
        case INVALID_OPERAND_ERROR:
            System.out.println("피연산자 미배정 (수식) 오류입니다.\n");
            break;
        case INVALID_EXPRESSION_ERROR:
            System.out.println("피연산자 공백 피연산자 (수식) 오류입니다.\n");
            break;
        case INVALID_EXPRESSION_TYPE_ERROR:
            System.out.println("분류할 수 없는 수식 타입입니다.\n");
            break;
        case INVALID_PREVIOUS_VALUE_ERROR:
            System.out.println("직전 결과 값이 비어있습니다.\n");
            break;
        case INVALID_CHARACTER_ERROR:
            System.out.println("올바르지 않은 피연산자 형식이 포함되었습니다.\n");
            break;
        case VALUE_OUT_OF_BOUND_ERROR:
            System.out.println("값 범위 초과 오류입니다.\n");
            break;
        // 함수, 변수 정의
        case FUNCTION_DEFINE_ERROR:
            System.out.println("함수 정의부 문법 오류입니다. @함수면[%매개변수1, ...] = 수식 \n");
            break;
        case FUNCTION_PARAMETER_DUPLICATE_DEFINE_ERROR:
            System.out.println("중복된 이름의 매개변수가 사용되었습니다.\n");
            break;
        case FUNCTION_PARAMETER_SIZE_UNMATCH_ERROR:
            System.out.println("입력된 매개변수의 개수가 올바르지 않습니다.\n");
            break;
        case FUNCTION_EXPRESSION_ERROR:
            System.out.println("정의된 함수의 수식이 올바르지 않습니다.\n");
            break;
        case FUNCTION_BRACKET_ERROR:
            System.out.println("함수의 사용 형식이 올바르지 않습니다.\n");
            break;
        case VARIABLE_DEFINE_ERROR:
            System.out.println("변수 정의부 문법 오류입니다. $변수명 = 수식\n");
            break;
        case VARIABLE_UNDEFINE_ERROR:
            System.out.println("정의되지 않는 변수가 사용되었습니다.\n");
            break;
        default:
            break;
        }
    }
}