package chatApp.service;

import chatApp.Entities.GroupMembers;
import chatApp.Entities.PublicGroups;
import chatApp.repository.GroupMembersRepository;
import chatApp.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupMembersService {

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private GroupRepository groupRepository;


    public void joinToGroup(int userId, String groupName) {
        PublicGroups groupChatByName = findGroupChatByName(groupName);
        groupMembersRepository.save(new GroupMembers(groupChatByName.getId(), userId));
    }

    public PublicGroups findGroupChatByName(String groupName) {
        Optional<PublicGroups> group = groupRepository.findByGroupName(groupName);
        if(group.isEmpty()) {
            return groupRepository.save(new PublicGroups(0, groupName));
        }
        return group.get();
    }

}
