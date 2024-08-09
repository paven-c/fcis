package com.paven.module.common.repository.mapper.permission;

import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.component.mybatis.core.query.LambdaQueryWrapperX;
import com.paven.module.common.controller.permission.vo.menu.MenuListReqVO;
import com.paven.module.common.repository.pojo.permission.Menu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface MenuMapper extends BaseMapperX<Menu> {

    default Menu selectByParentIdAndName(Long parentId, String name) {
        return selectOne(Menu::getParentId, parentId, Menu::getMenuName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(Menu::getParentId, parentId);
    }

    default List<Menu> selectList(MenuListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<Menu>()
                .likeIfPresent(Menu::getMenuName, reqVO.getName())
                .eqIfPresent(Menu::getStatus, reqVO.getStatus()));
    }

    default List<Menu> selectListByPermission(String permission) {
        return selectList(Menu::getPermission, permission);
    }
}