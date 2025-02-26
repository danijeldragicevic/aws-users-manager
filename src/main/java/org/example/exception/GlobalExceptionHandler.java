package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import software.amazon.awssdk.services.iam.model.IamException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AWS SDK-specific exceptions (e.g., IAM errors).
     */
    @ExceptionHandler(IamException.class)
    public ResponseEntity<ErrorResponse> handleAwsServiceException(IamException exception, WebRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    /**
     * Handles 400 - Bad Request (Invalid argument, missing parameters, etc.).
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception exception, WebRequest request) {
        return buildErrorResponse("Invalid request", HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles 404 - No Handler Found Exception.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException exception, WebRequest request) {
        return buildErrorResponse("Resource not found", HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles 405 - Method Not Allowed.
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(Exception exception, WebRequest request) {
        return buildErrorResponse("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    /**
     * Handles all other exceptions (Fallback handler).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, WebRequest request) {
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Utility method to build an `ErrorResponse` and return `ResponseEntity`.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        String requestURI = (((ServletWebRequest) request).getRequest().getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                requestURI
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
