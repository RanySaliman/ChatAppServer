package chatApp.service;

import chatApp.Entities.ConfirmationToken;
import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.Utils.EmailActivationFacade;
import chatApp.Utils.Role;
import chatApp.controller.AuthController;
import chatApp.repository.ConfirmationTokenRepository;
import chatApp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static chatApp.Utils.Status.*;

@Service
public class AuthService {

    private final Map<String, Integer> tokenId;
    private final Map<String, Object> responseMap = new HashMap<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private EmailActivationFacade emailActivationFacade;
    private static Logger logger = LogManager.getLogger(AuthService.class.getName());



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
        responseMap.clear();
        Optional<User> user = userRepository.findByEmail(UserReq.getEmail());

        if(user.isPresent()) {
            responseMap.put("email", "email already in use");
            logger.error(user.get().getEmail() + " already in use");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responseMap);
        }

        UserReq.setRole(Role.USER);
        User user1 = userRepository.save(UserReq);

        ConfirmationToken confirmationToken = new ConfirmationToken(user1);
        confirmationTokenRepository.save(confirmationToken);

        emailActivationFacade.sendVerificationEmail(user1.getEmail(), confirmationToken);

        logger.info(user1.getFullName() + " user has been created successfully");
        responseMap.put("message", "successful Registration");
        responseMap.put("data", user1);

        return ResponseHandler.generateResponse(true, HttpStatus.OK, user1);
    }

    public String confirmation(String confirmationToken) {
        responseMap.clear();
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null) {
            Optional<User> user = userRepository.findByEmail(token.getUser().getEmail());
            if(user.isPresent()) {
                user.get().setEnabled(true);
                userRepository.save(user.get());
                responseMap.put("message", "account Verified");
                logger.info(user.get().getFullName() + " account was verified");
                return loginPageOrErrorPage(true);
            }

        }
        logger.error("email confirmation went wrong");
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
        responseMap.clear();
        Optional<User> user = userRepository.findByEmail(req.getEmail());

        if(user.isEmpty()) {
            responseMap.put("email", "could not find a user with this email");
            logger.error(req.getEmail() + " could not find a user with this email");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responseMap);
        }

        if(! user.get().isEnabled()) {
            responseMap.put("password", "Please Verify Your Email Address");
            logger.error(req.getEmail() + " did not verify his email");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responseMap);
        }

        if(! req.getPassword().equals(user.get().getPassword())) {
            responseMap.put("password", "incorrect password");
            logger.error("email or password incorrect");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responseMap);
        }

        String token = addTokenToUser(user.get());
        responseMap.put("data", user);
        responseMap.put("token", token);

        user.get().setStatus(ONLINE);
        logger.info(user.get().getFullName() + " has set status to be Online");

        userRepository.save(user.get());
        logger.info(user.get().getFullName() + " has logged in");
        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseMap);
    }

    public ResponseEntity<Object> loginAsGuest(User req) {
        responseMap.clear();
//        Optional<User> user = userRepository.findByNikName(req.getNikeName());

//        if(user.isEmpty()) {
//            responseMap.put("email", "could not find a user with this email");
//            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responseMap);
//        }

        req.setNikeName("Guest-" + req.getNikeName());
        req.setStatus(ONLINE);
        logger.info(req.getNikeName() + " set status to be Online");
        User savedUser = userRepository.save(req);

        String token = addTokenToUser(req);
        responseMap.put("data", savedUser);
        responseMap.put("token", token);
        logger.info(req.getNikeName() + " has logged in");

        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseMap);
    }

    public ResponseEntity<Object> logout(String  token) {

        Optional<User> user = findByToken(token);
        user.get().setStatus(OFFLINE);
        logger.info(user.get().getFullName() + " status is now offline");
        userRepository.save(user.get());
        tokenId.remove(token);
        logger.info(user.get().getFullName() + " has logged out");
        return ResponseHandler.generateResponse(true, HttpStatus.OK, null);
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
