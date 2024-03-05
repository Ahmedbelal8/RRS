package com.octane.rrs.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.octane.rrs.dto.exception.ExceptionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Ahmed Belal
 * The CustomizedResponseEntityExceptionHandler advice extends the ResponseEntityExceptionHandler to override the exception experience.
 */
@ControllerAdvice
public class GlobalResponseEntityExceptionHandler // extends ResponseEntityExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler.class);

    /**
     * This method deals with any unhandled exception to return the fault scheme.
     *
     * @param ex      The thrown exception
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleStandardExceptions(Exception ex, WebRequest request) {
        logger.debug("Exception>>Handle All Exceptions :");
        logger.info(ex.getClass().getName());
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
                request.getDescription(false), ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getErrorCodeDescription(), new Date(), null);
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handelAccessDeniedException(AccessDeniedException ex,
                                                                    WebRequest request) {
        logger.debug("Exception>>Handle Access Denied Exception :");
        logger.info(ex.getClass().getName());
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false), ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getErrorCodeDescription(), new Date(), null);
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }


    /**
     * This method handles any CustomException. Any exception thrown in the project should be handled by CustomException if the developer has a need to ad cutom logic to the exception handling (like error codes or custom http status).
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({CustomException.class,})
    public final ResponseEntity<Object> handleGenericCustomExceptions(CustomException ex, WebRequest request) {
        logger.debug("Exception>>Handle All Exceptions :");
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(
                ex.getErrorCode().getHttpStatus()
                , ex.getMessage()
                , request.getDescription(false)
                , ex.getErrorCode().getCode()
                , ex.getErrorCode().getErrorCodeDescription()
                , new Date()
                , null);
        return new ResponseEntity(exceptionResponse, ex.getErrorCode().getHttpStatus());
    }


    /**
     * This method handles any Binding exceptions which is the exceptions handled by spring-boot validations.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({BindException.class, MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(Exception ex, WebRequest request) {
        logger.debug("Exception>>Handle All Exceptions :");
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(
                HttpStatus.BAD_REQUEST
                , ErrorCode.INVALID_INPUT.getErrorCodeDescription()
                , ""
                , ErrorCode.INVALID_INPUT.getCode()
                , ErrorCode.INVALID_INPUT.getErrorCodeDescription()
                , new Date()
                , getNotValidArguments(ex));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> getNotValidArguments(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MissingServletRequestParameterException) {
            errors.put(((MissingServletRequestParameterException) ex).getParameterName(), ((MissingServletRequestParameterException) ex).getBody().getDetail());
            return errors;
        } else if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return errors;
        } else return null;
    }
    /**
     * This method handles any HttpMessageNotReadableException.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               WebRequest request) {
        String msg = null;
        Throwable cause = ex.getCause();
        if (cause instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) cause;
            msg = jpe.getOriginalMessage();
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) cause;
            if (mie.getPath() != null && mie.getPath().size() > 0) {
                msg = "Invalid request field: " + mie.getPath().get(0).getFieldName();
            } else {
                msg = "Invalid request message";
            }
        } else if (cause instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) cause;
            msg = jme.getOriginalMessage();
            if (jme.getPath() != null && jme.getPath().size() > 0) {
                msg = "Invalid request field: " + jme.getPath().get(0).getFieldName() +
                        ": " + msg;
            }
        }
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(HttpStatus.BAD_REQUEST,
                msg, request.getDescription(false),
                ErrorCode.INVALID_INPUT.getCode(), ErrorCode.INVALID_INPUT.getErrorCodeDescription(), new Date(), null);

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
