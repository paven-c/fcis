package com.fancy.module.agent.controller.vo;

import static com.fancy.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import com.fancy.common.pojo.PageParam;
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

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "部门编号，同时筛选子部门", example = "")
    private Long deptId;

}
