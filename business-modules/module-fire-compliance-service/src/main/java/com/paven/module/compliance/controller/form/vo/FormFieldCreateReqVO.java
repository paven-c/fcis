package com.paven.module.compliance.controller.form.vo;

import com.paven.module.compliance.repository.dto.FieldConditionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FormFieldCreateReqVO implements Serializable {


    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long formId;

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    /**
     * 条件
     */
    private List<FieldConditionDTO> conditions;

    /**
     * 字段排序
     */
    private Integer sort;

}