package pentastagiu.convertor;

/**
 * This enum holds the valid types of a transaction
 */
public enum TransactionType {
    incoming("incoming"), outgoing("outgoing");

    private String text;

    TransactionType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /**
     * This method transforms a string into an AccountType
     * @param text the string that will be transformed
     * @return the corresponding TRANSACTION_TYPE
     */
    public static TransactionType fromString(String text) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.text.equalsIgnoreCase(text)) {
                return transactionType;
            }
        }
        return null;
    }
}
