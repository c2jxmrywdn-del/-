package com.example.todoapp.exception;

/**
 * 待办事项业务异常基类
 * 所有业务异常都继承此类
 */
public class TodoException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;

    public TodoException(String errorCode, String message) {
        this(errorCode, message, 400);
    }

    public TodoException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public TodoException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, 400, cause);
    }

    public TodoException(String errorCode, String message, int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
