package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.QueryAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.vo.AgUserBalanceDetailVo;
import com.fancy.module.agent.enums.AgUserBalanceDetailBillType;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;

import java.math.BigDecimal;

/**
 * <p>
 * 订单扣减流水表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgUserBalanceDetailService extends IService<AgUserBalanceDetail> {

    PageResult<AgUserBalanceDetailVo> myTransactionPageList(QueryAgUserBalanceDetailReq req);

    /**
     *
     * @param id 主键id
     * @param balance 变更金额
     * @param billType 账单类型 0出账 1入账
     * @return
     */
    int updateBalance(Long id , BigDecimal balance, AgUserBalanceDetailBillType billType);
}
