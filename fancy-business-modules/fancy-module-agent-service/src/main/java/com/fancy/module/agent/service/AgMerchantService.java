package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.QueryAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;

import java.util.List;

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

    void updateMerchant(EditAgMerchantReq req);

    PageResult<AgMerchantVo> pageListMerchant(QueryAgMerchantReq req);

    /**
     * 客户列表
     * @param req
     * @return
     */
    List<AgMerchantVo> listMerchant(QueryAgMerchantReq req);
}
