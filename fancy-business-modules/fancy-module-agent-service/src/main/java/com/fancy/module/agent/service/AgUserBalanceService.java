package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.repository.pojo.AgUserBalance;

/**
 * <p>
 * 订单扣减流水表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgUserBalanceService extends IService<AgUserBalance> {

    /**
     * 变更余额
     *
     * @param req 变更明细
     * @return 是否成功
     */
    boolean changeBalance(EditAgUserBalanceDetailReq req);

    /**
     * 创建用户余额
     *
     * @param userId 用户id
     */
    void createUserBalance(Long userId);
}
