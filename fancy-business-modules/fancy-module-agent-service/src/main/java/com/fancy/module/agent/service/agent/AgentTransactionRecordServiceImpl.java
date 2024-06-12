package com.fancy.module.agent.service.agent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.repository.mapper.agent.AgentTransactionRecordMapper;
import com.fancy.module.agent.repository.pojo.agent.AgentTransactionRecord;
import org.springframework.stereotype.Service;

/**
 * @author paven
 */
@Service
public class AgentTransactionRecordServiceImpl extends ServiceImpl<AgentTransactionRecordMapper, AgentTransactionRecord> implements
        AgentTransactionRecordService {

}
