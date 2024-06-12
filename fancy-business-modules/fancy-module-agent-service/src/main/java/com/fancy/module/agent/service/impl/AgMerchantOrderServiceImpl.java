package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.repository.mapper.AgMerchantOrderMapper;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.service.AgMerchantOrderService;
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

}
