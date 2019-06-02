package pentastagiu.util;

/**
 * This enum holds the type of a notification status.
 */
public enum NotificationStatus {
    SEND("SEND"), NOT_SEND("NOT_SEND");

    private String text;

    NotificationStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /**
     * This method transforms a string into an NotificationStatus
     * @param text the string that will be transformed
     * @return the corresponding NotificationStatus
     */
    public static NotificationStatus fromString(String text) {
        for (NotificationStatus notificationStatus : NotificationStatus.values()) {
            if (notificationStatus.text.equalsIgnoreCase(text)) {
                return notificationStatus;
            }
        }
        return null;
    }
}
