package com.fancy.module.common.controller.permission.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;
import lombok.Data;

@Schema(description = "赋予角色菜单RequestVO")
@Data
public class PermissionAssignRoleMenuReqVO {

    @Schema(description = "角色编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "角色编号不能为空")
    private Long roleId;

    @Schema(description = "菜单编号列表", example = "1,3,5")
    private Set<Long> menuIds = Collections.emptySet();

}
