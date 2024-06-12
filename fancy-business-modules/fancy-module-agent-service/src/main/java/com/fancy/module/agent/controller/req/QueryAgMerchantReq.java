package com.fancy.module.agent.controller.req;

import com.fancy.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class QueryAgMerchantReq extends PageParam {

    private Long agMerchantId;

    private String name;
    /**
     * 授权状态
     */
    private Integer authStatus;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
