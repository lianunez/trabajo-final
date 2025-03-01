package org.domain.product.exception;

public class CreatePostErrorException extends RuntimeException{
    public CreatePostErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
