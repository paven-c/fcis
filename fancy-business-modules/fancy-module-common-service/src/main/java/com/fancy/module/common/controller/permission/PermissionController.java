package com.fancy.module.common.controller.permission;


import static com.fancy.common.pojo.CommonResult.success;

import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.controller.auth.vo.AuthPermissionInfoRespVO;
import com.fancy.module.common.controller.permission.vo.permission.PermissionAssignRoleDataScopeReqVO;
import com.fancy.module.common.controller.permission.vo.permission.PermissionAssignRoleMenuReqVO;
import com.fancy.module.common.controller.permission.vo.permission.PermissionAssignUserRoleReqVO;
import com.fancy.module.common.convert.auth.AuthConvert;
import com.fancy.module.common.repository.pojo.permission.Menu;
import com.fancy.module.common.repository.pojo.permission.Role;
import com.fancy.module.common.service.permission.MenuService;
import com.fancy.module.common.service.permission.PermissionService;
import com.fancy.module.common.service.permission.RoleService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 权限 Controller，提供赋予用户、角色的权限的 API 接口
 *
 * @author paven
 */
@Tag(name = "权限")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;

    @Operation(summary = "获取系统的权限信息")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/menus-tree")
    @PreAuthorize("@ss.hasRole('super_admin')")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionTree(@RequestParam("roleId") Long roleId) {
        Role role = roleService.getRole(roleId);
        // 角色菜单
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(Lists.newArrayList(role.getId()));
        // 全部菜单
        List<Menu> menuList = menuService.getMenuList();
        menuList.removeIf(menu -> DeleteStatusEnum.DELETED.getStatus().equals(menu.getDeleted()));
        return success(AuthConvert.INSTANCE.convertTree(menuList, menuIds));
    }

    @Operation(summary = "赋予角色菜单")
    @PostMapping("/assign-role-menu")
    @PreAuthorize("@ss.hasRole('super_admin')")
    public CommonResult<Boolean> assignRoleMenu(@Validated @RequestBody PermissionAssignRoleMenuReqVO reqVO) {
        // 执行菜单的分配
        permissionService.assignRoleMenu(reqVO.getRoleId(), reqVO.getMenuIds());
        return success(true);
    }

    @Operation(summary = "获得角色拥有的菜单编号")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/role-menus")
    @PreAuthorize("@ss.hasRole('super_admin')")
    public CommonResult<Set<Long>> getRoleMenuList(Long roleId) {
        return success(permissionService.getRoleMenuListByRoleId(roleId));
    }

    @PostMapping("/assign-role-data-scope")
    @Operation(summary = "赋予角色数据权限")
    @PreAuthorize("@ss.hasPermission('common:permission:assign-role-data-scope')")
    public CommonResult<Boolean> assignRoleDataScope(@Valid @RequestBody PermissionAssignRoleDataScopeReqVO reqVO) {
        permissionService.assignRoleDataScope(reqVO.getRoleId(), reqVO.getDataScope(), reqVO.getDataScopeDeptIds());
        return success(true);
    }

    @Operation(summary = "获得管理员拥有的角色编号列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @GetMapping("/list-user-roles")
    @PreAuthorize("@ss.hasPermission('common:permission:assign-user-role')")
    public CommonResult<Set<Long>> listAdminRoles(@RequestParam("userId") Long userId) {
        return success(permissionService.getUserRoleIdListByUserId(userId));
    }

    @Operation(summary = "赋予用户角色")
    @PostMapping("/assign-user-role")
    @PreAuthorize("@ss.hasPermission('common:permission:assign-user-role')")
    public CommonResult<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        permissionService.assignUserRole(reqVO.getUserId(), reqVO.getRoleIds());
        return success(true);
    }

}
