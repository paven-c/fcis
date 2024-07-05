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
    TEXT(1, "单行文本"),

    /**
     * 多行文本
     */
    TEXTAREA(2, "多行文本"),

    /**
     * 数字
     */
    NUMBER(3, "数字"),

    /**
     * 单选
     */
    RADIO(4, "单选"),

    /**
     * 复选
     */
    CHECKBOX(5, "多选"),

    /**
     * 下拉
     */
    DROPDOWN(6, "下拉列表"),

    /**
     * 日期选择器
     */
    DATE(7, "日期选择器"),

    /**
     * 日期时间选择器
     */
    DATETIME(8, "日期时间选择器"),

    /**
     * 文件上传
     */
    FILE(9, "文件上传"),

    /**
     * 开关
     */
    SWITCH(10, "开关"),

    /**
     * 密码
     */
    PASSWORD(11, "密码"),

    /**
     * 邮箱
     */
    EMAIL(12, "邮箱"),

    /**
     * 手机
     */
    PHONE(13, "手机"),

    /**
     * URL
     */
    URL(14, "URL"),

    /**
     * 隐藏
     */
    HIDDEN(15, "隐藏字段"),
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(FieldTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 状态名
     */
    private final String name;

    public static final FieldTypeEnum[] DICT_FIELDS = {RADIO, CHECKBOX, DROPDOWN};

    /**
     * 是否字典字段
     *
     * @param type 字段类型
     * @return 是否字典字段
     */
    public static boolean isDictField(Integer type) {
        return Arrays.stream(DICT_FIELDS).anyMatch(fieldType -> fieldType.getType().equals(type));
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

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
