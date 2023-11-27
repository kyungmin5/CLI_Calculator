import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class FunctionForm {
        private ArrayList<String> varaibleIndex;
        private HashMap<String, Double> variableMap = new HashMap<String,Double>();
        private String functionBody;
    
        FunctionForm(ArrayList<String> para, String functionBody, UserVariable variables, Double previousValue) throws ErrorHandler {
            this.varaibleIndex = para;
            ChangeExpression(functionBody, variables, previousValue);

            System.out.println("[FunctionForm]\n" + para + " \n" + functionBody + "\n");
        }

        private void ChangeExpression(String expression, UserVariable userVariables, Double previousValue) throws ErrorHandler
        {
            Set<String> variables = userVariables.getVarableSets();
            Pattern pattern;
            
            for (String string : variables) {
                String newString = "\\$" + string + "\\b";
                pattern = Pattern.compile(newString);
                Matcher matcher = pattern.matcher(expression);

                if (matcher.find())
                {
                    expression = expression.replaceAll(newString, userVariables.getVariable(string).toString());
                }
            }

            if(expression.contains("$"))
            {
                System.err.println("unDefined Variable Used");
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
            }

            String newstring =  "_" + "\\b";
            pattern = Pattern.compile(newstring);
            Matcher matcher = pattern.matcher(expression);

            if (matcher.find())
            {
                System.out.println("In");

                if(Double.isNaN(previousValue))
                {
                    System.err.println("previousValue is NAN");
                    throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
                }
                expression = expression.replaceAll(newstring, previousValue.toString());
            }
            this.functionBody = expression;
        }
    
        public String getFunction(ArrayList<Double> paraList) throws ErrorHandler{

            if(paraList.size() != varaibleIndex.size())
            {
                System.err.println("paraList size and varaibleIndex size dont match");
                throw new ErrorHandler(ErrorType.INVALID_EXPRESSION_ERROR);
            }

            for (int i=0; i<paraList.size(); i++) {
                variableMap.put(varaibleIndex.get(i), paraList.get(i));
            }

            String newFunctionBody = functionBody;

            for (String string : varaibleIndex) {
                String newstring =  "\\%" + string + "\\b";
                Pattern pattern = Pattern.compile(newstring);
                Matcher matcher = pattern.matcher(newFunctionBody);

                if (matcher.find())
                {
                    newFunctionBody = newFunctionBody.replaceAll(newstring, variableMap.get(string).toString());
                }
            }

            return newFunctionBody;
        }
    }


public class UserFunction {
    private HashMap<String, FunctionForm> functionMap = new HashMap<String,FunctionForm>();
    // <함수이름, <사용되는 변수, 사용되는 식>>

    public String getFunction(String functionName, ArrayList<Double> paraList) throws ErrorHandler
    {
       if(functionMap.containsKey(functionName))
       {
            return functionMap.get(functionName).getFunction(paraList);
       }else{
            System.err.println("function name doesnt match");
            throw new ErrorHandler(ErrorType.INVALID_OPERAND_ERROR);
       }
    }

    public void setFunction(UserVariable userVariables, Double previousValue , String functionName, ArrayList<String> functionPara, String functionBody) throws ErrorHandler
    {
        functionMap.put(functionName, new FunctionForm(functionPara, functionBody, userVariables, previousValue));
        System.out.println("[setFunction]\n" + functionName + " \n" + functionPara + "\n" + functionBody);
    }


}
