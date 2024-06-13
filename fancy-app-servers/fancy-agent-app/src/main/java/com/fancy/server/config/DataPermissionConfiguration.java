package com.fancy.server.config;

import com.fancy.module.agent.repository.pojo.agent.Agent;
import com.fancy.component.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模块的数据权限 Configuration
 *
 * @author paven
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer deptDataPermissionRuleCustomizer() {
        return rule -> {
            rule.addDeptColumn(User.class, "dept_id");
            rule.addDeptColumn(Agent.class, "dept_id");
        };
    }

}
