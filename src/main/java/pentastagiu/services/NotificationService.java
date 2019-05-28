package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Notification;
import pentastagiu.model.User;
import pentastagiu.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public void addNotification(User cachedUser, String details){
        Notification notificationToBeSaved = new Notification();

        notificationToBeSaved.setDetails(details);
        notificationToBeSaved.setCreatedTime(LocalDateTime.now());
        notificationToBeSaved.setSentTime(LocalDateTime.now());
        notificationToBeSaved.setUser(cachedUser);
        notificationRepository.save(notificationToBeSaved);
    }
}
