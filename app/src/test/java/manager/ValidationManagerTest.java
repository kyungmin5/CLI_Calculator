package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import error.ErrorHandler;

public class ValidationManagerTest {
  private ValidationManager validationManager = new ValidationManager();

  // 변수
  @Test
  public void blankVariableTest() throws ErrorHandler {
    validationManager.checkVariableDefineExpression("$1sdf3 = 124");
  }

  @Test
  public void onlyLeftBlankVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$1sdf3 =124");
    });
  }

  @Test
  public void multipleLeftBlankVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$1sdf3   = 124");
    });
  }

  @Test
  public void onlyRightBlankVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$1sdf3= 124");
    });
  }

  @Test
  public void multipleRightBlankVariableTest() throws ErrorHandler {
    validationManager.checkVariableDefineExpression("$1sdf3 =    124");
  }

  @Test
  public void noBlankVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$1sdf3=124");
    });
  }

  @Test
  public void emptyNameVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$ = 124");
    });
  }

  @Test
  public void emptyExpressionVariableTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkVariableDefineExpression("$x = ");
    });
  }

  // 함수
  // 공백에 대해
  @Test
  public void blankFunctionTest() throws ErrorHandler {
    validationManager.checkFunctionDefine("@test[%d,%x] = 124");
  }

  @Test
  public void onlyLeftBlankFunctionTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@test[%d,%x] =124");
    });
  }

  @Test
  public void multipleLeftBlankFunctionTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@test[%d,%x]   = 124");
    });
  }

  @Test
  public void onlyRightBlankFunctionTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@test[%d,%x]= 124");
    });
  }

  @Test
  public void multipleRightBlankFunctionTest() throws ErrorHandler {
    validationManager.checkFunctionDefine("@test[%d,%x] =    124");
  }

  @Test
  public void noBlankFunctionTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@test[%d,%x]=124");
    });
  }

  // 형식에 대해
  @Test
  public void parameterTest() throws ErrorHandler {
    validationManager.checkFunctionDefine("@g[%d, %a] = 12");
    validationManager.checkFunctionDefine("@g[%d,%a] = 12");
  }

  @Test
  public void emptyParameterTest() throws ErrorHandler {
    validationManager.checkFunctionDefine("@g[] = 12");
  }
  
  @Test
  public void commaEndTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@vsad[%x, %y,] = %x + %y");
    });
  }
  
  @Test
  public void emptyPercentNameTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@vsad[x] = %x + %y");
    });
  }

  @Test
  public void emptyParameterNameTest() throws ErrorHandler {
    assertThrows(ErrorHandler.class, () -> {
      validationManager.checkFunctionDefine("@vsad[%, %y] = %x + %y");
    });
  }
}
