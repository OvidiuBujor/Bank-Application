package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.User;
import pentastagiu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This class contains all logic for the current logged in user:
 * load and dispose current user.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        Optional<User> userToReturn = userRepository.findById(id);
        return userToReturn.orElseGet(User::new);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }
    public void loadUsers(){
        createUser(new User("Ovidiu","123", LocalDateTime.now(),LocalDateTime.now()));
        createUser(new User("Andrei","22", LocalDateTime.now(),LocalDateTime.now()));
        createUser(new User("Vasile","11", LocalDateTime.now(),LocalDateTime.now()));
    }
}
