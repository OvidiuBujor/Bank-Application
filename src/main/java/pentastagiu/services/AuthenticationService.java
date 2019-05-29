package pentastagiu.services;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AuthenticationRepository;
import pentastagiu.util.CustomException;

import java.util.Optional;
@Service
public class AuthenticationService {

    private AuthenticationRepository authenticationRepository;

    private UserService userService;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository, UserService userService){
        this.authenticationRepository = authenticationRepository;
        this.userService = userService;
    }

    public Authentication saveAuthentication(Authentication authentication){
        return authenticationRepository.save(authentication);
    }

    public Optional<Authentication> findByToken(String token){
        return authenticationRepository.findByToken(token);
    }

    public Authentication login(String username, String password) throws CustomException{
        if (userService.validateUser(username,password)) {
            User user = userService.getUserByUsername(username);
            Optional<Authentication> authenticationToBeReturned = findByUser(user);
            if (authenticationToBeReturned.isPresent()) {
                throw new CustomException("User already logged in!", HttpStatus.ALREADY_REPORTED);
            }else{
                return createAuthentication(user);
            }
        }else
            throw new CustomException("Credentials are not correct!",HttpStatus.NOT_FOUND);
    }

    private Optional<Authentication> findByUser(User user) {
        return authenticationRepository.findByUser(user);
    }

    private Authentication createAuthentication(User user){
        Authentication authentication = new Authentication();
        authentication.setUser(user);
        authentication.setToken(createToken());
        return authentication;
    }

    private String createToken(){
        String token;
        do{
            token = generateToken();
        }while(validateToken(token));
        return token;
    }

    private synchronized String generateToken() {
        return RandomString.make(20);
    }

    private boolean validateToken(String token) {
        return authenticationRepository.findByToken(token).isPresent();
    }

    public void deleteToken(String token) throws CustomException {
        Optional<Authentication> authentication = authenticationRepository.findByToken(token);
       if(authentication.isPresent())
            authenticationRepository.delete(authentication.get());
       else
           throw new CustomException("Token not found", HttpStatus.NOT_FOUND);
    }
}
