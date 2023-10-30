import java.util.Scanner;
import java.text.DecimalFormat;
public class CalculatorApp {
    public static void main(String[] args) {
        StaticVariable staticVariable = new StaticVariable();
        System.out.println(staticVariable.scriptString);

        menu: while (true) {
            // 1. 입력 받기
            Scanner scanner = new Scanner(System.in);
            System.out.print(staticVariable.menuString);
            System.out.print("> ");
            String expression = scanner.nextLine();

            switch (expression) {
                case "1":
                    System.out.println(staticVariable.scriptString);
                    break;
                case "2":
                    generateCalculator();
                    break;
                case "3":
                    System.out.println("\n계산기를 종료합니다.");
                    break menu;
                default:
                    ErrorHandler error = new ErrorHandler(ErrorType.Command_error);
                    error.PrintError();
            }
        }
    }

    private static void generateCalculator() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("계산할 수식을 입력하세요: ");
        Calculator c = new Calculator();
        String expression = scanner.nextLine();
        System.out.println();

        while(!expression.equalsIgnoreCase("q")) {
            try {
                double result = c.calculate(expression);
                System.out.println("결과 : " + result);
                System.out.print("계산할 수식을 입력하세요: ");
                expression = scanner.nextLine();
                System.out.println();
            } catch (ErrorHandler e) {
                e.PrintError();
                System.out.print("계산할 수식을 입력하세요: ");
                expression = scanner.nextLine();
                System.out.println();
            }
        }
    }
}