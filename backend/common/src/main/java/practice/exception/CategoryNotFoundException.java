package practice.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends RuntimeException {
    public static HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

    public CategoryNotFoundException(int categoryId) {
        super(String.format("Category %s does not exist", categoryId));
    }
}
