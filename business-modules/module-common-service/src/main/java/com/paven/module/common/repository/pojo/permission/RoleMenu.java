package com.paven.module.common.repository.pojo.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.paven.component.mybatis.core.dataobject.BasePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单关联
 *
 * @author paven
 */
@TableName("role_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenu extends BasePojo {

    /**
     * 自增主键
     */
    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
