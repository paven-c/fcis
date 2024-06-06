package com.fancy.module.common.repository.mapper.permission;

import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.admin.permission.vo.menu.MenuListReqVO;
import com.fancy.module.common.repository.pojo.permission.Menu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface MenuMapper extends BaseMapperX<Menu> {

    default Menu selectByParentIdAndName(Long parentId, String name) {
        return selectOne(Menu::getParentId, parentId, Menu::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(Menu::getParentId, parentId);
    }

    default List<Menu> selectList(MenuListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<Menu>()
                .likeIfPresent(Menu::getName, reqVO.getName())
                .eqIfPresent(Menu::getStatus, reqVO.getStatus()));
    }

    default List<Menu> selectListByPermission(String permission) {
        return selectList(Menu::getPermission, permission);
    }
}
