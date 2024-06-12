package com.fancy.server.config;

import com.fancy.module.common.config.datapermission.rule.dept.DeptDataPermissionRuleCustomizer;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author paven
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer sysDeptDataPermissionRuleCustomizer() {
        return rule -> {
            // 设置部门字段
            rule.addDeptColumn(User.class, "dept_id");
            rule.addDeptColumn(Dept.class, "id");
        };
    }

}
