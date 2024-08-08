package com.paven.module.compliance.repository.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.paven.module.compliance.repository.dto.FieldConditionDTO;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;


/**
 * @author Yanyi
 */
@Data
@Builder
@TableName("sys_form_rule")
public class FormRule implements Serializable {

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
     * 操作符
     */
    private String operator;

    /**
     * 值
     */
    private String value;

    /**
     * 父ID
     */
    private Long parentId;

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