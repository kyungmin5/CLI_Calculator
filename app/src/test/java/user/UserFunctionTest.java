package user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class UserFunctionTest {
  @Test
  public void functionValidTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertDoesNotThrow(() -> {
      calculator.calculate("@13v[] = 5");
      calculator.calculate("  @ab[%c,%d] = %c + %d");
      calculator.calculate("@ff[%h2,%h23] = %h23 * 5");
    });
  }

  // 함수 정의 시, 정의부 이름 오류
  @Test
  public void functionInvalidNameDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    ArrayList<ErrorHandler> errorList = new ArrayList<ErrorHandler>();
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@[%b] = %b ^ 2 ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ab2uㅁ3[%a,%b] = %a + %b ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ ab23[%a,%b] = %a + %b ");
    })); 
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@a b23[%a,  %b] = %a + %b ");
    })); 
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ds 2[%x,%y] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ds 2[%x,%] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ds 2[%x,%] =");
    }));
    for (ErrorHandler e : errorList) {
      assertEquals("FUNCTION_DEFINE_ERROR", e.getErrorType().name());
    }
  }

  // 함수 정의 시, 정의부 매개변수 오류
  @Test
  public void functionInvalidParameterDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    ArrayList<ErrorHandler> errorList = new ArrayList<ErrorHandler>();
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[,] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[a] = %a");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[%x,%] = ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@vsad[%x, %y,] = %x + %y");
    }));
    for (ErrorHandler e : errorList) {
      assertEquals("FUNCTION_DEFINE_ERROR", e.getErrorType().name());
    }
  }

  // 함수 정의 시, 표현식 오류, 매개변수 정상
  @Test
  public void functionInvalidExpressionDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@x[%a, %b] = %a + %b + _");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@x[%a, %b] = %a + ");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@f2[%x, %y] = %x - %y * $temp");
    });
  }

  // 함수 정의 시, 매개변수 오류, 표현식 오류
  @Test
  public void functionInvalidParameterUseTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@a123[] = %x + 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@a[%x] = %x + %y");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@vsad[%x, %y] = %x + %y - %z");
    });
  }

  // 함수 사용 시, 인자 오류
  @Test
  public void functionInvalidArgumentsTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    calculator.calculate("@f[%x, %y] = %x + %y * 3");
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@f[_, 4]");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("7 + @f[3, 2, 5]");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("10 - @f[6] + 100");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@f[$temp, 4]");
    });
  }
}
