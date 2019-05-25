package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.repository.AutheticationRepository;

import java.nio.charset.Charset;
import java.util.Random;

@Service
public class AutheticationService {

    @Autowired
    AutheticationRepository autheticationRepository;

    public Authentication createAuthentication(Authentication authentication){
        return autheticationRepository.save(authentication);
    }

    public Authentication findByToken(String token){
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
        byte[] nbyte = new byte[30];
        randomnr.nextBytes(nbyte);
        return new String(nbyte, Charset.forName("UTF-8"));
    }

    private boolean validateToken(String token) {
        return autheticationRepository.findByToken(token) != null;
    }

    public void deleteToken(String token){
        Authentication authentication = autheticationRepository.findByToken(token);
       if(authentication != null){
            autheticationRepository.delete(authentication);
        }
    }
}
