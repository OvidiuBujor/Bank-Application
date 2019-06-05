package pentastagiu.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pentastagiu.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Test
    void getUserById() {
        User user = authenticationService.findByToken("fYonKtquD8WCgctRH6za").getUser();
        assertEquals(user ,userService.getUserById((long) 1));
    }

    @Test
    void createUser() {

    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void validateUser() {
    }

    @Test
    void getUserByUsername() {
    }
}