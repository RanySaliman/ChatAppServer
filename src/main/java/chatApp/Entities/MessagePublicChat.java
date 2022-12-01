package chatApp.Entities;


public class MessagePublicChat {
    private User sender;
    private String receiver;
    private String message;


    public User getSender() {
        return sender;
    }


    public void setSender(User sender) {
        this.sender = sender;
    }



    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getReceiver() {
        return receiver;
    }


    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessagePublicChat{");
        sb.append("sender=").append(sender);
        sb.append(", receiver='").append(receiver).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}