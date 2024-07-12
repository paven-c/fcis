package com.paven.module.common.repository.pojo.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.paven.component.mybatis.core.dataobject.BasePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户和角色关联
 *
 * @author paven
 */
@TableName("user_role")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BasePojo {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 角色 ID
     */
    private Long roleId;

}
