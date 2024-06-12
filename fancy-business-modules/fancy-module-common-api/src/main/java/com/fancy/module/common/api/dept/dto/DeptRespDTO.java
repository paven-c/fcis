package com.fancy.module.common.api.dept.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class DeptRespDTO {

    /**
     * 部门ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态
     * <p>
     * {@link com.fancy.common.enums.CommonStatusEnum}
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
