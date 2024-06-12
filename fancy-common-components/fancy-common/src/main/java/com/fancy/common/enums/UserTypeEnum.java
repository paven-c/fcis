package com.fancy.common.enums;

import cn.hutool.core.util.ArrayUtil;
import com.fancy.common.core.IntArrayValuable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局用户类型枚举
 *
 * @author paven
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum implements IntArrayValuable {

    /**
     * 代理平台用户
     */
    AGENT(1, "代理平台用户");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(UserTypeEnum::getValue).toArray();

    /**
     * 类型
     */
    private final Integer value;

    /**
     * 类型名
     */
    private final String name;

    public static UserTypeEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), UserTypeEnum.values());
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
