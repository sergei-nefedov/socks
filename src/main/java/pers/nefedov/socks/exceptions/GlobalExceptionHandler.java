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
        log(HttpStatus.INSUFFICIENT_STORAGE, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler(NoDataForUpdateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ResponseEntity<ErrorResponse> handleException(NoDataForUpdateException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(UUID.randomUUID().toString());
        errorResponse.setMessage(exception.getMessage());
        log(HttpStatus.UNPROCESSABLE_ENTITY, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchSocksInStockException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<ErrorResponse> handleException(NoSuchSocksInStockException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(UUID.randomUUID().toString());
        errorResponse.setMessage(exception.getMessage());
        log(HttpStatus.NOT_FOUND, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadErrorException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    private ResponseEntity<ErrorResponse> handleException(FileUploadErrorException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(UUID.randomUUID().toString());
        errorResponse.setMessage(exception.getMessage());
        log(HttpStatus.UNSUPPORTED_MEDIA_TYPE, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ErrorResponse> handleException(InvalidRequestParametersException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(UUID.randomUUID().toString());
        errorResponse.setMessage(exception.getMessage());
        log(HttpStatus.BAD_REQUEST, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private static void log(HttpStatus insufficientStorage, ErrorResponse errorResponse) {
        logger.error("CODE {} ID={} MESSAGE: {}", insufficientStorage.value(), errorResponse.getId(),
                errorResponse.getMessage());
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
