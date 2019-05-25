package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.UserConvertor;
import pentastagiu.convertor.UserDTO;
import pentastagiu.model.User;
import pentastagiu.services.UserService;
import pentastagiu.util.CustomException;

import javax.websocket.server.PathParam;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserConvertor userConverter;

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathParam(value = "id") Long id) {
        return userConverter.convertToUserDTO(userService.getUserById(id));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody User user) {
        return userConverter.convertToUserDTO(userService.createUser(user));
    }

    @PutMapping("/user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user) {

        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return new ResponseEntity<>(userConverter.convertToUserDTO(userService.updateUser(updatedUser)),
                    HttpStatus.OK);

        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/user/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserDTO getError() throws CustomException {
        throw new CustomException("User not found", HttpStatus.NOT_FOUND);
    }
}
