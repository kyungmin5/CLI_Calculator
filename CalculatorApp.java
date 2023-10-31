import java.util.Scanner;

public class CalculatorApp {
    public static void main(String[] args) {
        System.out.println(StaticVariable.scriptString);

        while (true) {
            try {
                if (printMenu()) {
                   break;
                }
            } catch (ErrorHandler e) {
                e.PrintError();
            }
        }
    }

    // 메뉴 기능
    private static boolean printMenu() throws ErrorHandler {
            Scanner scanner = new Scanner(System.in);
            System.out.print(StaticVariable.menuString + "> ");
            String expression = scanner.nextLine();

            switch (expression) {
                case "1":
                    System.out.println(StaticVariable.scriptString);
                    break;
                case "2":
                    generateCalculator();
                    break;
                case "3":
                    System.out.println(StaticVariable.exitString);
                    scanner.close();
                    return true;
                default:
                    scanner.close();
                    throw new ErrorHandler(ErrorType.Command_error);
            }
            scanner.close();
            return false;
    }

    private static void generateCalculator() {
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            System.out.print(StaticVariable.calculatorString);
            String expression = scanner.nextLine();
            System.out.println();
            if (expression.equalsIgnoreCase("q")) {
                break;
            }
            try {
                double result = Calculator.calculate(expression);
                System.out.print("결과 : " + result + "\n");
            } catch (ErrorHandler e) {
                e.PrintError();
            }   
        }
        scanner.close();
    }
}