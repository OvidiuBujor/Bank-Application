package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.services.AutheticationService;
import pentastagiu.services.UserService;
import pentastagiu.util.CustomException;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

@RestController
public class AutheticationController {

    @Autowired
    AutheticationService autheticationService;

    @Autowired
    UserService userService;

    @GetMapping("/authentication/{username}/{password}")
    public Authentication login(@PathVariable(value = "username") String username,
                                       @PathVariable(value = "password") String password){
        System.out.println(username);
        System.out.println(password);
        User user = userService.validateUser(username,password);
        Authentication authenticationToBeReturned = new Authentication();
        if (user != null) {
            authenticationToBeReturned.setUser(user);
            authenticationToBeReturned.setToken(autheticationService.createToken());
            authenticationToBeReturned.setCreationTime(LocalDateTime.now());
            autheticationService.createAuthentication(authenticationToBeReturned);
        }
        return authenticationToBeReturned;
    }

    @GetMapping("/authentication/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void getError() throws CustomException {
        throw new CustomException("Authentication error",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public Authentication createAuthentication(@RequestBody Authentication authentication){
        return autheticationService.createAuthentication(authentication);
    }

    @DeleteMapping("/authentication/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathParam(value ="token") String token){
        autheticationService.deleteToken(token);
    }
}
