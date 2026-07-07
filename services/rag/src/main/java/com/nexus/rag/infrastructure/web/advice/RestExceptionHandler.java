package com.nexus.rag.infrastructure.web.advice;

import com.nexus.rag.infrastructure.web.constant.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
public class RestExceptionHandler {

    private final MessageSource messageSource;

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
