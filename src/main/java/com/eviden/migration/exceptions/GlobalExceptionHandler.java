package com.eviden.migration.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Metodo que gestiona las exepciones
     * lanzadas por WebClientResponseException
     * @param ex
     */
    @ExceptionHandler(WebClientResponseException.class)
    public void handleWebClientResponseException(WebClientResponseException ex){
        //almacenar respuesta de la petición
        String response = ex.getResponseBodyAsString();
        HttpStatus statusCode = ex.getStatusCode();
        //mostrar respuesta
        log.error("Error: {}, {}", statusCode, response);
    }

    /**
     * Metodo que gestiona la autenticacion
     * en el drupal
     * @param ex
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public void handleAuthenticationFailedException(AuthenticationFailedException ex){
        //almacenar respuesta de la petición
        String response = ex.getMessage();
        HttpStatus status = ex.getStatus();
        //mostrar respuesta
        log.error("Error autenticacion: {} {}", status, response);
    }
}
