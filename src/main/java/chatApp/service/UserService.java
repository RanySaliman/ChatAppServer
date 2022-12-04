package chatApp.service;

import chatApp.Entities.User;
import chatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;



    /**
     * Adds a user to the database if it has a unique email
     * @param user - the user's data
     * @return a saved user with it's generated id
     * @throws SQLDataException when the provided email already exists
     */
    public User addUser(User user) throws SQLDataException {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new SQLDataException(String.format("Email %s exists in users table", user.getEmail()));
        }
        return userRepository.save(user);
    }

    public Optional<User> findById(int id) {
        return authService.isAuth(id);
    }
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public Set<User> findByQuery(String query) {
        Set<User> result = new HashSet<User>();
        result.addAll(userRepository.findByFirstNameStartingWith(query));
        result.addAll(userRepository.findByLastNameStartingWith(query));

        return result;
    }

}
