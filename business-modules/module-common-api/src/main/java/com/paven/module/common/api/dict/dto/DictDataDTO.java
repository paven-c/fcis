package com.paven.module.common.api.dict.dto;

import com.paven.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 字典数据 Response DTO
 *
 * @author paven
 */
@Data
public class DictDataDTO {

    private Long id;

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 状态 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
