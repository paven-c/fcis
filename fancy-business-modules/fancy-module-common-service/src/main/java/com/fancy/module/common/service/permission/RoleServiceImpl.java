package com.fancy.module.common.service.permission;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.common.util.collection.CollectionUtils.convertMap;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_ADMIN_CODE_ERROR;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_CODE_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_IS_DISABLE;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_NAME_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.ROLE_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.admin.permission.vo.role.RolePageReqVO;
import com.fancy.module.common.controller.admin.permission.vo.role.RoleSaveReqVO;
import com.fancy.module.common.enums.permission.DataScopeEnum;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import com.fancy.module.common.enums.permission.RoleTypeEnum;
import com.fancy.module.common.repository.cache.redis.RedisKeyConstants;
import com.fancy.module.common.repository.mapper.permission.RoleMapper;
import com.fancy.module.common.repository.pojo.permission.Role;
import com.google.common.annotations.VisibleForTesting;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 角色 Service 实现类
 *
 * @author paven
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleSaveReqVO createReqVO, Integer type) {
        // 1. 校验角色
        validateRoleDuplicate(createReqVO.getName(), createReqVO.getCode(), null);
        // 2. 插入到数据库
        Role role = BeanUtils.toBean(createReqVO, Role.class)
                .setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()))
                .setStatus(CommonStatusEnum.ENABLE.getStatus())
                .setDataScope(DataScopeEnum.ALL.getScope());
        roleMapper.insert(role);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("role", role);
        return role.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#updateReqVO.id")
    public void updateRole(RoleSaveReqVO updateReqVO) {
        // 1.1 校验是否可以更新
        Role role = validateRoleForUpdate(updateReqVO.getId());
        // 1.2 校验角色的唯一字段是否重复
        validateRoleDuplicate(updateReqVO.getName(), updateReqVO.getCode(), updateReqVO.getId());
        // 2. 更新到数据库
        Role updateObj = BeanUtils.toBean(updateReqVO, Role.class);
        roleMapper.updateById(updateObj);
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("role", role);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        Role updateObject = new Role();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void deleteRole(Long id) {
        // 1. 校验是否可以更新
        Role role = validateRoleForUpdate(id);
        // 2.1 标记删除
        roleMapper.deleteById(id);
        // 2.2 删除相关数据
        permissionService.processRoleDeleted(id);
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(role, RoleSaveReqVO.class));
        LogRecordContext.putVariable("role", role);
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        Role role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    Role validateRoleForUpdate(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.PRESET.getType().equals(role.getType())) {
            throw exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
        return role;
    }

    @Override
    public Role getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id",
            unless = "#result == null")
    public Role getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public List<Role> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<Role> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public List<Role> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<Role> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 这里采用 for 循环从缓存中获取，主要考虑 Spring CacheManager 无法批量操作的问题
        RoleServiceImpl self = getSelf();
        return CollectionUtils.convertList(ids, self::getRoleFromCache);
    }

    @Override
    public PageResult<Role> getRolePage(RolePageReqVO reqVO) {
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();
        return ids.stream().anyMatch(id -> {
            Role role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<Role> roles = roleMapper.selectBatchIds(ids);
        Map<Long, Role> roleMap = convertMap(roles, Role::getId);
        // 校验
        ids.forEach(id -> {
            Role role = roleMap.get(id);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw exception(ROLE_IS_DISABLE, role.getRoleName());
            }
        });
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}