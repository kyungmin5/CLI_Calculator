package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import error.*;

public class FunctionForm {
        private ArrayList<String> parameterNameList;
        private HashMap<String, Double> variableMap = new HashMap<String,Double>();
        private String functionBody;
    
        FunctionForm(
            ArrayList<String> para,
            String functionBody,
            UserVariable variables,
            Double previousValue
        ) throws ErrorHandler {
            this.parameterNameList = para;
            changeVariable(functionBody, variables);
            changePreviousValue(functionBody, previousValue);

            System.out.println("[FunctionForm]\n" + para + " \n" + functionBody + "\n");
        }

        // 사용된 변수 치환
        private void changeVariable(
            String expression, 
            UserVariable userVariables
        ) throws ErrorHandler {
            Set<String> variables = userVariables.getVarableSets();
            Pattern pattern;
            
            for (String string : variables) {
                String variableName = "\\$" + string + "\\b";
                pattern = Pattern.compile(variableName);
                Matcher matcher = pattern.matcher(expression);

                if (matcher.find()) {
                    expression = expression.replaceAll(variableName, userVariables.getVariable(string).toString());
                }
            }

            // 정의되지 않은 변수가 사용되었을 때
            if (expression.contains("$")) {
                throw new ErrorHandler(ErrorType.VARIABLE_UNDEFINE_ERROR);
            }

            this.functionBody = expression;
        }
    
        // 사용된 직전 결과값 치환
        private void changePreviousValue(
            String expression, 
            Double previousValue
        ) throws ErrorHandler {
            String newstring =  "_" + "\\b";
            Pattern pattern  = Pattern.compile(newstring);
            Matcher matcher = pattern.matcher(expression);

            if (matcher.find()) {
                if(Double.isNaN(previousValue)) {
                    throw new ErrorHandler(ErrorType.INVALID_PREVIOUS_VALUE_ERROR);
                }
                expression = expression.replaceAll(newstring, previousValue.toString());
            }
            this.functionBody = expression;
        }

        // 함수 호출
        public String getFunction(ArrayList<Double> paraList) throws ErrorHandler {
            // 매개변수 개수 확인
            if (paraList.size() != parameterNameList.size()) {
                // System.err.println("paraList size and varaibleIndex size dont match");
                throw new ErrorHandler(ErrorType.FUNCTION_PARAMETER_SIZE_UNMATCH_ERROR);
            }

            // 매개변수 값 저장
            for (int i=0; i<paraList.size(); i++) {
                variableMap.put(parameterNameList.get(i), paraList.get(i));
            }

            String newFunctionBody = functionBody;

            // 매개변수 값 치환
            for (String parameterName : parameterNameList) {
                String newstring =  parameterName + "\\b";
                Pattern pattern = Pattern.compile(newstring);
                Matcher matcher = pattern.matcher(newFunctionBody);

                if (matcher.find()) {
                    newFunctionBody = newFunctionBody.replaceAll(newstring, variableMap.get(parameterName).toString());
                }
            }

            return newFunctionBody;
        }
    }