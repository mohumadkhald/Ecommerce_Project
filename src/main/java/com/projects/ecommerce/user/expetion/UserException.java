package com.projects.ecommerce.user.expetion;

import com.projects.ecommerce.Auth.expetion.AuthenticationnException;
import com.projects.ecommerce.order.StockNotFoundException;
import com.projects.ecommerce.utilts.traits.ApiTrait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController

public class UserException {

    public UserException(ApiTrait apiTrait) {
    }


    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ex.getField(), ex.getField() + " Already Exists"); // Assuming "email" is the field causing the error
        return  ApiTrait.errorMessage(error, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put("user", "User Not Found"); // Assuming "user" is the field causing the error
        return  ApiTrait.errorMessage(error, ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ex.getField(), ex.getMessage()); // Assuming "user" is the field causing the error
        return  ApiTrait.errorMessage(error, ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStockNotFoundException(StockNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("errors", ex.getErrors());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getAllErrors().forEach(
                error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errMsg = error.getDefaultMessage();
                    errors.put(fieldName, errMsg);
                }
        );
        return ApiTrait.errorMessage(errors, "Validation Failed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationnException.class)
    public ResponseEntity<?> handleAuthException(AuthenticationnException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put("Problem", ex.getMessage());
        return  ApiTrait.errorMessage(error, "Authentication Failed", HttpStatus.BAD_REQUEST);
    }

}