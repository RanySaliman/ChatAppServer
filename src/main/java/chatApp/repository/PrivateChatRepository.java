package chatApp.repository;

import chatApp.Entities.CompositeKeyPrivateChat;
import chatApp.Entities.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, CompositeKeyPrivateChat> {

    List<PrivateChat> findBySenderUserAndReceiverUser(int senderUser, int receiverUser);
}
//
//    SELECT m.content, m.date_time FROM chatapp.message as m ,chatapp.private_chat
//        WHERE chatapp.private_chat.sender_user = 112
//        AND chatapp.private_chat.receiver_user = 120
//        OR chatapp.private_chat.sender_user = 120
//        AND chatapp.private_chat.receiver_user = 112
//        ORDER BY m.date_time ASC;
