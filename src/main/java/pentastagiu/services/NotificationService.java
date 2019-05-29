package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Notification;
import pentastagiu.model.User;
import pentastagiu.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    Notification addNotification(User cachedUser, String details){
        Notification notificationToBeSaved = new Notification();

        notificationToBeSaved.setDetails(details);
        notificationToBeSaved.setCreatedTime(LocalDateTime.now());
        notificationToBeSaved.setSentTime(LocalDateTime.now());
        notificationToBeSaved.setUser(cachedUser);
        return notificationRepository.save(notificationToBeSaved);
    }
}
