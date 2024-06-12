package com.fancy.module.common.service.oauth.impl;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception0;
import static com.fancy.common.util.collection.CollectionUtils.convertSet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fancy.common.enums.UserTypeEnum;
import com.fancy.common.exception.enums.GlobalErrorCodeConstants;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.date.DateUtils;
import com.fancy.component.security.core.LoginUser;
import com.fancy.module.common.controller.oauth.vo.token.OAuth2AccessTokenPageReqVO;
import com.fancy.module.common.repository.cache.redis.oauth2.OAuth2AccessTokenRedisDAO;
import com.fancy.module.common.repository.mapper.oauth.OAuth2AccessTokenMapper;
import com.fancy.module.common.repository.mapper.oauth.OAuth2RefreshTokenMapper;
import com.fancy.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.fancy.module.common.repository.pojo.oauth.OAuth2Client;
import com.fancy.module.common.repository.pojo.oauth.OAuth2RefreshToken;
import com.fancy.module.common.repository.pojo.user.User;
import com.fancy.module.common.service.oauth.OAuth2ClientService;
import com.fancy.module.common.service.oauth.OAuth2TokenService;
import com.fancy.module.common.service.user.UserService;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2.0 Token Service 实现类
 *
 * @author paven
 */
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    @Resource
    private OAuth2AccessTokenMapper oauth2AccessTokenMapper;
    @Resource
    private OAuth2RefreshTokenMapper oauth2RefreshTokenMapper;
    @Resource
    private OAuth2AccessTokenRedisDAO oauth2AccessTokenRedisDAO;
    @Resource
    private OAuth2ClientService oauth2ClientService;
    @Resource
    @Lazy
    private UserService userService;

    @Override
    @Transactional
    public OAuth2AccessToken createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
        OAuth2Client client = oauth2ClientService.validOAuthClientFromCache(clientId);
        // 创建刷新令牌
        OAuth2RefreshToken refreshTokenDO = createOAuth2RefreshToken(userId, userType, client, scopes);
        // 创建访问令牌
        return createOAuth2AccessToken(refreshTokenDO, client);
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshToken, String clientId) {
        // 查询访问令牌
        OAuth2RefreshToken refreshTokenDO = oauth2RefreshTokenMapper.selectByRefreshToken(refreshToken);
        if (refreshTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "无效的刷新令牌");
        }

        // 校验 Client 匹配
        OAuth2Client client = oauth2ClientService.validOAuthClientFromCache(clientId);
        if (ObjectUtil.notEqual(clientId, refreshTokenDO.getClientId())) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "刷新令牌的客户端编号不正确");
        }

        // 移除相关的访问令牌
        List<OAuth2AccessToken> oAuth2AccessTokens = oauth2AccessTokenMapper.selectListByRefreshToken(refreshToken);
        if (CollUtil.isNotEmpty(oAuth2AccessTokens)) {
            oauth2AccessTokenMapper.deleteBatchIds(convertSet(oAuth2AccessTokens, OAuth2AccessToken::getId));
            oauth2AccessTokenRedisDAO.deleteList(convertSet(oAuth2AccessTokens, OAuth2AccessToken::getAccessToken));
        }

        // 已过期的情况下，删除刷新令牌
        if (DateUtils.isExpired(refreshTokenDO.getExpiresTime())) {
            oauth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "刷新令牌已过期");
        }

        // 创建访问令牌
        return createOAuth2AccessToken(refreshTokenDO, client);
    }

    @Override
    public OAuth2AccessToken getAccessToken(String accessToken) {
        // 优先从 Redis 中获取
        OAuth2AccessToken oAuth2AccessToken = oauth2AccessTokenRedisDAO.get(accessToken);
        if (accessToken != null) {
            return oAuth2AccessToken;
        }

        // 获取不到，从 MySQL 中获取
        oAuth2AccessToken = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        // 如果在 MySQL 存在，则往 Redis 中写入
        if (oAuth2AccessToken != null && !DateUtils.isExpired(oAuth2AccessToken.getExpiresTime())) {
            oauth2AccessTokenRedisDAO.set(oAuth2AccessToken);
        }
        return oAuth2AccessToken;
    }

    @Override
    public OAuth2AccessToken checkAccessToken(String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = getAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌不存在");
        }
        if (DateUtils.isExpired(oAuth2AccessToken.getExpiresTime())) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌已过期");
        }
        return oAuth2AccessToken;
    }

    @Override
    public OAuth2AccessToken removeAccessToken(String accessToken) {
        // 删除访问令牌
        OAuth2AccessToken oAuth2AccessToken = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return null;
        }
        oauth2AccessTokenMapper.deleteById(oAuth2AccessToken.getId());
        oauth2AccessTokenRedisDAO.delete(accessToken);
        // 删除刷新令牌
        oauth2RefreshTokenMapper.deleteByRefreshToken(oAuth2AccessToken.getRefreshToken());
        return oAuth2AccessToken;
    }

    @Override
    public PageResult<OAuth2AccessToken> getAccessTokenPage(OAuth2AccessTokenPageReqVO reqVO) {
        return oauth2AccessTokenMapper.selectPage(reqVO);
    }

    private OAuth2AccessToken createOAuth2AccessToken(OAuth2RefreshToken refreshTokenDO, OAuth2Client client) {
        OAuth2AccessToken accessToken = new OAuth2AccessToken().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDO.getUserId()).setUserType(refreshTokenDO.getUserType())
                .setUserInfo(buildUserInfo(refreshTokenDO.getUserId(), refreshTokenDO.getUserType()))
                .setClientId(client.getClientId()).setScopes(refreshTokenDO.getScopes())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(client.getAccessTokenValiditySeconds()));
        oauth2AccessTokenMapper.insert(accessToken);
        oauth2AccessTokenRedisDAO.set(accessToken);
        return accessToken;
    }

    private OAuth2RefreshToken createOAuth2RefreshToken(Long userId, Integer userType, OAuth2Client client, List<String> scopes) {
        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken().setRefreshToken(generateRefreshToken())
                .setUserId(userId).setUserType(userType)
                .setClientId(client.getClientId()).setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(client.getRefreshTokenValiditySeconds()));
        oauth2RefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    /**
     * 加载用户信息，获取到昵称、部门等信息
     *
     * @param userId   用户编号
     * @param userType 用户类型
     * @return 用户信息
     * @see LoginUser
     */
    private Map<String, String> buildUserInfo(Long userId, Integer userType) {
        if (userType.equals(UserTypeEnum.AGENT.getValue())) {
            User user = userService.getUser(userId);
            return MapUtil.builder(LoginUser.INFO_KEY_NICKNAME, user.getNickname()).put(LoginUser.INFO_KEY_DEPT_ID, StrUtil.toStringOrNull(user.getDeptId()))
                    .build();
        }
        return null;
    }

    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

    private static String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }

}
