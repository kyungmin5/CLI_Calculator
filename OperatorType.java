import java.util.Arrays;
import java.util.List;

public enum OperatorType {
    PLUS("+", 0),
    MINUS("-", 0),
    MULTIPLATION("*", 1),
    DIVISION("/", 1),
    POWER("^", 2);

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
