package com.fancy.module.agent.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.QueryAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.controller.vo.AgUserBalanceDetailVo;
import com.fancy.module.agent.convert.balance.AgUserBalanceConvert;
import com.fancy.module.agent.convert.merchant.AgMerchantOrderConvert;
import com.fancy.module.agent.repository.mapper.AgUserBalanceDetailMapper;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import com.fancy.module.agent.service.AgUserBalanceDetailService;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
public class AgUserBalanceDetailServiceImpl extends ServiceImpl<AgUserBalanceDetailMapper, AgUserBalanceDetail> implements AgUserBalanceDetailService {

    @Override
    public PageResult<AgUserBalanceDetailVo> myTransactionPageList(QueryAgUserBalanceDetailReq req) {
        PageResult<AgUserBalanceDetail> agUserBalanceDetailPageResult = baseMapper.selectPage(req, Wrappers.lambdaQuery(AgUserBalanceDetail.class)
                .eq(ObjectUtil.isNotEmpty(req.getBillType()), AgUserBalanceDetail::getBillType, req.getBillType())
                .eq(ObjectUtil.isNotEmpty(req.getObjectType()), AgUserBalanceDetail::getObjectType, req.getObjectType())
                .in(ObjectUtil.isNotEmpty(req.getCreatorIds()), AgUserBalanceDetail::getAgUserId, req.getCreatorIds())
                .between(ObjectUtil.isNotEmpty(req.getStartTime()) && ObjectUtil.isNotEmpty(req.getEndTime()), AgUserBalanceDetail::getCreateTime, req.getStartTime(), req.getEndTime())
                .eq(AgUserBalanceDetail::getDeleted, 0)
                .orderByDesc(AgUserBalanceDetail::getCreateTime));
        if (ObjectUtil.isEmpty(agUserBalanceDetailPageResult.getList())) {
            return new PageResult<>(agUserBalanceDetailPageResult.getTotal(), req.getPageNum(), req.getPageSize());
        }
        List<AgUserBalanceDetailVo> agUserBalanceDetailVos = AgUserBalanceConvert.INSTANCE.convertAgUserBalanceDetailVo(agUserBalanceDetailPageResult.getList());

        return new PageResult<>(agUserBalanceDetailVos, agUserBalanceDetailPageResult.getTotal(), req.getPageNum(), req.getPageSize());
    }

    @Override
    public int updateBalance(Long id, BigDecimal balance, Integer billType) {
        return baseMapper.updateBalance(id, balance, billType);
    }
}
