package chatApp.service;

import chatApp.Entities.*;
import chatApp.Response.ResponseHandler;
import chatApp.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GroupChatsRepository groupChatsRepository;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private GroupRepository groupRepository;

    private static Logger logger = LogManager.getLogger(ChatService.class.getName());


    public PrivateChat savePrivateChat(PrivateChat chat) {
        logger.info("saving private message in data base");
        return privateChatRepository.save(chat);
    }

    public GroupChats saveGroupChat(GroupChats chat) {
        logger.info("saving group message in data base");
        return groupChatsRepository.save(chat);
    }

    public ResponseEntity<Object> getPrivateHistoryMessages(int senderUser, int receiverUser) {

        logger.info("getting private history messages between user id " + senderUser + " and user id " + receiverUser);
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

        logger.info("getting group history messages from " + groupName);

        Optional<PublicGroups> byGroupName = groupRepository.findByGroupName(groupName);
        int groupId = byGroupName.get().getId();

        List<Map<String, Object>> messages = new ArrayList<>();
        List<GroupChats> groupMessages = new ArrayList<>();
        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());

        for (int id : membersIds) {
            List<GroupChats> messagesToGroup = groupChatsRepository.findBySenderUserAndGroupId(id, groupId);
            groupMessages.addAll(messagesToGroup);
        }

        if (groupMessages.size() > 1) {
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

    public List<User> getGroupMembers(String groupName) {

        logger.info("getting group members list of " + groupName);
        Optional<PublicGroups> byGroupName = groupRepository.findByGroupName(groupName);
        int groupId = byGroupName.get().getId();
        List<GroupMembers> members = groupMembersRepository.findByGroupId(groupId);

        if (members.size() > 1) {
            members = members.stream().sorted(this::compareGroupMembers).collect(Collectors.toList());
        }

        List<User> membersAsUsers = new ArrayList<>();

        members.forEach(m -> {
            membersAsUsers.add(userRepository.getUserById(m.getUserId()).get());
        });
        return membersAsUsers;
    }

    public String exportMessages(int senderUser, int receiverUser) {

        logger.info("exporting private messages between user id " + senderUser + " and user id " + receiverUser);
        List<Map<String, Object>> messages = new ArrayList<>();
        List<PrivateChat> privateChats = privateChatRepository.findBySenderUserAndReceiverUser(senderUser, receiverUser);
        privateChats.addAll(privateChatRepository.findBySenderUserAndReceiverUser(receiverUser, senderUser));
        List<PrivateChat> sortedChats = privateChats.stream().sorted(this::comparePrivateChat).collect(Collectors.toList());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String exportedMsg = "";
        for (PrivateChat p : sortedChats) {

            exportedMsg += "[" + messageRepository.findById(p.getMessage()).getDateTime().format(formatter) + "]";
            exportedMsg += " " + userRepository.getUserById(p.getSenderUser()).get().getFullName() + ":";
            exportedMsg += " " + messageRepository.findById(p.getMessage()).getContent() + " \n";
        }

        return exportedMsg; // need to remove response handlers
    }

    public String exportPublicMessages(String groupName) {

        logger.info("exporting group messages from " + groupName);
        Optional<PublicGroups> byGroupName = groupRepository.findByGroupName(groupName);
        int groupId = byGroupName.get().getId();

        List<GroupMembers> members = groupMembersRepository.findByGroupId(groupId);
        List<Integer> membersIds = groupMembersRepository.findByGroupId(groupId).stream().map(GroupMembers::getUserId).collect(Collectors.toList());

        String exportedGroupChats = "";
        List<GroupChats> gc = new ArrayList<>();
        for (int id : membersIds) {

            gc.addAll(groupChatsRepository.findBySenderUserAndGroupId(id, groupId));

        }

        List<GroupChats> sortedGroupChats = gc.stream().sorted(this::compareGroupChat).collect(Collectors.toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (GroupChats groupChat : sortedGroupChats) {

            exportedGroupChats += "[" + messageRepository.findById(groupChat.getMessage()).getDateTime().format(formatter) + "]";
            exportedGroupChats += " " + userRepository.getUserById(groupChat.getSenderUser()).get().getFullName() + ":";
            exportedGroupChats += " " + messageRepository.findById(groupChat.getMessage()).getContent() + " \n";
        }

        return exportedGroupChats; // need to remove response handlers
    }

    public List<User> getPrivateChats(int id) {
        List<Integer> privateChats = privateChatRepository.findPrivateChats(id);
        List<User> users = new ArrayList<>();

        privateChats.forEach(c -> {
            users.add(userRepository.findById(c).get());
        });

        return users;
    }

    public int comparePrivateChat(PrivateChat p1, PrivateChat p2) {
        logger.info("comparing and sorting private messages by date");
        return messageRepository.findById(p1.getMessage()).compareTo(messageRepository.findById(p2.getMessage()));
    }

    public int compareGroupChat(GroupChats g1, GroupChats g2) {
        logger.info("comparing and sorting group messages by date");
        return messageRepository.findById(g1.getMessage()).compareTo(messageRepository.findById(g2.getMessage()));
    }

    public int compareGroupMembers(GroupMembers m1, GroupMembers m2) {

        logger.info("sorting group members by roles");
        Optional<User> user1 = userRepository.getUserById(m1.getUserId());
        Optional<User> user2 = userRepository.getUserById(m2.getUserId());
        return user2.get().getRole().compareTo(user1.get().getRole());
    }

    public Optional<PublicGroups> findGroupChatByName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }

}
