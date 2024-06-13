package com.fancy.module.common.controller.auth.vo;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author paven
 */
@Schema(description = "登录用户的权限信息ResponseVO，额外包括用户信息和角色列表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthPermissionInfoRespVO {

    @Schema(description = "用户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserVO user;

    @Schema(description = "角色标识数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> roles;

    @Schema(description = "操作权限数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> permissions;

    @Schema(description = "菜单树", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MenuVO> menus;

    @Schema(description = "用户信息 VO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserVO {

        @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private Long id;

        @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String nickname;

        @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String avatar;

        @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private Long deptId;

    }

    @Schema(description = "登录用户的菜单信息ResponseVO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuVO {

        @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private Long id;

        @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private Long parentId;

        @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String menuName;

        @Schema(description = "菜单标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String title;

        @Schema(description = "路由地址,仅菜单类型为菜单或者目录时，才需要传", example = "post")
        private String path;

        @Schema(description = "组件路径,仅菜单类型为菜单时，才需要传", example = "system/post/index")
        private String component;

        @Schema(description = "组件名", example = "SystemUser")
        private String componentName;

        @Schema(description = "菜单图标,仅菜单类型为菜单或者目录时，才需要传", example = "/menu/list")
        private String icon;

        @Schema(description = "是否可见", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
        private Boolean visible;

        @Schema(description = "是否缓存", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
        private Boolean keepAlive;

        @Schema(description = "是否总是显示", example = "false")
        private Boolean alwaysShow;

        @Schema(description = "元信息", example = "")
        private JSONObject meta;

        @Schema(description = "选中标记", example = "")
        private Boolean checked;

        /**
         * 子路由
         */
        private List<MenuVO> children;

    }

}
