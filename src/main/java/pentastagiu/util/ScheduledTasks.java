package pentastagiu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pentastagiu.convertor.NotificationStatus;
import pentastagiu.model.Mail;
import pentastagiu.model.Notification;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.services.EmailServiceImpl;
import pentastagiu.services.NotificationService;
import pentastagiu.services.PersonService;

@Component
public class ScheduledTasks {

    private Logger LOGGER = LogManager.getLogger();

    private NotificationService notificationService;

    private EmailServiceImpl emailServiceImpl;

    private PersonService personService;

    @Autowired
    public ScheduledTasks(NotificationService notificationService,
                          EmailServiceImpl emailServiceImpl,
                          PersonService personService){
        this.notificationService = notificationService;
        this.emailServiceImpl = emailServiceImpl;
        this.personService = personService;
    }

    @Scheduled(cron = "0 * * * * ?")//send notifications with status NOT_SEND each minute
    public void sendEmail() {
        Iterable<Notification> notificationList =  notificationService.getNotifications();
        for(Notification notification: notificationList){
            if(notification.getStatus() == NotificationStatus.NOT_SEND) {
                User user = notification.getUser();
                Person personDetails = personService.getPersonDetails(user.getId());
                Mail email = emailServiceImpl.createEmail(personDetails,notification);
                emailServiceImpl.send(email.getFrom(),email.getTo(),email.getSubject(),email.getContent());
                notification.setStatus(NotificationStatus.SEND);
                notificationService.updateNotification(notification);
                System.out.println("Email sent for notification: " + notification.getDetails());
                LOGGER.info("Email sent for notification: " + notification.getDetails());
            }
        }
    }
}
