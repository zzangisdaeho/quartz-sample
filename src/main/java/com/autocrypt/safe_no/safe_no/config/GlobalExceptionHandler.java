package com.autocrypt.safe_no.safe_no.config;

import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 서비스 내부 로직 에러 처리 담당
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex, WebRequest request) {
        log.warn("CustomException error occur : ", ex);
        return ResponseEntity.status(ex.getStatus())
                .header("Content-Type", "application/json")
                .body(new ErrorResponseDto(ex.getMessage(), ex.getStatus()));
    }

    // 요청 유효성 검사 담당
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException error occur : ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json")
                .body(new ErrorResponseDto(errors, HttpStatus.BAD_REQUEST));
    }

    // 스프링에서 요청 파싱하는 과정에서 나는 에러 담당
    @ExceptionHandler(value = {HttpMessageConversionException.class, ConversionFailedException.class})
    public ResponseEntity<ErrorResponseDto> handleConversionError(Exception ex){
        log.warn("MessageConversionException error occur : ", ex);
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json")
                .body(new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    // 캐치하지 못한 에러 담당
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex){
        log.error("Unhandled Error occur : ", ex);
        return ResponseEntity.internalServerError()
                .header("Content-Type", "application/json")
                .body(new ErrorResponseDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Data
    public static class ErrorResponseDto {
        private Object message;
        private HttpStatus status;

        public ErrorResponseDto(Object message, HttpStatus status) {
            this.message = message;
            this.status = status;
        }
    }
}
