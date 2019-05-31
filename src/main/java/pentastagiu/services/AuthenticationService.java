package pentastagiu.services;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AuthenticationRepository;
import pentastagiu.exceptions.CredentialsNotCorrectException;
import pentastagiu.exceptions.TokenNotFoundException;
import pentastagiu.exceptions.UserAlreadyLoggedInException;

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

    public Authentication findByToken(String token){
        if (existsByToken(token))
            return authenticationRepository.findByToken(token).get();
        throw new TokenNotFoundException("Token not found.");
    }

    public boolean existsByToken(String token){
        return authenticationRepository.existsByToken(token);
    }

    public Authentication login(String username, String password){
        if (userService.validateUser(username,password)) {
            User user = userService.getUserByUsername(username);
            Optional<Authentication> authenticationToBeReturned = findByUser(user);
            if (authenticationToBeReturned.isPresent()) {
                throw new UserAlreadyLoggedInException("User already logged in.");
            }else{
                return createAuthentication(user);
            }
        }else
            throw new CredentialsNotCorrectException("Credentials are not correct.");
    }

    private Optional<Authentication> findByUser(User user) {
        return authenticationRepository.findByUser(user);
    }

    private Authentication createAuthentication(User user){
        Authentication authentication = new Authentication();
        authentication.setUser(user);
        authentication.setToken(createToken());
        authenticationRepository.save(authentication);
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

    public void deleteToken(String token) {
        if(existsByToken(token)) {
            authenticationRepository.delete(authenticationRepository.findByToken(token).get());
        }else
           throw new TokenNotFoundException("Token not found");
    }

    public Iterable<Authentication> getAuthentications(){
        return authenticationRepository.findAll();
    }

    public void deleteAuthentication(Authentication authentication){
        authenticationRepository.delete(authentication);
    }
}
