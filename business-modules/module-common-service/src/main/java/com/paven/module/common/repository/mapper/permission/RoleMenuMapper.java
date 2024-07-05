package com.paven.module.common.repository.mapper.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.module.common.repository.pojo.permission.RoleMenu;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMenuMapper extends BaseMapperX<RoleMenu> {

    default List<RoleMenu> selectListByRoleId(Long roleId) {
        return selectList(RoleMenu::getRoleId, roleId);
    }

    default List<RoleMenu> selectListByRoleId(Collection<Long> roleIds) {
        return selectList(RoleMenu::getRoleId, roleIds);
    }

    default List<RoleMenu> selectListByMenuId(Long menuId) {
        return selectList(RoleMenu::getMenuId, menuId);
    }

    default void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        delete(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId)
                .in(RoleMenu::getMenuId, menuIds));
    }

    default void deleteListByMenuId(Long menuId) {
        delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
    }

}
