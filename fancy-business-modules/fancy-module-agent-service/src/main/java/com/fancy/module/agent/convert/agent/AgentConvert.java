package com.fancy.module.agent.convert.agent;


import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.module.agent.api.agent.dto.AgentSaveReqDTO;
import com.fancy.module.agent.controller.vo.AgentRespVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface AgentConvert {

    AgentConvert INSTANCE = Mappers.getMapper(AgentConvert.class);

    @Mapping(target = "parentAgentId", source = "parentAgentId")
    @Mapping(target = "deptId", source = "deptId")
    AgentSaveReqVO convert(AgentSaveReqDTO bean, Long deptId, Long parentAgentId);

    @Mapping(target = "contactorName", source = "bean.contactor")
    @Mapping(target = "parentAgentName", expression = "java(agentNames.get(bean.getParentId()))")
    @Mapping(target = "parentAgentId", source = "bean.parentId")
    @Mapping(target = "currentBalance", expression = "java(cn.hutool.core.map.MapUtil.isNotEmpty(balanceAmounts) ? balanceAmounts.get(bean.getUserId()) : null)")
    AgentRespVO convertVO(Agent bean, Map<Long, String> agentNames, Map<Long, String> balanceAmounts);

    default List<AgentRespVO> convertList(List<Agent> list, Map<Long, String> agentNames, Map<Long, String> balanceAmounts) {
        return CollectionUtils.convertList(list, agent -> convertVO(agent, agentNames, balanceAmounts));
    }

    Agent convertBean(AgentSaveReqVO reqVO);
}
