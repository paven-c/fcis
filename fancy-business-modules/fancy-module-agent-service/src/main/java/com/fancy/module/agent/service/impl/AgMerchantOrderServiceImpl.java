package com.fancy.module.agent.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.security.core.util.SecurityFrameworkUtils;
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
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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

    @Resource
    private UserApi userApi;

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
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        return baseMapper.countAgMerchantOrderOverviewVo(Collections.singletonList(loginUserId), null);
    }

    @Override
    public PageResult<AgMerchantOrderVo> pageList(QueryAgMerchantOrderReq req) {
        PageResult<AgMerchantOrder> agMerchantOrderPageResult = baseMapper.selectPage(req, Wrappers.lambdaQuery(AgMerchantOrder.class)
                .eq(ObjectUtil.isNotEmpty(req.getAgMerchantId()), AgMerchantOrder::getAgMerchantId, req.getAgMerchantId())
                .in(ObjectUtil.isNotEmpty(req.getCreatorIds()), AgMerchantOrder::getCreatorId, req.getCreatorIds())
                .between(
                        ObjectUtil.isNotEmpty(req.getStartTime()) && ObjectUtil.isNotEmpty(req.getEndTime()), AgMerchantOrder::getCreateTime,
                        req.getStartTime(), req.getEndTime())
                .eq(AgMerchantOrder::getDeleted, 0)
                .orderByDesc(AgMerchantOrder::getCreateTime));
        if (ObjectUtil.isEmpty(agMerchantOrderPageResult.getList())) {
            return new PageResult<>(agMerchantOrderPageResult.getTotal(), req.getPageNum(), req.getPageSize());
        }
        List<AgMerchantOrderVo> agMerchantOrderVos = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrderVo(agMerchantOrderPageResult.getList());
        List<Long> collect = agMerchantOrderVos.stream()
                .map(AgMerchantOrderVo::getCreatorId)
                .filter(ObjectUtil::isNotEmpty)
                .collect(Collectors.toList());
        Map<Long, String> userMap = userApi.getUserByIds(collect)
                .stream()
                .collect(Collectors.toMap(UserRespDTO::getId, UserRespDTO::getUsername));

        agMerchantOrderVos.forEach(agMerchantOrderVo -> {
            if (ObjectUtil.isNotEmpty(agMerchantOrderVo.getCreatorId())) {
                agMerchantOrderVo.setAgUserName(userMap.get(agMerchantOrderVo.getCreatorId()));
            }
        });
        return new PageResult<>(agMerchantOrderVos, agMerchantOrderPageResult.getTotal(), req.getPageNum(), req.getPageSize());
    }
}
