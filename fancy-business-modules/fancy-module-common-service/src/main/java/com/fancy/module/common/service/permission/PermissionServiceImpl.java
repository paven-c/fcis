package com.fancy.module.common.service.permission;


import static com.fancy.common.util.collection.CollectionUtils.convertSet;
import static com.fancy.common.util.json.JsonUtils.toJsonString;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.component.datapermission.core.annotation.DataPermission;
import com.fancy.module.common.api.permission.dto.DeptDataPermissionRespDTO;
import com.fancy.module.common.enums.permission.DataScopeEnum;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import com.fancy.module.common.repository.cache.redis.RedisKeyConstants;
import com.fancy.module.common.repository.mapper.permission.RoleMenuMapper;
import com.fancy.module.common.repository.mapper.permission.UserRoleMapper;
import com.fancy.module.common.repository.pojo.permission.Menu;
import com.fancy.module.common.repository.pojo.permission.Role;
import com.fancy.module.common.repository.pojo.permission.RoleMenu;
import com.fancy.module.common.repository.pojo.permission.UserRole;
import com.fancy.module.common.service.dept.DeptService;
import com.fancy.module.common.service.user.UserService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限 Service 实现类
 *
 * @author paven
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    @Lazy
    private RoleService roleService;
    @Resource
    @Lazy
    private MenuService menuService;
    @Resource
    private DeptService deptService;
    @Resource
    @Lazy
    private UserService userService;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }
        // 用户角色列表
        List<Role> roles = getEnableUserRoleListByUserIdFromCache(userId);
        if (CollUtil.isEmpty(roles)) {
            return false;
        }
        // 超管无需权限拦截
        boolean isSuperAdmin = roles.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getCode()));
        if (isSuperAdmin) {
            return true;
        }
        // 遍历进行权限比对, 只要有一个权限即通过
        return Arrays.stream(permissions).anyMatch(permission -> hasAnyPermission(roles, permission));
    }

    /**
     * 判断指定角色，是否拥有该 permission 权限
     *
     * @param roles      指定角色数组
     * @param permission 权限标识
     * @return 是否拥有
     */
    private boolean hasAnyPermission(List<Role> roles, String permission) {
        List<Long> menuIds = menuService.getMenuIdListByPermissionFromCache(permission);
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }
        Set<Long> roleIds = convertSet(roles, Role::getId);
        for (Long menuId : menuIds) {
            // 拥有该菜单的角色编号集合
            Set<Long> menuRoleIds = getSelf().getMenuRoleIdListByMenuIdFromCache(menuId);
            // 如果有交集，说明有权限
            if (CollUtil.containsAny(menuRoleIds, roleIds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(roles)) {
            return true;
        }
        // 当前登录的角色。如果为空，说明没有权限
        List<Role> roleList = getEnableUserRoleListByUserIdFromCache(userId);
        if (CollUtil.isEmpty(roleList)) {
            return false;
        }
        // 判断是否有角色
        Set<String> userRoles = convertSet(roleList, Role::getCode);
        return CollUtil.containsAny(userRoles, Sets.newHashSet(roles));
    }

    // ========== 角色-菜单的相关方法  ==========

    @Override
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, allEntries = true)
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 获得角色拥有菜单编号
        Set<Long> dbMenuIds = convertSet(roleMenuMapper.selectListByRoleId(roleId), RoleMenu::getMenuId);
        // 计算新增和删除的菜单编号
        Set<Long> menuIdList = CollUtil.emptyIfNull(menuIds);
        Collection<Long> createMenuIds = CollUtil.subtract(menuIdList, dbMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbMenuIds, menuIdList);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (CollUtil.isNotEmpty(createMenuIds)) {
            roleMenuMapper.insertBatch(CollectionUtils.convertList(createMenuIds, menuId -> {
                RoleMenu entity = new RoleMenu();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                return entity;
            }));
        }
        if (CollUtil.isNotEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, allEntries = true),
            @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, allEntries = true)
    })
    public void processRoleDeleted(Long roleId) {
        // 标记删除 UserRole
        userRoleMapper.deleteListByRoleId(roleId);
        // 标记删除 RoleMenu
        roleMenuMapper.deleteListByRoleId(roleId);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public void processMenuDeleted(Long menuId) {
        roleMenuMapper.deleteListByMenuId(menuId);
    }

    @Override
    public Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }
        // 如果是管理员的情况下，获取全部菜单编号
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return convertSet(menuService.getMenuList(), Menu::getId);
        }
        // 如果是非管理员的情况下，获得拥有的菜单编号
        return convertSet(roleMenuMapper.selectListByRoleId(roleIds), RoleMenu::getMenuId);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public Set<Long> getMenuRoleIdListByMenuIdFromCache(Long menuId) {
        return convertSet(roleMenuMapper.selectListByMenuId(menuId), RoleMenu::getRoleId);
    }

    // ========== 用户-角色的相关方法  ==========

    @Override
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // 获得角色拥有角色编号
        Set<Long> dbRoleIds = convertSet(userRoleMapper.selectListByUserId(userId), UserRole::getRoleId);
        // 计算新增和删除的角色编号
        Set<Long> roleIdList = CollUtil.emptyIfNull(roleIds);
        Collection<Long> createRoleIds = CollUtil.subtract(roleIdList, dbRoleIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbRoleIds, roleIdList);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (!CollectionUtil.isEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                UserRole entity = new UserRole();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deleteMenuIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void processUserDeleted(Long userId) {
        userRoleMapper.deleteListByUserId(userId);
    }

    @Override
    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        return convertSet(userRoleMapper.selectListByUserId(userId), UserRole::getRoleId);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public Set<Long> getUserRoleIdListByUserIdFromCache(Long userId) {
        return getUserRoleIdListByUserId(userId);
    }

    @Override
    public Set<Long> getUserRoleIdListByRoleId(Collection<Long> roleIds) {
        return convertSet(userRoleMapper.selectListByRoleIds(roleIds), UserRole::getUserId);
    }

    /**
     * 获得用户拥有的角色，并且这些角色是开启状态的
     *
     * @param userId 用户编号
     * @return 用户拥有的角色
     */
    @VisibleForTesting
    List<Role> getEnableUserRoleListByUserIdFromCache(Long userId) {
        // 获得用户拥有的角色编号
        Set<Long> roleIds = getSelf().getUserRoleIdListByUserIdFromCache(userId);
        // 获得角色数组，并移除被禁用的
        List<Role> roles = roleService.getRoleListFromCache(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus()));
        return roles;
    }

    // ========== 用户-部门的相关方法  ==========

    @Override
    public void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds) {
        roleService.updateRoleDataScope(roleId, dataScope, dataScopeDeptIds);
    }

    @Override
    @DataPermission(enable = false)
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        // 获得用户的角色
        List<Role> roles = getEnableUserRoleListByUserIdFromCache(userId);
        // 如果角色为空，则只能查看自己
        DeptDataPermissionRespDTO result = new DeptDataPermissionRespDTO();
        if (CollUtil.isEmpty(roles)) {
            result.setSelf(true);
            return result;
        }
        // 获得用户的部门编号的缓存
        Supplier<Long> userDeptId = Suppliers.memoize(() -> userService.getUser(userId).getDeptId());
        // 遍历每个角色，计算
        for (Role role : roles) {
            if (role.getDataScope() == null) {
                continue;
            }
            // 全部数据权限
            if (Objects.equals(role.getDataScope(), DataScopeEnum.ALL.getScope())) {
                result.setAll(true);
                continue;
            }
            // 自定义部门可见
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
                CollUtil.addAll(result.getDeptIds(), role.getDataScopeDeptIds());
                // 自定义可见部门时，保证可以看到自己所在的部门。否则，一些场景下可能会有问题。
                CollUtil.addAll(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // 本部门可见
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
                CollectionUtils.addIfNotNull(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // 本部门及其子部门
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
                CollUtil.addAll(result.getDeptIds(), deptService.getChildDeptIdListFromCache(userDeptId.get()));
                // 添加本身部门编号
                CollUtil.addAll(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // 本人数据
            if (Objects.equals(role.getDataScope(), DataScopeEnum.SELF.getScope())) {
                result.setSelf(true);
                continue;
            }
            log.error("[getDeptDataPermission][LoginUser({}) role({}) 无法处理]", userId, toJsonString(result));
        }
        return result;
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PermissionServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}
