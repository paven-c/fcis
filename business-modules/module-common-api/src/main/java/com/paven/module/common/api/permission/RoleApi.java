package com.paven.module.common.api.permission;

import com.paven.module.common.api.permission.dto.RoleRespDTO;
import java.util.Collection;

/**
 * 角色 API 接口
 *
 * @author paven
 */
public interface RoleApi {

    /**
     * 校验角色们是否有效。如下情况，视为无效： 1. 角色编号不存在 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    void validRoleList(Collection<Long> ids);

    /**
     * 通过编号获得角色
     *
     * @param code 角色编号
     * @return 角色
     */
    RoleRespDTO getRoleByCode(String code);

}
