package com.nexus.catalog.infrastructure.web.advice;

import com.nexus.catalog.domain.exception.DuplicateEntryException;
import com.nexus.catalog.domain.exception.EntryNotFoundException;
import com.nexus.catalog.domain.exception.InvalidHierarchyException;
import com.nexus.catalog.domain.exception.InvalidProductStateException;
import com.nexus.catalog.infrastructure.web.constants.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateEntry(DuplicateEntryException exception) {

        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.DUPLICATE_ENTRY, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.CONFLICT));
        problemDetail.setTitle(ErrorConstants.Title.CONFLICT);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);

    }

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntryNotFound(EntryNotFoundException exception) {

        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.ENTRY_NOT_FOUND, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.NOT_FOUND));
        problemDetail.setTitle(ErrorConstants.Title.NOT_FOUND);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);

    }

    @ExceptionHandler(InvalidHierarchyException.class)
    public ResponseEntity<ProblemDetail> handleInvalidHierarchy(InvalidHierarchyException exception) {

        String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.INVALID_HIERARCHY, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.INVALID_HIERARCHY));
        problemDetail.setTitle(ErrorConstants.Title.INVALID_HIERARCHY);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);

    }

    @ExceptionHandler(InvalidProductStateException.class)
    public ResponseEntity<ProblemDetail> handleInvalidProductState(InvalidProductStateException exception) {

        String message = messageSource.getMessage(exception.getMessage(), new Object[]{exception.getErrorCode()}, LocaleContextHolder.getLocale());
        log.error(ErrorConstants.Log.INVALID_PRODUCT_STATE, message);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, message);
        problemDetail.setType(URI.create(ErrorConstants.Type.INVALID_PRODUCT_STATE));
        problemDetail.setTitle(ErrorConstants.Title.INVALID_PRODUCT_STATE);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());
        problemDetail.setProperty("errorCode", exception.getErrorCode().name());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception exception) {

        log.error(ErrorConstants.Log.UNHANDLED_EXCEPTION, exception);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.Message.UNEXPECTED_ERROR);
        problemDetail.setType(URI.create(ErrorConstants.Type.INTERNAL_SERVER_ERROR));
        problemDetail.setTitle(ErrorConstants.Title.INTERNAL_SERVER_ERROR);
        problemDetail.setProperty(ErrorConstants.TIMESTAMP, Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);

    }

}
