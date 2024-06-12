package com.fancy.module.agent.repository.mapper.agent;

import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface AgentMapper extends BaseMapperX<Agent> {

    /**
     * 根据代理商名称查询代理商
     *
     * @param agentName 代理商名称
     * @return 代理商
     */
    default Agent selectByAgentName(String agentName) {
        return selectOne(Agent::getAgentName, agentName, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    /**
     * 根据代理商手机号查询代理商
     *
     * @param mobile 代理商手机号
     * @return 代理商
     */
    default Agent selectByMobile(String mobile) {
        return selectOne(Agent::getMobile, mobile, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    /**
     * 根据代理商id查询子级代理商
     *
     * @param agentId 代理商id
     * @return 代理商
     */
    default Long selectCountByParentId(Long agentId) {
        return selectCount(Agent::getParentId, agentId);
    }
}
