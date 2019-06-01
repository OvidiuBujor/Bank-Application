package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.PersonConvertor;
import pentastagiu.convertor.PersonDTO;
import pentastagiu.convertor.UserConvertor;
import pentastagiu.convertor.UserDTO;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.services.AuthenticationService;
import pentastagiu.services.PersonService;
import pentastagiu.services.UserService;

@RestController
public class UserController {

    private UserService userService;

    private UserConvertor userConverter;

    private AuthenticationService authenticationService;

    private PersonService personService;

    private PersonConvertor personConvertor;

    @Autowired
    public UserController(UserService userService,
                          UserConvertor userConvertor,
                          @Lazy AuthenticationService authenticationService,
                          PersonService personService,
                          PersonConvertor personConvertor) {
        this.userService = userService;
        this.userConverter = userConvertor;
        this.authenticationService = authenticationService;
        this.personService = personService;
        this.personConvertor = personConvertor;
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
     */
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody User user){
        return userConverter.convertToUserDTO(userService.createUser(user));
    }

    /**
     * This method updates the password for a logged in user
     * @param token used to validate the user
     * @param password the new password
     * @return UserDTO corresponding object
     */
    @PutMapping("/user/update/{token}/{password}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "token") String token,
                                              @PathVariable(value = "password") String password) {
        User updatedUser = userService.updateUser(token, password);
        if (updatedUser != null) {
            return new ResponseEntity<>(userConverter.convertToUserDTO(updatedUser), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * This method adds user personal details
     * like address, email, first and last names.
     * @param token used to validate user
     * @param person the details to be added
     * @return the corresponding PersonDTO object
     */
    @PutMapping("/user/addDetails/{token}")
    public ResponseEntity<PersonDTO> addUserDetails(@PathVariable(value = "token") String token,
                                                    @RequestBody Person person){
        if(authenticationService.existsByToken(token)){
            return new ResponseEntity<>(personConvertor.convertToPersonDTO(personService.savePersonDetails(person,token)),
                    HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * This method deletes an User from database
     * @param id of the User to be deleted
     */
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }
}