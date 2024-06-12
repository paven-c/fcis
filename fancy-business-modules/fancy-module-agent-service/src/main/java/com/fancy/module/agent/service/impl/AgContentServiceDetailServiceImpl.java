package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.repository.mapper.AgContentServiceDetailMapper;
import com.fancy.module.agent.repository.pojo.AgContentServiceDetail;
import com.fancy.module.agent.service.AgContentServiceDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容服务子表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgContentServiceDetailServiceImpl extends ServiceImpl<AgContentServiceDetailMapper, AgContentServiceDetail> implements
        AgContentServiceDetailService {

}
