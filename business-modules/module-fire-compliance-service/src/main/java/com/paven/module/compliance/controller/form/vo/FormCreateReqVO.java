package com.paven.module.compliance.controller.form.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FormCreateReqVO implements Serializable {


    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "建筑名称不能为空")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "请选择状态")
    private Integer status;

}