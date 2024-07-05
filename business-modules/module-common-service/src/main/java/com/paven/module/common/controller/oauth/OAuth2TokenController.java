package com.paven.module.common.controller.oauth;

import static com.paven.common.pojo.CommonResult.success;

import com.paven.common.pojo.CommonResult;
import com.paven.common.pojo.PageResult;
import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.controller.oauth.vo.token.OAuth2AccessTokenPageReqVO;
import com.paven.module.common.controller.oauth.vo.token.OAuth2AccessTokenRespVO;
import com.paven.module.common.enums.LoginLogTypeEnum;
import com.paven.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.paven.module.common.service.auth.AuthService;
import com.paven.module.common.service.oauth.OAuth2TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paven
 */
@Tag(name = "OAuth2.0 令牌")
@RestController
@RequestMapping("/oauth2-token")
public class OAuth2TokenController {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private AuthService authService;

    @GetMapping("/page")
    @Operation(summary = "获得访问令牌分页", description = "只返回有效期内的")
    @PreAuthorize("@ss.hasPermission('common:oauth2-token:page')")
    public CommonResult<PageResult<OAuth2AccessTokenRespVO>> getAccessTokenPage(@Valid OAuth2AccessTokenPageReqVO reqVO) {
        PageResult<OAuth2AccessToken> pageResult = oauth2TokenService.getAccessTokenPage(reqVO);
        return success(BeanUtils.toBean(pageResult, OAuth2AccessTokenRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "")
    @PreAuthorize("@ss.hasPermission('common:oauth2-token:delete')")
    public CommonResult<Boolean> deleteAccessToken(@RequestParam("accessToken") String accessToken) {
        authService.logout(accessToken, LoginLogTypeEnum.LOGOUT_DELETE.getType());
        return success(true);
    }

}
