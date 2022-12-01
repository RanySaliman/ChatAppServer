package chatApp.repository;

import chatApp.Entities.CompositeKeyGroupChats;
import chatApp.Entities.CompositeKeyPrivateChat;
import chatApp.Entities.GroupChats;
import chatApp.Entities.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupChatsRepository extends JpaRepository<GroupChats, CompositeKeyGroupChats> {

}

