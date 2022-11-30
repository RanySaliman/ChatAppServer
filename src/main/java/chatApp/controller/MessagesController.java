package chatApp.controller;

import chatApp.Entities.Message;
import chatApp.Entities.PrivateChat;
import chatApp.Utils.ChanelType;
import chatApp.repository.PrivateChatRepository;
import chatApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class MessagesController {

    @Autowired
    private MessageService messageService;

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

//    @RequestMapping(value = "{chanelType}/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Object> getPrivateMessage(@PathVariable int id, @PathVariable ChanelType chanelType) {
//        switch(chanelType) {
//            case PRIVATE:
//                return messageService.getPrivateMessages(id, 5);
//            case PUBLIC:
//                return messageService.getPublicMessages(3);
//        }
//        return null;
//    }

    @RequestMapping(value = "{chanelType}/{id}", method = RequestMethod.GET)
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
