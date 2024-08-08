package com.paven.common.enums;

/**
 * @author paven
 */
public enum ComparisonOperator {

    /**
     * 等于
     */
    EQUALS,

    /**
     * 不等于
     */
    NOT_EQUALS,

    /**
     * 大于
     */
    GREATER_THAN,

    /**
     * 大于等于
     */
    GREATER_THAN_OR_EQUALS,

    /**
     * 小于
     */
    LESS_THAN,

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUALS,

    /**
     * 包含
     */
    CONTAINS,
    ;

    public static ComparisonOperator findByName(String operator) {
        for (ComparisonOperator value : ComparisonOperator.values()) {
            if (value.name().equals(operator)) {
                return value;
            }
        }
        return null;
    }
}
