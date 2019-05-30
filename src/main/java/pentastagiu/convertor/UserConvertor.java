package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.model.User;

/**
 * This class converts an User in a UserDTO
 */
@Component
public class UserConvertor {
    public User convertFromUserDTO(UserDTO userDto) {
        return new User(userDto.getUsername(), null);
    }

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getUsername());
    }
}
