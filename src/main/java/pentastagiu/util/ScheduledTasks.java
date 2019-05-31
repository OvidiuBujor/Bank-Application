package pentastagiu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pentastagiu.convertor.NotificationStatus;
import pentastagiu.model.*;
import pentastagiu.services.AuthenticationService;
import pentastagiu.services.EmailServiceImpl;
import pentastagiu.services.NotificationService;
import pentastagiu.services.PersonService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * This class handles the scheduled
 * tasks: sending emails and deleting
 * expired tokens.
 */
@Component
public class ScheduledTasks {

    private Logger LOGGER = LogManager.getLogger();

    private NotificationService notificationService;

    private EmailServiceImpl emailServiceImpl;

    private PersonService personService;

    private AuthenticationService authenticationService;

    /**
     * The period for which a token is valid
     */
    @Value ("${tokenValability}")
    private Long tokenValability;

    @Autowired
    public ScheduledTasks(NotificationService notificationService,
                          EmailServiceImpl emailServiceImpl,
                          PersonService personService,
                          AuthenticationService authenticationService){
        this.notificationService = notificationService;
        this.emailServiceImpl = emailServiceImpl;
        this.personService = personService;
        this.authenticationService = authenticationService;
    }

    /**
     * This method sends email with a notification regarding
     * a transfer to the email address of the owner of these
     * accounts. This method runs every minute.
     */
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

    /**
     * This method deletes all the expired tokens.
     * It runs every minute.
     */
    @Scheduled(cron = "0 * * * * ?")
    public void deleteTokens(){
        Iterable<Authentication> authentications = authenticationService.getAuthentications();
        for(Authentication authentication: authentications){
            Long duration = Duration.between(authentication.getCreationTime(),LocalDateTime.now()).toMinutes();
            if (duration >= tokenValability) {
                authenticationService.deleteAuthentication(authentication);
                System.out.println(LocalDateTime.now() + "  Authentication with id = " + authentication.getId() + " was deleted.");
            }
        }
    }
}
