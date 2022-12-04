package chatApp.Utils;

import chatApp.Entities.ConfirmationToken;
import chatApp.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailActivationFacade {


    @Autowired
    private EmailSenderService emailSenderService = new EmailSenderService(new JavaMailSenderImpl());

    public void sendEmail(String email, ConfirmationToken confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("chatappory@gmail.com");
        mailMessage.setText("Thank you for registering to the ChatApp Application.\n"
                + "This is a verification email, please click the link to verify your email address\n"
                + "http://localhost:8080/auth/confirm-account?token=" + confirmationToken.getConfirmationToken()
                + "\nThank you...");
        emailSenderService.sendEmail(mailMessage);
    }
}
