package user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class UserVariableTest {
  // 변수 값 저장 형식 및 값 테스트
  @Test
  public void variableValidDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    double result = calculator.calculate("$x123 = 4 * 5 - 3");
    assertEquals(17, result);
    calculator.calculate("$xfd123 = 4 * 5 -3");
    result = calculator.calculate("$xfd123 + 3");
    assertEquals(20, result);
    calculator.calculate("$ab12 = 1 + 3 * 3");
    result = calculator.calculate("$ab12 * _");
    assertEquals(200, result);
  }

  // 변수 값 저장 형식 오류 테스트
  @Test
  public void variableInValidDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    ArrayList<ErrorHandler> errorList = new ArrayList<ErrorHandler>();
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$xㅗ123 = 4 * 5 -3");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x 123 = 4 * 5 -3");
    })); 
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$dfsaf= 4 * 5 - 3");
    })); 
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$ddsa =4 * 5 - 3");
    }));
    for (ErrorHandler e : errorList) {
      assertEquals("VARIABLE_DEFINE_ERROR", e.getErrorType().name());
    }
  }

  // 변수 값 저장 수식 오류 테스트
  @Test
  public void saveVariableInValidExpressionTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = $r + @f[3, 5] + 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = ( + 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$dfsaf = - 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$var = ( + 3)");
    });
  }
}