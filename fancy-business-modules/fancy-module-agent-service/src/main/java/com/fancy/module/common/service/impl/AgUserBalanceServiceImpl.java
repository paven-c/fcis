package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.common.repository.mapper.AgMerchantOrderMapper;
import com.fancy.module.common.repository.mapper.AgUserBalanceMapper;
import com.fancy.module.common.repository.pojo.AgMerchantOrder;
import com.fancy.module.common.repository.pojo.AgUserBalance;
import com.fancy.module.common.service.AgMerchantOrderService;
import com.fancy.module.common.service.AgUserBalanceService;
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
public class AgUserBalanceServiceImpl extends ServiceImpl<AgUserBalanceMapper, AgUserBalance> implements AgUserBalanceService {

}
