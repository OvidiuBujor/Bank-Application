package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Notification;
import pentastagiu.model.User;
import pentastagiu.repository.NotificationRepository;

import java.time.LocalDateTime;

/**
 * This class handles the operations
 * regarding notifications: add, update.
 */
@Service
public class NotificationService {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    Notification addNotification(User cachedUser, String details){
        Notification notificationToBeSaved = new Notification();

        notificationToBeSaved.setDetails(details);
        notificationToBeSaved.setCreatedTime(LocalDateTime.now());
        notificationToBeSaved.setSentTime(LocalDateTime.now());
        notificationToBeSaved.setUser(cachedUser);
        return notificationRepository.save(notificationToBeSaved);
    }

    /**
     * This method is used to update the status a notification
     * once is sent to the user via email.
     */
    public Notification updateNotification(Notification notification){
        return notificationRepository.save(notification);
    }

    public Iterable<Notification> getNotifications(){
        return notificationRepository.findAll();
    }
}
