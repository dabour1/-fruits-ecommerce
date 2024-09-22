

package com.springBoot.fruits_ecommerce.exception;

 
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // @ExceptionHandler(UniqueConstraintViolationException.class)
    // public ResponseEntity<String> handleUniqueConstraintViolationException(UniqueConstraintViolationException ex) {
    //     return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    // }
    //  @ExceptionHandler(DataIntegrityViolationException.class)
    // public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    //     // Check if the exception message contains "unique" to identify unique constraint violations
    //     if (ex.getMessage().contains("Email")) {
    //         return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    //     }
    //     return new ResponseEntity<>("Database error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    // }
     @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(IllegalArgumentException ex) {
       
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
       
        
    }

   
}
