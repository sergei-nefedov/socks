package pers.nefedov.socks.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShortageInStockException.class)
    @ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
    private ResponseEntity<ErrorResponse> handleException(ShortageInStockException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(UUID.randomUUID().toString());
        errorResponse.setMessage(exception.getMessage());
        logger.error("CODE {} ID={} MESSAGE: {}", HttpStatus.INSUFFICIENT_STORAGE.value(), errorResponse.getId(),
                errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ResponseBody
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    static class ErrorResponse {
        private String id;
        private String message;
    }
}
