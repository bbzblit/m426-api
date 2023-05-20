package dev.bbzblit.m426.helper;

import dev.bbzblit.m426.entity.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<ErrorResponse> handelMissingRequestCookie(
            MissingRequestCookieException ex) {
        if (ex.getCookieName().equals("session")) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("Unauthorized",
                    "You have to be logged in to connect to this endpoint", 401),
                    HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<ErrorResponse>(new ErrorResponse("BAD REQUEST",
                ex.getMessage(), 404), HttpStatus.BAD_REQUEST);
    }
}
