package com.fancy.module.common.convert.auth;


import static com.fancy.common.util.collection.CollectionUtils.convertSet;
import static com.fancy.common.util.collection.CollectionUtils.filterList;
import static com.fancy.module.common.repository.pojo.permission.Menu.ID_ROOT;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.auth.vo.AuthLoginRespVO;
import com.fancy.module.common.controller.auth.vo.AuthPermissionInfoRespVO;
import com.fancy.module.common.enums.permission.MenuTypeEnum;
import com.fancy.module.common.repository.pojo.oauth.OAuth2AccessToken;
import com.fancy.module.common.repository.pojo.permission.Menu;
import com.fancy.module.common.repository.pojo.permission.Role;
import com.fancy.module.common.repository.pojo.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;

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
                .menus(buildMenuTree(menuList))
                .build();
    }

    @Mapping(target = "meta",expression = "java(stringToJsonNode(menu.getMeta()))")
    AuthPermissionInfoRespVO.MenuVO convertTreeNode(Menu menu);

    default JSONObject stringToJsonNode(String json) {
        return JSONUtil.parseObj(json);
    }

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @return 菜单树
     */
    default List<AuthPermissionInfoRespVO.MenuVO> buildMenuTree(List<Menu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return Collections.emptyList();
        }
        // 移除按钮
        menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.BUTTON.getType()));
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(Menu::getSort));
        // 构建菜单树
        Map<Long, AuthPermissionInfoRespVO.MenuVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(), AuthConvert.INSTANCE.convertTreeNode(menu)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !node.getParentId().equals(ID_ROOT)).forEach(childNode -> {
            // 获得父节点
            AuthPermissionInfoRespVO.MenuVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]", childNode.getId(), childNode.getParentId());
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
