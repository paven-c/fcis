package com.fancy.module.common.api.permission;

import com.fancy.module.common.api.permission.dto.DeptDataPermissionRespDTO;
import com.fancy.module.common.service.permission.PermissionService;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 权限 API 实现类
 *
 * @author paven
 */
@Service
public class PermissionApiImpl implements PermissionApi {

    @Resource
    private PermissionService permissionService;

    @Override
    public Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds) {
        return permissionService.getUserRoleIdListByRoleId(roleIds);
    }

    @Override
    public Set<String> getUserRoleCodeListByUserIds(Long userId) {
        return permissionService.getUserRoleCodeListByUserId(userId);
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return permissionService.hasAnyRoles(userId, roles);
    }

    @Override
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        return permissionService.getDeptDataPermission(userId);
    }

    @Override
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        permissionService.assignUserRole(userId, roleIds);
    }

}
