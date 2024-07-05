package com.paven.module.compliance.controller.form.vo;

import com.paven.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Yanyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FormPageReqVO extends PageParam {

    @Schema(description = "formId", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long formId;

    @Schema(description = "name", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

}