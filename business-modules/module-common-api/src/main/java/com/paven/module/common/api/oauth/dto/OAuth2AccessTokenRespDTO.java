package com.paven.module.common.api.oauth.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * OAuth2.0 访问令牌的信息 Response DTO
 *
 * @author paven
 */
@Data
@Accessors(chain = true)
public class OAuth2AccessTokenRespDTO implements Serializable {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

}
