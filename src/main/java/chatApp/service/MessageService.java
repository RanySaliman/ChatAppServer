package chatApp.service;

import chatApp.Entities.GroupMembers;
import chatApp.Entities.Message;
import chatApp.Entities.PrivateChat;
import chatApp.Entities.User;
import chatApp.Response.ResponseHandler;
import chatApp.repository.GroupMembersRepository;
import chatApp.repository.MessageRepository;
import chatApp.repository.PrivateChatRepository;
import chatApp.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    private final Map<String, Object> responseMap = new HashMap<>();


    public Message create(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setDateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }


    public PrivateChat send(PrivateChat chat) {
        return privateChatRepository.save(chat);
    }


    public List<PrivateChat> getMessages(int senderUser, int receiverUser) {
        responseMap.clear();
        List<Map<String, Object>> messages = new ArrayList<>();


        List<PrivateChat> privateChats = privateChatRepository.findBySenderUserAndReceiverUser(senderUser, receiverUser);
        privateChats.addAll(privateChatRepository.findBySenderUserAndReceiverUser(receiverUser, senderUser));

        List<PrivateChat> sortedChats = privateChats.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());
        return sortedChats;
    }


    public ResponseEntity<Object> getPrivateMessages(int senderUser, int receiverUser) {

        List<Map<String, Object>> messages = new ArrayList<>();
        List<PrivateChat> privateChats = getMessages(senderUser, receiverUser);

        privateChats.forEach(p -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()));
            formattedMap.put("receiver", userRepository.getUserById(p.getReceiverUser()));
            formattedMap.put("message", messageRepository.findById(p.getMessage()));
            messages.add(formattedMap);
        });

        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }

    public ResponseEntity<Object> exportMessages(int senderUser, int receiverUser) {

        List<Map<String, Object>> messages = new ArrayList<>();
        List<PrivateChat> privateChats = getMessages(senderUser, receiverUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        privateChats.forEach(p -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("date", messageRepository.findById(p.getMessage()).getDateTime().format(formatter));
            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()).map(User::getFullName));
            formattedMap.put("message", messageRepository.findById(p.getMessage()).getContent());

            messages.add(formattedMap);
        });

        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }

    public ResponseEntity<Object> exportPublicMessages(int groupId) {

        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());
        List<PrivateChat> groupMessages = new ArrayList<>();
        List<Map<String, Object>> messages = new ArrayList<>();


        membersIds.forEach(m -> {
            groupMessages.addAll(getMessages(m, groupId));
        });

        List<PrivateChat> sortedChats = groupMessages.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sortedChats.forEach(p -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("date", messageRepository.findById(p.getMessage()).getDateTime().format(formatter));
            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()).map(User::getFullName));
            formattedMap.put("message", messageRepository.findById(p.getMessage()).getContent());

            messages.add(formattedMap);
        });


        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }



    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }


    public int comparePrivateChat(PrivateChat p1, PrivateChat p2) {
        return messageRepository.findById(p1.getMessage()).compareTo(messageRepository.findById(p2.getMessage()));
    }


    public ResponseEntity<Object> getPublicMessages(int groupId) {
        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());
        List<PrivateChat> groupMessages = new ArrayList<>();
        List<Map<String, Object>> messages = new ArrayList<>();


        membersIds.forEach(m -> {
            groupMessages.addAll(getMessages(m, groupId));
        });

        List<PrivateChat> sortedChats = groupMessages.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());

        sortedChats.forEach(p -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()));
            formattedMap.put("receiver", userRepository.getUserById(p.getReceiverUser()));
            formattedMap.put("message", messageRepository.findById(p.getMessage()));
            messages.add(formattedMap);
        });


        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }
}
