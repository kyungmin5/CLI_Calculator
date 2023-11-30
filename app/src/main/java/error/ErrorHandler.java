package error;

public class ErrorHandler extends Exception {

    ErrorType errorType;
    public ErrorHandler(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    public void PrintError() {
      switch (errorType) {
        case COMMAND_ERROR:
          System.out.println("정해진 1 , 2 , 3 명령어만 입력해주세요\n");
          break;
        case LENGTH_ERROR:
            System.out.println("preProcessing_길이제한 오류입니다\n");
            break;
        case BRACKET_ERROR:
            System.out.println("preProcessing_괄호쌍 오류입니다\n");
            break;
        case DIVIDE_ZERO_ERROR:
            System.out.println("performOperation_나누기 0 (수식) 오류입니다\n");
            break;
        case INVALID_OPERATOR_ERROR:
            System.out.println("performOperation_존재하지 않는 연산자 (수식) 오류입니다\n");
            break;
        case INVALID_OPERAND_ERROR:
            System.out.println("RecursiveCaluculate_피연산자 미배정 (수식) 오류입니다\n");
            break;
        case INVALID_EXPRESSION_ERROR:
            System.out.println("RecursiveCaluculate_피연산자 공백 피연산자 (수식) 오류입니다\n");
            break;
        case VALUE_OUT_OF_BOUND_ERROR:
            System.out.println("값 범위 초과 오류입니다\n");
            break;
        case VARIABLE_DEFINE_ERROR:
            System.out.println("변수 정의부 문법 오류입니다. $변수명 = 수식\n");
            break;
        case FUNCTION_DEFINE_ERROR:
            System.out.println("함수 정의부 문법 오류입니다. @함수면[%매개변수1, ...] = 수식 \n");
            break;
        default:
            break;
        }
    }
}