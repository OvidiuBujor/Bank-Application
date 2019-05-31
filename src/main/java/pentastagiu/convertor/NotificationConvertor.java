package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.model.Notification;

/**
 * This class converts a Notification to
 * a NotificationDTO that is used for responses of
 * the server
 */
@Component
public class NotificationConvertor {
    public NotificationDTO convertToNotificationDTO(Notification notification){
        return new NotificationDTO(notification.getUser().getUsername(),
                notification.getDetails(),
                notification.getCreatedTime());
    }
}
