package br.com.aluraflix.application.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private String timestamp;
    private int code;
    private HttpStatus status;
    private String details;
    private List<String> errors;

}
