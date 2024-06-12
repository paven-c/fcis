package com.fancy.module.common.controller.dict.vo.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典类型精简信息ResponseVO")
@Data
public class DictTypeSimpleRespVO {

    @Schema(description = "字典类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "字典类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    private String type;

}
