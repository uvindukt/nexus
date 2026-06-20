package com.nexus.analytics.infrastructure.web.advice;

import com.nexus.analytics.domain.exception.AnalyticsServiceException;
import com.nexus.analytics.domain.exception.EntryNotFoundException;
import com.nexus.analytics.infrastructure.web.constant.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AnalyticsServiceException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleDuplicateEntry(AnalyticsServiceException exception) {

        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.DUPLICATE_ENTRY, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.CONFLICT));
        problemDetail.setTitle(ErrorConstants.Title.CONFLICT);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail));
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleEntryNotFound(EntryNotFoundException exception) {

        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.ENTRY_NOT_FOUND, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.NOT_FOUND));
        problemDetail.setTitle(ErrorConstants.Title.NOT_FOUND);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleGenericException(Exception exception) {

        log.error(ErrorConstants.Log.UNHANDLED_EXCEPTION, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.Message.UNEXPECTED_ERROR);
        problemDetail.setType(URI.create(ErrorConstants.Type.INTERNAL_SERVER_ERROR));
        problemDetail.setTitle(ErrorConstants.Title.INTERNAL_SERVER_ERROR);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail));
    }

}
