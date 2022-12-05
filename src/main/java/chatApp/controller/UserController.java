package chatApp.controller;

import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.Utils.Validator;
import chatApp.service.AuthService;
import chatApp.service.GroupMembersService;
import chatApp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.sql.SQLDataException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    private final Map<String, Object> responeMap = new HashMap<>();

    private static Logger logger = LogManager.getLogger(UserController.class.getName());

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserById(@PathVariable int id){
        responeMap.clear();
        Optional<User> userById = userService.findById(id);
        logger.info("searching user by id " + id);
        if(userById.isEmpty()) {
            responeMap.put("error","there is no user with this id");
            logger.error("searched for id that doesn't exist - id = " + id);
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        responeMap.put("data",userById.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, responeMap);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updateFields(@PathVariable int id, @RequestBody Map<String, String> fields) {
        System.out.println(fields);
        logger.info("updating field for user with id " + id);
        Optional<Map<String, String>> validationErrors = Validator.validateFields(fields);
        if(validationErrors.isPresent()) {
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, validationErrors);
        }
        System.out.println(id);
        Optional<User> userById = userService.findById(id);

        if(userById.isEmpty()) {
            responeMap.put("error","there is no user with this id");
            logger.error("tried to update field for user with non existing id. id = " + id);
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        fields.forEach((key, value) -> {

            Field field = ReflectionUtils.findField(User.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, userById.get(), value);
        });
        User updatedUser = userService.update(userById.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, updatedUser);
    }

        @RequestMapping(value = "search", method = RequestMethod.GET)
        public ResponseEntity<Object> search(@RequestParam("query") String query){
            Set<User> byQuery = userService.findByQuery(query);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, byQuery);
        }

        @RequestMapping(value = "mute/{id}", method = RequestMethod.GET)
        public User mutedUser(@RequestHeader String token, @PathVariable int id){
            Optional<User> user = authService.findByToken(token);
            logger.info("trying to mute user " + user.get().getFullName());
            if(user.isPresent()) {
                userService.getUserById(id).get().setMuted(!(userService.getUserById(id).get().isMuted()));
                return userService.update(userService.getUserById(id).get());
            }

            return null;

    }


}
