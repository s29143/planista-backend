package edu.pjatk.planista.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleBadCreds(BadCredentialsException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        pd.setTitle("Invalid credentials");
        pd.setDetail("Username or password is incorrect.");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        log.info("Handling method argument not valid exception");
        pd.setTitle(messageSource.getMessage(
                "error.validation",
                null,
                "Validation error",
                locale
        ));
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                err -> {
                                    if(err.getDefaultMessage() == null) {
                                        return "{field} error";
                                    }
                                    return messageSource.getMessage(err.getDefaultMessage(), null, "{field} error", locale);
                                },
                                Collectors.toList()
                        )
                ));

        List<String> globalErrors = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(e -> {
                    if(e.getDefaultMessage() == null) {
                        return "Global error";
                    }
                    return messageSource.getMessage(e.getDefaultMessage(), null, "Global error", locale);
                })
                .toList();

        if (!globalErrors.isEmpty()) {
            errors.put("_global", globalErrors);
        }

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(BindException.class)
    public ProblemDetail handleBindException(BindException ex, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        log.info("Handling bind exception");
        pd.setTitle(messageSource.getMessage(
                "error.validation",
                null,
                "Validation error",
                locale
        ));

        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                err -> messageSource.getMessage(err, locale),
                                Collectors.toList()
                        )
                ));

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        log.info("Handling constraint violation");
        pd.setTitle(messageSource.getMessage(
                "error.validation",
                null,
                "Validation error",
                locale
        ));

        Map<String, List<String>> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String path = violation.getPropertyPath().toString();
            String field = path.contains(".")
                    ? path.substring(path.lastIndexOf('.') + 1)
                    : path;

            errors.computeIfAbsent(field, k -> new ArrayList<>())
                    .add(violation.getMessage());
        }

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        log.info("Handling data integrity violation");

        pd.setTitle(messageSource.getMessage(
                "error.validation",
                null,
                "Validation error",
                locale
        ));

        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);

        String field = "unknown";
        String messageKey = "error.dataIntegrity";
        String defaultMessage = "Invalid data.";
        org.hibernate.exception.ConstraintViolationException hibernateCve = findHibernateConstraintViolation(ex);
        if (hibernateCve != null) {
            String constraintName = hibernateCve.getConstraintName();
            field = mapConstraintToField(constraintName);
            messageKey = mapConstraintToMessageKey(constraintName);
            defaultMessage = "Duplicate value.";
        }

        String message = messageSource.getMessage(
                messageKey,
                null,
                defaultMessage,
                locale
        );

        Map<String, List<String>> errors = Map.of(field, List.of(message));
        pd.setProperty("errors", errors);


        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("Handling Exception");
        pd.setTitle(messageSource.getMessage(
                "error.internal",
                null,
                "Internal server error",
                locale
        ));

        return pd;
    }

    private String mapConstraintToField(String constraintName) {
        if (constraintName == null) {
            return "unknown";
        }

        if ("company_unique".equalsIgnoreCase(constraintName)) {
            return "fullName";
        }

        return "unknown";
    }

    private String mapConstraintToMessageKey(String constraintName) {
        if (constraintName == null) {
            return "error.dataIntegrity";
        }

        if ("company_unique".equalsIgnoreCase(constraintName)) {
            return "company.fullName.unique";
        }

        return "error.dataIntegrity";
    }

    private org.hibernate.exception.ConstraintViolationException findHibernateConstraintViolation(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof org.hibernate.exception.ConstraintViolationException cve) {
                return cve;
            }
            current = current.getCause();
        }
        return null;
    }
}