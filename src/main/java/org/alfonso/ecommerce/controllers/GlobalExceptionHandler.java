package org.alfonso.ecommerce.controllers;

import org.alfonso.ecommerce.entities.Error;
import org.alfonso.ecommerce.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMissingBody(HttpMessageNotReadableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("msg", "El cuerpo de la petición es obligatorio");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.CONFLICT.value());
        errorResponse.put("msg", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("msg", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ObjectMappingException.class)
    public ResponseEntity<Map<String, Object>> handleObjectMappingException(ObjectMappingException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("msg", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingFilesException.class)
    public ResponseEntity<Map<String, Object>> handleMissingFilesException(MissingFilesException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("msg", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({InvalidFileFormatException.class, FileTooLargeException.class})
    public ResponseEntity<Map<String, Object>> handleFileValidationExceptions(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("msg", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage()
                ));

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "VALIDATION_FAILED");
        errorResponse.put("msg", "Uno o más campos fallaron en la validación.");
        errorResponse.put("fieldErrors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoStockAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleNoStockAvailableException(NoStockAvailableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.CONFLICT.value());
        errorResponse.put("msg", ex.getMessage());
        errorResponse.put("error", "OUT_OF_STOCK");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("msg", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Error> handleBadCredentials(BadCredentialsException ex) {
        Error error = new Error();
        error.setError(ex.getMessage());
        error.setMsg("Usuario o contraseña incorrecta");
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Error> handleUserNotFound(UserNotFoundException ex) {
        Error error = new Error();
        error.setError(ex.getMessage());
        error.setMsg("Usuario no encontrado");
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler({InvalidRefreshToken.class})
    public ResponseEntity<Error> handleInvalidRefreshToken(InvalidRefreshToken ex) {
        Error error = new Error();
        error.setError(ex.getMessage());
        error.setMsg("Token refresh inválido");
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }

    @ExceptionHandler({UserAlreadyExists.class})
    public ResponseEntity<Error> handleUserAlreadyExists(UserAlreadyExists ex) {
        Error error = new Error();
        error.setError(ex.getMessage());
        error.setMsg("El correo ya está siendo usado. Por favor, utilice uno distinto");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }

    @ExceptionHandler({InvalidJwtTokenException.class})
    public ResponseEntity<Error> handleInvalidJwtToken(InvalidJwtTokenException ex) {
        Error error = new Error();
        error.setError("TOKEN_INVALID_OR_EXPIRED");
        error.setMsg(ex.getMessage());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }

    @ExceptionHandler({CartUserMismatchException.class})
    public ResponseEntity<Error> handleCartUserMismatchException(CartUserMismatchException ex) {
        Error error = new Error();
        error.setError("CART_USER_MISMATCH");
        error.setMsg(ex.getMessage());
        error.setStatus(HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
    }

    @ExceptionHandler({MissingAuthorizationHeaderException.class})
    public ResponseEntity<Error> handleMissingAuthorization(MissingAuthorizationHeaderException ex) {
        Error error = new Error();
        error.setError(ex.getMessage());
        error.setMsg("Token no proporcionado");
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }
}
