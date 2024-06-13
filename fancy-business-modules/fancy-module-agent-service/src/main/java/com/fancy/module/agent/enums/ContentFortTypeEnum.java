package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentFortTypeEnum {
    /**
     *
     */
    PACKAGE(0,"套餐"),
    CONTENT(1,"内容"),
    ;

    private final Integer type;
    private final String name;

    public static ContentFortTypeEnum transToContentService(Integer type) {
        for (ContentFortTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
