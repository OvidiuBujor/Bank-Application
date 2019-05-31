package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.User;
import pentastagiu.repository.UserRepository;
import pentastagiu.exceptions.CustomException;
import pentastagiu.exceptions.UserAlreadyRegisteredException;
import pentastagiu.exceptions.UserNotFoundException;

import java.util.Optional;

/**
 * This class contains all logic for the user
 */

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    

    /**
     * This method gets the User based on
     * the id sent as parameter.
     * @param id of the returned user
     * @return the user with the id passed as
     * parameter
     */
    public User getUserById(Long id) {
        Optional<User> userToReturn = userRepository.findById(id);
        return userToReturn.orElseGet(User::new);
    }

    /**
     * This method creates a new User
     * @param user the user to be created
     * @return the created User
     * @throws CustomException in case user is
     * already registered.
     */
    public User createUser(User user) {
        Optional<User> result = userRepository.findByUsername(user.getUsername());
        if (result.isPresent())
            throw new UserAlreadyRegisteredException("User already registered!");
        return userRepository.save(user);
    }

    /**
     * This method updates the password of an user.
     * @param user that contains the new information
     * @return the updated user
     * @throws CustomException in case the user to be updated doesn't
     * exists in database.
     */
    public User updateUser(User user) {
        Optional<User> resultedUser = userRepository.findById(user.getId());
        if(resultedUser.isPresent()) {
            User userToUpdate = resultedUser.get();
            userToUpdate.setPassword(user.getPassword());
            userRepository.save(userToUpdate);
            return resultedUser.orElseGet(User::new);
        }
        throw new UserNotFoundException("User not found.");
    }

    /**
     * This method deletes an user based on the id
     * provided as parameter.
     * @param id of the user to be deleted
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * This method validates the credentials of an
     * user that wants to login
     * @param username of the user that wants to login
     * @param password of the user that wants to login
     * @return true if user credentials are correct,
     * false otherwise
     */
    boolean validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }

    /**
     * This method returns an User
     * based on the username passed as parameter.
     * @param username of the user to be found
     * @return the corresponding User
     * @throws CustomException in case User doesn't
     * exists in database.
     */
    User getUserByUsername(String username){
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent())
            return result.get();
        throw new UserNotFoundException("User does not exists.");
    }
}
