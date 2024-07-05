package com.paven.module.common.convert.auth;


import static com.paven.common.util.collection.CollectionUtils.convertSet;
import static com.paven.common.util.collection.CollectionUtils.filterList;
import static com.paven.module.common.repository.pojo.permission.Menu.ID_ROOT;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.controller.auth.vo.AuthLoginRespVO;
import com.paven.module.common.controller.auth.vo.AuthPermissionInfoRespVO;
import com.paven.module.common.enums.permission.MenuTypeEnum;
import com.paven.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.paven.module.common.repository.pojo.permission.Menu;
import com.paven.module.common.repository.pojo.permission.Role;
import com.paven.module.common.repository.pojo.user.User;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessToken bean);

    default AuthPermissionInfoRespVO convert(User user, List<Role> roleList, List<Menu> menuList) {
        return AuthPermissionInfoRespVO.builder()
                .user(BeanUtils.toBean(user, AuthPermissionInfoRespVO.UserVO.class))
                .roles(convertSet(roleList, Role::getCode))
                .permissions(convertSet(menuList, Menu::getPermission, p -> StrUtil.isNotBlank(p.getPermission())))
                .menus(buildMenuTree(menuList, Sets.newHashSet(), true))
                .build();
    }

    default AuthPermissionInfoRespVO convertTree(List<Menu> menuList, Set<Long> menuIds) {
        return AuthPermissionInfoRespVO.builder()
                .permissions(convertSet(menuList, Menu::getPermission, p -> StrUtil.isNotBlank(p.getPermission())))
                .menus(buildMenuTree(menuList, menuIds, false))
                .build();
    }

    @Mapping(target = "meta", expression = "java(stringToJsonNode(menu.getMeta()))")
    @Mapping(target = "checked", expression = "java(menuIds != null && !menuIds.isEmpty() && menuIds.contains(menu.getId()))")
    AuthPermissionInfoRespVO.MenuVO convertTreeNode(Menu menu, Set<Long> menuIds);

    default JSONObject stringToJsonNode(String json) {
        return JSONUtil.parseObj(json);
    }

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @param menuIds  角色菜单列表
     * @return 菜单树
     */
    default List<AuthPermissionInfoRespVO.MenuVO> buildMenuTree(List<Menu> menuList, Set<Long> menuIds, Boolean removeButton) {
        if (CollUtil.isEmpty(menuList)) {
            return Collections.emptyList();
        }
        if (BooleanUtil.isTrue(removeButton)) {
            // 移除按钮
            menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.BUTTON.getType()));
        }
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(Menu::getSort));
        // 构建菜单树
        Map<Long, AuthPermissionInfoRespVO.MenuVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(), AuthConvert.INSTANCE.convertTreeNode(menu, menuIds)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !node.getParentId().equals(ID_ROOT)).forEach(childNode -> {
            // 获得父节点
            AuthPermissionInfoRespVO.MenuVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        return filterList(treeNodeMap.values(), node -> ID_ROOT.equals(node.getParentId()));
    }
}
