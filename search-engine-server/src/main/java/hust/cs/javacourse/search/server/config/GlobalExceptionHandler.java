package hust.cs.javacourse.search.server.config;

import hust.cs.javacourse.search.server.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ApiResult<Void> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        return ApiResult.error(413, "文件大小超过限制");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Void> handleRuntime(RuntimeException e) {
        log.error("Unexpected runtime exception", e);
        return ApiResult.internalError("服务器内部错误: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Void> handleAll(Exception e) {
        log.error("Unhandled exception", e);
        return ApiResult.internalError("服务器内部错误");
    }
}
