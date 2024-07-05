package com.paven.module.common.convert.role;

import com.paven.module.common.api.permission.dto.RoleRespDTO;
import com.paven.module.common.repository.pojo.permission.Role;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:40:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class RoleConvertImpl implements RoleConvert {

    @Override
    public RoleRespDTO convert(Role bean) {
        if ( bean == null ) {
            return null;
        }

        RoleRespDTO roleRespDTO = new RoleRespDTO();

        roleRespDTO.setId( bean.getId() );
        roleRespDTO.setRoleName( bean.getRoleName() );
        roleRespDTO.setCode( bean.getCode() );
        roleRespDTO.setSort( bean.getSort() );
        roleRespDTO.setDataScope( bean.getDataScope() );
        Set<Long> set = bean.getDataScopeDeptIds();
        if ( set != null ) {
            roleRespDTO.setDataScopeDeptIds( new LinkedHashSet<Long>( set ) );
        }
        roleRespDTO.setStatus( bean.getStatus() );
        roleRespDTO.setType( bean.getType() );
        roleRespDTO.setRemark( bean.getRemark() );

        return roleRespDTO;
    }
}
