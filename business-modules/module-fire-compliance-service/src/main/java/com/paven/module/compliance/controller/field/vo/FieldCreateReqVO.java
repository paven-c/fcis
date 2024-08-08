package com.paven.module.compliance.controller.field.vo;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
public class FieldCreateReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "表单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "表单ID不能为空")
    private Long formId;

    @Schema(description = "字段标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "字段标题不能为空")
    private String title;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "字段名称不能为空")
    @JsonProperty("__vModel__")
    private String name;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "字段类型不能为空")
    private String type;

    @Schema(description = "字段配置", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("__config__")
    private JSONObject configJson;

    @Schema(description = "字段插槽", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("__slot__")
    private JSONObject slotJson;

    @Schema(description = "字段样式", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("style")
    private JSONObject styleJson;

    @Schema(description = "行数限制", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("autosize")
    private JSONObject autosizeJson;

    @Schema(description = "组件属性（级联选择）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("props")
    private JSONObject propsJson;

    @Schema(description = "是否显示密码", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("show-password")
    private Boolean showPassword;

    @Schema(description = "占位提示文案", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String placeholder;

    @Schema(description = "是否可清空", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Boolean clearable;

    @Schema(description = "前缀图标", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("prefix-icon")
    private String prefixIcon;

    @Schema(description = "后缀图标", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("suffix-icon")
    private String suffixIcon;

    @Schema(description = "最大长度", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("maxlength")
    private Integer maxLength;

    @Schema(description = "是否显示字数统计", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("show-word-limit")
    private Boolean showWordLimit;

    @Schema(description = "组件尺寸大小", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String size;

    @Schema(description = "是否显示全选（级联选择）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("show-all-levels")
    private Boolean showAllLevels;

    @Schema(description = "是否只读", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Boolean readonly;

    @Schema(description = "是否禁用", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Boolean disabled;

    @Schema(description = "是否可搜索", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Boolean filterable;

    @Schema(description = "是否多选", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Boolean multiple;

    @Schema(description = "分隔符", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String separator;

    @Schema(description = "开启状态文字（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("active-text")
    private String activeText;

    @Schema(description = "关闭状态文字（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("inactive-text")
    private String inactiveText;

    @Schema(description = "开启状态颜色（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("active-color")
    private String activeColor;

    @Schema(description = "关闭状态颜色（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("inactive-color")
    private String inactiveColor;

    @Schema(description = "开启状态值（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("active-value")
    private String activeValue;

    @Schema(description = "关闭状态值（开关）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("inactive-value")
    private String inactiveValue;

    @Schema(description = "是否范围选择", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("is-range")
    private Boolean isRange;

    @Schema(description = "范围选择分隔符", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("range-separator")
    private String rangeSeparator;

    @Schema(description = "开始日期的占位内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("start-placeholder")
    private String startPlaceholder;

    @Schema(description = "结束日期的占位内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("end-placeholder")
    private String endPlaceholder;

    @Schema(description = "日期选择器特有的配置项", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("picker-options")
    private JSONObject pickerOptionsJson;

    @Schema(description = "日期格式", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String format;

    @Schema(description = "值格式", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @JsonProperty("value-format")
    private String valueFormat;

    @Schema(description = "字段字典类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String dicType;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Integer sort;

    @Schema(description = "状态（0停用 1启用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private Integer status;

}