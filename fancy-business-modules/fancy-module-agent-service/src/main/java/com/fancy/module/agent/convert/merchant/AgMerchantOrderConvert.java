package com.fancy.module.agent.convert.merchant;


import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderDetailVo;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.api.content.Dto.CmsMerchantReqDto;
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

    @Mapping(target = "serviceName", source = "name")
    AgMerchantOrderDetailVo convertAgMerchantOrderDetailVo(AgMerchantOrderDetail agMerchantOrderDetail);
    List<AgMerchantOrderDetailVo> convertAgMerchantOrderDetailVo(List<AgMerchantOrderDetail> agMerchantOrderDetails);
}
