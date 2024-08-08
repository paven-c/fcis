package com.paven.common.enums;

import com.paven.common.core.IntArrayValuable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型枚举
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum FieldTypeEnum implements IntArrayValuable {

    /**
     * 单行文本
     */
    TEXT(1, "input", "单行文本"),

    /**
     * 多行文本
     */
    TEXTAREA(2, "textarea", "多行文本"),

    /**
     * 密码
     */
    PASSWORD(3, "password", "密码"),

    /**
     * 下拉选择
     */
    SELECT(4, "select", "下拉选择"),

    /**
     * 级联选择
     */
    CASCADER(5, "cascader", "级联选择"),

    /**
     * 单选框组
     */
    RADIO(6, "radio", "单选框组"),

    /**
     * 多选框组
     */
    CHECKBOX(7, "checkbox", "多选框组"),

    /**
     * 开关
     */
    SWITCH(8, "switch", "开关"),

    /**
     * 时间选择
     */
    TIME_PICKER(9, "time", "时间选择"),

    /**
     * 时间范围
     */
    TIME_RANGE(10, "time-range", "时间范围"),

    /**
     * 日期选择
     */
    DATE(11, "date", "日期选择"),

    /**
     * 日期范围
     */
    DATE_RANGE(12, "date-range", "日期范围"),
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(FieldTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 字段code
     */
    private final String code;

    /**
     * 字段描述
     */
    private final String desc;

    public static final FieldTypeEnum[] DICT_FIELDS = {RADIO, CHECKBOX, SELECT};

    /**
     * 是否字典字段
     *
     * @param code 字段类型
     * @return 是否字典字段
     */
    public static boolean isDictField(String code) {
        return Arrays.stream(DICT_FIELDS).anyMatch(fieldType -> fieldType.getCode().equals(code));
    }

    /**
     * 根据类型获取枚举
     *
     * @param type 字段类型
     * @return 字段类型枚举
     */
    public static FieldTypeEnum findByType(Integer type) {
        return Arrays.stream(values()).filter(fieldType -> fieldType.getType().equals(type)).findFirst()
                .orElse(null);
    }

    public static FieldTypeEnum findByCode(String code) {
        return Arrays.stream(values()).filter(fieldType -> fieldType.getCode().equals(code)).findFirst()
                .orElse(null);
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
