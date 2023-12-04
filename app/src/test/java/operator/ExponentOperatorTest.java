package operator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class ExponentOperatorTest {
  private Calculator calculator = new Calculator();
  
  // ID 2.1
  @Test
  public void exponentOperatorTest() throws ErrorHandler {
    double result = calculator.calculate("1 ^ 2");
    assertEquals(1, result, 0);
    result = calculator.calculate("5 * 2 ^ 2 + 2");
    assertEquals(22, result, 0);
    result = calculator.calculate("2 ^ ( 3 ^ 2)");
    assertEquals(512, result, 0);
    result = calculator.calculate("2 ^ ( 3 ^ 2)");
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("");
    });
  }
}
