package chatApp.Entities;

import javax.persistence.*;

@Entity
@Table(name = "group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String groupName;


    public Group() {
    }


    public Group(int id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getGroupName() {
        return groupName;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Group{");
        sb.append("id=").append(id);
        sb.append(", groupName='").append(groupName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
