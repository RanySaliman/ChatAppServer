package chatApp.controller;

import chatApp.Entities.Message;
import chatApp.Entities.PrivateChat;
import chatApp.Entities.User;
import chatApp.Utils.ChanelType;

import chatApp.service.AuthService;

import chatApp.service.MessageService;
import chatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthService authService;

    //        @RequestMapping(value = "private/{id}", method = RequestMethod.GET)
    //        public List<Message> getMessage(@PathVariable int id) {
    //            List<Message> byUserId = messageRepository.findByUserId(id);
    //            return byUserId;
    //        }


    @RequestMapping(value = "message", method = RequestMethod.POST)
    public Message creteMessage(@RequestBody String content) {
        return messageService.create(content);
    }


    @RequestMapping(value = "send", method = RequestMethod.POST)
    public void sendMessage(@RequestBody PrivateChat chat) {
        messageService.send(chat);
    }

    @RequestMapping(value = "privateChat", method = RequestMethod.GET)
    public List<User> getPrivateChats(@RequestHeader String token) {
        Optional<User> user = authService.findByToken(token);
        return messageService.getPrivateChats(user.get().getId());
    }


    @RequestMapping(value = "{chanelType}/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPrivateMessage(@RequestHeader String token, @PathVariable int id, @PathVariable ChanelType chanelType) {
        Optional<User> user = authService.findByToken(token);
        if(user.isPresent()) {
            switch(chanelType) {
                case PRIVATE:
                    return messageService.getPrivateMessages(user.get().getId(), id);
                case PUBLIC:
                    return messageService.getPublicMessages(id);
            }
        }
        return null;
    }

    public ResponseEntity<Object> exportMessages(@PathVariable int id, @PathVariable ChanelType chanelType) {
        switch(chanelType) {
            case PRIVATE:
                return messageService.exportMessages(id, 5);
            case PUBLIC:
                return messageService.exportPublicMessages(id);
        }
        return null;
    }


}
