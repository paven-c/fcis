package com.fancy.module.agent.convert.balance;


import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.vo.AgUserBalanceDetailVo;
import com.fancy.module.agent.controller.vo.UserBalanceRespVO;
import com.fancy.module.agent.repository.pojo.AgUserBalance;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface AgUserBalanceConvert {

    AgUserBalanceConvert INSTANCE = Mappers.getMapper(AgUserBalanceConvert.class);


    @Mapping(target = "objectType", expression = "java(req.getObjectType().getType())")
    @Mapping(target = "billTypeAgUserId", source = "req.fromAgUserId")
    @Mapping(target = "billTypeAgUserName", source = "req.fromUserName")
    @Mapping(target = "name", source = "req.toAgUsername")
    @Mapping(target = "agUserId", source = "req.toAgUserId")
    AgUserBalanceDetail convertAgUserBalanceDetail(EditAgUserBalanceDetailReq req, Integer billType, BigDecimal beforePrice, BigDecimal afterPrice);

    List<AgUserBalanceDetailVo> convertAgUserBalanceDetailVo(List<AgUserBalanceDetail> list);

    AgUserBalanceDetailVo convertAgUserBalanceDetailVo(AgUserBalanceDetail agUserBalanceDetail);

    @Mapping(target = "name", source = "nickName")
    UserBalanceRespVO convertVO(AgUserBalance balance, String nickName);
}
