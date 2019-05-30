package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Notification;
import pentastagiu.model.User;
import pentastagiu.repository.NotificationRepository;

import java.time.LocalDateTime;

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

    public Notification updateNotification(Notification notification){
        return notificationRepository.save(notification);
    }

    public Iterable<Notification> getNotifications(){
        return notificationRepository.findAll();
    }
}
