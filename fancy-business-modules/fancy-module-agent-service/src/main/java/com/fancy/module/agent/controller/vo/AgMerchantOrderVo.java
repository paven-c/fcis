package com.fancy.module.agent.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgMerchantOrderVo {
    /**
     * 订单ID
     */
    private Long id;
    /**
     * 订单消耗点数
     */
    private BigDecimal orderMoney;

    /**
     * 服务内容
     */
    private String serviceName;

    /**
     * 代理商户id
     */
    private Long agMerchantId;

    /**
     * 代理商户名称
     */
    private String agMerchantName;
    /**
     * 所属代理
     */
    private String agUserName;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    private List<AgMerchantOrderDetailVo> orderDetailList; //<订单明细>
}
