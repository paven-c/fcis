package com.fancy.module.common.repository.mapper.permission;

import com.fancy.common.pojo.PageResult;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.permission.vo.role.RolePageReqVO;
import com.fancy.module.common.repository.pojo.permission.Role;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

/**
 * @author paven
 */
@Mapper
public interface RoleMapper extends BaseMapperX<Role> {

    default PageResult<Role> selectPage(RolePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Role>()
                .likeIfPresent(Role::getRoleName, reqVO.getName())
                .likeIfPresent(Role::getCode, reqVO.getCode())
                .eqIfPresent(Role::getStatus, reqVO.getStatus())
                .betweenIfPresent(Role::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(Role::getSort));
    }

    default Role selectByName(String name) {
        return selectOne(Role::getRoleName, name);
    }

    default Role selectByCode(String code) {
        return selectOne(Role::getCode, code);
    }

    default List<Role> selectListByStatus(@Nullable Collection<Integer> statuses) {
        return selectList(Role::getStatus, statuses);
    }

}
