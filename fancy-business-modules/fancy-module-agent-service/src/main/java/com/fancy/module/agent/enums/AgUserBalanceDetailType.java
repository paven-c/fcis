package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgUserBalanceDetailType {

    FIRST_LEVEL_AGENT_RECHARGE(1,"一级代理充值"),
    SECONDARY_AGENT_RECHARGE(2,"二级代理充值"),
    CONTENT_SERVICE_CONSUMPTION(3,"内容服务消耗"),
    ;

    private final Integer type;
    private final String name;

    public static AgUserBalanceDetailType getByType(Integer type) {
        for (AgUserBalanceDetailType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
