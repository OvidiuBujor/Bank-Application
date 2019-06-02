package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.exceptions.*;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.UserRepository;

import java.util.Optional;

/**
 * This class contains all logic for the user
 */

@Service
public class UserService {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;

    @Autowired
    public UserService(AuthenticationService authenticationService, UserRepository userRepository){
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        Optional<User> userToReturn = userRepository.findById(id);
        return userToReturn.orElseGet(User::new);
    }

    User getUserByUsername(String username){
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent())
            return result.get();
        throw new UserNotFoundException("User does not exists.");
    }

    public User createUser(User user) {
        Optional<User> result = userRepository.findByUsername(user.getUsername());
        if (result.isPresent())
            throw new UserAlreadyRegisteredException("User already registered. Please use other username.");
        if(user.getUsername() == null || user.getPassword() == null)
            throw new CredentialsNotCorrectException("The credentials are not complete. Please provide username and password.");
        return userRepository.save(user);
    }

    public User updateUserPassword(String token, String password) {
        if (authenticationService.existsByToken(token)){
                Authentication authentication = authenticationService.findByToken(token);
                User userToUpdate = authentication.getUser();
                userToUpdate.setPassword(password);
                userRepository.save(userToUpdate);
                return userToUpdate;
        }
         throw new TokenNotFoundException("Authentication failed. Token not found. ");
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * This method validates the credentials of an
     * user that wants to login.
     */
    boolean validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }


}
