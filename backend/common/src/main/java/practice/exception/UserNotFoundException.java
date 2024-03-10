package practice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{
    public static HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

    public UserNotFoundException(Integer userID){
        super(String.format("User %s does not exist", userID));
    }
}
