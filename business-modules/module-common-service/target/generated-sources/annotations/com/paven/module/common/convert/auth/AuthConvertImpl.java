package com.paven.module.common.convert.auth;

import com.paven.module.common.controller.auth.vo.AuthLoginRespVO;
import com.paven.module.common.controller.auth.vo.AuthPermissionInfoRespVO;
import com.paven.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.paven.module.common.repository.pojo.permission.Menu;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:40:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class AuthConvertImpl implements AuthConvert {

    @Override
    public AuthLoginRespVO convert(OAuth2AccessToken bean) {
        if ( bean == null ) {
            return null;
        }

        AuthLoginRespVO.AuthLoginRespVOBuilder authLoginRespVO = AuthLoginRespVO.builder();

        authLoginRespVO.userId( bean.getUserId() );
        authLoginRespVO.accessToken( bean.getAccessToken() );
        authLoginRespVO.refreshToken( bean.getRefreshToken() );
        authLoginRespVO.expiresTime( bean.getExpiresTime() );

        return authLoginRespVO.build();
    }

    @Override
    public AuthPermissionInfoRespVO.MenuVO convertTreeNode(Menu menu, Set<Long> menuIds) {
        if ( menu == null && menuIds == null ) {
            return null;
        }

        AuthPermissionInfoRespVO.MenuVO.MenuVOBuilder menuVO = AuthPermissionInfoRespVO.MenuVO.builder();

        if ( menu != null ) {
            menuVO.id( menu.getId() );
            menuVO.parentId( menu.getParentId() );
            menuVO.menuName( menu.getMenuName() );
            menuVO.title( menu.getTitle() );
            menuVO.path( menu.getPath() );
            menuVO.component( menu.getComponent() );
            menuVO.componentName( menu.getComponentName() );
            menuVO.icon( menu.getIcon() );
            menuVO.visible( menu.getVisible() );
            menuVO.keepAlive( menu.getKeepAlive() );
        }
        menuVO.meta( stringToJsonNode(menu.getMeta()) );
        menuVO.checked( menuIds != null && !menuIds.isEmpty() && menuIds.contains(menu.getId()) );

        return menuVO.build();
    }
}
