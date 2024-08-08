package com.paven.module.common.repository.pojo.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.component.mybatis.core.dataobject.BasePojo;
import com.paven.component.mybatis.core.typehandler.JsonLongSetTypeHandler;
import com.paven.module.common.enums.permission.DataScopeEnum;
import com.paven.module.common.enums.permission.RoleCodeEnum;
import com.paven.module.common.enums.permission.RoleTypeEnum;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色
 *
 * @author paven
 */
@TableName(value = "role", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Role extends BasePojo {

    /**
     * 角色ID
     */
    @TableId
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
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
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
