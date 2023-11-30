package operator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cli_calculator.*;
import error.*;

public class OperatorTest {
  private Calculator calculator = new Calculator();

  // 연산자가 하나일 때
  @Test
  public void singleOperatorTest() throws ErrorHandler {
    String expression = "3 + 3";
    double result = calculator.calculate(expression);  
    assertEquals(6, result, 0);
  }

  // 연산자가 두개일 때
  @Test
  public void doubleOperatorTest() throws ErrorHandler {
    String expression = "3 ^ 3 * 2";
    double result = calculator.calculate(expression);  
    assertEquals(54, result, 0);
  }

  // 연산자 오류 테스트
  @Test
  public void operatorErrorTest(){
    assertThrows(ErrorHandler.class, () -> {
      String expression = "3 )3";
      calculator.calculate(expression);
    });
  }
}
