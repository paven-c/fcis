package com.fancy.common.enums;

import cn.hutool.core.util.ObjUtil;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除状态
 *
 * @author Yanyi
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    /**
     * 使用中
     */
    ACTIVATED(0, "使用中"),

    /**
     * 已删除
     */
    DELETED(1, "已删除"),

    ;

    private final Integer status;

    private final String desc;

    public static boolean isActivated(Boolean deleted) {
        return ObjUtil.equal(ACTIVATED.getStatus(), deleted);
    }

    public static String getDescByStatus(Integer status) {
        return Arrays.stream(values()).filter(enumItem -> enumItem.getStatus().equals(status))
                .findFirst()
                .map(DeleteStatusEnum::getDesc)
                .orElse(null);
    }
}