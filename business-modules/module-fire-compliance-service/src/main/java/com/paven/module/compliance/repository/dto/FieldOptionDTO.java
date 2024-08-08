package com.paven.module.compliance.repository.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Yanyi
 */
@Data
public class FieldOptionDTO implements Serializable {

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 字典名称
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

}
