package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgUserBalanceDetailType {

    FIRST_LEVEL_AGENT_RECHARGE(1,"一级代理充值",AgUserBalanceDetailRecordType.RECHARGE_RECORDS),
    SECONDARY_AGENT_RECHARGE(2,"二级代理充值",AgUserBalanceDetailRecordType.RECHARGE_RECORDS),
    CONTENT_SERVICE_CONSUMPTION(3,"内容服务消耗",AgUserBalanceDetailRecordType.CONSUMPTION_RECORDS),
    ;

    private final Integer type;
    private final String name;
    private final AgUserBalanceDetailRecordType recordType;

    public static AgUserBalanceDetailType getByType(Integer type) {
        for (AgUserBalanceDetailType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
