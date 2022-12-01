package chatApp.repository;

import chatApp.Entities.CompositeKeyGroupChats;
import chatApp.Entities.Group;
import chatApp.Entities.GroupChats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByGroupName(String groupName);
}

