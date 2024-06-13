package com.fancy.module.agent.convert.balance;


import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.agent.controller.vo.AgUserBalanceDetailVo;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import com.fancy.module.common.api.content.Dto.CmsMerchantReqDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author paven
 */
@Mapper
public interface AgUserBalanceConvert {

    AgUserBalanceConvert INSTANCE = Mappers.getMapper(AgUserBalanceConvert.class);


    @Mapping(target = "objectType",expression = "java(req.getObjectType().getType())")
    @Mapping(target = "billTypeAgUserId",source = "req.fromAgUserId")
    @Mapping(target = "billTypeAgUserName",source = "req.fromUserName")
    @Mapping(target = "name",source = "req.toAgUsername")
    @Mapping(target = "agUserId",source = "req.toAgUserId")
    AgUserBalanceDetail convertAgUserBalanceDetail(EditAgUserBalanceDetailReq req,Integer billType, BigDecimal beforePrice,BigDecimal afterPrice);

    List<AgUserBalanceDetailVo> convertAgUserBalanceDetailVo(List<AgUserBalanceDetail> list);
    AgUserBalanceDetailVo convertAgUserBalanceDetailVo(AgUserBalanceDetail agUserBalanceDetail);
}
