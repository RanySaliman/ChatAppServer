package chatApp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatController {
//    @MessageMapping("/hello")
//    @SendTo("/topic/mainChat")
//    public ChatMessage greeting(HelloMessage message) throws Exception {
//        return new ChatMessage("SYSTEM", message.getName() + "joined the chat");
//    }
//
//    @MessageMapping("/plain")
//    @SendTo("/topic/mainChat")
//    public ChatMessage sendPlainMessage(ChatMessage message) {
//        return message;
//    }

}