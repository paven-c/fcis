package com.fancy.module.common.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author paven
 */

@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /**
     * 预置角色
     */
    PRESET(1),

    /**
     * 自定义角色
     */
    CUSTOM(2);

    private final Integer type;

}
