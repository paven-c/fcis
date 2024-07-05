package com.paven.module.compliance.controller.form.vo;

import com.paven.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Yanyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FormCheckReqVO extends PageParam {

    @Schema(description = "formId", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long formId;

    @Schema(description = "data", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Map<String,Object> data;

}