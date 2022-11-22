package chatApp.mailActivation;

public class GMailer {
    private void sendMail(String chatApp, String message) {
        //send email to ourselves


    }

    public static void main(String[] args) {
        new GMailer().sendMail("ChatApp", "this is activation token please click it in order to login to the chatApp application");
    }


}
