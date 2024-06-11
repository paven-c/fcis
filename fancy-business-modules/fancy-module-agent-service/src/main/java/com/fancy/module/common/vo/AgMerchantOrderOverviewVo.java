package com.fancy.module.common.vo;

import com.fancy.module.common.repository.pojo.AgMerchant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgMerchantOrderOverviewVo {
    /**
     * 下单客户数
     */
    private Integer numberOfCustomersOrdered;

    /**
     * 订单数
     */
    private Integer numberOfOrders;

    /**
     * 订单消耗
     */
    private Integer orderConsumption;
}
