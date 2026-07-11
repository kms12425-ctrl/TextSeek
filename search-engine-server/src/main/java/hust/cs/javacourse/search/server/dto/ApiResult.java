package hust.cs.javacourse.search.server.dto;

/**
 * 统一 API 响应格式
 *
 * @param <T> data 字段类型
 */
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功 */
    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(200, "success", data);
    }

    /** 成功（无数据） */
    public static <T> ApiResult<T> ok() {
        return new ApiResult<>(200, "success", null);
    }

    /** 失败 */
    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    /** 400 Bad Request */
    public static <T> ApiResult<T> badRequest(String message) {
        return new ApiResult<>(400, message, null);
    }

    /** 404 Not Found */
    public static <T> ApiResult<T> notFound(String message) {
        return new ApiResult<>(404, message, null);
    }

    /** 500 Internal Server Error */
    public static <T> ApiResult<T> internalError(String message) {
        return new ApiResult<>(500, message, null);
    }

    // ── Getters ──

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
