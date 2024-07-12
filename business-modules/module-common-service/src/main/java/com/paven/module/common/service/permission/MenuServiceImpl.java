package com.paven.module.common.service.permission;


import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.common.util.collection.CollectionUtils.convertList;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_EXISTS_CHILDREN;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_NAME_DUPLICATE;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_NOT_EXISTS;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_PARENT_ERROR;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_PARENT_NOT_DIR_OR_MENU;
import static com.paven.module.common.enums.ErrorCodeConstants.MENU_PARENT_NOT_EXISTS;
import static com.paven.module.common.repository.pojo.permission.Menu.ID_ROOT;

import cn.hutool.core.collection.CollUtil;
import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.controller.permission.vo.menu.MenuListReqVO;
import com.paven.module.common.controller.permission.vo.menu.MenuSaveVO;
import com.paven.module.common.enums.permission.MenuTypeEnum;
import com.paven.module.common.repository.cache.redis.RedisKeyConstants;
import com.paven.module.common.repository.mapper.permission.MenuMapper;
import com.paven.module.common.repository.pojo.permission.Menu;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单 Service 实现
 *
 * @author paven
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private PermissionService permissionService;

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#createReqVO.permission", condition = "#createReqVO.permission != null")
    public Long createMenu(MenuSaveVO createReqVO) {
        // 校验父菜单存在
        validateParentMenu(createReqVO.getParentId(), null);
        // 校验菜单（自己）
        validateMenu(createReqVO.getParentId(), createReqVO.getName(), null);
        // 新增菜单
        Menu menu = BeanUtils.toBean(createReqVO, Menu.class);
        menu.setMenuName(createReqVO.getName());
        initMenuProperty(menu);
        menuMapper.insert(menu);
        // 返回
        return menu.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void updateMenu(MenuSaveVO updateReqVO) {
        // 校验更新的菜单是否存在
        if (menuMapper.selectById(updateReqVO.getId()) == null) {
            throw exception(MENU_NOT_EXISTS);
        }
        // 校验父菜单存在
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // 校验菜单（自己）
        validateMenu(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());
        // 更新到数据库
        Menu updateObj = BeanUtils.toBean(updateReqVO, Menu.class);
        initMenuProperty(updateObj);
        menuMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void deleteMenu(Long id) {
        // 校验是否还有子菜单
        if (menuMapper.selectCountByParentId(id) > 0) {
            throw exception(MENU_EXISTS_CHILDREN);
        }
        // 校验删除的菜单是否存在
        if (menuMapper.selectById(id) == null) {
            throw exception(MENU_NOT_EXISTS);
        }
        // 标记删除
        menuMapper.deleteById(id);
        // 删除授予给角色的权限
        permissionService.processMenuDeleted(id);
    }

    @Override
    public List<Menu> getMenuList() {
        return menuMapper.selectList();
    }

    @Override
    public List<Menu> getMenuList(MenuListReqVO reqVO) {
        return menuMapper.selectList(reqVO);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#permission")
    public List<Long> getMenuIdListByPermissionFromCache(String permission) {
        List<Menu> menus = menuMapper.selectListByPermission(permission);
        return convertList(menus, Menu::getId);
    }

    @Override
    public Menu getMenu(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<Menu> getMenuList(Collection<Long> ids) {
        // 当 ids 为空时，返回一个空的实例对象
        if (CollUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return menuMapper.selectBatchIds(ids);
    }

    /**
     * 校验父菜单是否合法
     * <p>
     * 1. 不能设置自己为父菜单 2. 父菜单不存在 3. 父菜单必须是 {@link MenuTypeEnum#MENU} 菜单类型
     *
     * @param parentId 父菜单编号
     * @param childId  当前菜单编号
     */
    @VisibleForTesting
    void validateParentMenu(Long parentId, Long childId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // 不能设置自己为父菜单
        if (parentId.equals(childId)) {
            throw exception(MENU_PARENT_ERROR);
        }
        Menu menu = menuMapper.selectById(parentId);
        // 父菜单不存在
        if (menu == null) {
            throw exception(MENU_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if (!MenuTypeEnum.DIR.getType().equals(menu.getType()) && !MenuTypeEnum.MENU.getType().equals(menu.getType())) {
            throw exception(MENU_PARENT_NOT_DIR_OR_MENU);
        }
    }

    /**
     * 校验菜单是否合法
     * <p>
     * 1. 校验相同父菜单编号下，是否存在相同的菜单名
     *
     * @param name     菜单名字
     * @param parentId 父菜单编号
     * @param id       菜单编号
     */
    @VisibleForTesting
    void validateMenu(Long parentId, String name, Long id) {
        Menu menu = menuMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的菜单
        if (id == null) {
            throw exception(MENU_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(MENU_NAME_DUPLICATE);
        }
    }

    /**
     * 初始化菜单的通用属性。
     * <p>
     * 例如说，只有目录或者菜单类型的菜单，才设置 icon
     *
     * @param menu 菜单
     */
    private void initMenuProperty(Menu menu) {
        // 菜单为按钮类型时，无需 component、icon、path 属性，进行置空
        if (MenuTypeEnum.BUTTON.getType().equals(menu.getType())) {
            menu.setComponent("");
            menu.setComponentName("");
            menu.setIcon("");
            menu.setPath("");
        }
    }

}
