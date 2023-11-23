public class ErrorHandler extends Exception {

    ErrorType errorType;
    public ErrorHandler(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    public void PrintError() {
      switch (errorType) {
        case Command_error:
          System.out.println("정해진 1 , 2 , 3 명령어만 입력해주세요\n");
          break;
        case Length_error:
            System.out.println("preProcessing_길이제한 오류입니다\n");
            break;
        case Bracket_error:
            System.out.println("preProcessing_괄호쌍 오류입니다\n");
            break;
        case OperatorEnd_error:  //사용되고 있지 않은 오류 형태
        case DivideZero_error:
            System.out.println("performOperation_나누기 0 (수식) 오류입니다\n");
            break;
        case InValidOperator_error:
            System.out.println("performOperation_존재하지 않는 연산자 (수식) 오류입니다\n");
            break;
        case InValidOperand_error:
            System.out.println("RecursiveCaluculate_피연산자 미배정 (수식) 오류입니다\n");
            break;
        case InValidExperssion_error:
            System.out.println("RecursiveCaluculate_피연산자 공백 피연산자 (수식) 오류입니다\n");
            break;
        case ResultValueOutofBound_error:
            System.out.println("checkRangeResult_결과값 범위 초과 오류입니다\n");
            break;
        case TempValueOutofBound_error:
            System.out.println("checkRangeResult_피연산자값 범위 초과 오류입니다\n");
            break;
        }
    }
}