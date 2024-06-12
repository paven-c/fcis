package com.fancy.module.common.controller.dept.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author paven
 */
@Schema(description = "部门列表RequestVO")
@Data
@Accessors(chain = true)
public class DeptListReqVO {

    @Schema(description = "部门名称，模糊匹配", example = "")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

}
