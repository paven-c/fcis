package com.paven.module.compliance.repository.dto;

import com.paven.common.enums.ComparisonOperator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class FieldConditionDTO {

    private Long id;

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 字段标题
     */
    private String fieldTitle;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 逻辑运算符
     */
    private String logicalOperator;

    /**
     * 操作符
     */
    private String operator;

    /**
     * 值
     */
    private String value;

    /**
     * 或条件集合
     */
    private List<FieldConditionDTO> conditions;

}