package user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class UserVariableTest {
  // 3.1
  // 변수 값 저장 형식 및 값 테스트
  @Test
  public void variableValidDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    double result = calculator.calculate("$ab = 4 * 5 - 3");
    assertEquals(17, result);
    result = calculator.calculate("$ab - 12");
    assertEquals(5, result);
    calculator.calculate("$q1w2e3 = 4 * _ - $ab");
    result = calculator.calculate("$q1w2e3 + 3");
    assertEquals(6, result);
    calculator.calculate("@f[%x] = %x ^ 2");
    calculator.calculate("$q1w2e3 = 4 * @f[2]");
    result = calculator.calculate("$q1w2e3 / 8");
    assertEquals(2, result);
  }

  // 3.2
  // 변수 값 저장 형식 오류 테스트
  @Test
  public void variableInvalidDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    // 변수명
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$na이름me123 = 4 * 5 - 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$na me = 4 * 5 - 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$ = 4 * 5 - 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("name = 4 * 5 - 3");
    });
    // = 기준 공백
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$blank=4 * 5 - 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$blank =4 * 5 - 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$blank= 4 * 5 - 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$blank    = 4 * 5 - 3");
    });
  }

  // 3.3
  // 변수 값 저장 수식 오류 테스트
  @Test
  public void variableInvalidExpressionTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = $r + @f[3, 5] + 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = ( + 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = - 3");
    }); 
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x = ( + 3)");
    });
  }

  // 3.4
  // 정의되지 않은 변수 사용 테스트
  @Test
  public void variableInvalidUseTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("$x + 1");
    });
  }

  // 3.5
  // 변수 LifeCycle 테스트
  @Test
  public void variableLifeCycleTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      Calculator calculator = new Calculator();
      calculator.calculate("$x = 1106");
      calculator = new Calculator();
      calculator.calculate("$x * 1106");
    });
  }
}