package com.paven.component.datapermission.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.paven.component.datapermission.core.aop.DataPermissionAnnotationAdvisor;
import com.paven.component.datapermission.core.db.DataPermissionMappedStatementInterceptor;
import com.paven.component.datapermission.core.rule.DataPermissionRule;
import com.paven.component.datapermission.core.rule.DataPermissionRuleFactory;
import com.paven.component.mybatis.core.util.MyBatisUtils;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限的自动配置类
 *
 * @author paven
 */
@AutoConfiguration
public class DataPermissionAutoConfiguration {

    @Bean
    public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
        return new DataPermissionRuleFactory(rules);
    }

    @Bean
    public DataPermissionMappedStatementInterceptor dataPermissionDatabaseInterceptor(MybatisPlusInterceptor mybatisPlusInterceptor,
            DataPermissionRuleFactory ruleFactory) {
        // 创建 DataPermissionDatabaseInterceptor 拦截器
        DataPermissionMappedStatementInterceptor interceptor = new DataPermissionMappedStatementInterceptor(ruleFactory);
        // 添加到 interceptor 中
        MyBatisUtils.addInterceptor(mybatisPlusInterceptor, interceptor, 0);
        return interceptor;
    }

    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }

}
