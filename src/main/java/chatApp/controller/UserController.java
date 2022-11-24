package chatApp.controller;

import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.Utils.Validator;
import chatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private final Map<String, Object> responeMap = new HashMap<>();


//    @RequestMapping(method = RequestMethod.POST)
//    public String createUser(@RequestBody User user){
//        try {
//            User u = userService.addUser(user);
//            return u.getId().toString() + u.getId().toString();
//        } catch (SQLDataException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Email already exists", e);
//        }
//    }

//    @RequestMapping(value = "search", method = RequestMethod.GET)
//    public ResponseEntity<Object> search(@RequestParam("query") String query){
//        List<User> byQuery = userService.findByQuery(query);
//        return ResponseHandler.generateResponse(true, HttpStatus.OK, byQuery);
//    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserById(@PathVariable int id){
        responeMap.clear();
        Optional<User> userById = userService.findById(id);

        if(userById.isEmpty()) {
            responeMap.put("error","there is no user with this id");
            return ResponseHandler.generateErrorResponse(false, HttpStatus.BAD_REQUEST, responeMap);
        }

        responeMap.put("data",userById.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, responeMap);
    }


//        @RequestMapping( method = RequestMethod.Post)
//        public ResponseEntity<Object> topics(@PathVariable String type){
//            List<User> topics = userService.findByTopic(type);
//            return ResponseHandler.generateResponse(true, HttpStatus.OK, topics);
//        }

}
