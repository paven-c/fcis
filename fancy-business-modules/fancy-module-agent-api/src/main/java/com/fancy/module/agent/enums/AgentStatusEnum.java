package com.fancy.module.agent.enums;

import com.fancy.common.core.IntArrayValuable;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author paven
 */
@Getter
@AllArgsConstructor
public enum AgentStatusEnum implements IntArrayValuable {

    /**
     * 待提交
     */
    INIT(1, "待提交"),

    /**
     * 待审核
     */
    PENDING_REVIEW(2, "待审核"),

    /**
     * 审核通过
     */
    APPROVED(3, "审核通过"),

    /**
     * 审核拒绝
     */
    REJECTED(4, "审核拒绝"),

    ;

    private final Integer status;

    private final String desc;

    private static final List<Integer> ACTIVITY_STATUS = Lists.newArrayList(APPROVED.getStatus());

    /**
     * 企业角色可见状态
     */
    public static final List<Integer> COMPANY_AVAILABLE_STATUS = Lists.newArrayList(PENDING_REVIEW.getStatus(), APPROVED.getStatus(), REJECTED.getStatus());

    public static boolean isActivityStatus(Integer status) {
        return ACTIVITY_STATUS.contains(status);
    }

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(AgentStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
