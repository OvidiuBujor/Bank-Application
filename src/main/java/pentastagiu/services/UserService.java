package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.User;
import pentastagiu.repository.UserRepository;

import java.util.Optional;

/**
 * This class contains all logic for the current logged in user:
 * load and dispose current user.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

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
}
