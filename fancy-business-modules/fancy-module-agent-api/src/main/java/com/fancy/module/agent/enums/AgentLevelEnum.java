package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum AgentLevelEnum {

    /**
     * 一级代理
     */
    FIRST_LEVEL(1, "一级代理"),

    /**
     * 二级代理
     */
    SECOND_LEVEL(2, "二级代理"),

    ;

    private final Integer type;

    private final String desc;

}
