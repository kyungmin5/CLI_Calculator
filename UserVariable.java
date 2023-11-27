
import java.util.HashMap;
import java.util.Set;

public class UserVariable {
    private HashMap<String, Double> variableMap = new HashMap<String,Double>();

    public Set<String> getVarableSets()
    {
        return variableMap.keySet();
    }

    public Double getVariable(String variableName) throws ErrorHandler
    {
       if(variableMap.containsKey(variableName))
       {
            return variableMap.get(variableName);
       }else{
            throw new ErrorHandler(ErrorType.INVALID_OPERAND_ERROR);
       }
    }

    public void setVariable(String variableName, Double value)
    {
        variableMap.put(variableName, value);
    }

}