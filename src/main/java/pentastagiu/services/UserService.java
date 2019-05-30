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

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        Optional<User> userToReturn = userRepository.findById(id);
        return userToReturn.orElseGet(User::new);
    }

    public User createUser(User user) throws CustomException {
        Optional<User> result = userRepository.findByUsername(user.getUsername());
        if (result.isPresent())
            throw new CustomException("User already registered!", HttpStatus.BAD_REQUEST);
        return userRepository.save(user);
    }

    public User updateUser(User user) throws CustomException{
        Optional<User> resultedUser = userRepository.findById(user.getId());
        if(resultedUser.isPresent()) {
            User userToUpdate = resultedUser.get();
            userToUpdate.setPassword(user.getPassword());
            userRepository.save(userToUpdate);
            return resultedUser.orElseGet(User::new);
        }
        throw new CustomException("User not found.",HttpStatus.NOT_FOUND);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


    boolean validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }

    User getUserByUsername(String username) throws CustomException{
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent())
            return result.get();
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
    }
}
