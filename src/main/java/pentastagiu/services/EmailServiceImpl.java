package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pentastagiu.util.Mail;
import pentastagiu.model.Notification;
import pentastagiu.model.Person;

/**
 * This class is used to send notifications
 * regarding transfers.
 */
@Service
public class EmailServiceImpl implements EmailService{

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    public Mail createEmail(Person personDetails, Notification notification){
        Mail email = new Mail();
        email.setFrom("bujorovidiuionut@gmail.com");
        email.setTo(personDetails.getEmail());
        email.setSubject("Notification details for transfer");
        email.setContent(notification.getDetails());
        return email;
    }

    @Override
    public void send(String from, String to, String title, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(body);
        message.setTo(to);
        message.setFrom(from);
        emailSender.send(message);
    }
}
