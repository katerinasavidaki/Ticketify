package gr.aueb.ticketify.core;

import gr.aueb.ticketify.core.exceptions.*;
import gr.aueb.ticketify.dto.ResponseMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ResponseMessageDTO(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(new ResponseMessageDTO(ex.getCode(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityInvalidArgumentException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(EntityInvalidArgumentException ex) {
        return new ResponseEntity<>(new ResponseMessageDTO(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotAuthorizedException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(EntityNotAuthorizedException ex) {
        return new ResponseEntity<>(new ResponseMessageDTO(ex.getCode(), ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AppServerException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppServerException ex) {
        return new ResponseEntity<>(new ResponseMessageDTO(ex.getCode(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle any other Exception that has not been caught
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDTO> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ResponseMessageDTO("Internal Error",
                "Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
