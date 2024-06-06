package com.fancy.module.common.repository.mapper.oauth;

import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.repository.pojo.oauth.OAuth2RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapperX<OAuth2RefreshToken> {

    default int deleteByRefreshToken(String refreshToken) {
        return delete(new LambdaQueryWrapperX<OAuth2RefreshToken>().eq(OAuth2RefreshToken::getRefreshToken, refreshToken));
    }

    default OAuth2RefreshToken selectByRefreshToken(String refreshToken) {
        return selectOne(OAuth2RefreshToken::getRefreshToken, refreshToken);
    }

}