package com.paven.module.common.api.oauth;

import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.api.oauth.dto.OAuth2AccessTokenCheckRespDTO;
import com.paven.module.common.api.oauth.dto.OAuth2AccessTokenCreateReqDTO;
import com.paven.module.common.api.oauth.dto.OAuth2AccessTokenRespDTO;
import com.paven.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.paven.module.common.service.oauth.OAuth2TokenService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * OAuth2.0 Token API 实现类
 *
 * @author paven
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public OAuth2AccessTokenRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessToken accessToken = oauth2TokenService.createAccessToken(reqDTO.getUserId(), reqDTO.getUserType(), reqDTO.getClientId(), reqDTO.getScopes());
        return BeanUtils.toBean(accessToken, OAuth2AccessTokenRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = oauth2TokenService.checkAccessToken(accessToken);
        return BeanUtils.toBean(oAuth2AccessToken, OAuth2AccessTokenCheckRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenRespDTO removeAccessToken(String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = oauth2TokenService.removeAccessToken(accessToken);
        return BeanUtils.toBean(oAuth2AccessToken, OAuth2AccessTokenRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId) {
        OAuth2AccessToken oAuth2AccessToken = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return BeanUtils.toBean(oAuth2AccessToken, OAuth2AccessTokenRespDTO.class);
    }

}
