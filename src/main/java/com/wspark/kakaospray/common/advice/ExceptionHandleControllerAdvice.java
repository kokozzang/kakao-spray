package com.wspark.kakaospray.common.advice;

import com.wspark.kakaospray.common.dto.ErrorResponse;
import com.wspark.kakaospray.common.exception.CommonException;
import com.wspark.kakaospray.common.exception.custom.BadRequestException;
import com.wspark.kakaospray.common.exception.wrapper.InternalServerError;
import com.wspark.kakaospray.common.exception.wrapper.MethodNotAllowed;
import com.wspark.kakaospray.common.exception.wrapper.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@Slf4j
public class ExceptionHandleControllerAdvice {

  @ExceptionHandler(value = CommonException.class)
  public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {
    return this.sendError(e, new ErrorResponse(e));
  }

  /**
   * 400 Bad Request
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
   * https://tools.ietf.org/html/rfc7231#section-6.5.4
   * @param e
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = {
      BadRequestException.class,
      MethodArgumentNotValidException.class,
      MethodArgumentNotValidException.class,
      MissingServletRequestParameterException.class,
      MethodArgumentTypeMismatchException.class,
      HttpMessageNotReadableException.class,
      MissingRequestHeaderException.class,
      BindException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) throws Exception {
    BadRequestException badRequestException = new BadRequestException(e);
    return this.sendError(badRequestException, new ErrorResponse(badRequestException));
  }

  /**
   * 404 Not Found
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
   * https://tools.ietf.org/html/rfc7231#section-6.5.1
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
    CommonException commonException = new NotFound();
    return this.sendError(commonException, new ErrorResponse(commonException));
  }



  /**
   * 405 Method Not Allowed
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/405
   * https://tools.ietf.org/html/rfc7231#section-6.5.5
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    CommonException commonException = new MethodNotAllowed();
    return this.sendError(commonException, new ErrorResponse(commonException));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    logger.error(e.getMessage(), e);
    CommonException commonException = new InternalServerError();
    return this.sendError(commonException, new ErrorResponse(commonException));
  }

  private ResponseEntity<ErrorResponse> sendError(CommonException e, final ErrorResponse errorResponse) {
    return ResponseEntity
        .status(e.getClass().getAnnotation(ResponseStatus.class).code())
        .body(errorResponse);
  }

}
