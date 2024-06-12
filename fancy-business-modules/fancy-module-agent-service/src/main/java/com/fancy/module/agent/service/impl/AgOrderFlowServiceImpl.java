package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.repository.mapper.AgOrderFlowMapper;
import com.fancy.module.agent.repository.pojo.AgOrderFlow;
import com.fancy.module.agent.service.AgOrderFlowService;
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
