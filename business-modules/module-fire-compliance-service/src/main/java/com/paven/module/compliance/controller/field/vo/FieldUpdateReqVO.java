package com.paven.module.compliance.controller.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FieldUpdateReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    @Schema(description = "字段标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String title;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer type;

    /**
     * 状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 字段字典类型
     */
    private String dicType;

    /**
     * 排序
     */
    private Integer sort;

}