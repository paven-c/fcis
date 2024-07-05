package com.paven.common.enums;

import cn.hutool.core.util.ObjUtil;
import com.paven.common.core.IntArrayValuable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IntArrayValuable {

    /**
     * 关闭
     */
    DISABLE(0, "关闭"),

    /**
     * 开启
     */
    ENABLE(1, "开启"),

    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getStatus).toArray();

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static boolean enabled(Integer status) {
        return ObjUtil.equal(ENABLE.status, status);
    }

    public static boolean disabled(Integer status) {
        return ObjUtil.equal(DISABLE.status, status);
    }

}
