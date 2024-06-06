package com.fancy.common.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(0, "女"),

    /**
     * 未知
     */
    UNKNOWN(-1, "未知");

    private final Integer code;

    private final String desc;

    public static GenderEnum findByCode(Integer code) {
        return Arrays.stream(GenderEnum.values()).filter(genderEnum -> genderEnum.getCode().equals(code)).findFirst().orElse(UNKNOWN);
    }
}
