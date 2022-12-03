package chatApp.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(CompositeKeyGroupMembers.class)
public class GroupMembers {

    @Id
    private int groupId;
    @Id
    private int userId;


    public GroupMembers() {
    }


    public GroupMembers(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }


    public int getGroupId() {
        return groupId;
    }


    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }
}
