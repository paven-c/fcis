package com.fancy.module.agent.convert.merchant;


import com.fancy.module.agent.controller.req.EditAgMerchantOrderReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author paven
 */
@Mapper
public interface AgMerchantOrderConvert {

    AgMerchantOrderConvert INSTANCE = Mappers.getMapper(AgMerchantOrderConvert.class);

    AgMerchantOrder convertAgMerchantOrder(EditAgMerchantOrderReq req,Long creatorId,Long deptId);
    List<AgMerchantOrderDetail> convertAgMerchantOrder(List<EditAgMerchantOrderReq.OrderDetail> orderDetails);

    List<AgMerchantOrderVo> convertAgMerchantOrderVo(List<AgMerchantOrder> agMerchantOrders);
}
