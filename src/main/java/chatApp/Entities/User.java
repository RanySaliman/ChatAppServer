package chatApp.Entities;

import chatApp.Utils.Role;
import chatApp.Utils.Status;

//import javax.management.relation.Role;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean isEnabled;
    private String photoURL;
    private String nikeName;
    private Date dateOfBirth;
    private String bio;
    private Role role = Role.GUEST;
    private Status status = Status.OFFLINE;
    private boolean muted = false;

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public boolean getEnabled() {
        return isEnabled;
    }


    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    public String getPhotoURL() {
        return photoURL;
    }


    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    public String getNikeName() {
        return nikeName;
    }


    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }


    public Date getDateOfBirth() {
        return dateOfBirth;
    }


    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getBio() {
        return bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(! (o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", isEnabled=").append(isEnabled);
        sb.append(", photoURL='").append(photoURL).append('\'');
        sb.append(", nikeName='").append(nikeName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", bio='").append(bio).append('\'');
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append(", muted=").append(muted);
        sb.append('}');
        return sb.toString();
    }
}
