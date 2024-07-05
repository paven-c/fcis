package com.paven.module.compliance.controller.field.vo;

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
    private String title;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private Integer type;

    /**
     * 状态（0停用 1启用）
     */
    private Integer status;

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