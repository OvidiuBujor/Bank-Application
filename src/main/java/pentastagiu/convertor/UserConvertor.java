package pentastagiu.convertor;

import pentastagiu.model.User;

public class UserConvertor {
    public User convertFromUserDTO(UserDTO userDto) {
        return new User(userDto.getUsername(), null,null,null);
    }

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getUsername());
    }
}
