package pentastagiu.convertor;

public enum OperationType {
    DEPOSIT("DEPOSIT"), WITHDRAW("WITHDRAW");

    private String text;

    OperationType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /**
     * This method transforms a string into an OperationType
     * @param text the string that will be transformed
     * @return the corresponding OperationType
     */
    public static OperationType fromString(String text) {
        for (OperationType operation_type : OperationType.values()) {
            if (operation_type.text.equalsIgnoreCase(text)) {
                return operation_type;
            }
        }
        return null;
    }
}
