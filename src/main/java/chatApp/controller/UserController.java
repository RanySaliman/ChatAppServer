package chatApp.controller;

import chatApp.Entities.Message;
import chatApp.Entities.Topic;
import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLDataException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public String createUser(@RequestBody User user){
        try {
            User u = userService.addUser(user);
            return u.getId().toString() + u.getId().toString();
        } catch (SQLDataException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already exists", e);
        }
    }

//    @RequestMapping(value = "search", method = RequestMethod.GET)
//    public ResponseEntity<Object> search(@RequestParam("query") String query){
//        List<User> byQuery = userService.findByQuery(query);
//        return ResponseHandler.generateResponse(true, HttpStatus.OK, byQuery);
//    }

//    @RequestMapping(value = "topic/{type}", method = RequestMethod.GET)
//    public ResponseEntity<Object> topics(@PathVariable String type){
//        List<User> topics = userService.findByTopic(type);
//        return ResponseHandler.generateResponse(true, HttpStatus.OK, topics);
//    }


//        @RequestMapping( method = RequestMethod.Post)
//        public ResponseEntity<Object> topics(@PathVariable String type){
//            List<User> topics = userService.findByTopic(type);
//            return ResponseHandler.generateResponse(true, HttpStatus.OK, topics);
//        }

}
