package com.paven.module.compliance.controller.form.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FormFieldSaveReqVO implements Serializable {

    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long formId;

    @Schema(description = "建筑字段", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<FormFieldCreateReqVO> fields;

}