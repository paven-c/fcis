package com.fancy.module.agent.controller.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author paven
 */
@Data
@Builder
public class AgentRechargeReqVO {

    /**
     * 代理商ID
     */
    private Long agentId;

    /**
     * 充值金额
     */
    @Min(value = 1, message = "充值金额不能小于1")
    @Max(value = 999999999, message = "充值金额不能大于999999999")
    private Long amount;

    /**
     * 评论
     */
    @Length(max = 50, message = "备注最多支持50字符")
    private String remarks;
}
