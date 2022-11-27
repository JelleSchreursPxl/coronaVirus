package be.pxl.coronavirus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class DoctorExceptionHandler {

    @ExceptionHandler(value = DoctorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String clubException(Exception ex) { return ex.getMessage(); }

    private static class ErrorResponseBody {
        private final String message;
        private final List<String> errors;

        public ErrorResponseBody(String message, List<String> errors) {
            this.message = message;
            this.errors = errors;
        }

        public String getMessage() { return message; }
        public List<String> getErrors() { return errors; }
    }
}
