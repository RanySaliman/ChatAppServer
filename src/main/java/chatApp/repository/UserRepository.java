package chatApp.repository;

import chatApp.Entities.Message;
import chatApp.Entities.Topic;
import chatApp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, UUID> {
        Optional<User> findByEmail(String email);

//        List<User> findByTopicsName(String name);

        //List<Message> findByTopicsName(String name);

}
