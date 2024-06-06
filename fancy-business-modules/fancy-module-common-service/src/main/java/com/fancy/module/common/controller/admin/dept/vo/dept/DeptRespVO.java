package com.fancy.module.common.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author paven
 */
@Schema(description = "部门信息ResponseVO")
@Data
public class DeptRespVO {

    @Schema(description = "部门编号", example = "")
    private Long id;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "父部门 ID", example = "")
    private Long parentId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer sort;

    @Schema(description = "负责人的用户编号", example = "")
    private Long leaderUserId;

    @Schema(description = "联系电话", example = "")
    private String phone;

    @Schema(description = "邮箱", example = "")
    private String email;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

}
