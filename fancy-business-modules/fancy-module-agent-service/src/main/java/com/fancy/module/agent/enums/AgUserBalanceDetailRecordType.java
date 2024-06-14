package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AgUserBalanceDetailRecordType {

    RECHARGE_RECORDS(0,"充值记录"),
    CONSUMPTION_RECORDS(1,"消耗记录"),
    ;

    private final Integer type;
    private final String name;

    public static AgUserBalanceDetailRecordType getByType(Integer type) {
        for (AgUserBalanceDetailRecordType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
