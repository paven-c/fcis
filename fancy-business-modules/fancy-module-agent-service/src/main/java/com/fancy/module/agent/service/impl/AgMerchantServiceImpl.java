package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.repository.mapper.AgMerchantMapper;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.service.AgMerchantService;
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
public class AgMerchantServiceImpl extends ServiceImpl<AgMerchantMapper, AgMerchant> implements AgMerchantService {

    @Override
    public void addMerchant(EditAgMerchantReq req) {

        // merchant/create 创建商户
        // cmsMerchantConfig/addOrUpdate 更新商户配置
    }
}
