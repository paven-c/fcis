package com.paven.component.datapermission.core.rule;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.paven.component.datapermission.core.annotation.DataPermission;
import com.paven.component.datapermission.core.aop.DataPermissionContextHolder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * 默认的 DataPermissionRuleFactoryImpl 实现类 支持通过 {@link DataPermissionContextHolder} 过滤数据权限
 *
 * @author paven
 */
@RequiredArgsConstructor
public class DataPermissionRuleFactory {

    /**
     * 数据权限规则数组
     */
    private final List<DataPermissionRule> rules;

    public List<DataPermissionRule> getDataPermissionRules() {
        return rules;
    }

    public List<DataPermissionRule> getDataPermissionRule(String mappedStatementId) {
        if (CollUtil.isEmpty(rules)) {
            return Collections.emptyList();
        }
        DataPermission dataPermission = DataPermissionContextHolder.get();
        if (dataPermission == null) {
            return rules;
        }
        // 启用状态
        if (!dataPermission.enable()) {
            return Collections.emptyList();
        }
        // 指定规则
        if (ArrayUtil.isNotEmpty(dataPermission.includeRules())) {
            return rules.stream().filter(rule -> ArrayUtil.contains(dataPermission.includeRules(), rule.getClass())).collect(Collectors.toList());
        }
        // 排除规则
        if (ArrayUtil.isNotEmpty(dataPermission.excludeRules())) {
            return rules.stream().filter(rule -> !ArrayUtil.contains(dataPermission.excludeRules(), rule.getClass())).collect(Collectors.toList());
        }
        // 全部规则
        return rules;
    }

}
