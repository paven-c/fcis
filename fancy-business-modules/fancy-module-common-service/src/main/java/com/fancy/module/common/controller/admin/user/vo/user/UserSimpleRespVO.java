package com.fancy.module.common.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author paven
 */
@Schema(description = "用户精简信息ResponseVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String nickname;

    @Schema(description = "部门ID", example = "")
    private Long deptId;
    @Schema(description = "部门名称", example = "")
    private String deptName;

}
