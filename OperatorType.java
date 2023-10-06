public enum OperatorType {
    PLUS("+", 0),
    MINUS("-", 0),
    MULTIPLATION("*", 1),
    DIVISION("/", 1);

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
            default:
                throw new ErrorHandler(ErrorType.InValidOperator_error);
        }
    }
}
