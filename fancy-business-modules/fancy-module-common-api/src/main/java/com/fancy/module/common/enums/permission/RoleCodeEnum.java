package com.fancy.module.common.enums.permission;

import cn.hutool.core.util.StrUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色标识枚举
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("super_admin", "超级管理员"),

    /**
     * 财务
     */
    FINANCE("finance", "财务"),

    /**
     * 项目经理
     */
    PROJECT_MANAGER("project_manager", "项目经理"),

    /**
     * 运营
     */
    OPERATIONS_STAFF("operations_staff", "运营"),

    /**
     * 一级代理
     */
    FIRST_LEVEL_AGENT("first_level_agent", "一级代理"),

    /**
     * 二级代理
     */
    SECOND_LEVEL_AGENT("second_level_agent", "二级代理"),

    /**
     * 客户
     */
    CUSTOMER("customer", "客户"),

    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    private static final List<String> AGENT_ROLES = List.of(FIRST_LEVEL_AGENT.getCode(), SECOND_LEVEL_AGENT.getCode());

    public static boolean isSuperAdmin(String code) {
        return StrUtil.equals(code, SUPER_ADMIN.getCode());
    }

    public static boolean isAgent(String code) {
        return AGENT_ROLES.contains(code);
    }

    public static List<String> getAgent() {
        return List.of(FIRST_LEVEL_AGENT.getCode(), SECOND_LEVEL_AGENT.getCode());
    }

}
