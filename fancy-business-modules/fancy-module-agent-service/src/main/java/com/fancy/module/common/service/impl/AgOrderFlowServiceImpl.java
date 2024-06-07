package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.common.repository.mapper.AgOrderFlowMapper;
import com.fancy.module.common.repository.pojo.AgOrderFlow;
import com.fancy.module.common.service.AgOrderFlowService;
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
public class AgOrderFlowServiceImpl extends ServiceImpl<AgOrderFlowMapper, AgOrderFlow> implements AgOrderFlowService {

}
