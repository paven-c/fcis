package com.fancy.module.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    /**
     *
     */
    RUNNING(1,"生成中"),
    COMPLETED(2,"已完成"),
    DELIVERED(3,"已交付"),
    ;

    private final Integer type;
    private final String name;

    public static TaskStatusEnum getByType(Integer type) {
        for (TaskStatusEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
    public static TaskStatusEnum getByName(String name) {
        for (TaskStatusEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
