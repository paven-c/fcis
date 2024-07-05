package com.paven.module.compliance.controller.form.vo;

import com.paven.module.compliance.repository.dto.FieldConditionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FormRuleSaveReqVO implements Serializable {


    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long formId;

    @Schema(description = "规则条件", requiredMode = RequiredMode.NOT_REQUIRED, example = "")
    private List<FieldConditionDTO> conditions;

}