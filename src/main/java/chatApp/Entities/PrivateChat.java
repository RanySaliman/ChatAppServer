package chatApp.Entities;

import javax.persistence.*;

@Entity
@IdClass(CompositeKeyPrivateChat.class)
public class PrivateChat {
    @Id
    private int senderUser;
    @Id
    private int receiverUser;
    @Id
    private int message;


    //<editor-fold desc="Getters and Setters">
    public int getSenderUser() {
        return senderUser;
    }


    public void setSenderUser(int senderUser) {
        this.senderUser = senderUser;
    }


    public int getReceiverUser() {
        return receiverUser;
    }


    public void setReceiverUser(int receiverUser) {
        this.receiverUser = receiverUser;
    }


    public int getMessage() {
        return message;
    }


    public void setMessage(int message) {
        this.message = message;
    }
    //</editor-fold>
}


