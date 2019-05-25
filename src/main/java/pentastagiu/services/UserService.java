package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.model.User;
import pentastagiu.repository.UserRepository;
import pentastagiu.util.CustomException;

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

    public User createUser(User user) throws CustomException {
        User result;
        result = userRepository.findByUsername(user.getUsername());
        if (result == null)
            userRepository.save(user);
        else
            throw new CustomException("User already registered!", HttpStatus.BAD_REQUEST);
        return user;
    }

    public User updateUser(String password, Long id) {
        userRepository.updateUser(password,id);
        Optional<User> userToReturn = userRepository.findById(id);
        return userToReturn.orElseGet(User::new);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }
}
