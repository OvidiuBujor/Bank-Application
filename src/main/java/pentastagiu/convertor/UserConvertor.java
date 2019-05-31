package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.model.User;

/**
 * This class converts an User to
 * an UserDTO that is used for responses of
 * the server
 */
@Component
public class UserConvertor {
    public User convertFromUserDTO(UserDTO userDTO) {
        return new User(userDTO.getUsername());
    }

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getUsername());
    }
}
