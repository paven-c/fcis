package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.common.repository.mapper.AgContentServiceDetailMapper;
import com.fancy.module.common.repository.mapper.AgMerchantOrderDetailMapper;
import com.fancy.module.common.repository.pojo.AgContentServiceDetail;
import com.fancy.module.common.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.service.AgContentServiceDetailService;
import com.fancy.module.common.service.AgMerchantOrderDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单子表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgMerchantOrderDetailServiceDetailServiceImpl extends ServiceImpl<AgMerchantOrderDetailMapper, AgMerchantOrderDetail> implements
        AgMerchantOrderDetailService {

}
