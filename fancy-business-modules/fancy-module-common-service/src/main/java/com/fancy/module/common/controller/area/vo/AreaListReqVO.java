package com.fancy.module.common.controller.area.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author paven
 */
@Schema(description = "区域查询VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaListReqVO {

    @Schema(description = "父区域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "父区域ID不能为空")
    private String parentAreaId;

}