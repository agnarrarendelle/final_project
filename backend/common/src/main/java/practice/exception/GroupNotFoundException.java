package practice.exception;

import org.springframework.http.HttpStatus;

public class GroupNotFoundException extends RuntimeException{
    public static HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

    public GroupNotFoundException(Integer groupID){
        super(String.format("Group %s does not exist", groupID));
    }
}
