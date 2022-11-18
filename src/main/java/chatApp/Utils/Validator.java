package chatApp.Utils;


import chatApp.Entities.User;
import chatApp.Utils.UserFields;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class Validator {

    private static Pattern pattern;
    private static final Map<String, String> errorsMap = new HashMap<String, String>();


    public static Optional<Map<String, String>> validateRegister(User user) {

        if(! isValidName(user.getFirstName())) {
            errorsMap.put("first name", getNameConstraints());
        }
        if(! isValidName(user.getLastName())) {
            errorsMap.put("last name", getNameConstraints());
        }
        if(! isValidEmail(user.getEmail())) {
            errorsMap.put("email", "invalid email");
        }
        if(! isValidPassword(user.getPassword())) {
            errorsMap.put("password", getPasswordConstraints());
        }
        if(errorsMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(errorsMap);
    }


    public static Optional<Map<String, String>> validateLogin(User user) {

        if(! isValidEmail(user.getEmail())) {
            errorsMap.put("email", "invalid email");
        }
        if(! isValidPassword(user.getPassword())) {
            errorsMap.put("password", getPasswordConstraints());
        }
        if(errorsMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(errorsMap);
    }


    public static Optional<Map<String, String>> validateFields(Map<String, String> fields) {

        fields.forEach((key, value) -> {

            switch(key) {
                case "firstName":
                    if(! isValidName(value)) {
                        errorsMap.put("firstName", getNameConstraints());
                    }
                case "lastName":
                    if(! isValidName(value)) {
                        errorsMap.put("lastName", getNameConstraints());
                    }
                case "email":
                    if(! isValidEmail(value)) {
                        errorsMap.put("email", "invalid email");
                    }
                case "password":
                    if(! isValidPassword(value)) {
                        errorsMap.put("password", getPasswordConstraints());
                    }
            }
        });

        if(errorsMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(errorsMap);
    }


    public static boolean isValidName(String name) {
        String regex = "[A-Za-z]\\w{1,29}$";
        pattern = Pattern.compile(regex);
        return name != null && pattern.matcher(name).matches();
    }


    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }


    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])" +
                "(?=.*[a-z])(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$).{8,20}$";
        pattern = Pattern.compile(regex);
        return password != null && pattern.matcher(password).matches();
    }


    public static String getPasswordConstraints() {
        return "password must contains at least 8 characters and at most 20 characters." +
                "At least one digit." +
                "At least one upper case alphabet." +
                "At least one lower case alphabet." +
                "At least one special character which includes !@#$%&*()-+=^." +
                "no white space.";
    }


    public static String getNameConstraints() {
        return "name must contains at least 2 alphabetical letters";
    }

}
