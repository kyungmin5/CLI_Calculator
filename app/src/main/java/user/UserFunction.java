package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import manager.*;
import error.*;

public class UserFunction {
    // <함수이름, <사용되는 변수, 사용되는 식>>
    private HashMap<String, FunctionForm> functionMap = new HashMap<String,FunctionForm>();

    public String getFunction(String functionName, ArrayList<Double> paraList) throws ErrorHandler {
        if(functionMap.containsKey(functionName)) {
            return functionMap.get(functionName).getFunction(paraList);
            // for (Double d : paraList) {
            //     System.out.println(d);
            // }
            // String newFunctionBody =  
            // System.out.println("[newFunctionBody]: " + newFunctionBody);

            // while(newFunctionBody.contains("@")) {
            //     int startIndex = newFunctionBody.indexOf("%");
            //     int index = 1;
            //     String newfunctionName = "";
            //     for(; (index + startIndex < newFunctionBody.length()) && (newFunctionBody.charAt(index + startIndex) != '['); index++)
            //     {
            //         newfunctionName += newFunctionBody.charAt(index + startIndex);
            //     }

            //     index = index + 1;
            //     String paraString = "";
            //     for( ; index + startIndex<newFunctionBody.length(); index++)
            //     {
            //         if(newFunctionBody.charAt(index + startIndex) == ']')
            //         {
            //             break;
            //         }
            //         paraString += newFunctionBody.charAt(index + startIndex);
            //     }

            //     ArrayList<Double> para = new ArrayList<Double>();
            //     String[] paras = paraString.split(",");
            //     if(paras[0].length() != 0)
            //     {
            //         for (String string : paras) {
            //             string = string.trim();
            //             para.add(Double.valueOf(string));
            //         }
            //     }
                
            //     String functionExpression = functionMap.get(newfunctionName).getFunction(para);
            //     String before = newFunctionBody.substring(0, startIndex);
            //     String after = newFunctionBody.substring(index + startIndex + 1);
            //     newFunctionBody =  before + functionExpression + after;

            //     System.out.println("[newFunctionBody]\n" + newFunctionBody);

            // }

            // return newFunctionBody;

       } else {
            System.err.println("function name doesnt match");
            throw new ErrorHandler(ErrorType.INVALID_OPERAND_ERROR);
       }
    }

    // 함수에 다른 함수가 쓰였을 시, 해당 함수의 존재여부
    // 함수에 정의되지 않은 매개변수가 쓰였을 시, 유효성 검사
    public void setFunction(
        UserVariable userVariables,
        Double previousValue,
        String functionName,
        ArrayList<String> functionPara,
        String functionBody
    ) throws ErrorHandler {
        Set<String> set = functionPara.stream().collect(Collectors.toSet());
        if (set.size() != functionPara.size()) {
            throw new ErrorHandler(ErrorType.FUNCTION_PARAMETER_DUPLICATE_DEFINE_ERROR);
        }
        FunctionForm function = new FunctionForm(functionPara, functionBody, userVariables, previousValue);
        (new ValidationManager()).checkFunctionExpression(function, functionPara.size());
        functionMap.put(functionName, function);
        // System.out.println("[setFunction]\n" + functionName + " \n" + functionPara + "\n" + functionBody);
    }
}
