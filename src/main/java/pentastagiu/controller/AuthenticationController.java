package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.AuthenticationConvertor;
import pentastagiu.convertor.AuthenticationDTO;
import pentastagiu.services.AuthenticationService;
import pentastagiu.exceptions.CustomException;

@RestController
public class AuthenticationController {


    private AuthenticationService authenticationService;

    private AuthenticationConvertor authenticationConvertor;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    AuthenticationConvertor authenticationConvertor){
        this.authenticationService = authenticationService;
        this.authenticationConvertor =  authenticationConvertor;
    }

    /**
     * This method generates an authentication
     * in case credentials entered are correct,
     * otherwise throws an exception.
     * @param username to be checked
     * @param password to be checked
     * @return an authentication if credentials are correct
     * @throws CustomException in case credentials are not correct or
     * the user with these credentials is already logged in
     */
    @GetMapping("/authentication/{username}/{password}")
    public ResponseEntity<AuthenticationDTO> login(@PathVariable(value = "username") String username,
                                                   @PathVariable(value = "password") String password) {
        return new ResponseEntity<>(authenticationConvertor.convertToAuthenticationDTO(authenticationService.login(username,password)),
                HttpStatus.OK);
    }

    /**
     * This method performs the logout process for
     * the token provided as parameter.
     * @param token that will be deleted
     * @throws CustomException in case token provided doesn't exits
     * in database
     */
    @DeleteMapping("/authentication/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable(value ="token") String token) {
        authenticationService.deleteToken(token);
    }
}
