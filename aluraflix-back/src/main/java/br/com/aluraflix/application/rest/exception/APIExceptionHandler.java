package br.com.aluraflix.application.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Object> handleApiException(APIException ex, WebRequest request){
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.valueOf(ex.getStatusCode()), request );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (ex instanceof APIException) {
            body = buildExceptionResponse(status, ((APIException) ex).getReason(), Arrays.asList(ex.getMessage()));
        } else if (ex instanceof BindException) {
            body = processBindingResultErrors((BindException) ex, status, "Erros de validação direto do DTO");
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ExceptionResponse processBindingResultErrors(BindingResult bindingResult, HttpStatus status, String details) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String errorText = error.getField() + " : " + error.getDefaultMessage();
            if (!errors.contains(errorText)) {
                errors.add(errorText);
            }
        }
        for (ObjectError error : bindingResult.getGlobalErrors()) {
            String errorText = error.getObjectName() + " : " + error.getDefaultMessage();
            if (!errors.contains(errorText)) {
                errors.add(errorText);
            }
        }

        return buildExceptionResponse(status, details, errors);

    }

    private ExceptionResponse buildExceptionResponse(HttpStatus status, String details, List<String> errors) {
        return new ExceptionResponse(LocalDateTime.now().format(ofPattern("yyyy-MM-dd HH:mm:ss")), status.value(), status, details, errors);
    }
}