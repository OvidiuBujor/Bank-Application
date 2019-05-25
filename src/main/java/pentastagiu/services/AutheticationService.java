package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AutheticationRepository;
import pentastagiu.util.CustomException;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class AutheticationService {

    @Autowired
    AutheticationRepository autheticationRepository;

    public Authentication createAuthentication(Authentication authentication){
        return autheticationRepository.save(authentication);
    }

    public Optional<Authentication> findByToken(String token){
        return autheticationRepository.findByToken(token);
    }

    public String createToken(){
        String token;
        do{
            token = generateToken();
        }while(validateToken(token));
        return token;
    }

    private String generateToken(){
        Random randomnr = new Random();
        byte[] nbyte = new byte[20];
        randomnr.nextBytes(nbyte);
        return new String(nbyte, Charset.forName("US-ASCII"));
    }

    private boolean validateToken(String token) {
        return autheticationRepository.findByToken(token).isPresent();
    }

    public void deleteToken(String token) throws CustomException {
        Optional<Authentication> authentication = autheticationRepository.findByToken(token);
       if(authentication.isPresent())
            autheticationRepository.delete(authentication.get());
       else
           throw new CustomException("No token found", HttpStatus.NOT_FOUND);
    }

    public Optional<Authentication> findByUser(User user) {
        return autheticationRepository.findByUser(user);
    }
}
