import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        menu: while (true) {
            // 1. 입력 받기
            PrintMenu();
            String expression = scanner.nextLine();
            switch (expression) {
                    case "1":
                        PrintCalcInfo();
                        break;
                    case "2":
                            System.out.print("계산할 수식을 입력하세요: ");
                    expression = scanner.nextLine();
                    System.out.println();

                    while(!expression.equalsIgnoreCase("q")){
                        try{
                            double result = Calculator.calculate(expression);
                            System.out.println("결과: " + result);

                            System.out.println();
                            System.out.print("계산할 수식을 입력하세요: ");
                            expression = scanner.nextLine();
                            System.out.println();
                        } catch (ErrorHandler e) {
                            e.PrintError();
                        }
                    }
                        break;
                    case "3":
                        System.out.println("\n계산기를 종료합니다.");
                        break menu;
                    default:
                        ErrorHandler error = new ErrorHandler(ErrorType.Command_error);
                        error.PrintError();
                }
            }
        scanner.close();
    }

    public static void PrintMenu()
    {
        System.out.println();
        System.out.println("메뉴를 입력하세요");
        System.out.println("1 : 설명서 띄우기 || 2 : 계산하기 || 3 : 종료하기");
        System.out.print("입력 : ");
    }

    public static void PrintCalcInfo()
    {
        System.out.println();
        System.out.println("======================================================================================");
        System.out.println("초간단 계산기의 사용 설명서입니다. ");
        System.out.println("1. 직전 계산의 결과값을 _ 기호를 사용하여 다음 계산에 사용하실 수 있습니다. ");
        System.out.println("2. 결과값에 소수가 나올 경우 소수점 6자리까지 계산합니다. ");
        System.out.println("3. 사용가능한 연산자는 +,-,/,*입니다 ");
        System.out.println("4. 수식의 길이는 (     )까지 받을 수 있습니다. ");
        System.out.println("5. 수학적 정의에 올바른 수식만 받을 수 있습니다.  ");
        System.out.println("Ex) 0으로 나누려한 경우,괄호의 짝이 맞지 않는 경우 등등 입력 받을 수 없습니다. ");
        System.out.println("6. 계산하기 창에서 3~5의 규칙을 어길 경우 오류 문구를 출력하고 재입력 받습니다. ");
        System.out.println("7. 계산하기를 종료하려면 q 문자를 입력 후 키보드 Enter키를 눌러주세요.");
        System.out.println("======================================================================================");
        System.out.println();
/*
PrintMenu(), PrintCalcInfo() 에서
System.out.println()을 한번만 호출하는건 어떨까요?
"""
메뉴를 입력하세요
1 : 설명서 띄우기 || 2 : 계산하기 || 3 : 종료하기
입력 : """

"""

======================================================================================
초간단 계산기의 사용 설명서입니다.
1.직전 계산의 결과값을 다음 계산에 사용하실 수 있습니다.
2.결과값에 소수가 나올 경우 소수점 6자리까지 계산합니다.
3. 사용가능한 연산자는 +,-,/,*입니다
4. 수식의 길이는 (     )까지 받을 수 있습니다.
5.수학적 정의에 올바른 수식만 받을 수 있습니다.
Ex) 0으로 나누려한 경우,괄호의 짝이 맞지 않는 경우 등등 입력 받을 수 없습니다.
계산하기 창에서 3~5의 규칙을 어길 경우 오류 문구를 출력하고 재입력 받습니다.
======================================================================================

"""
*/

    }
}