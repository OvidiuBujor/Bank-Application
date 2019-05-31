package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.model.Authentication;

/**
 * This class converts an Authentication to
 * an AuthenticationDTO that is used for responses of
 * the server
 */
@Component
public class AuthenticationConvertor {

    public AuthenticationDTO convertToAuthenticationDTO(Authentication authentication){
        return new AuthenticationDTO(authentication.getUser().getUsername(),
                authentication.getToken(),
                authentication.getCreationTime());
    }
}
