package chatApp.controller;


import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.Utils.Validator;
import chatApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    private final Map<String, Object> errorsMap = new HashMap<>();

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody User req) {

        Optional<Map<String, String>> validationErrors = Validator.validateRegister(req);
        if(validationErrors.isPresent()) {
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, validationErrors);
        }

        return authService.createUser(req);
    }

//    @RequestMapping(value = "login", method = RequestMethod.POST)
//    public ResponseEntity<Object> logIn(@RequestBody User req) {
//
//        Optional<Map<String, String>> errors = Validator.validateLogin(req);
//        if(errors.isPresent()) {
//            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, errors);
//        }
//
//        Optional<User> user = authService.findByEmail(req.getEmail());
//        if(user.isEmpty()) {
//            errorsMap.put("email", "could not find a user with this email");
//            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, errorsMap);
//        }
//
//        if(!req.getPassword().equals(user.get().getPassword())){
//            errorsMap.put("password", "incorrect password");
//            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, errorsMap);
//        }
//
//        Map<String, Object> data = authService.addTokenToUser(user.get());
//        return ResponseHandler.generateResponse(true, HttpStatus.OK, data);
//    }

}
