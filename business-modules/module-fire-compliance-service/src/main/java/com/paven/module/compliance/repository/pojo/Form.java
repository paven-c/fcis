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
@TableName("sys_form")
public class Form extends BasePojo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 建筑属性ID
     */
    private Long id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 字段条件
     */
    private String conditions;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

}