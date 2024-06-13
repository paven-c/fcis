package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.QueryAgMerchantOrderReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;

import java.util.List;

/**
 * <p>
 * 订单扣减流水表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgMerchantOrderService extends IService<AgMerchantOrder> {

    AgMerchantOrderVo info(QueryAgMerchantOrderReq req);

    AgMerchantOrderOverviewVo overview(List<Long> creatorIds);

    PageResult<AgMerchantOrderVo> pageList(QueryAgMerchantOrderReq req);
}
