package cli_calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cli_calculator.Calculator;
import error.ErrorHandler;

public class CalculatorTest {
  @Test
  public void calculatorScenarioTest() throws ErrorHandler {
    Calculator calculator = new Calculator();
    calculator.calculate("$year = 2000 + 23");
    calculator.calculate("1000 + 4");
    calculator.calculate("   @r[%d] = %d + 1");
    calculator.calculate("@t[%q,%w] = %q + %w");
    calculator.calculate("  @d[%a,%s,%d,     %f] = %a + %a + %s + %f");
    Double result = calculator.calculate(" @d[@t[@r[2],@r[2]],(5-1),$year,@d[1,3,    5   , 7]] - 10 + @t[   _,4]");
    assertEquals(1026, result, 0);
  }

    // 함수 중첩 테스트 1
    // @Test
    // public void recursiveSingleFunctionCaluculateTest() throws ErrorHandler {
    //   Calculator calculator = new Calculator();                
    //   calculator.calculate("@r[%d] = %d + 1");
    //   calculator.calculate("@d[%a,%s,%d,     %f] = %a + %a + %s + %f");
    //   Double result = calculator.calculate("@d[@r[2],(5-1),@r[1234],@d[1,3,    5   , 7]]");
    //   // @d[3,4,1235,12] -> 22
    //   assertEquals(22, result, 0);
    // }
}
