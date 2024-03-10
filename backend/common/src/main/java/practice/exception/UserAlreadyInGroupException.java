package practice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyInGroupException extends RuntimeException{
    public static HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

    public UserAlreadyInGroupException(int userId, int groupId){
        super(String.format("User %s is already in group %s", userId, groupId));
    }
}
