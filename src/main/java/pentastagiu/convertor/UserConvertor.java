package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.model.User;

@Component
public class UserConvertor {
    public User convertFromUserDTO(UserDTO userDto) {
        return new User(userDto.getUsername(), null,null,null);
    }

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getUsername());
    }
}
