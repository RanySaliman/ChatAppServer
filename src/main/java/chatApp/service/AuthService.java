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

//    public Map<String, Object> addTokenToUser(User user){
//        Map<String, Object> dataMap = new HashMap<>();
//        String token = createToken();
//        tokenId.put(token, user.getId());
//
//        dataMap.put("user",user);
//        dataMap.put("token", token);
//        return dataMap;
//    }

//    public Optional<User> findByToken(String token) {
//        if(tokenId.containsKey(token)) {
//            return userRepository.getUserById(tokenId.get(token));
//        }
//        return Optional.empty();
//    }

    private String createToken(){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder;
        do {
            stringBuilder = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                stringBuilder.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
            }
        }
        while (tokenId.get(stringBuilder) != null);
        return stringBuilder.toString();
    }


    public ResponseEntity<Object> createUser(User UserReq) {
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
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8082/auth/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        responeMap.put("message","successful Registration");
        responeMap.put("data",user1);

        return ResponseHandler.generateResponse(true, HttpStatus.OK, user1);
    }
}
