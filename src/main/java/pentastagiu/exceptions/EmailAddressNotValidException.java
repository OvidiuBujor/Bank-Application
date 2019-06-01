package pentastagiu.exceptions;

public class EmailAddressNotValidException extends RuntimeException {
    public EmailAddressNotValidException(String message) {
        super(message);
    }
}
