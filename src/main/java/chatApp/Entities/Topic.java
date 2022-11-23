package chatApp.Entities;

import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "topic_messages")
public class Topic {

    @Id
    @Column(name="ROWID")
    private String name;

    @OneToMany
    private List<Message> messageList;



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<Message> getMessageList() {
        return messageList;
    }


    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
