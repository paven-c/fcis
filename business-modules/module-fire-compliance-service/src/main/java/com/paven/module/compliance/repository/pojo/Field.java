package com.paven.module.compliance.repository.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.paven.component.mybatis.core.dataobject.BasePojo;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Yanyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("sys_field")
public class Field extends BasePojo {

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
     *
     * @see com.paven.common.enums.FieldTypeEnum
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
     * 删除时间
     */
    private LocalDateTime deletedTime;

}