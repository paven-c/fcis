package com.paven.module.compliance.controller.field.vo;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FieldRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段ID
     */
    private Long id;

    /**
     * 字段标题
     */
    @JsonProperty("label")
    private String title;

    /**
     * 字段名称
     */
    @JsonProperty("__vModel__")
    private String name;

    /**
     * 字段配置
     */
    @JsonProperty("__config__")
    private JSONObject configJson;

    /**
     * 字段插槽
     */
    @JsonProperty("__slot__")
    private JSONObject slotJson;

    /**
     * 字段样式
     */
    @JsonProperty("style")
    private JSONObject styleJson;

    /**
     * 行数限制
     */
    @JsonProperty("autosize")
    private JSONObject autosizeJson;

    /**
     * 组件属性（级联选择）
     */
    @JsonProperty("props")
    private JSONObject propsJson;

    /**
     * 是否显示密码
     */
    @JsonProperty("show-password")
    private Boolean showPassword;

    /**
     * 占位提示文案
     */
    private String placeholder;

    /**
     * 是否可清空
     */
    private Boolean clearable;

    /**
     * 前缀图标
     */
    @JsonProperty("prefix-icon")
    private String prefixIcon;

    /**
     * 后缀图标
     */
    @JsonProperty("suffix-icon")

    private String suffixIcon;

    /**
     * 最大长度
     */
    @JsonProperty("maxlength")
    private Integer maxLength;

    /**
     * 是否显示字数统计
     */
    @JsonProperty("show-word-limit")
    private Boolean showWordLimit;

    /**
     * 组件尺寸大小
     */
    private String size;

    /**
     * 是否显示全选（级联选择）
     */
    @JsonProperty("show-all-levels")
    private Boolean showAllLevels;

    /**
     * 是否只读
     */
    private Boolean readonly;

    /**
     * 是否禁用
     */
    private Boolean disabled;

    /**
     * 是否可搜索
     */
    private Boolean filterable;

    /**
     * 是否多选
     */
    private Boolean multiple;

    /**
     * 分隔符
     */
    private String separator;

    /**
     * 开启状态文字（开关）
     */
    @JsonProperty("active-text")
    private String activeText;

    /**
     * 关闭状态文字（开关）
     */
    @JsonProperty("inactive-text")
    private String inactiveText;

    /**
     * 开启状态颜色（开关）
     */
    @JsonProperty("active-color")
    private String activeColor;

    /**
     * 关闭状态颜色（开关）
     */
    @JsonProperty("inactive-color")
    private String inactiveColor;

    /**
     * 开启状态值（开关）
     */
    @JsonProperty("active-value")
    private String activeValue;

    /**
     * 关闭状态值（开关）
     */
    @JsonProperty("inactive-value")
    private String inactiveValue;

    /**
     * 是否范围选择
     */
    @JsonProperty("is-range")
    private Boolean isRange;

    /**
     * 范围选择分隔符
     */
    @JsonProperty("range-separator")
    private String rangeSeparator;

    /**
     * 开始日期的占位内容
     */
    @JsonProperty("start-placeholder")
    private String startPlaceholder;

    /**
     * 结束日期的占位内容
     */
    @JsonProperty("end-placeholder")
    private String endPlaceholder;

    /**
     * 日期选择器特有的配置项
     */
    @JsonProperty("picker-options")
    private JSONObject pickerOptionsJson;

    /**
     * 日期格式
     */
    private String format;

    /**
     * 值格式
     */
    @JsonProperty("value-format")
    private String valueFormat;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段字典类型
     */
    private String dicType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 表单ID
     */
    private Long formId;

}