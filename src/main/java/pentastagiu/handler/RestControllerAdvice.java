//package pentastagiu.handler;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import pentastagiu.util.CustomException;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//public class RestControllerAdvice {
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CustomException> handleAllExceptions(HttpServletRequest request, Exception ex) {
//        return new ResponseEntity<>(
//                new CustomException("An error occurred. Please try again", HttpStatus.INTERNAL_SERVER_ERROR),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
