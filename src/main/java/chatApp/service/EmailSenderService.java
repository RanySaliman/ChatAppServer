package chatApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * this function will print success message when the email activation is sent
     * @param email
     * @return message of success
     */
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
        System.out.println("email sent successfully");
    }
}
