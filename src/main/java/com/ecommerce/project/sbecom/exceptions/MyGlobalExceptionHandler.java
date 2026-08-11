package com.ecommerce.project.sbecom.exceptions;

import com.ecommerce.project.sbecom.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName=((FieldError)error).getField();
            String errorMsg=error.getDefaultMessage();
            errors.put(fieldName,errorMsg);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException exception){
        String message=exception.getMessage();
        APIResponse apiResponse=new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException exception){
        String message=exception.getMessage();
        APIResponse apiResponse=new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoCategoriesPresentException.class)
    public ResponseEntity<APIResponse> myNoCategoriesPresentException(NoCategoriesPresentException exception){
        String message=exception.getMessage();
        APIResponse apiResponse=new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
