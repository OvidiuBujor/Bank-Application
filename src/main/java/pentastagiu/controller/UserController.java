package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.PersonConvertor;
import pentastagiu.DTOs.PersonDTO;
import pentastagiu.convertor.UserConvertor;
import pentastagiu.DTOs.UserDTO;
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

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable(value = "id") Long id) {
        return userConverter.convertToUserDTO(userService.getUserById(id));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody User user){
        return userConverter.convertToUserDTO(userService.createUser(user));
    }

    @PutMapping("/user/update/{token}/{password}")
    public ResponseEntity<UserDTO> updateUserPassword(@PathVariable(value = "token") String token,
                                                      @PathVariable(value = "password") String password) {
        User updatedUser = userService.updateUserPassword(token, password);
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
     * @return the corresponding PersonDTO
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


    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }
}