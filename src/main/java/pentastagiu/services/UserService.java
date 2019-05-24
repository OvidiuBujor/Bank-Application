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

    public User getUserById(Integer id) {
        Optional<User> userToReturn = userRepository.findById(id);
        if (userToReturn.isPresent())
            return userToReturn.get();
        else
            return new User();
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
}
