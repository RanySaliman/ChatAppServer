package chatApp.repository;

import chatApp.Entities.CompositeKeyGroupChats;
import chatApp.Entities.GroupChats;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupChatsRepository extends JpaRepository<GroupChats, CompositeKeyGroupChats> {

}

