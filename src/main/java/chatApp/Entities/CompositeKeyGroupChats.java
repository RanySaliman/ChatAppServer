package chatApp.Entities;

import javax.persistence.Id;
import java.io.Serializable;

public class CompositeKeyGroupChats implements Serializable {

    @Id
    private int senderUser;
    @Id
    private int group;
    @Id
    private int message;
}
