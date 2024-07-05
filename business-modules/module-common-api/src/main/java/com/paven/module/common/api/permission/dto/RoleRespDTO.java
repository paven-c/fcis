package com.paven.module.common.api.permission.dto;

import com.paven.common.enums.CommonStatusEnum;
import com.paven.module.common.enums.permission.DataScopeEnum;
import com.paven.module.common.enums.permission.RoleCodeEnum;
import com.paven.module.common.enums.permission.RoleTypeEnum;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色DTO
 *
 * @author paven
 */
@Data
@Accessors(chain = true)
public class RoleRespDTO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色标识
     * <p>
     * {@link RoleCodeEnum}
     */
    private String code;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 数据范围
     * <p>
     * 枚举 {@link DataScopeEnum}
     */
    private Integer dataScope;

    /**
     * 数据范围(指定部门数组)
     * <p>
     * 适用于 {@link #dataScope} 的值为 {@link DataScopeEnum#DEPT_CUSTOM} 时
     */
    private Set<Long> dataScopeDeptIds;

    /**
     * 角色状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 角色类型
     * <p>
     * 枚举 {@link RoleTypeEnum}
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

}
