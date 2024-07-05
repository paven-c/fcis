package com.paven.module.compliance.controller.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FieldCreateReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long formId;

    @Schema(description = "字段标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "字段标题不能为空")
    private String title;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "字段名称不能为空")
    private String name;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "字段类型不能为空")
    private Integer type;

    /**
     * 状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}