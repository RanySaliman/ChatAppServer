package chatApp.service;

import chatApp.Entities.GroupMembers;
import chatApp.Entities.PublicGroups;
import chatApp.repository.GroupMembersRepository;
import chatApp.repository.GroupRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupMembersService {

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private GroupRepository groupRepository;

    private static Logger logger = LogManager.getLogger(GroupMembersService.class.getName());

    public void joinToGroup(int userId, String groupName) {
        PublicGroups groupChatByName = findGroupChatByName(groupName);
        groupMembersRepository.save(new GroupMembers(groupChatByName.getId(), userId));
        logger.info("user with id " + userId + " joined the group " + groupName);
    }

    public PublicGroups findGroupChatByName(String groupName) {
        Optional<PublicGroups> group = groupRepository.findByGroupName(groupName);
        logger.info("searching group " + groupName);
        if(group.isEmpty()) {
            return groupRepository.save(new PublicGroups(0, groupName));
        }
        return group.get();
    }

}
