package com.paven.module.common.convert.role;


import com.paven.module.common.api.permission.dto.RoleRespDTO;
import com.paven.module.common.repository.pojo.permission.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    RoleRespDTO convert(Role bean);
}
