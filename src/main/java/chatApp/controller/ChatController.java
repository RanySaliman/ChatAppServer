package chatApp.controller;

import chatApp.Entities.*;
import chatApp.service.GroupMembersService;
import chatApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupMembersService groupMembersService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessagePublicChat receiveMessage(@Payload MessagePublicChat message){

        if(message.getStatus().equals(Status.JOIN)) {
            groupMembersService.joinToGroup(message.getSender().getId(), message.getReceiver());
        } else {
            Message messageObj = messageService.create(message.getMessage());
            Optional<PublicGroups> groupChatByName = messageService.findGroupChatByName(message.getReceiver());
            messageService.saveGroupChat(new GroupChats(message.getSender().getId(), groupChatByName.get().getId(), messageObj.getId()));
        }

        return message;
    }

    @MessageMapping("/private-message")
    public MessageChat recMessage(@Payload MessageChat message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver().getEmail(),"/private",message);
        Message messageObj = messageService.create(message.getMessage());
        messageService.savePrivateChat(new PrivateChat(message.getSender().getId(), message.getReceiver().getId(), messageObj.getId()));
        return message;
    }
}