package com.paven.common.exception;

import lombok.Data;

/**
 * 错误码对象
 *
 * @author paven
 */
@Data
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

}
