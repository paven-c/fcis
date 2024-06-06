package com.fancy.module.common.service.permission;

import com.fancy.module.common.controller.admin.permission.vo.menu.MenuListReqVO;
import com.fancy.module.common.controller.admin.permission.vo.menu.MenuSaveVO;
import com.fancy.module.common.repository.pojo.permission.Menu;
import java.util.Collection;
import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author paven
 */
public interface MenuService {

    /**
     * 创建菜单
     *
     * @param createReqVO 菜单信息
     * @return 创建出来的菜单编号
     */
    Long createMenu(MenuSaveVO createReqVO);

    /**
     * 更新菜单
     *
     * @param updateReqVO 菜单信息
     */
    void updateMenu(MenuSaveVO updateReqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 获得所有菜单列表
     *
     * @return 菜单列表
     */
    List<Menu> getMenuList();

    /**
     * 筛选菜单列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<Menu> getMenuList(MenuListReqVO reqVO);

    /**
     * 获得权限对应的菜单编号数组
     *
     * @param permission 权限标识
     * @return 数组
     */
    List<Long> getMenuIdListByPermissionFromCache(String permission);

    /**
     * 获得菜单
     *
     * @param id 菜单编号
     * @return 菜单
     */
    Menu getMenu(Long id);

    /**
     * 获得菜单数组
     *
     * @param ids 菜单编号数组
     * @return 菜单数组
     */
    List<Menu> getMenuList(Collection<Long> ids);

}
