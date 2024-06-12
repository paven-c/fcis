package com.fancy.module.common.api.dept.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author paven
 */
@Builder
@Data
public class DeptSaveReqDTO {

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
     * 邮箱
     */
    private String email;

    /**
     * 状态
     * <p>
     * {@link com.fancy.common.enums.CommonStatusEnum}
     */
    private Integer status;

}
