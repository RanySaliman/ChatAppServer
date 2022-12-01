package chatApp.controller;

import chatApp.Entities.Message;
import chatApp.Entities.MessageChat;
import chatApp.Entities.MessagePublicChat;
import chatApp.Entities.PrivateChat;
import chatApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessagePublicChat receiveMessage(@Payload MessagePublicChat message){
        System.out.println(message.getMessage());
        Message messageObj = messageService.create(message.getMessage());
        messageService.send(new PrivateChat(message.getSender().getId(), 114, messageObj.getId()));
        return message;
    }

    @MessageMapping("/private-message")
    public MessageChat recMessage(@Payload MessageChat message){
        System.out.println(message.getMessage());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver().getEmail(),"/private",message);
        Message messageObj = messageService.create(message.getMessage());
        messageService.send(new PrivateChat(message.getSender().getId(), message.getReceiver().getId(), messageObj.getId()));
        return message;
    }
}