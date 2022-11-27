package chatApp.service;

import chatApp.Entities.ConfirmationToken;
import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.repository.ConfirmationTokenRepository;
import chatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final Map<String, Integer> tokenId;
    private final Map<String, Object> responeMap = new HashMap<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    public AuthService() {
        tokenId = new HashMap<>();
    }


    private String createToken() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder;
        do {
            stringBuilder = new StringBuilder(6);
            for(int i = 0; i < 6; i++) {
                stringBuilder.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
            }
        }
        while(tokenId.get(stringBuilder) != null);
        return stringBuilder.toString();
    }


    public ResponseEntity<Object> createUser(User UserReq) {
        responeMap.clear();
        Optional<User> user = userRepository.findByEmail(UserReq.getEmail());

        if(user.isPresent()) {
            responeMap.put("email", "email already in use");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        User user1 = userRepository.save(UserReq);
        ConfirmationToken confirmationToken = new ConfirmationToken(user1);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user1.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("omar.ah.2014@gmail.com");
        mailMessage.setText("Thank you for registering for ChatApp Application.\n"
                + "This is a verification email, please click the link to verify your email address\n"
                + "http://localhost:8080/auth/confirm-account?token=" + confirmationToken.getConfirmationToken()
                + "\nThank you...");
        emailSenderService.sendEmail(mailMessage);

        responeMap.put("message", "successful Registration");
        responeMap.put("data", user1);

        return ResponseHandler.generateResponse(true, HttpStatus.OK, user1);
    }


    public String confirmation(String confirmationToken) {
        responeMap.clear();
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null) {
            Optional<User> user = userRepository.findByEmail(token.getUser().getEmail());
            if(user.isPresent()) {
                user.get().setEnabled(true);
                userRepository.save(user.get());
                responeMap.put("message", "account Verified");

                return loginPageOrErrorPage(true);
            }

        }
        return loginPageOrErrorPage(false);
    }


    public String loginPageOrErrorPage(boolean isVerified) {

        if(isVerified) {

            return "<html>\n" + "<header><title>account Verified</title></header>\n" +
                    "<body>\n" +
                    "<p>Congratulations! Your account has been activated and email is verified! <br />" +
                    "please go to this link to login : <a href=\"http://localhost:3000/login\">ChatApp</a></p>\n" +
                    "</body>\n" + "</html>";
        }
        return "<html>\n" + "<header><title>account  not Verified</title></header>\n" +
                "<body>\n" +
                "<p>The link is invalid or broken!</p>\n" +
                "</body>\n" + "</html>";

    }


    public ResponseEntity<Object> login(User req) {
        responeMap.clear();
        Optional<User> user = userRepository.findByEmail(req.getEmail());

        if(user.isEmpty()) {
            responeMap.put("email", "could not find a user with this email");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        if(! user.get().isEnabled()) {
            responeMap.put("password", "Please Verify Your Email Address");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        if(! req.getPassword().equals(user.get().getPassword())) {
            responeMap.put("password", "incorrect password");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        String token = addTokenToUser(user.get());
        responeMap.put("data", user);
        responeMap.put("token", token);

        return ResponseHandler.generateResponse(true, HttpStatus.OK, responeMap);
    }


    public String addTokenToUser(User user) {
        Map<String, Object> dataMap = new HashMap<>();
        String token = createToken();
        tokenId.put(token, user.getId());
        return token;
    }


    public Optional<User> findByToken(String token) {
        if(tokenId.containsKey(token)) {
            return userRepository.getUserById(tokenId.get(token));
        }
        return Optional.empty();
    }


    public Optional<User> isAuth(int id) {
        if(tokenId.containsValue(id)) {
            return userRepository.getUserById(id);
        }
        return Optional.empty();
    }
}
