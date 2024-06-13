package com.fancy.module.agent.controller.req;

import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EditAgMerchantOrderReq extends AgMerchantOrder {

    /**
     * 订单明细
     */
    private List<AgMerchantOrderDetail> orderDetailList;
}
