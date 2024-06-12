package com.fancy.module.common.repository.pojo.dept;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.component.mybatis.core.dataobject.BasePojo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门表
 *
 * @author paven
 */
@TableName("dept")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Dept extends BasePojo {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 部门ID
     */
    @TableId
    private Long id;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 父部门ID
     * <p>
     * 关联 {@link #id}
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 部门状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
