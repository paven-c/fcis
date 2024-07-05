package com.paven.module.compliance.controller.field.vo;

import com.paven.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Yanyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class FieldPageReqVO extends PageParam {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long formId;

    @Schema(description = "字段标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String title;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer type;

}