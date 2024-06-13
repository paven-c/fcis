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

    @Mapping(target = "serviceName", source = "orderName")
    AgMerchantOrder.AgMerchantOrderDetailVo convertAgMerchantOrderDetailVo(AgMerchantOrderDetail agMerchantOrderDetail);
    List<AgMerchantOrder.AgMerchantOrderDetailVo> convertAgMerchantOrderDetailVo(List<AgMerchantOrderDetail> agMerchantOrderDetails);
    @Mapping(target = "status", defaultValue = "0")
    AgMerchantOrder convertAgMerchantOrder(EditAgMerchantOrderReq req,Long creatorId,Long deptId);
    @Mapping(target = "serviceStatus", defaultValue = "0")
    AgMerchantOrderDetail convertAgMerchantOrder(EditAgMerchantOrderReq.OrderDetail orderDetail,Long creatorId,Long deptId);
    List<AgMerchantOrderDetail> convertAgMerchantOrder(List<EditAgMerchantOrderReq.OrderDetail> orderDetails,Long creatorId,Long deptId);

    List<AgMerchantOrderVo> convertAgMerchantOrderVo(List<AgMerchantOrder> agMerchantOrders);
    @Mapping(target = "agMerchantName", source = "name")
    @Mapping(target = "serviceName", source = "orderName")
    AgMerchantOrderVo convertAgMerchantOrderVo(AgMerchantOrder agMerchantOrders);
}
