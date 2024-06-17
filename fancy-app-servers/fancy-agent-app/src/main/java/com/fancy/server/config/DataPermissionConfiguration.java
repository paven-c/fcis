package com.fancy.server.config;

import com.fancy.component.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.repository.pojo.AgOrderTask;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import com.fancy.module.agent.repository.pojo.agent.Agent;
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
            rule.addDeptColumn(Agent.class, "dept_id");
            rule.addDeptColumn(AgUserBalanceDetail.class, "dept_id");
        };
    }

}
