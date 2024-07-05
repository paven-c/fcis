package com.paven.module.common.enums.permission;

import com.paven.common.core.IntArrayValuable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举类
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum implements IntArrayValuable {

    /**
     * 全部数据权限
     */
    ALL(1),

    /**
     * 指定部门数据权限
     */
    DEPT_CUSTOM(2),

    /**
     * 部门数据权限
     */
    DEPT_ONLY(3),

    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD(4),

    /**
     * 子部门数据权限
     */
    CHILD(5),

    /**
     * 仅本人数据权限
     */
    SELF(5);

    /**
     * 范围
     */
    private final Integer scope;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(DataScopeEnum::getScope).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
