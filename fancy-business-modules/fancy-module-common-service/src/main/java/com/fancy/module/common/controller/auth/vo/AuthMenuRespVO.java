package com.fancy.module.common.controller.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "菜单Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMenuRespVO {

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long parentId;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时，才需要传", example = "")
    private String path;

    @Schema(description = "组件路径,仅菜单类型为菜单时，才需要传", example = "system/post/index")
    private String component;

    @Schema(description = "组件名", example = "User")
    private String componentName;

    @Schema(description = "菜单图标,仅菜单类型为菜单或者目录时，才需要传", example = "/menu/list")
    private String icon;

    @Schema(description = "是否可见", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean visible;

    @Schema(description = "是否缓存", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否总是显示", example = "false")
    private Boolean alwaysShow;

    /**
     * 子路由
     */
    private List<AuthMenuRespVO> children;

}
