public enum OperatorType {
    PLUS("+", 0),
    MINUS("-", 0),
    MULTIPLATION("*", 1),
    DIVISION("/", 1),
    POWER("^", 2),
    ;

    private String operator;
    private int priority;
    
    OperatorType(String operator, int priority) {
        this.operator = operator;
        this.priority = priority;
    }

    public static OperatorType fromString(String str) throws ErrorHandler {
        switch (str) {
            case "+":
                return OperatorType.PLUS;
            case "-":
                return OperatorType.MINUS;
            case "*":
                return OperatorType.MULTIPLATION;
            case "/":
                return OperatorType.DIVISION;
            case "^":
                return OperatorType.POWER;
            default:
                throw new ErrorHandler(ErrorType.InValidOperator_error);
        }
    }
}
