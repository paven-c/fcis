package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum AgentStatusEnum {

    /**
     * 待提交
     */
    INIT(1, "待提交"),

    /**
     * 待审核
     */
    PENDING_REVIEW(2, "待审核"),

    /**
     * 审核通过
     */
    APPROVED(3, "审核通过"),

    /**
     * 审核不通过
     */
    REJECTED(4, "审核不通过"),

    ;

    private final Integer status;

    private final String desc;

}
