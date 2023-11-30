package user;

import java.util.ArrayList;
import java.util.HashMap;

import error.*;

public class UserFunction {
    private HashMap<String, FunctionForm> functionMap = new HashMap<String,FunctionForm>();
    // <함수이름, <사용되는 변수, 사용되는 식>>

    public String getFunction(String functionName, ArrayList<Double> paraList) throws ErrorHandler
    {
       if(functionMap.containsKey(functionName))
       {
            String newFunctionBody =  functionMap.get(functionName).getFunction(paraList);
            System.out.println("[newFunctionBody]\n" + newFunctionBody);


            while(newFunctionBody.contains("@"))
            {
                int startIndex = newFunctionBody.indexOf("@");
                int index = 1;
                String newfunctionName = "";
                for(; (index + startIndex < newFunctionBody.length()) && (newFunctionBody.charAt(index + startIndex) != '['); index++)
                {
                    newfunctionName += newFunctionBody.charAt(index + startIndex);
                }

                index = index + 1;
                String paraString = "";
                for( ; index + startIndex<newFunctionBody.length(); index++)
                {
                    if(newFunctionBody.charAt(index + startIndex) == ']')
                    {
                        break;
                    }
                    paraString += newFunctionBody.charAt(index + startIndex);
                }

                ArrayList<Double> para = new ArrayList<Double>();
                String[] paras = paraString.split(",");
                if(paras[0].length() != 0)
                {
                    for (String string : paras) {
                        string = string.trim();
                        para.add(Double.valueOf(string));
                    }
                }
                
                String functionExpression = functionMap.get(newfunctionName).getFunction(para);
                String before = newFunctionBody.substring(0, startIndex);
                String after = newFunctionBody.substring(index + startIndex + 1);
                newFunctionBody =  before + functionExpression + after;

                System.out.println("[newFunctionBody]\n" + newFunctionBody);

            }

            return newFunctionBody;

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
