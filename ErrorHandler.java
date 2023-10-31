public class ErrorHandler extends Exception {

    ErrorType errorType;
    public ErrorHandler(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    public void PrintError() {
      switch (errorType) {
        case Command_error:
          System.out.println("정해진 1 , 2 , 3 명령어만 입력해 주세요\n");
          break;
        case Length_error:
        case Bracket_error:
        case OperatorEnd_error:
        case DivideZero_error:
        case InValidOperator_error:
        case InValidOperand_error:
        case InValidExperssion_error:
          System.out.println("수식 오류입니다.");
          // System.out.println("잘못된 수식입니다.");
          break;
        case ResultValueOutofBound_error:
        case TempValueOutofBound_error_error:
          System.out.println("결과 오류입니다");
          // System.out.println("해당 프로그램에서 유효하지 않은 결과 값이 발생했습니다.");
          break;
        }
    }
}