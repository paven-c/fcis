package com.paven.module.common.api.permission;

import com.paven.module.common.api.permission.dto.RoleRespDTO;
import com.paven.module.common.convert.role.RoleConvert;
import com.paven.module.common.service.permission.RoleService;
import jakarta.annotation.Resource;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * 角色 API 实现类
 *
 * @author paven
 */
@Service
public class RoleApiImpl implements RoleApi {

    @Resource
    private RoleService roleService;

    @Override
    public void validRoleList(Collection<Long> ids) {
        roleService.validateRoleList(ids);
    }

    @Override
    public RoleRespDTO getRoleByCode(String code) {
        return RoleConvert.INSTANCE.convert(roleService.getRoleByCode(code));
    }
}
