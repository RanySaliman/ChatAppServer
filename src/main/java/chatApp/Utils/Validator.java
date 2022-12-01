package chatApp.Utils;


import chatApp.Entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class Validator {

    private static final Map<String, String> errorsMap = new HashMap<String, String>();

    /**
     * checking that all the registration input is valid
     * @param user
     * @return in case of error the user will get an error message
     */
    public static Optional<Map<String, String>> validateRegister(User user) {
        errorsMap.clear();

        if(! isValidName(user.getFirstName())) {
            errorsMap.put("first name", "first name" + getNameConstraints());
        }
        if(! isValidName(user.getLastName())) {
            errorsMap.put("last name", "last name" + getNameConstraints());
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

    /**
     * checking that all the login input is valid
     * @param user
     * @return in case of error the user will get an error message
     */
    public static Optional<Map<String, String>> validateLogin(User user) {
        errorsMap.clear();

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

    /**
     * validating the fields of register and login 
     * @param fields
     * @return will break if there is an error in the input, return true if all input is correct
     */
    public static Optional<Map<String, String>> validateFields(Map<String, String> fields) {
        errorsMap.clear();

        fields.forEach((key, value) -> {
            switch(key) {
                case "firstName":
                    if(! isValidName(value)) {
                        errorsMap.put("firstName", "first name" + getNameConstraints());
                    }
                    break;
                case "lastName":
                    if(! isValidName(value)) {
                        errorsMap.put("lastName", "last name" + getNameConstraints());
                    }
                    break;

                case "email":
                    if(! isValidEmail(value)) {
                        errorsMap.put("email", "invalid email");
                    }
                    break;

                case "nikeName":
                    if(! isValidNickName(value)) {
                        errorsMap.put("nikeName", getNikeNameConstraints());
                    }
                    break;
            }
        });

        if(errorsMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(errorsMap);
    }


    //<editor-fold desc="String validation of input ">
    public static boolean isValidName(String name) {
        String regex1 = "^[a-zA-Z]{2,20}$";
        Pattern pattern = Pattern.compile(regex1);

        return name != null && pattern.matcher(name).matches();
    }

    public static boolean isValidNickName(String name) {
        String regex1 = "^[a-zA-Z0-9_]{2,20}$";
        Pattern pattern = Pattern.compile(regex1);

        return name != null && pattern.matcher(name).matches();
    }


    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }


    public static boolean isValidPassword(String password) {
        String regex = "^.{5,32}$";
        Pattern pattern = Pattern.compile(regex);
        return password != null && pattern.matcher(password).matches();
    }
    //</editor-fold>


    //<editor-fold desc="Getters of Constraints">
    public static String getPasswordConstraints() {
        return "password length must be at least 6 characters long";
    }


    public static String getNameConstraints() {
        return " must contains at least 2 alphabetical letters and contain only characters";
    }

    public static String getNikeNameConstraints() {
        return " must contains at least 2 alphabetical letters and can contain only characters, numbers and underscore";
    }
    //</editor-fold>

}
