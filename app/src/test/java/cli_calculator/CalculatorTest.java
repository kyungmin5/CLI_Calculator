package cli_calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class CalculatorTest {
  // @Test
  // public void calculatorScenarioTest() throws ErrorHandler {
    // Calculator calculator = new Calculator();
    // calculator.calculate("@r[%d] = %d + 1");
    // calculator.calculate("@d[%a,%s,%d,     %f] = %a + %a + %s + %f");
    // Double result = calculator.calculate("@d[@r[2],(5-1),@r[1234],@d[1,3,    5   , 7]]");
    // // @d[3,4,1235,12] -> 22
    // assertEquals(22, result, 0);
  // }

  // ID 1.3
  @Test
  public void calculatorTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    double result = calculator.calculate("1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10");  
    assertEquals(55, result, 0);
    result = calculator.calculate("(1 * 2) + 6 / 3");  
    assertEquals(4, result, 0);
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("1 % 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("123456789 ^ 123456789 ^ 1234566789 ^ 123456789 ^ 123456789");
    });
    result = calculator.calculate("44.1234321234 - 30");
    assertEquals(14.123432, result, 0);   
    assertNotEquals(14.1234321234, result);
  }

    // ID 1.4
  @Test
  public void previousValueTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    // 직전값 미배정
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("_ + 4");  
    });
    // 정상 직전값
    calculator.calculate("7");  
    double result = calculator.calculate("_ * 4");  
    assertEquals(28, result, 0);
    // 생명주기
    assertThrows(ErrorHandler.class, () -> {
      Calculator throwalculator = new Calculator();
      throwalculator.calculate("2023");
      throwalculator = new Calculator();
      throwalculator.calculate("_ + 2023");
    });
  }
}
