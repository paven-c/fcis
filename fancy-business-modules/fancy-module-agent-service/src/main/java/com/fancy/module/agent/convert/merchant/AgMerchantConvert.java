package com.fancy.module.agent.convert.merchant;


import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.common.api.content.dto.CmsMerchantReqDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author paven
 */
@Mapper
public interface AgMerchantConvert {

    AgMerchantConvert INSTANCE = Mappers.getMapper(AgMerchantConvert.class);

    @Mapping(target = "merchantName", source = "name")
    @Mapping(target = "storeId", source = "shopId")
    @Mapping(target = "phone", source = "contactsPhone")
    CmsMerchantReqDto convertCmsMerchantReqDto(EditAgMerchantReq req);
    AgMerchant convertAgMerchant(EditAgMerchantReq req);
    AgMerchantVo convertAgMerchantVo(AgMerchant agMerchant);


    List<AgMerchantVo> convertList(List<AgMerchant> list);
}
