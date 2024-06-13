package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgUserBalanceDetailBillType {

    PAYMENT_OUT(0,"出账"),
    ACCOUNTING(1,"入账"),
    ;

    private final Integer type;
    private final String name;

    public static AgUserBalanceDetailBillType getByType(Integer type) {
        for (AgUserBalanceDetailBillType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
