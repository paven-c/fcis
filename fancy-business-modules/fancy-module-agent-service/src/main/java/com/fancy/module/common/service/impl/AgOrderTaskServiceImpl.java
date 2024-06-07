package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.common.repository.mapper.AgOrderTaskMapper;
import com.fancy.module.common.repository.pojo.AgOrderTask;
import com.fancy.module.common.service.AgOrderTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单任务表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgOrderTaskServiceImpl extends ServiceImpl<AgOrderTaskMapper, AgOrderTask> implements AgOrderTaskService {

}
