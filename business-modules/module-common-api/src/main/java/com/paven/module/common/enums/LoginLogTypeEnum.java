package com.paven.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录日志的类型枚举
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum LoginLogTypeEnum {

    /**
     * 使用账号登录
     */
    LOGIN_USERNAME(100),

    /**
     * 主动登出
     */
    LOGOUT_SELF(200),

    /**
     * 强制踢出
     */
    LOGOUT_DELETE(202),
    ;

    /**
     * 日志类型
     */
    private final Integer type;

}
