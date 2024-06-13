package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentServiceTypeEnum {
    /**
     *
     */
    PIC(1,"图片"),
    VIDEO(2,"视频"),
    PACKAGE(3,"套餐"),
    ;

    private final Integer type;
    private final String name;

    public static ContentServiceTypeEnum getByType(Integer type) {
        for (ContentServiceTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
