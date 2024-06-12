package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.QueryAgMerchantOrderReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderDetailVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.convert.merchant.AgMerchantOrderConvert;
import com.fancy.module.agent.repository.mapper.AgMerchantOrderDetailMapper;
import com.fancy.module.agent.repository.mapper.AgMerchantOrderMapper;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.service.AgMerchantOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单扣减流水表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgMerchantOrderServiceImpl extends ServiceImpl<AgMerchantOrderMapper, AgMerchantOrder> implements AgMerchantOrderService {

    @Resource
    private AgMerchantOrderDetailMapper agMerchantOrderDetailMapper;
    @Override
    public AgMerchantOrderVo info(QueryAgMerchantOrderReq req) {
        List<AgMerchantOrderDetailVo> collect = agMerchantOrderDetailMapper.selectList(Wrappers.lambdaQuery(AgMerchantOrderDetail.class)
                        .eq(AgMerchantOrderDetail::getAgMerchantOrderId, req.getAgMerchantOrderId())
                        .eq(AgMerchantOrderDetail::getDeleted, 0))
                .stream().map(AgMerchantOrderConvert.INSTANCE::convertAgMerchantOrderDetailVo)
                .collect(Collectors.toList());
        return new AgMerchantOrderVo().setOrderDetailList(collect);
    }

    @Override
    public AgMerchantOrderOverviewVo overview() {
        //获取登录人Id和部门Id
        return baseMapper.countAgMerchantOrderOverviewVo(null, null);
    }

    @Override
    public PageResult<AgMerchantOrderVo> pageList(QueryAgMerchantOrderReq req) {
        return null;
    }
}
