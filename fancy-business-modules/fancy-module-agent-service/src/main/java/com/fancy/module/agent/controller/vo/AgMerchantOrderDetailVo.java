package com.fancy.module.agent.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgMerchantOrderDetailVo {
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 服务内容
     */
    private String serviceName;
    /**
     * 服务任务总数
     */
    private Long serviceTotalNum;

}
