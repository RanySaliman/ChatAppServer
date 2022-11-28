package chatApp.service;

import chatApp.repository.GroupMembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMembersService {

    @Autowired
    private GroupMembersRepository groupMembersRepository;

}
