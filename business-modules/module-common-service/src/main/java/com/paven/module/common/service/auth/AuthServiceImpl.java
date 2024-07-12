package com.paven.module.common.service.auth;


import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.module.common.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static com.paven.module.common.enums.ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR;
import static com.paven.module.common.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;

import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.enums.UserTypeEnum;
import com.paven.common.util.validation.ValidationUtils;
import com.paven.module.common.controller.auth.vo.AuthLoginReqVO;
import com.paven.module.common.controller.auth.vo.AuthLoginRespVO;
import com.paven.module.common.convert.auth.AuthConvert;
import com.paven.module.common.enums.LoginLogTypeEnum;
import com.paven.module.common.enums.oauth.OAuth2ClientConstants;
import com.paven.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.paven.module.common.repository.pojo.user.User;
import com.paven.module.common.service.oauth.OAuth2TokenService;
import com.paven.module.common.service.user.UserService;
import com.google.common.annotations.VisibleForTesting;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Auth Service 实现类
 *
 * @author paven
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private Validator validator;
    @Resource
    private CaptchaService captchaService;

    /**
     * 验证码的开关，默认为 true
     */
    @Value("${app.captcha.enable:true}")
    private Boolean captchaEnable;

    @Override
    public User authenticate(String username, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        // 校验账号是否存在
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (CommonStatusEnum.DISABLE.getStatus().equals(user.getStatus())) {
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        // 校验验证码
        validateCaptcha(reqVO);
        // 使用账号密码，进行登录
        User user = authenticate(reqVO.getMobile(), reqVO.getPassword());
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(OAuth2ClientConstants.CLIENT_ID_AGENT, user.getId(), reqVO.getMobile());
    }

    @VisibleForTesting
    void validateCaptcha(AuthLoginReqVO reqVO) {
        // 如果验证码关闭，则不进行校验
        if (!captchaEnable) {
            return;
        }
        // 校验验证码
        ValidationUtils.validate(validator, reqVO, AuthLoginReqVO.CodeEnableGroup.class);
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        // 验证不通过
        if (!response.isSuccess()) {
            throw exception(AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(String clientId, Long userId, String username) {
        // 创建访问令牌
        OAuth2AccessToken accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(), clientId, null);
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public AuthLoginRespVO refreshToken(String clientId, String refreshToken) {
        OAuth2AccessToken accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public void logout(String token, Integer logType) {
        // 删除访问令牌
        OAuth2AccessToken accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        // 删除成功，则记录登出日志
//        createLogoutLog(accessTokenDO.getUserId(), accessTokenDO.getUserType(), logType);
    }


    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.AGENT;
    }

}
