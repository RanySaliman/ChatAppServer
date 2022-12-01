package chatApp.Entities;

import javax.persistence.*;

@Entity
@IdClass(CompositeKeyGroupChats.class)
public class GroupChats {
    @Id
    private int senderUser;
    @Id
    private int groupId;
    @Id
    private int message;


    public GroupChats() {
    }


    public GroupChats(int senderUser, int groupId, int message) {
        this.senderUser = senderUser;
        this.groupId = groupId;
        this.message = message;
    }


    public int getSenderUser() {
        return senderUser;
    }


    public void setSenderUser(int senderUser) {
        this.senderUser = senderUser;
    }


    public int getGroupId() {
        return groupId;
    }


    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    public int getMessage() {
        return message;
    }


    public void setMessage(int message) {
        this.message = message;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupChats{");
        sb.append("senderUser=").append(senderUser);
        sb.append(", groupId=").append(groupId);
        sb.append(", message=").append(message);
        sb.append('}');
        return sb.toString();
    }
}


