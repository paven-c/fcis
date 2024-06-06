package com.fancy.module.common.repository.mapper.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.module.common.repository.pojo.permission.UserRole;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRole> {

    default List<UserRole> selectListByUserId(Long userId) {
        return selectList(UserRole::getUserId, userId);
    }

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .in(UserRole::getRoleId, roleIds));
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
    }

    default List<UserRole> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(UserRole::getRoleId, roleIds);
    }

}
