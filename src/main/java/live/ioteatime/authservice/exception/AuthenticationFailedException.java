package live.ioteatime.authservice.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String e){
        super(e);
    }
}
