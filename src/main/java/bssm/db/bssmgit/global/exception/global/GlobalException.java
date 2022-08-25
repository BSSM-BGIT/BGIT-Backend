package bssm.db.bssmgit.global.exception.global;

import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleGatiException(CustomException e, HttpServletRequest request) {
        log.error("\nERROR_CODE: {} \n접근 경로: {} \nMessage: {}",
                e.getErrorCode(), request.getRequestURI(), e.getErrorCode().getMessage());

        return ErrorResponse.builder()
                .status(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleBadRequest(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return ErrorResponse.builder()
                .status(ErrorCode.BAD_REQUEST.getCode())
                .message(
                        e.getBindingResult().getFieldErrors().get(0).getField() + "의 " +
                                e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return ErrorResponse.builder()
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
