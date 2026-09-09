package uz.brbtech.quasar_backend.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.brbtech.quasar_backend.dto.response.ErrorResponse;
import uz.brbtech.quasar_backend.dto.response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // dto va request validatsiya xatolari
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> validHandleException(MethodArgumentNotValidException e,
                                            HttpServletRequest request) {
        List<ErrorResponse> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())  // Bad request kodi
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation error")
                .success(false)
                .errors(errors)
                .path(request.getRequestURI())
                .build();
    }

    // 400 - Parametr validatsiya
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<?> handleConstraintViolation(ConstraintViolationException ex,
                                                 HttpServletRequest request) {
        List<ErrorResponse> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> ErrorResponse.builder()
                        .field(v.getPropertyPath().toString())
                        .message(v.getMessage())
                        .build())
                .toList();
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .success(false)
                .errors(errors)
                .path(request.getRequestURI())
                .build();
    }

    // 400 - Noto‘g‘ri argument yuborilgan
    @ExceptionHandler(IllegalArgumentException.class)
    public Response<?> handleIllegalArgumentException(IllegalArgumentException ex,
                                                      HttpServletRequest request) {
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid argument: " + ex.getMessage())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // 400
    @ExceptionHandler(BadRequestException.class)
    public Response<?> handleBadRequestFoundException(BadRequestException badRequestException,
                                                      HttpServletRequest request) {
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message(badRequestException.getMessage())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // 403 - Ruxsat berilmagan
    @ExceptionHandler(AccessDeniedException.class)
    public Response<?> handleAccessDenied(AccessDeniedException exception,
                                          HttpServletRequest request
    ) {
        return Response.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // 405 - Noto'g'ri HTTP metod
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                HttpServletRequest request) {
        return Response.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .message("Method not allowed: " + ex.getMethod())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // 409 - DB constraint buzilgan (masalan duplicate key)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response<?> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                    HttpServletRequest request) {
        return Response.builder()
                .code(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT)
                .message("Conflict: " + ex.getRootCause().getMessage())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // 500
    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception exception,
                                       HttpServletRequest request) {
        return Response.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())   // Internal Server Error request kodi
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Something wrong -> " + exception.getMessage())
                .success(false)
                .path(request.getRequestURI())
                .build();
    }

    // Custom exception
    @ExceptionHandler(CustomException.class)
    public Response<?> handleCustomException(CustomException ex, HttpServletRequest req) {
        return Response.builder()
                .code(ex.getStatus().value())
                .status(ex.getStatus())
                .message(ex.getMessage())
                .success(false)
                .path(req.getRequestURI())
                .build();
    }

    // JSON parsing xatolarini ushlaydi
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<?> handleMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        String message = "Request body is invalid";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().isEmpty()
                    ? "unknown"
                    : invalidFormatException.getPath().get(invalidFormatException.getPath().size() - 1).getFieldName();
            Object invalidValue = invalidFormatException.getValue();
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType != null && targetType.isEnum()) {
                String allowedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                message = "Invalid value '" + invalidValue + "' for field '" + fieldName
                        + "'. Allowed values: [" + allowedValues + "]";
            } else {
                message = "Invalid value '" + invalidValue + "' for field '" + fieldName + "'";
            }
        } else {
            ex.getMostSpecificCause();
            if (ex.getMostSpecificCause().getMessage() != null) {
                message = ex.getMostSpecificCause().getMessage();
            }
        }
        return Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .path(request.getRequestURI())
                .build();
    }
}