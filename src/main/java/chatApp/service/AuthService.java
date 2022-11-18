package chatApp.service;

import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public ResponseEntity<Object> createUser(User req) {
        Optional<User> user = userRepository.findByEmail(req.getEmail());

        if(user.isPresent()) {
            responeMap.put("email", "email already in use");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        User user1 = userRepository.save(req);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, user1);
    }
}
