package com.paven.module.common.controller.auth;


import static com.paven.common.pojo.CommonResult.success;
import static com.paven.common.util.collection.CollectionUtils.convertSet;
import static com.paven.component.web.core.util.WebFrameworkUtils.getLoginUserId;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.pojo.CommonResult;
import com.paven.module.common.controller.auth.vo.AuthLoginReqVO;
import com.paven.module.common.controller.auth.vo.AuthLoginRespVO;
import com.paven.module.common.controller.auth.vo.AuthPermissionInfoRespVO;
import com.paven.module.common.convert.auth.AuthConvert;
import com.paven.module.common.enums.LoginLogTypeEnum;
import com.paven.module.common.enums.oauth.OAuth2ClientConstants;
import com.paven.module.common.repository.pojo.permission.Menu;
import com.paven.module.common.repository.pojo.permission.Role;
import com.paven.module.common.repository.pojo.user.User;
import com.paven.module.common.service.auth.AuthService;
import com.paven.module.common.service.permission.MenuService;
import com.paven.module.common.service.permission.PermissionService;
import com.paven.module.common.service.permission.RoleService;
import com.paven.module.common.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.paven.component.security.config.SecurityProperties;
import com.paven.component.security.core.util.SecurityFrameworkUtils;

/**
 * @author paven
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshToken(OAuth2ClientConstants.CLIENT_ID_AGENT, refreshToken));
    }

    @GetMapping("/permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 获得用户信息
        User user = userService.getUser(getLoginUserId());
        if (user == null) {
            return success(null);
        }
        // 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(getLoginUserId());
        if (CollUtil.isEmpty(roleIds)) {
            return success(AuthConvert.INSTANCE.convert(user, Collections.emptyList(), Collections.emptyList()));
        }
        List<Role> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus()));
        // 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, Role::getId));
        List<Menu> menuList = menuService.getMenuList(menuIds);
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus()));
        return success(AuthConvert.INSTANCE.convert(user, roles, menuList));
    }


}
