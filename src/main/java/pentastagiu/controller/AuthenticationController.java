package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Authentication;
import pentastagiu.services.AuthenticationService;
import pentastagiu.util.CustomException;

@RestController
public class AuthenticationController {


    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @GetMapping("/authentication/{username}/{password}")
    public ResponseEntity<Authentication> login(@PathVariable(value = "username") String username,
                                               @PathVariable(value = "password") String password) throws CustomException {
        return new ResponseEntity<>(authenticationService.login(username,password),HttpStatus.OK);
    }

    @GetMapping("/authentication/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void getError() throws CustomException {
        throw new CustomException("Authentication error",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Authentication> createAuthentication(@RequestBody Authentication authentication){
        return new ResponseEntity<>(authenticationService.saveAuthentication(authentication),HttpStatus.OK);
    }

    @DeleteMapping("/authentication/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable(value ="token") String token) throws CustomException{
        authenticationService.deleteToken(token);
    }
}
