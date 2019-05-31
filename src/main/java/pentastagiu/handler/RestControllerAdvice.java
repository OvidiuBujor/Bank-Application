package pentastagiu.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pentastagiu.exceptions.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({ TokenNotFoundException.class })
    void handleTokenNotFoundException(HttpServletResponse response, TokenNotFoundException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler({ UserAlreadyLoggedInException.class })
    void handleUserAlreadyLoggedInException(HttpServletResponse response, UserAlreadyLoggedInException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ CredentialsNotCorrectException.class })
    void handleCredentialsNotCorrectException(HttpServletResponse response, CredentialsNotCorrectException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ UserNotLoggedInException.class })
    void handleUserNotLoggedInException(HttpServletResponse response, UserNotLoggedInException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ InsufficientFundsException.class })
    void handleInsufficientFundsException(HttpServletResponse response, InsufficientFundsException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ AccountNotExistsException.class })
    void handleAccountNotExistsException(HttpServletResponse response, AccountNotExistsException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler({ AccountTypesMismatchException.class })
    void handleAccountTypesMismatchException(HttpServletResponse response, AccountTypesMismatchException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ UserAlreadyRegisteredException.class })
    void handleUserAlreadyRegisteredException(HttpServletResponse response, UserAlreadyRegisteredException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({ UserNotFoundException.class })
    void handleUserNotFoundException(HttpServletResponse response, UserNotFoundException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
