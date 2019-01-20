package com.shahidfoy.reactiveblog.exception_handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

public class RestExceptionHandler implements WebExceptionHandler {

    private ObjectMapper objectMapper;

    public RestExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof PostNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);

            // marks the response as complete and forbids writing to it
            return exchange.getResponse().setComplete();
        }
        return Mono.error(ex);
    }

}
