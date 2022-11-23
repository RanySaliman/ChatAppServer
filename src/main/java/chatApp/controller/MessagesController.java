package chatApp.controller;

import chatApp.Entities.Message;
import chatApp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class MessagesController {

    @Autowired
    private MessageRepository messageRepository;

//        @RequestMapping(value = "private/{id}", method = RequestMethod.GET)
//        public List<Message> getMessage(@PathVariable int id) {
//            List<Message> byUserId = messageRepository.findByUserId(id);
//            return byUserId;
//        }


}
