package com.ecommer.product.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @GraphQlExceptionHandler(NumberFormatException.class)
    public Mono<GraphQLError> numberFormatExceptionHandler(NumberFormatException e) {
        GraphQLError build = GraphQLError.newError().errorType(ErrorType.InvalidSyntax).message("For input format invalid").build();
        return Mono.just(build);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GraphQlExceptionHandler(ProductNotFound.class)
    public Mono<GraphQLError> productNotFoundExceptionHandler(ProductNotFound e) {
        log.info(e.getMessage());
        GraphQLError build = GraphQLError.newError().errorType(ErrorType.ValidationError).message(e.getMessage()).build();
        return Mono.just(build);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @GraphQlExceptionHandler(value = IllegalArgumentException.class)
    public Mono<GraphQLError> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.info(e.getMessage());
        GraphQLError build = GraphQLError.newError().errorType(ErrorType.InvalidSyntax).message(e.getMessage()).build();
        return Mono.just(build);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GraphQlExceptionHandler(value = NullPointerException.class)
    public Mono<GraphQLError> nullPointerExceptionHandler(NullPointerException e) {
        log.info(e.getMessage());
        GraphQLError build = GraphQLError.newError().errorType(ErrorType.InvalidSyntax).message(e.getMessage()).build();
        return Mono.just(build);
    }
    @GraphQlExceptionHandler(value = Exception.class)
    public Mono<GraphQLError> exceptionHandler(Exception e) {
        log.info(e.getMessage());
        GraphQLError build = GraphQLError.newError().errorType(ErrorType.InvalidSyntax).message(e.getMessage()).build();
        return Mono.just(build);
    }
}
