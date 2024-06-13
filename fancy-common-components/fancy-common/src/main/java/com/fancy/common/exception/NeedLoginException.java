package com.fancy.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 用户未登录 Exception
 *
 * @author Yanyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class NeedLoginException extends RuntimeException {

    /**
     * 业务错误码
     */
    @Getter
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public NeedLoginException() {
    }

    public NeedLoginException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public NeedLoginException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public NeedLoginException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public NeedLoginException setMessage(String message) {
        this.message = message;
        return this;
    }
}