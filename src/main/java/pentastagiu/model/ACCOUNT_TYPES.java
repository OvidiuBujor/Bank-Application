package pentastagiu.model;

/**
 * This enum holds the valid account types for an account.
 */
public enum ACCOUNT_TYPES {
    RON("RON"), EUR("EUR");

    private String text;

    ACCOUNT_TYPES(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /**
     * This method transforms a string into an ACCOUNT_TYPES
     * @param text the string that will be transformed
     * @return the corresponding ACCOUNT_TYPE
     */
    public static ACCOUNT_TYPES fromString(String text) {
        for (ACCOUNT_TYPES account_type : ACCOUNT_TYPES.values()) {
            if (account_type.text.equalsIgnoreCase(text)) {
                return account_type;
            }
        }
        return null;
    }
}
