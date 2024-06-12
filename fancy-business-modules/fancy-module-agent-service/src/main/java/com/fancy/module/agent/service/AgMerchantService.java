package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;

/**
 * <p>
 * 订单扣减流水表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgMerchantService extends IService<AgMerchant> {

    /**
     * 创建商户
     * @param req
     */
    void addMerchant(EditAgMerchantReq req);
}
