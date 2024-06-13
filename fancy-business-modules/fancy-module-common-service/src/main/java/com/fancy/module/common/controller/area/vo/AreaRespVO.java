package com.fancy.module.common.controller.area.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class AreaRespVO {

    @Schema(description = "区域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "父区域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long parentId;

    @Schema(description = "区域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String areaName;

}