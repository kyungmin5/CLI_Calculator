import java.util.Scanner;
import java.text.NumberFormat;

public class CalculatorApp {
    private static Scanner scanner = new Scanner(System.in);
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
                    return true;
                default:
                    throw new ErrorHandler(ErrorType.COMMAND_ERROR);
            }
            return false;
    }

    // 계산 기능
    private static void generateCalculator() {
        Calculator calculator = new Calculator();

        while(true) {
            System.out.print(StaticVariable.calculatorString);
            String expression = scanner.nextLine();

            if (expression.equals("q")) {
                break;
            }

            try {
                double result = calculator.calculate(expression);
                if (result == (int) result) {
                    System.out.println((int) result);
                } else {
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(6);
                    numberFormat.setGroupingUsed(false);
                    System.out.println(numberFormat.format(result));
                }
            } catch (ErrorHandler e) {
                e.PrintError();
            }   
        }
    }
}