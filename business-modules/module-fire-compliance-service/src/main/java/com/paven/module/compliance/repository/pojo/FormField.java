package com.paven.module.compliance.repository.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
@Builder
@TableName("sys_form_field")
public class FormField implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 建筑属性ID
     */
    private Long formId;

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}