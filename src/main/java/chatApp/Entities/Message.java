package chatApp.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String content;

    private LocalDateTime dateTime;

    //<editor-fold desc="Getters and Setters">
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    //</editor-fold>

    public int compareTo(Message other) {
        return this.getDateTime().compareTo(other.getDateTime());
    }
}
