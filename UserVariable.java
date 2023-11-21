import java.text.DecimalFormat;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class UserVariable {
    private HashMap<String, Double> variableMap = new HashMap<String,Double>();

    public Double getVariable(String variableName) throws ErrorHandler
    {
       if(variableMap.containsKey(variableName))
       {
            return variableMap.get(variableName);
       }else{
            throw new ErrorHandler(ErrorType.InValidOperand_error);
       }
    }

    public void setVariable(String variableName, Double value)
    {
        variableMap.put(variableName, value);
    }

}