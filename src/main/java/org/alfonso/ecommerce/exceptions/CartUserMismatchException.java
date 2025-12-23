package org.alfonso.ecommerce.exceptions;

public class CartUserMismatchException extends RuntimeException {
    public CartUserMismatchException(String message) {
        super(message);
    }
}
