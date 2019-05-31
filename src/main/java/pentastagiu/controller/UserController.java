package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.UserConvertor;
import pentastagiu.convertor.UserDTO;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.services.AuthenticationService;
import pentastagiu.services.PersonService;
import pentastagiu.services.UserService;
import pentastagiu.exceptions.CustomException;

@RestController
public class UserController {

    private UserService userService;

    private UserConvertor userConverter;

    private AuthenticationService authenticationService;

    private PersonService personService;

    @Autowired
    public UserController(UserService userService,
                          UserConvertor userConvertor,
                          AuthenticationService authenticationService,
                          PersonService personService) {
        this.userService = userService;
        this.userConverter = userConvertor;
        this.authenticationService = authenticationService;
        this.personService = personService;
    }

    /**
     * This method returns the user with
     * the id provided
     * @param id of the user to be returned
     * @return the user with the id provided
     */
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable(value = "id") Long id) {
        return userConverter.convertToUserDTO(userService.getUserById(id));
    }

    /**
     * This method saves the User passed as
     * parameter in database.
     * @param user to be saved
     * @return the corresponding UserDTO
     * @throws CustomException in case User already exists
     * in database
     */
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody User user){
        return userConverter.convertToUserDTO(userService.createUser(user));
    }

    /**
     * This method updates the User password
     * @param user contains the new password
     * @return the corresponding UseDTO of the updated user
     */
    @PutMapping("/user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return new ResponseEntity<>(userConverter.convertToUserDTO(updatedUser), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * This method deletes an User
     * from database
     * @param id of the User to be deleted
     */
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/user/{token}")
    public void addUserDetails(@PathVariable(value = "token") String token,
                               @RequestBody Person person){
        if(authenticationService.existsByToken(token)){
            personService.savePersonDetails(person,token);
        }
    }
}