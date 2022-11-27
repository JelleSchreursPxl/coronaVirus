package be.pxl.coronavirus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST)
public class DoctorException extends RuntimeException{
    public DoctorException(String message) {super(message);}
}
