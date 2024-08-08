package com.paven.module.compliance.repository.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FromFieldDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段ID
     */
    private Long id;

    /**
     * 字段标题
     */
    private String title;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段配置
     */
    private String configJson;

    /**
     * 字段插槽
     */
    private String slotJson;

    /**
     * 字段样式
     */
    private String styleJson;

    /**
     * 行数限制（多行文本）
     */
    private String autosizeJson;

    /**
     * 组件属性（级联选择）
     */
    private String propsJson;

    /**
     * 是否显示密码
     */
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
    private String prefixIcon;

    /**
     * 后缀图标
     */
    private String suffixIcon;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 组件尺寸大小
     */
    private String size;

    /**
     * 是否显示字数统计
     */
    private Boolean showWordLimit;

    /**
     * 是否显示全选（级联选择）
     */
    private Boolean showAllLevels;

    /**
     * 是否只读
     */
    private Boolean readonly;

    /**
     * 是否关闭
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
    private String activeText;

    /**
     * 关闭状态文字（开关）
     */
    private String inactiveText;

    /**
     * 开启状态颜色（开关）
     */
    private String activeColor;

    /**
     * 关闭状态颜色（开关）
     */
    private String inactiveColor;

    /**
     * 开启状态值（开关）
     */
    private String activeValue;

    /**
     * 关闭状态值（开关）
     */
    private String inactiveValue;

    /**
     * 是否范围选择
     */
    private Boolean isRange;

    /**
     * 范围选择分隔符
     */
    private String rangeSeparator;

    /**
     * 开始日期的占位内容
     */
    private String startPlaceholder;

    /**
     * 结束日期的占位内容
     */
    private String endPlaceholder;

    /**
     * 日期选择器特有的配置项
     */
    private String pickerOptionsJson;

    /**
     * 日期格式
     */
    private String format;

    /**
     * 值格式
     */
    private String valueFormat;

    /**
     * 状态（0停用 1开启）
     */
    private Integer status;

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