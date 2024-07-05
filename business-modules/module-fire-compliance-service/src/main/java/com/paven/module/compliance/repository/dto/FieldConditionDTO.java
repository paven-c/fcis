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

    /**
     * 字段名称
     */
    @NotEmpty(message = "字段名称不能为空")
    private String fieldName;

    /**
     * 操作符
     */
    @NotNull(message = "操作符不能为空")
    private ComparisonOperator operator;

    /**
     * 值
     */
    private Object value;

    /**
     * 或条件集合
     */
    private List<FieldConditionDTO> conditions;

}