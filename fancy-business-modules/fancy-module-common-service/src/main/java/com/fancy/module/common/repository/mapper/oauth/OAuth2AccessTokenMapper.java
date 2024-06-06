package com.fancy.module.common.repository.mapper.oauth;

import com.fancy.common.pojo.PageResult;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.admin.oauth.vo.token.OAuth2AccessTokenPageReqVO;
import com.fancy.module.common.repository.pojo.oauth.OAuth2AccessToken;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface OAuth2AccessTokenMapper extends BaseMapperX<OAuth2AccessToken> {

    default OAuth2AccessToken selectByAccessToken(String accessToken) {
        return selectOne(OAuth2AccessToken::getAccessToken, accessToken);
    }

    default List<OAuth2AccessToken> selectListByRefreshToken(String refreshToken) {
        return selectList(OAuth2AccessToken::getRefreshToken, refreshToken);
    }

    default PageResult<OAuth2AccessToken> selectPage(OAuth2AccessTokenPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OAuth2AccessToken>()
                .eqIfPresent(OAuth2AccessToken::getUserId, reqVO.getUserId())
                .eqIfPresent(OAuth2AccessToken::getUserType, reqVO.getUserType())
                .likeIfPresent(OAuth2AccessToken::getClientId, reqVO.getClientId())
                .gt(OAuth2AccessToken::getExpiresTime, LocalDateTime.now())
                .orderByDesc(OAuth2AccessToken::getId));
    }

}
