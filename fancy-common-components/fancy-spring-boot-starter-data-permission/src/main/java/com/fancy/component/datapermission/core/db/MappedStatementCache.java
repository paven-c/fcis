package com.fancy.component.datapermission.core.db;

import cn.hutool.core.collection.CollUtil;
import com.fancy.common.util.collection.SetUtils;
import com.fancy.component.datapermission.core.rule.DataPermissionRule;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * MappedStatementCache 记录DataPermissionRule 规则，对指定 MappedStatement 无需重写
 *
 * @author paven
 */
public class MappedStatementCache {

    /**
     * 指定数据权限规则，对指定 MappedStatement 无需重写的缓存
     * <p>
     * value：{@link MappedStatement#getId()} 编号
     */
    @Getter
    private final Map<Class<? extends DataPermissionRule>, Set<String>> noRewritableMappedStatements = new ConcurrentHashMap<>();

    /**
     * 判断是否无需重写
     *
     * @param ms    MappedStatement
     * @param rules 数据权限规则数组
     * @return 是否无需重写
     */
    public boolean noRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
        if (CollUtil.isEmpty(rules)) {
            return true;
        }
        for (DataPermissionRule rule : rules) {
            Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
            if (!CollUtil.contains(mappedStatementIds, ms.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 添加无需重写的 MappedStatement
     *
     * @param ms    MappedStatement
     * @param rules 数据权限规则数组
     */
    public void addNoRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
        for (DataPermissionRule rule : rules) {
            Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
            if (CollUtil.isNotEmpty(mappedStatementIds)) {
                mappedStatementIds.add(ms.getId());
            } else {
                noRewritableMappedStatements.put(rule.getClass(), SetUtils.asSet(ms.getId()));
            }
        }
    }

}