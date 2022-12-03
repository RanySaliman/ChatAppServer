package chatApp.service;

import chatApp.Entities.*;
import chatApp.Response.ResponseHandler;
import chatApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    private GroupChatsRepository groupChatsRepository;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final Map<String, Object> responseMap = new HashMap<>();


    public Message create(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setDateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }


    public PrivateChat savePrivateChat(PrivateChat chat) {
        return privateChatRepository.save(chat);
    }


    public GroupChats saveGroupChat(GroupChats chat) {
        return groupChatsRepository.save(chat);
    }


    public List<User> getPrivateChats(int id) {
        List<Integer> privateChats = privateChatRepository.findPrivateChats(id);
        List<User> users = new ArrayList<>();

        privateChats.forEach(c -> {
            users.add(userRepository.findById(c).get());
        });

        return users;
    }


    public ResponseEntity<Object> getPrivateHistoryMessages(int senderUser, int receiverUser) {

        List<Map<String, Object>> messages = new ArrayList<>();
        List<PrivateChat> privateChats = privateChatRepository.findBySenderUserAndReceiverUser(senderUser, receiverUser);
        privateChats.addAll(privateChatRepository.findBySenderUserAndReceiverUser(receiverUser, senderUser));
        List<PrivateChat> sortedChats = privateChats.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());

        sortedChats.forEach(p -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()));
            formattedMap.put("receiver", userRepository.getUserById(p.getReceiverUser()));
            formattedMap.put("message", messageRepository.findById(p.getMessage()).getContent());
            messages.add(formattedMap);
        });

        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }


    public ResponseEntity<Object> getGroupHistoryMessages(String groupName) {

        Optional<PublicGroups> byGroupName = groupRepository.findByGroupName(groupName);
        int groupId = byGroupName.get().getId();

        List<Map<String, Object>> messages = new ArrayList<>();
        List<GroupChats> groupMessages = new ArrayList<>();
        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());

        for(int id : membersIds) {
            List<GroupChats> messagesToGroup = groupChatsRepository.findBySenderUserAndGroupId(id, groupId);
            groupMessages.addAll(messagesToGroup);
        }

        if(groupMessages.size() > 1) {
            groupMessages = groupMessages.stream().sorted(this::compareGroupChat).collect(Collectors.toList());
        }

        groupMessages.forEach(m -> {
            Map<String, Object> formattedMap = new HashMap<>();
            formattedMap.put("sender", userRepository.getUserById(m.getSenderUser()));
            formattedMap.put("receiver", groupRepository.findById(groupId).get().getGroupName());
            formattedMap.put("message", messageRepository.findById(m.getMessage()).getContent());
            messages.add(formattedMap);
        });

        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    }

    //
    //    public ResponseEntity<Object> exportMessages(int senderUser, int receiverUser) {
    //
    //        List<Map<String, Object>> messages = new ArrayList<>();
    //        List<PrivateChat> privateChats = getMessages(senderUser, receiverUser);
    //
    //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //
    //        privateChats.forEach(p -> {
    //            Map<String, Object> formattedMap = new HashMap<>();
    //            formattedMap.put("date", messageRepository.findById(p.getMessage()).getDateTime().format(formatter));
    //            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()).map(User::getFullName));
    //            formattedMap.put("message", messageRepository.findById(p.getMessage()).getContent());
    //
    //            messages.add(formattedMap);
    //        });
    //
    //        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    //    }
    //
    //    public ResponseEntity<Object> exportPublicMessages(int groupId) {
    //
    //        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());
    //        List<PrivateChat> groupMessages = new ArrayList<>();
    //        List<Map<String, Object>> messages = new ArrayList<>();
    //
    //
    //        membersIds.forEach(m -> {
    //            groupMessages.addAll(getMessages(m, groupId));
    //        });
    //
    //        List<PrivateChat> sortedChats = groupMessages.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());
    //
    //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //
    //        sortedChats.forEach(p -> {
    //            Map<String, Object> formattedMap = new HashMap<>();
    //            formattedMap.put("date", messageRepository.findById(p.getMessage()).getDateTime().format(formatter));
    //            formattedMap.put("sender", userRepository.getUserById(p.getSenderUser()).map(User::getFullName));
    //            formattedMap.put("message", messageRepository.findById(p.getMessage()).getContent());
    //
    //            messages.add(formattedMap);
    //        });
    //
    //
    //        return ResponseHandler.generateResponse(true, HttpStatus.OK, messages);
    //    }


    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }


    public int comparePrivateChat(PrivateChat p1, PrivateChat p2) {
        return messageRepository.findById(p1.getMessage()).compareTo(messageRepository.findById(p2.getMessage()));
    }


    public int compareGroupChat(GroupChats g1, GroupChats g2) {
        return messageRepository.findById(g1.getMessage()).compareTo(messageRepository.findById(g2.getMessage()));
    }


    public Optional<PublicGroups> findGroupChatByName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }
}
