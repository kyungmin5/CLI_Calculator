package operator;

import java.util.Arrays;
import java.util.List;

public enum OperatorType {
    PLUS("+", 1),
    MINUS("-", 1),
    MULTIPLATION("*", 2),
    DIVISION("/", 2),
    POWER("^", 3);

    public static List<String> operators = Arrays.asList(Arrays.asList(OperatorType.values()).stream().map(type -> type.getOperator()).toArray(String[]::new));

    private String operator;
    private int priority;
    
    OperatorType(String operator, int priority) {
        this.operator = operator;
        this.priority = priority;
    }

    public String getOperator() {
        return operator;
    }

    public int getPriority() {
        return priority;
    }
}
