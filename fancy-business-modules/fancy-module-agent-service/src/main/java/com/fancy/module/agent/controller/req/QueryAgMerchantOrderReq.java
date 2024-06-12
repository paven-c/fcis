package com.fancy.module.agent.controller.req;

import com.fancy.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class QueryAgMerchantOrderReq extends PageParam {

    /**
     * 客户id
     */
    private Long agMerchantId;
    /**
     * 客户名称
     */
    private String name;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
