package com.fancy.component.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fancy.common.exception.ServiceException;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.util.servlet.ServletUtils;
import com.fancy.component.security.config.SecurityProperties;
import com.fancy.component.security.core.LoginUser;
import com.fancy.component.security.core.util.SecurityFrameworkUtils;
import com.fancy.component.web.core.handler.GlobalExceptionHandler;
import com.fancy.component.web.core.util.WebFrameworkUtils;
import com.fancy.module.common.api.oauth.OAuth2TokenApi;
import com.fancy.module.common.api.oauth.dto.OAuth2AccessTokenCheckRespDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Token 过滤器，验证 token 的有效性 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 *
 * @author paven
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final OAuth2TokenApi oauth2TokenApi;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotEmpty(token)) {
            Integer userType = WebFrameworkUtils.getLoginUserType(request);
            try {
                LoginUser loginUser = buildLoginUserByToken(token, userType);
                if (loginUser == null) {
                    loginUser = mockLoginUser(request, token, userType);
                }
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJson(response, result);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token, Integer userType) {
        try {
            OAuth2AccessTokenCheckRespDTO accessToken = oauth2TokenApi.checkAccessToken(token);
            if (accessToken == null) {
                return null;
            }
            if (userType != null && ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
                throw new AccessDeniedException("错误的用户类型");
            }
            return new LoginUser().setId(accessToken.getUserId())
                    .setUserType(accessToken.getUserType())
                    .setInfo(accessToken.getUserInfo())
                    .setScopes(accessToken.getScopes());
        } catch (ServiceException serviceException) {
            return null;
        }
    }

    /**
     * 模拟登录用户，方便日常开发调试
     *
     * @param request  请求
     * @param token    模拟的 token，格式为 {@link SecurityProperties#getMockSecret()} + 用户编号
     * @param userType 用户类型
     * @return 模拟的 LoginUser
     */
    private LoginUser mockLoginUser(HttpServletRequest request, String token, Integer userType) {
        if (!securityProperties.getMockEnable()) {
            return null;
        }
        // 必须以 mockSecret 开头
        if (!token.startsWith(securityProperties.getMockSecret())) {
            return null;
        }
        // 构建模拟用户
        Long userId = Long.valueOf(token.substring(securityProperties.getMockSecret().length()));
        return new LoginUser().setId(userId).setUserType(userType);
    }

}
