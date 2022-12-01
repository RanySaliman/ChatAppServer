package chatApp.controller;


import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.Utils.Role;
import chatApp.Utils.Validator;
import chatApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    private final Map<String, Object> errorsMap = new HashMap<>();

    /**
     * the function will get a user and will register it to the application after validation
     * @param req
     * @return creation of a user
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody User req) {

        Optional<Map<String, String>> validationErrors = Validator.validateRegister(req);
        if(validationErrors.isPresent()) {
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, validationErrors);
        }
        return authService.createUser(req);
    }

    /**
     * confirmation of the mail activation token
     * @param confirmationToken
     * @return the user will get an activation token to the mail
     */
    @RequestMapping(value="/confirm-account", method= RequestMethod.GET)
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return authService.confirmation(confirmationToken);
    }

    /**
     * User can log in to the app as USER
     * @param req
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<Object> logIn(@RequestBody User req) {

        Optional<Map<String, String>> errors = Validator.validateLogin(req);
        if(errors.isPresent()) {
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, errors);
        }

        return authService.login(req);
    }

    /**
     * user can login as a GUEST
     * @param req
     * @return
     */
    @RequestMapping(value = "loginGuest", method = RequestMethod.POST)
    public ResponseEntity<Object> logInAsGuest(@RequestBody User req) {

        Optional<Map<String, String>> errors = Validator.validateLogin(req);
        if(errors.isPresent()) {
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, errors);
        }

        return authService.loginAsGuest(req);
    }

    /**
     * User is logged out and the session is over
     * @param token
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<Object> logout(@RequestHeader String token) {

        return authService.logout(token);
    }

}
