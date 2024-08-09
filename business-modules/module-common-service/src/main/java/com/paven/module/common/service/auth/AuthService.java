package com.paven.module.common.service.auth;

import com.paven.module.common.controller.auth.vo.AuthLoginReqVO;
import com.paven.module.common.controller.auth.vo.AuthLoginRespVO;
import com.paven.module.common.repository.pojo.user.User;
import jakarta.validation.Valid;

/**
 * 管理后台的认证 Service 接口 提供用户的登录、登出的能力
 *
 * @author paven
 */
public interface AuthService {

    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    User authenticate(String username, String password);

    /**
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * 基于 token 退出登录
     *
     * @param token   token
     * @param logType 登出类型
     */
    void logout(String token, Integer logType);

    /**
     * 刷新访问令牌
     *
     * @param clientId     客户端ID
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AuthLoginRespVO refreshToken(String clientId, String refreshToken);

}