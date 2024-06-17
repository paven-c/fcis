package com.fancy.module.agent.controller.vo;

import com.fancy.common.pojo.PageParam;
import com.fancy.common.util.date.DateUtils;
import com.fancy.common.validation.InEnum;
import com.fancy.module.agent.enums.AgentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author paven
 */
@Schema(description = "代理商分页RequestVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentPageReqVO extends PageParam {

    @Schema(description = "代理商ID，精确匹配", example = "")
    private Long agentId;

    @Schema(description = "代理商名称，模糊匹配", example = "")
    private String agentName;

    @Schema(description = "手机号码，精确匹配", example = "")
    private String mobile;

    @Schema(description = "展示状态, AgentStatusEnum", example = "1")
    private Integer status;

    @Schema(description = "代理等级", example = "")
    @InEnum(value = AgentStatusEnum.class, message = "代理商类型必须是 {status}")
    private Integer level;

    @Schema(description = "创建时间开始", example = "")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "创建时间结束", example = "")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    private Boolean isCompanyRole;

}
