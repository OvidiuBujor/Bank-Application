package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.services.AutheticationService;
import pentastagiu.services.UserService;
import pentastagiu.util.CustomException;

import java.util.Optional;

@RestController
public class AutheticationController {

    @Autowired
    AutheticationService autheticationService;

    @Autowired
    UserService userService;


    @GetMapping("/authentication/{username}/{password}")
    public ResponseEntity<Authentication> login(@PathVariable(value = "username") String username,
                                               @PathVariable(value = "password") String password) throws CustomException {
        Optional<User> user = userService.validateUser(username,password);
        if (user.isPresent()) {
            Optional<Authentication> authenticationToBeReturned = autheticationService.findByUser(user.get());

            if (authenticationToBeReturned.isPresent()) {
                throw new CustomException("User already logged in!", HttpStatus.ALREADY_REPORTED);
            }else{
                Authentication authentication = new Authentication();
                authentication.setUser(user.get());
                authentication.setToken(autheticationService.createToken());
                autheticationService.createAuthentication(authentication);
                return new ResponseEntity<>(authentication,HttpStatus.OK);
            }
        }else
            throw new CustomException("Credentials are not correct!",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/authentication/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void getError() throws CustomException {
        throw new CustomException("Authentication error",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Authentication> createAuthentication(@RequestBody Authentication authentication){
        return new ResponseEntity<>(autheticationService.createAuthentication(authentication),HttpStatus.OK);
    }

    @DeleteMapping("/authentication/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable(value ="token") String token) throws CustomException{
        autheticationService.deleteToken(token);
    }
}
