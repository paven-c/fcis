package com.paven.module.common.controller.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author paven
 */
@Schema(description = "菜单列表RequestVO")
@Data
public class MenuListReqVO {

    @Schema(description = "菜单名称，模糊匹配", example = "")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

}
