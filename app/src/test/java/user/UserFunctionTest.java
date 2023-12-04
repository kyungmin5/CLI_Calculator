package user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class UserFunctionTest {
  // 4.1 정상
  @Test
  public void functionValidTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    calculator.calculate("$year = 2000 + 23");
    calculator.calculate("1000 + 4");
    calculator.calculate("   @r[%d] = %d + 1");
    calculator.calculate("@t[%q,%w] = %q + %w");
    calculator.calculate("  @d[%a,%s,%d,     %f] = %a + %a + %s + %f");
    double result = calculator.calculate(" @d[@t[@r[2],@r[2]],(5-1),$year,@d[1,3,    5   , 7]] - 10 + @t[   _,4]");
    assertEquals(1026, result, 0);
  }
  // @Test
  // public void functionValidTest() throws ErrorHandler {
  //   Calculator calculator = new Calculator();
  //   assertDoesNotThrow(() -> {
  //     calculator.calculate("@13v[] = 5");
  //     calculator.calculate("  @ab[%c,%d] = %c + %d");
  //     calculator.calculate("@ff[%h2,%h23] = %h23 * 5");
  //     // calculator.calculate("@13v[] = @13v[] + 5");
  //     double result = calculator.calculate("@13v[] ^ 2");
  //     assertEquals(25, result);
  //   });
  // }

  // 4.2
  // 함수 정의 시, 저장 형식 오류 중 이름 테스트
  @Test
  public void functionInvalidNameDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    ArrayList<ErrorHandler> errorList = new ArrayList<ErrorHandler>();
    // 이름
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@[%b] = %b ^ 2 ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@na이름me123[%a,%b] = %a + %b ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@ name[%a,%b] = %a + %b ");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@na me[%a,  %b] = %a + %b ");
    }));
    for (ErrorHandler e : errorList) {
      assertEquals("FUNCTION_DEFINE_ERROR", e.getErrorType().name());
    }
  }

  // 4.3
  // 함수 정의 시, 저장 형식 오류 중 매개변수 테스트
  @Test
  public void functionInvalidParameterDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    ArrayList<ErrorHandler> errorList = new ArrayList<ErrorHandler>();
    // 매개변수
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[x,%y] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[%x%y] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[x%y] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[%x,%y,] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[%x,%] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[,] = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[x] = %x + 3");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param[%x,%y = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param%x,%y = %x * %y");
    }));
    errorList.add(assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@param%x,%y] = %x * %y");
    }));
    for (ErrorHandler e : errorList) {
      assertEquals("FUNCTION_DEFINE_ERROR", e.getErrorType().name());
    }
  }

  // 4.4
  // 함수 정의 시, 저장 형식 오류 중 공백 테스트
  @Test
  public void functionInvalidBlankDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    // = 기준 공백
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@blank[%b, %k]=%b * 5 - %k");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@blank[%b, %k] =%b * 5 - %k");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@blank[%b, %k]= %b * 5 - %k");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@blank[%b, %k]    = %b * 5 - %k");
    });
  }

  // 4.5
  // 함수 정의 시, 수식 오류 테스트
  @Test
  public void functionInvalidExpressionDefineTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    // 수식 자체 오류
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@prev[%a, %b] = %a + %b + _");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@x[%a, %b] = %a + ");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@temp[%x, %y] = %x - %y * $temp");
    });
    // 수식 내 매개변수 오류
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@a123[] = %x + 3");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@a[%x] = %x + %y");
    });
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@three[%x, %y] = %x + %y - %z");
    });
  }

  // 4.6
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
    assertThrows(ErrorHandler.class, () -> {
      calculator.calculate("@f[3, 1 * 4");
    });
  }

  // 4.7
  // 변수 LifeCycle 테스트
  @Test
  public void functionLifeCycleTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      Calculator calculator = new Calculator();
      calculator.calculate("@a[%x,%y] = %x + %y");
      calculator = new Calculator();
      calculator.calculate("@a[1,2] + 1");
    });
  }
}
