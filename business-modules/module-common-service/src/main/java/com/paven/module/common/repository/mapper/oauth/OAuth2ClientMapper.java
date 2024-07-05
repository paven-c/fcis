package com.paven.module.common.repository.mapper.oauth;

import com.paven.common.pojo.PageResult;
import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.component.mybatis.core.query.LambdaQueryWrapperX;
import com.paven.module.common.controller.oauth.vo.client.OAuth2ClientPageReqVO;
import com.paven.module.common.repository.pojo.oauth.OAuth2Client;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 客户端 Mapper
 *
 * @author paven
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapperX<OAuth2Client> {

    default PageResult<OAuth2Client> selectPage(OAuth2ClientPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OAuth2Client>()
                .likeIfPresent(OAuth2Client::getName, reqVO.getName())
                .eqIfPresent(OAuth2Client::getStatus, reqVO.getStatus())
                .orderByDesc(OAuth2Client::getId));
    }

    default OAuth2Client selectByClientId(String clientId) {
        return selectOne(OAuth2Client::getClientId, clientId);
    }

}
