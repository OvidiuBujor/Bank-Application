package pentastagiu.convertor;

/**
 * This enum holds the valid account types for an account.
 */
public enum AccountType {
    RON("RON"), EUR("EUR");

    private String text;

    AccountType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /**
     * This method transforms a string into an AccountType
     * @param text the string that will be transformed
     * @return the corresponding ACCOUNT_TYPE
     */
    public static AccountType fromString(String text) {
        for (AccountType account_type : AccountType.values()) {
            if (account_type.text.equalsIgnoreCase(text)) {
                return account_type;
            }
        }
        return null;
    }
}
