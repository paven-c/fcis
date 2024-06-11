package com.fancy.module.common.vo;

import com.fancy.module.common.repository.pojo.AgMerchantOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgMerchantOrderVo extends AgMerchantOrder {
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
    private String createTime;
}
