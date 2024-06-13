package com.fancy.module.agent.controller.vo;

import lombok.Builder;
import lombok.Data;

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
    private Long amount;

    /**
     * 评论
     */
    private String remarks;
}
