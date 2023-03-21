package com.example.search.exception;

import jakarta.annotation.Nullable;
import com.example.search.component.I18nMessage;
import com.example.search.exception.code.ErrorCode;
import com.example.search.exception.code.ErrorField;
import com.example.search.exception.code.ErrorFieldType;
import com.example.search.exception.code.ErrorTitle;
import com.example.search.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final I18nMessage i18nMessage;

    @ExceptionHandler({
            BadRequestException.class,
            UnAuthorizedException.class,
            UnprocessableException.class,
            InternalErrorException.class
    })
    protected ResponseEntity<Object> handleException(
            InternalErrorException ex, WebRequest request) {
        HttpStatus status = getStatusCode(ex);
        ErrorResponse res = new ErrorResponse(ex.getCode(), ex.getMessage(), ex.getArgs());
        return handleExceptionInternal(ex, res, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({InvalidParameterException.class})
    protected ResponseEntity<Object> handleInValidException(
            InvalidParameterException ex, WebRequest request) {
        List<ErrorField> errorFieldList = new ArrayList<>();
        errorFieldList.add(ex.errorField);
        ErrorResponse res = new ErrorResponse(ErrorCode.DEMO_COMMON_INVALID_PARAM, errorFieldList);

        return handleExceptionInternal(ex, res, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {
        ErrorResponse res = new ErrorResponse(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR);
        return handleExceptionInternal(ex, res, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<ErrorField> errorFields = new ArrayList<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            ErrorField errorField = new ErrorField();

            errorField.setField(StringUtils.camelToSnake(fieldError.getField()));
            if (fieldError.getDefaultMessage().contains("Size")) {
                errorField.setType(ErrorFieldType.size);
                if (fieldError.getArguments().length == 2) {
                    errorField.setMax((int) fieldError.getArguments()[1]);
                } else if (fieldError.getArguments().length == 3) {
                    errorField.setMax((int) fieldError.getArguments()[2]);
                    errorField.setMax((int) fieldError.getArguments()[1]);
                }
            } else if (fieldError.getDefaultMessage().contains("pattern")) {
                errorField.setType(ErrorFieldType.pattern);
            } else if (fieldError.getDefaultMessage().contains("NotBlank")
                    || fieldError.getDefaultMessage().contains("NotEmpty")
                    || fieldError.getDefaultMessage().contains("NotNull")) {
                errorField.setType(ErrorFieldType.empty);
            } else if (fieldError.getDefaultMessage().contains("enum")
                    || fieldError.getDefaultMessage().contains("code")
                    || fieldError.getDefaultMessage().contains("Digits")) {
                errorField.setType(ErrorFieldType.value);
            } else {
                errorField.setType(ErrorFieldType.unknown);
            }
            errorFields.add(errorField);
        }
        ErrorResponse res = new ErrorResponse(ErrorCode.DEMO_COMMON_INVALID_PARAM, errorFields);
        return handleExceptionInternal(ex, res, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if (body == null) {
            body = new ErrorResponse(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR);
        }

        ErrorResponse res = (ErrorResponse) body;

        String title = i18nMessage.getMessage(res.getCode() + "_TITLE");
        String msg = i18nMessage.getMessage(res.getCode() + "_MESSAGE", res.getArgs());

        if (res.getCode().equals(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR.name())) {
            log.error(String.valueOf(ex));
        } else {
            log.warn(ex.getMessage());
        }

        if (title.contains(res.getCode())) {
            title = ErrorTitle.demo_error.name();
            msg = res.getMessage();
        }
        res.setTitle(title);
        res.setMessage(msg);
        return new ResponseEntity<>(res, headers, status);
    }

    private HttpStatus getStatusCode(InternalErrorException ex) {
        if (ex instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof UnprocessableException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
