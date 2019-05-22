package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.repository.AutheticationRepository;

@Service
public class AutheticationService {

    @Autowired
    AutheticationRepository autheticationRepository;
}
