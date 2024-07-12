package com.paven.component.datapermission.config;

import com.paven.component.datapermission.core.rule.dept.DeptDataPermissionRule;
import com.paven.component.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.paven.module.common.api.permission.PermissionApi;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import com.paven.component.security.core.LoginUser;

/**
 * 基于部门的数据权限 AutoConfiguration
 *
 * @author paven
 */
@AutoConfiguration
@ConditionalOnClass(LoginUser.class)
@ConditionalOnBean(value = {PermissionApi.class, DeptDataPermissionRuleCustomizer.class})
public class DeptDataPermissionAutoConfiguration {

    @Bean
    public DeptDataPermissionRule deptDataPermissionRule(PermissionApi permissionApi, List<DeptDataPermissionRuleCustomizer> customizers) {
        // 创建 DeptDataPermissionRule 对象
        DeptDataPermissionRule rule = new DeptDataPermissionRule(permissionApi);
        // 补全表配置
        customizers.forEach(customizer -> customizer.customize(rule));
        return rule;
    }

}
