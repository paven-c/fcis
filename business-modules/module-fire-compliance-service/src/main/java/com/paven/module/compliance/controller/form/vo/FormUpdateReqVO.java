package com.paven.module.compliance.controller.form.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FormUpdateReqVO implements Serializable {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "建筑名称不能为空")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer status;

}